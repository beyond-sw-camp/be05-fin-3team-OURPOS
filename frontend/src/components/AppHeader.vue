<template>
  <v-app-bar app dark>
    <v-btn icon @click="goBack">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-toolbar-title>{{ title }}</v-toolbar-title>
    <v-toolbar-items>
      <v-btn icon>
        <FontAwesomeIcon :icon="faShoppingCart" @click="goToCart"/>
      </v-btn>
    </v-toolbar-items>
  </v-app-bar>
</template>

<script setup>
import { useRouter } from 'vue-router';
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {faShoppingCart} from "@fortawesome/free-solid-svg-icons";
import { defineProps } from 'vue';

// Setup router
const router = useRouter();

// Create a reactive title property
defineProps(['title']);

const goToCart = () => {
  router.push('/cart');
};

// Method to go back to the previous page
const goBack = () => {
  router.back();
};

</script>

<style scoped>
/* Add any custom styles if necessary */
</style>
