/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TodoListComponent from '@/entities/todo-list/todo-list.vue';
import TodoListClass from '@/entities/todo-list/todo-list.component';
import TodoListService from '@/entities/todo-list/todo-list.service';
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
  describe('TodoList Management Component', () => {
    let wrapper: Wrapper<TodoListClass>;
    let comp: TodoListClass;
    let todoListServiceStub: SinonStubbedInstance<TodoListService>;

    beforeEach(() => {
      todoListServiceStub = sinon.createStubInstance<TodoListService>(TodoListService);
      todoListServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TodoListClass>(TodoListComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          todoListService: () => todoListServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      todoListServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTodoLists();
      await comp.$nextTick();

      // THEN
      expect(todoListServiceStub.retrieve.called).toBeTruthy();
      expect(comp.todoLists[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      todoListServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(todoListServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTodoList();
      await comp.$nextTick();

      // THEN
      expect(todoListServiceStub.delete.called).toBeTruthy();
      expect(todoListServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
