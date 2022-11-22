import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITodo } from '@/shared/model/todo.model';
import TodoService from './todo.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TodoDetails extends Vue {
  @Inject('todoService') private todoService: () => TodoService;
  @Inject('alertService') private alertService: () => AlertService;

  public todo: ITodo = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.todoId) {
        vm.retrieveTodo(to.params.todoId);
      }
    });
  }

  public retrieveTodo(todoId) {
    this.todoService()
      .find(todoId)
      .then(res => {
        this.todo = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
