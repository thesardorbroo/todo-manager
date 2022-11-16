<template>
  <div>
    <h2 id="page-heading" data-cy="TaskHeading">
      <span v-text="$t('managerApp.task.home.title')" id="task-heading">Tasks</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('managerApp.task.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'TaskCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-task">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('managerApp.task.home.createLabel')"> Create a new Task </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tasks && tasks.length === 0">
      <span v-text="$t('managerApp.task.home.notFound')">No tasks found</span>
    </div>
    <div class="table-responsive" v-if="tasks && tasks.length > 0">
      <table class="table table-striped" aria-describedby="tasks">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('managerApp.task.body')">Body</span></th>
            <th scope="row"><span v-text="$t('managerApp.task.image')">Image</span></th>
            <th scope="row"><span v-text="$t('managerApp.task.caption')">Caption</span></th>
            <th scope="row"><span v-text="$t('managerApp.task.groups')">Groups</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="task in tasks" :key="task.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TaskView', params: { taskId: task.id } }">{{ task.id }}</router-link>
            </td>
            <td>{{ task.body }}</td>
            <td>
              <a v-if="task.image" v-on:click="openFile(task.imageContentType, task.image)">
                <img v-bind:src="'data:' + task.imageContentType + ';base64,' + task.image" style="max-height: 30px" alt="task image" />
              </a>
              <span v-if="task.image">{{ task.imageContentType }}, {{ byteSize(task.image) }}</span>
            </td>
            <td>{{ task.caption }}</td>
            <td>
              <span v-for="(groups, i) in task.groups" :key="groups.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'GroupsView', params: { groupsId: groups.id } }">{{
                  groups.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TaskView', params: { taskId: task.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TaskEdit', params: { taskId: task.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(task)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="managerApp.task.delete.question" data-cy="taskDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-task-heading" v-text="$t('managerApp.task.delete.question', { id: removeId })">
          Are you sure you want to delete this Task?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-task"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeTask()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./task.component.ts"></script>
