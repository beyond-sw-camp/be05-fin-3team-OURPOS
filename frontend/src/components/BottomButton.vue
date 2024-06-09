<template>
  <v-container class="d-flex flex-column justify-end">
    <v-row>
      <v-col cols="12" md="4" sm="6">
        <v-btn rounded="lg" size="x-large" block class="mb-4">{{ msg }}</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { defineProps } from 'vue';

defineProps(['msg']);
</script>

<style scoped>

</style>
