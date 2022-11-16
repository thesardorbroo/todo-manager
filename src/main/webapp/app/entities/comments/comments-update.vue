<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="managerApp.comments.home.createOrEditLabel"
          data-cy="CommentsCreateUpdateHeading"
          v-text="$t('managerApp.comments.home.createOrEditLabel')"
        >
          Create or edit a Comments
        </h2>
        <div>
          <div class="form-group" v-if="comments.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="comments.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('managerApp.comments.message')" for="comments-message">Message</label>
            <input
              type="text"
              class="form-control"
              name="message"
              id="comments-message"
              data-cy="message"
              :class="{ valid: !$v.comments.message.$invalid, invalid: $v.comments.message.$invalid }"
              v-model="$v.comments.message.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('managerApp.comments.task')" for="comments-task">Task</label>
            <select class="form-control" id="comments-task" data-cy="task" name="task" v-model="comments.task">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="comments.task && taskOption.id === comments.task.id ? comments.task : taskOption"
                v-for="taskOption in tasks"
                :key="taskOption.id"
              >
                {{ taskOption.id }}
              </option>
            </select>
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
            :disabled="$v.comments.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./comments-update.component.ts"></script>
