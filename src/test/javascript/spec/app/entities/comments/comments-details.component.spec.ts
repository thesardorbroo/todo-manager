/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CommentsDetailComponent from '@/entities/comments/comments-details.vue';
import CommentsClass from '@/entities/comments/comments-details.component';
import CommentsService from '@/entities/comments/comments.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Comments Management Detail Component', () => {
    let wrapper: Wrapper<CommentsClass>;
    let comp: CommentsClass;
    let commentsServiceStub: SinonStubbedInstance<CommentsService>;

    beforeEach(() => {
      commentsServiceStub = sinon.createStubInstance<CommentsService>(CommentsService);

      wrapper = shallowMount<CommentsClass>(CommentsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { commentsService: () => commentsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundComments = { id: 123 };
        commentsServiceStub.find.resolves(foundComments);

        // WHEN
        comp.retrieveComments(123);
        await comp.$nextTick();

        // THEN
        expect(comp.comments).toBe(foundComments);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundComments = { id: 123 };
        commentsServiceStub.find.resolves(foundComments);

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
