<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="task">
        <h2 class="jh-entity-heading" data-cy="taskDetailsHeading">
          <span v-text="$t('managerApp.task.detail.title')">Task</span> {{ task.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('managerApp.task.body')">Body</span>
          </dt>
          <dd>
            <span>{{ task.body }}</span>
          </dd>
          <dt>
            <span v-text="$t('managerApp.task.image')">Image</span>
          </dt>
          <dd>
            <div v-if="task.image">
              <a v-on:click="openFile(task.imageContentType, task.image)">
                <img v-bind:src="'data:' + task.imageContentType + ';base64,' + task.image" style="max-width: 100%" alt="task image" />
              </a>
              {{ task.imageContentType }}, {{ byteSize(task.image) }}
            </div>
          </dd>
          <dt>
            <span v-text="$t('managerApp.task.caption')">Caption</span>
          </dt>
          <dd>
            <span>{{ task.caption }}</span>
          </dd>
          <dt>
            <span v-text="$t('managerApp.task.groups')">Groups</span>
          </dt>
          <dd>
            <span v-for="(groups, i) in task.groups" :key="groups.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'GroupsView', params: { groupsId: groups.id } }">{{ groups.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="task.id" :to="{ name: 'TaskEdit', params: { taskId: task.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./task-details.component.ts"></script>
