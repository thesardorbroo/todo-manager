<template>
  <div>
    <b-dropdown-item to="/groups">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.groups')">Groups</span>
    </b-dropdown-item>
    <b-dropdown-item to="/customer">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.customer')">Customer</span>
    </b-dropdown-item>
    <b-dropdown-item to="/task">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.task')">Task</span>
    </b-dropdown-item>
    <b-dropdown-item to="/comments">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.comments')">Comments</span>
    </b-dropdown-item>
    <b-dropdown-item to="/todo-list">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.todoList')">Todo List</span>
    </b-dropdown-item>
    <b-dropdown-item to="/todo">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.todo')">Todo</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
