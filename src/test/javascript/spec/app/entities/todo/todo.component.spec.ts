/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TodoComponent from '@/entities/todo/todo.vue';
import TodoClass from '@/entities/todo/todo.component';
import TodoService from '@/entities/todo/todo.service';
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
  describe('Todo Management Component', () => {
    let wrapper: Wrapper<TodoClass>;
    let comp: TodoClass;
    let todoServiceStub: SinonStubbedInstance<TodoService>;

    beforeEach(() => {
      todoServiceStub = sinon.createStubInstance<TodoService>(TodoService);
      todoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TodoClass>(TodoComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          todoService: () => todoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      todoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTodos();
      await comp.$nextTick();

      // THEN
      expect(todoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.todos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      todoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(todoServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTodo();
      await comp.$nextTick();

      // THEN
      expect(todoServiceStub.delete.called).toBeTruthy();
      expect(todoServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
