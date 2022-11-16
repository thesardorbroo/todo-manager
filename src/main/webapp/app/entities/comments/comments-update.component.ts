import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import TaskService from '@/entities/task/task.service';
import { ITask } from '@/shared/model/task.model';

import { IComments, Comments } from '@/shared/model/comments.model';
import CommentsService from './comments.service';

const validations: any = {
  comments: {
    message: {},
  },
};

@Component({
  validations,
})
export default class CommentsUpdate extends Vue {
  @Inject('commentsService') private commentsService: () => CommentsService;
  @Inject('alertService') private alertService: () => AlertService;

  public comments: IComments = new Comments();

  @Inject('taskService') private taskService: () => TaskService;

  public tasks: ITask[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commentsId) {
        vm.retrieveComments(to.params.commentsId);
      }
      vm.initRelationships();
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
    if (this.comments.id) {
      this.commentsService()
        .update(this.comments)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('managerApp.comments.updated', { param: param.id });
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
      this.commentsService()
        .create(this.comments)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('managerApp.comments.created', { param: param.id });
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

  public retrieveComments(commentsId): void {
    this.commentsService()
      .find(commentsId)
      .then(res => {
        this.comments = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.taskService()
      .retrieve()
      .then(res => {
        this.tasks = res.data;
      });
  }
}
