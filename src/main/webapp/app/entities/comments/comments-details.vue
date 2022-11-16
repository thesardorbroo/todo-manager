<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="comments">
        <h2 class="jh-entity-heading" data-cy="commentsDetailsHeading">
          <span v-text="$t('managerApp.comments.detail.title')">Comments</span> {{ comments.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('managerApp.comments.message')">Message</span>
          </dt>
          <dd>
            <span>{{ comments.message }}</span>
          </dd>
          <dt>
            <span v-text="$t('managerApp.comments.task')">Task</span>
          </dt>
          <dd>
            <div v-if="comments.task">
              <router-link :to="{ name: 'TaskView', params: { taskId: comments.task.id } }">{{ comments.task.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="comments.id" :to="{ name: 'CommentsEdit', params: { commentsId: comments.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./comments-details.component.ts"></script>
