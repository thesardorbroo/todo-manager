<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="todo">
        <h2 class="jh-entity-heading" data-cy="todoDetailsHeading">
          <span v-text="$t('managerApp.todo.detail.title')">Todo</span> {{ todo.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('managerApp.todo.createdAt')">Created At</span>
          </dt>
          <dd>
            <span>{{ todo.createdAt }}</span>
          </dd>
          <dt>
            <span v-text="$t('managerApp.todo.none')">None</span>
          </dt>
          <dd>
            <span>{{ todo.none }}</span>
          </dd>
          <dt>
            <span v-text="$t('managerApp.todo.task')">Task</span>
          </dt>
          <dd>
            <div v-if="todo.task">
              <router-link :to="{ name: 'TaskView', params: { taskId: todo.task.id } }">{{ todo.task.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('managerApp.todo.customer')">Customer</span>
          </dt>
          <dd>
            <div v-if="todo.customer">
              <router-link :to="{ name: 'CustomerView', params: { customerId: todo.customer.id } }">{{ todo.customer.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="todo.id" :to="{ name: 'TodoEdit', params: { todoId: todo.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./todo-details.component.ts"></script>
