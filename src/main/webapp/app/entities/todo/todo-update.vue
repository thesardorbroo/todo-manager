<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="managerApp.todo.home.createOrEditLabel"
          data-cy="TodoCreateUpdateHeading"
          v-text="$t('managerApp.todo.home.createOrEditLabel')"
        >
          Create or edit a Todo
        </h2>
        <div>
          <div class="form-group" v-if="todo.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="todo.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('managerApp.todo.createdAt')" for="todo-createdAt">Created At</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="todo-createdAt"
                  v-model="$v.todo.createdAt.$model"
                  name="createdAt"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="todo-createdAt"
                data-cy="createdAt"
                type="text"
                class="form-control"
                name="createdAt"
                :class="{ valid: !$v.todo.createdAt.$invalid, invalid: $v.todo.createdAt.$invalid }"
                v-model="$v.todo.createdAt.$model"
              />
            </b-input-group>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.todo.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./todo-update.component.ts"></script>
