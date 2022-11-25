/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TodoUpdateComponent from '@/entities/todo/todo-update.vue';
import TodoClass from '@/entities/todo/todo-update.component';
import TodoService from '@/entities/todo/todo.service';

import TaskService from '@/entities/task/task.service';

import CustomerService from '@/entities/customer/customer.service';
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
  describe('Todo Management Update Component', () => {
    let wrapper: Wrapper<TodoClass>;
    let comp: TodoClass;
    let todoServiceStub: SinonStubbedInstance<TodoService>;

    beforeEach(() => {
      todoServiceStub = sinon.createStubInstance<TodoService>(TodoService);

      wrapper = shallowMount<TodoClass>(TodoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          todoService: () => todoServiceStub,
          alertService: () => new AlertService(),

          taskService: () =>
            sinon.createStubInstance<TaskService>(TaskService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          customerService: () =>
            sinon.createStubInstance<CustomerService>(CustomerService, {
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
        comp.todo = entity;
        todoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(todoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.todo = entity;
        todoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(todoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTodo = { id: 123 };
        todoServiceStub.find.resolves(foundTodo);
        todoServiceStub.retrieve.resolves([foundTodo]);

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
