/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TodoListUpdateComponent from '@/entities/todo-list/todo-list-update.vue';
import TodoListClass from '@/entities/todo-list/todo-list-update.component';
import TodoListService from '@/entities/todo-list/todo-list.service';

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
  describe('TodoList Management Update Component', () => {
    let wrapper: Wrapper<TodoListClass>;
    let comp: TodoListClass;
    let todoListServiceStub: SinonStubbedInstance<TodoListService>;

    beforeEach(() => {
      todoListServiceStub = sinon.createStubInstance<TodoListService>(TodoListService);

      wrapper = shallowMount<TodoListClass>(TodoListUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          todoListService: () => todoListServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.todoList = entity;
        todoListServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(todoListServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.todoList = entity;
        todoListServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(todoListServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTodoList = { id: 123 };
        todoListServiceStub.find.resolves(foundTodoList);
        todoListServiceStub.retrieve.resolves([foundTodoList]);

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
