<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="todoList">
        <h2 class="jh-entity-heading" data-cy="todoListDetailsHeading">
          <span v-text="$t('managerApp.todoList.detail.title')">TodoList</span> {{ todoList.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('managerApp.todoList.createdAt')">Created At</span>
          </dt>
          <dd>
            <span>{{ todoList.createdAt }}</span>
          </dd>
          <dt>
            <span v-text="$t('managerApp.todoList.none')">None</span>
          </dt>
          <dd>
            <span>{{ todoList.none }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="todoList.id" :to="{ name: 'TodoListEdit', params: { todoListId: todoList.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./todo-list-details.component.ts"></script>
