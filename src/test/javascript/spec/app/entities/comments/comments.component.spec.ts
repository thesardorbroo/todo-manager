/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CommentsComponent from '@/entities/comments/comments.vue';
import CommentsClass from '@/entities/comments/comments.component';
import CommentsService from '@/entities/comments/comments.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Comments Management Component', () => {
    let wrapper: Wrapper<CommentsClass>;
    let comp: CommentsClass;
    let commentsServiceStub: SinonStubbedInstance<CommentsService>;

    beforeEach(() => {
      commentsServiceStub = sinon.createStubInstance<CommentsService>(CommentsService);
      commentsServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CommentsClass>(CommentsComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          commentsService: () => commentsServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      commentsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCommentss();
      await comp.$nextTick();

      // THEN
      expect(commentsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.comments[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      commentsServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(commentsServiceStub.retrieve.callCount).toEqual(1);

      comp.removeComments();
      await comp.$nextTick();

      // THEN
      expect(commentsServiceStub.delete.called).toBeTruthy();
      expect(commentsServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
