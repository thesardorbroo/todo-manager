import { Component, Vue, Inject } from 'vue-property-decorator';

import { IComments } from '@/shared/model/comments.model';
import CommentsService from './comments.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CommentsDetails extends Vue {
  @Inject('commentsService') private commentsService: () => CommentsService;
  @Inject('alertService') private alertService: () => AlertService;

  public comments: IComments = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commentsId) {
        vm.retrieveComments(to.params.commentsId);
      }
    });
  }

  public retrieveComments(commentsId) {
    this.commentsService()
      .find(commentsId)
      .then(res => {
        this.comments = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
