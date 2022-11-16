import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGroups } from '@/shared/model/groups.model';
import GroupsService from './groups.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class GroupsDetails extends Vue {
  @Inject('groupsService') private groupsService: () => GroupsService;
  @Inject('alertService') private alertService: () => AlertService;

  public groups: IGroups = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.groupsId) {
        vm.retrieveGroups(to.params.groupsId);
      }
    });
  }

  public retrieveGroups(groupsId) {
    this.groupsService()
      .find(groupsId)
      .then(res => {
        this.groups = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
