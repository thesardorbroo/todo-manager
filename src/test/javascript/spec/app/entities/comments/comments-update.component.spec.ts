/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CommentsUpdateComponent from '@/entities/comments/comments-update.vue';
import CommentsClass from '@/entities/comments/comments-update.component';
import CommentsService from '@/entities/comments/comments.service';

import TaskService from '@/entities/task/task.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Comments Management Update Component', () => {
    let wrapper: Wrapper<CommentsClass>;
    let comp: CommentsClass;
    let commentsServiceStub: SinonStubbedInstance<CommentsService>;

    beforeEach(() => {
      commentsServiceStub = sinon.createStubInstance<CommentsService>(CommentsService);

      wrapper = shallowMount<CommentsClass>(CommentsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          commentsService: () => commentsServiceStub,
          alertService: () => new AlertService(),

          taskService: () =>
            sinon.createStubInstance<TaskService>(TaskService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.comments = entity;
        commentsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commentsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.comments = entity;
        commentsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commentsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundComments = { id: 123 };
        commentsServiceStub.find.resolves(foundComments);
        commentsServiceStub.retrieve.resolves([foundComments]);

        // WHEN
        comp.beforeRouteEnter({ params: { commentsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.comments).toBe(foundComments);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
