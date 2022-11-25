<template>
  <div>
    <h2 id="page-heading" data-cy="TodoHeading">
      <span v-text="$t('managerApp.todo.home.title')" id="todo-heading">Todos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('managerApp.todo.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'TodoCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-todo">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('managerApp.todo.home.createLabel')"> Create a new Todo </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && todos && todos.length === 0">
      <span v-text="$t('managerApp.todo.home.notFound')">No todos found</span>
    </div>
    <div class="table-responsive" v-if="todos && todos.length > 0">
      <table class="table table-striped" aria-describedby="todos">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('managerApp.todo.createdAt')">Created At</span></th>
            <th scope="row"><span v-text="$t('managerApp.todo.none')">None</span></th>
            <th scope="row"><span v-text="$t('managerApp.todo.task')">Task</span></th>
            <th scope="row"><span v-text="$t('managerApp.todo.customer')">Customer</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="todo in todos" :key="todo.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TodoView', params: { todoId: todo.id } }">{{ todo.id }}</router-link>
            </td>
            <td>{{ todo.createdAt }}</td>
            <td>{{ todo.none }}</td>
            <td>
              <div v-if="todo.task">
                <router-link :to="{ name: 'TaskView', params: { taskId: todo.task.id } }">{{ todo.task.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="todo.customer">
                <router-link :to="{ name: 'CustomerView', params: { customerId: todo.customer.id } }">{{ todo.customer.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TodoView', params: { todoId: todo.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TodoEdit', params: { todoId: todo.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(todo)"
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
        ><span id="managerApp.todo.delete.question" data-cy="todoDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-todo-heading" v-text="$t('managerApp.todo.delete.question', { id: removeId })">
          Are you sure you want to delete this Todo?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-todo"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeTodo()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./todo.component.ts"></script>
