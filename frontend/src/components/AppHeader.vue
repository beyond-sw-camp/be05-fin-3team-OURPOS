<template>
  <v-app-bar app dark>
    <v-btn icon @click="goBack">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-toolbar-title>{{storeName}} {{ title }}</v-toolbar-title>
    <v-toolbar-items>
      <v-btn>
        <v-badge :content="cartItemCount" color="red" overlap>
          <v-icon size="36" class="mb-2" @click="goToCart">mdi-food</v-icon>
        </v-badge>
      </v-btn>
    </v-toolbar-items>
  </v-app-bar>
</template>


<script setup>
import {computed, ref} from 'vue';
import { useRouter } from 'vue-router';
import { defineProps } from 'vue';

// Setup router
const router = useRouter();

// Create a reactive title property
defineProps(['title']);
const storeName = ref('');

// Method to go back to the previous page
const goBack = () => {
  router.back();
};

// Method to navigate to cart page
const goToCart = () => {
  router.push('/cart');
};

// Computed property to get the count of items in the cart
const cartItemCount = computed(() => {
  const fullOrder = JSON.parse(localStorage.getItem('fullOrder')) || {};
  const orderDetailDtos = fullOrder.orderDetailDtos || [];
  return orderDetailDtos.length;
});

storeName.value = localStorage.getItem('fullOrder') ? JSON.parse(localStorage.getItem('fullOrder')).storeName : '';
</script>


<style scoped>
</style>
