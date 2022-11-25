/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TodoListDetailComponent from '@/entities/todo-list/todo-list-details.vue';
import TodoListClass from '@/entities/todo-list/todo-list-details.component';
import TodoListService from '@/entities/todo-list/todo-list.service';
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
  describe('TodoList Management Detail Component', () => {
    let wrapper: Wrapper<TodoListClass>;
    let comp: TodoListClass;
    let todoListServiceStub: SinonStubbedInstance<TodoListService>;

    beforeEach(() => {
      todoListServiceStub = sinon.createStubInstance<TodoListService>(TodoListService);

      wrapper = shallowMount<TodoListClass>(TodoListDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { todoListService: () => todoListServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTodoList = { id: 123 };
        todoListServiceStub.find.resolves(foundTodoList);

        // WHEN
        comp.retrieveTodoList(123);
        await comp.$nextTick();

        // THEN
        expect(comp.todoList).toBe(foundTodoList);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTodoList = { id: 123 };
        todoListServiceStub.find.resolves(foundTodoList);

        // WHEN
        comp.beforeRouteEnter({ params: { todoListId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.todoList).toBe(foundTodoList);
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
