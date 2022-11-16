<template>
  <div>
    <h2 id="page-heading" data-cy="GroupsHeading">
      <span v-text="$t('managerApp.groups.home.title')" id="groups-heading">Groups</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('managerApp.groups.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'GroupsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-groups"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('managerApp.groups.home.createLabel')"> Create a new Groups </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && groups && groups.length === 0">
      <span v-text="$t('managerApp.groups.home.notFound')">No groups found</span>
    </div>
    <div class="table-responsive" v-if="groups && groups.length > 0">
      <table class="table table-striped" aria-describedby="groups">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('managerApp.groups.groupName')">Group Name</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="groups in groups" :key="groups.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'GroupsView', params: { groupsId: groups.id } }">{{ groups.id }}</router-link>
            </td>
            <td>{{ groups.groupName }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'GroupsView', params: { groupsId: groups.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'GroupsEdit', params: { groupsId: groups.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(groups)"
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
        ><span id="managerApp.groups.delete.question" data-cy="groupsDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-groups-heading" v-text="$t('managerApp.groups.delete.question', { id: removeId })">
          Are you sure you want to delete this Groups?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-groups"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeGroups()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./groups.component.ts"></script>
