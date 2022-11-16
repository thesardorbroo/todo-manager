/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import GroupsComponent from '@/entities/groups/groups.vue';
import GroupsClass from '@/entities/groups/groups.component';
import GroupsService from '@/entities/groups/groups.service';
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
  describe('Groups Management Component', () => {
    let wrapper: Wrapper<GroupsClass>;
    let comp: GroupsClass;
    let groupsServiceStub: SinonStubbedInstance<GroupsService>;

    beforeEach(() => {
      groupsServiceStub = sinon.createStubInstance<GroupsService>(GroupsService);
      groupsServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<GroupsClass>(GroupsComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          groupsService: () => groupsServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      groupsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllGroupss();
      await comp.$nextTick();

      // THEN
      expect(groupsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.groups[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      groupsServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(groupsServiceStub.retrieve.callCount).toEqual(1);

      comp.removeGroups();
      await comp.$nextTick();

      // THEN
      expect(groupsServiceStub.delete.called).toBeTruthy();
      expect(groupsServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
