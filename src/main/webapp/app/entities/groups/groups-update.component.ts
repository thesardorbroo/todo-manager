import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import TaskService from '@/entities/task/task.service';
import { ITask } from '@/shared/model/task.model';

import { IGroups, Groups } from '@/shared/model/groups.model';
import GroupsService from './groups.service';

const validations: any = {
  groups: {
    groupName: {},
  },
};

@Component({
  validations,
})
export default class GroupsUpdate extends Vue {
  @Inject('groupsService') private groupsService: () => GroupsService;
  @Inject('alertService') private alertService: () => AlertService;

  public groups: IGroups = new Groups();

  @Inject('taskService') private taskService: () => TaskService;

  public tasks: ITask[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.groupsId) {
        vm.retrieveGroups(to.params.groupsId);
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
    if (this.groups.id) {
      this.groupsService()
        .update(this.groups)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('managerApp.groups.updated', { param: param.id });
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
      this.groupsService()
        .create(this.groups)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('managerApp.groups.created', { param: param.id });
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

  public retrieveGroups(groupsId): void {
    this.groupsService()
      .find(groupsId)
      .then(res => {
        this.groups = res;
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
