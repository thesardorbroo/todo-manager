import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ITodoList } from '@/shared/model/todo-list.model';

import TodoListService from './todo-list.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class TodoList extends Vue {
  @Inject('todoListService') private todoListService: () => TodoListService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public todoLists: ITodoList[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllTodoLists();
  }

  public clear(): void {
    this.retrieveAllTodoLists();
  }

  public retrieveAllTodoLists(): void {
    this.isFetching = true;
    this.todoListService()
      .retrieve()
      .then(
        res => {
          this.todoLists = res.data;
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

  public prepareRemove(instance: ITodoList): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTodoList(): void {
    this.todoListService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('managerApp.todoList.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllTodoLists();
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
