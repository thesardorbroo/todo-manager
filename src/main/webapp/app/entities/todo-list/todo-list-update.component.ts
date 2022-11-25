import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { ITodoList, TodoList } from '@/shared/model/todo-list.model';
import TodoListService from './todo-list.service';

const validations: any = {
  todoList: {
    createdAt: {},
    none: {},
  },
};

@Component({
  validations,
})
export default class TodoListUpdate extends Vue {
  @Inject('todoListService') private todoListService: () => TodoListService;
  @Inject('alertService') private alertService: () => AlertService;

  public todoList: ITodoList = new TodoList();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.todoListId) {
        vm.retrieveTodoList(to.params.todoListId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.todoList.id) {
      this.todoListService()
        .update(this.todoList)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('managerApp.todoList.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.todoListService()
        .create(this.todoList)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('managerApp.todoList.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveTodoList(todoListId): void {
    this.todoListService()
      .find(todoListId)
      .then(res => {
        this.todoList = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
