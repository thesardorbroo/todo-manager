import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IComments } from '@/shared/model/comments.model';

import CommentsService from './comments.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Comments extends Vue {
  @Inject('commentsService') private commentsService: () => CommentsService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public comments: IComments[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllCommentss();
  }

  public clear(): void {
    this.retrieveAllCommentss();
  }

  public retrieveAllCommentss(): void {
    this.isFetching = true;
    this.commentsService()
      .retrieve()
      .then(
        res => {
          this.comments = res.data;
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

  public prepareRemove(instance: IComments): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeComments(): void {
    this.commentsService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('managerApp.comments.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllCommentss();
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
