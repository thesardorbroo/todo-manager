<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="managerApp.task.home.createOrEditLabel"
          data-cy="TaskCreateUpdateHeading"
          v-text="$t('managerApp.task.home.createOrEditLabel')"
        >
          Create or edit a Task
        </h2>
        <div>
          <div class="form-group" v-if="task.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="task.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('managerApp.task.body')" for="task-body">Body</label>
            <input
              type="text"
              class="form-control"
              name="body"
              id="task-body"
              data-cy="body"
              :class="{ valid: !$v.task.body.$invalid, invalid: $v.task.body.$invalid }"
              v-model="$v.task.body.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('managerApp.task.image')" for="task-image">Image</label>
            <div>
              <img
                v-bind:src="'data:' + task.imageContentType + ';base64,' + task.image"
                style="max-height: 100px"
                v-if="task.image"
                alt="task image"
              />
              <div v-if="task.image" class="form-text text-danger clearfix">
                <span class="pull-left">{{ task.imageContentType }}, {{ byteSize(task.image) }}</span>
                <button
                  type="button"
                  v-on:click="clearInputImage('image', 'imageContentType', 'file_image')"
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_image"
                id="file_image"
                data-cy="image"
                v-on:change="setFileData($event, task, 'image', true)"
                accept="image/*"
                v-text="$t('entity.action.addimage')"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="image"
              id="task-image"
              data-cy="image"
              :class="{ valid: !$v.task.image.$invalid, invalid: $v.task.image.$invalid }"
              v-model="$v.task.image.$model"
            />
            <input type="hidden" class="form-control" name="imageContentType" id="task-imageContentType" v-model="task.imageContentType" />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('managerApp.task.caption')" for="task-caption">Caption</label>
            <input
              type="text"
              class="form-control"
              name="caption"
              id="task-caption"
              data-cy="caption"
              :class="{ valid: !$v.task.caption.$invalid, invalid: $v.task.caption.$invalid }"
              v-model="$v.task.caption.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('managerApp.task.groups')" for="task-groups">Groups</label>
            <select
              class="form-control"
              id="task-groups"
              data-cy="groups"
              multiple
              name="groups"
              v-if="task.groups !== undefined"
              v-model="task.groups"
            >
              <option v-bind:value="getSelected(task.groups, groupsOption)" v-for="groupsOption in groups" :key="groupsOption.id">
                {{ groupsOption.id }}
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
            :disabled="$v.task.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./task-update.component.ts"></script>
