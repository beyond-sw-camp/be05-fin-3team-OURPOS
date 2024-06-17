<template>
  <v-app-bar app dark>
    <v-toolbar-title>
      <v-btn icon @click="goBack">
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
      {{ storeName }} {{ title }}
    </v-toolbar-title>
    <v-toolbar-items>
      <v-btn icon>
        <v-badge :content="cartItemCount" color="red" overlap>
          <v-icon size="34" class="mb-2" @click="goToCart">mdi-truck-delivery</v-icon>
        </v-badge>
      </v-btn>
    </v-toolbar-items>
  </v-app-bar>
</template>


<script setup>
import {computed, ref} from 'vue';
import {useRouter} from 'vue-router';
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {faShoppingCart} from "@fortawesome/free-solid-svg-icons";
import {defineProps} from 'vue';

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
  router.push('/cart/delivery');
};

// Computed property to get the count of items in the cart
const cartItemCount = computed(() => {
  const deliveryOrder = JSON.parse(localStorage.getItem('deliveryOrder')) || {};
  const orderDetailDtos = deliveryOrder.orderDetailDtos || [];
  return orderDetailDtos.length;
});

storeName.value = localStorage.getItem('deliveryOrder') ? JSON.parse(localStorage.getItem('deliveryOrder')).storeName : '';
</script>


<style scoped>
</style>
