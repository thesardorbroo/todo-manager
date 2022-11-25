import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITodoList } from '@/shared/model/todo-list.model';
import TodoListService from './todo-list.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TodoListDetails extends Vue {
  @Inject('todoListService') private todoListService: () => TodoListService;
  @Inject('alertService') private alertService: () => AlertService;

  public todoList: ITodoList = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.todoListId) {
        vm.retrieveTodoList(to.params.todoListId);
      }
    });
  }

  public retrieveTodoList(todoListId) {
    this.todoListService()
      .find(todoListId)
      .then(res => {
        this.todoList = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
