<template>
  <v-app-bar app dark>
    <v-btn icon @click="goBack">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-toolbar-title>{{ title }}</v-toolbar-title>
  </v-app-bar>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ref } from 'vue';

// Setup router
const router = useRouter();

// Create a reactive title property
const title = ref('');

// Method to go back to the previous page
const goBack = () => {
  router.back();
};

// Accept title as a prop
defineProps({
  title: String
});
</script>

<style scoped>
/* Add any custom styles if necessary */
</style>
