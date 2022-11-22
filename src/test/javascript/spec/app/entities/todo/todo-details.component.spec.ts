/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TodoDetailComponent from '@/entities/todo/todo-details.vue';
import TodoClass from '@/entities/todo/todo-details.component';
import TodoService from '@/entities/todo/todo.service';
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
  describe('Todo Management Detail Component', () => {
    let wrapper: Wrapper<TodoClass>;
    let comp: TodoClass;
    let todoServiceStub: SinonStubbedInstance<TodoService>;

    beforeEach(() => {
      todoServiceStub = sinon.createStubInstance<TodoService>(TodoService);

      wrapper = shallowMount<TodoClass>(TodoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { todoService: () => todoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTodo = { id: 123 };
        todoServiceStub.find.resolves(foundTodo);

        // WHEN
        comp.retrieveTodo(123);
        await comp.$nextTick();

        // THEN
        expect(comp.todo).toBe(foundTodo);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTodo = { id: 123 };
        todoServiceStub.find.resolves(foundTodo);

        // WHEN
        comp.beforeRouteEnter({ params: { todoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.todo).toBe(foundTodo);
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
