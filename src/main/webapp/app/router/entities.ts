import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Groups = () => import('@/entities/groups/groups.vue');
// prettier-ignore
const GroupsUpdate = () => import('@/entities/groups/groups-update.vue');
// prettier-ignore
const GroupsDetails = () => import('@/entities/groups/groups-details.vue');
// prettier-ignore
const Customer = () => import('@/entities/customer/customer.vue');
// prettier-ignore
const CustomerUpdate = () => import('@/entities/customer/customer-update.vue');
// prettier-ignore
const CustomerDetails = () => import('@/entities/customer/customer-details.vue');
// prettier-ignore
const Task = () => import('@/entities/task/task.vue');
// prettier-ignore
const TaskUpdate = () => import('@/entities/task/task-update.vue');
// prettier-ignore
const TaskDetails = () => import('@/entities/task/task-details.vue');
// prettier-ignore
const Comments = () => import('@/entities/comments/comments.vue');
// prettier-ignore
const CommentsUpdate = () => import('@/entities/comments/comments-update.vue');
// prettier-ignore
const CommentsDetails = () => import('@/entities/comments/comments-details.vue');
// prettier-ignore
const TodoList = () => import('@/entities/todo-list/todo-list.vue');
// prettier-ignore
const TodoListUpdate = () => import('@/entities/todo-list/todo-list-update.vue');
// prettier-ignore
const TodoListDetails = () => import('@/entities/todo-list/todo-list-details.vue');
// prettier-ignore
const Todo = () => import('@/entities/todo/todo.vue');
// prettier-ignore
const TodoUpdate = () => import('@/entities/todo/todo-update.vue');
// prettier-ignore
const TodoDetails = () => import('@/entities/todo/todo-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'groups',
      name: 'Groups',
      component: Groups,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'groups/new',
      name: 'GroupsCreate',
      component: GroupsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'groups/:groupsId/edit',
      name: 'GroupsEdit',
      component: GroupsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'groups/:groupsId/view',
      name: 'GroupsView',
      component: GroupsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer',
      name: 'Customer',
      component: Customer,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/new',
      name: 'CustomerCreate',
      component: CustomerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/:customerId/edit',
      name: 'CustomerEdit',
      component: CustomerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/:customerId/view',
      name: 'CustomerView',
      component: CustomerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task',
      name: 'Task',
      component: Task,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task/new',
      name: 'TaskCreate',
      component: TaskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task/:taskId/edit',
      name: 'TaskEdit',
      component: TaskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task/:taskId/view',
      name: 'TaskView',
      component: TaskDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'comments',
      name: 'Comments',
      component: Comments,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'comments/new',
      name: 'CommentsCreate',
      component: CommentsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'comments/:commentsId/edit',
      name: 'CommentsEdit',
      component: CommentsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'comments/:commentsId/view',
      name: 'CommentsView',
      component: CommentsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo-list',
      name: 'TodoList',
      component: TodoList,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo-list/new',
      name: 'TodoListCreate',
      component: TodoListUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo-list/:todoListId/edit',
      name: 'TodoListEdit',
      component: TodoListUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo-list/:todoListId/view',
      name: 'TodoListView',
      component: TodoListDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo',
      name: 'Todo',
      component: Todo,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo/new',
      name: 'TodoCreate',
      component: TodoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo/:todoId/edit',
      name: 'TodoEdit',
      component: TodoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'todo/:todoId/view',
      name: 'TodoView',
      component: TodoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
