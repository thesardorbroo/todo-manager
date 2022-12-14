import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import GroupsService from './groups/groups.service';
import CustomerService from './customer/customer.service';
import TaskService from './task/task.service';
import CommentsService from './comments/comments.service';
import TodoListService from './todo-list/todo-list.service';
import TodoService from './todo/todo.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('groupsService') private groupsService = () => new GroupsService();
  @Provide('customerService') private customerService = () => new CustomerService();
  @Provide('taskService') private taskService = () => new TaskService();
  @Provide('commentsService') private commentsService = () => new CommentsService();
  @Provide('todoListService') private todoListService = () => new TodoListService();
  @Provide('todoService') private todoService = () => new TodoService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
