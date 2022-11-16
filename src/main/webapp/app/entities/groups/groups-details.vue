<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="groups">
        <h2 class="jh-entity-heading" data-cy="groupsDetailsHeading">
          <span v-text="$t('managerApp.groups.detail.title')">Groups</span> {{ groups.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('managerApp.groups.groupName')">Group Name</span>
          </dt>
          <dd>
            <span>{{ groups.groupName }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="groups.id" :to="{ name: 'GroupsEdit', params: { groupsId: groups.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./groups-details.component.ts"></script>
