import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ITodo } from '@/shared/model/todo.model';

import TodoService from './todo.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Todo extends Vue {
  @Inject('todoService') private todoService: () => TodoService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public todos: ITodo[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllTodos();
  }

  public clear(): void {
    this.retrieveAllTodos();
  }

  public retrieveAllTodos(): void {
    this.isFetching = true;
    this.todoService()
      .retrieve()
      .then(
        res => {
          this.todos = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: ITodo): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTodo(): void {
    this.todoService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('managerApp.todo.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllTodos();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
