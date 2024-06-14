<template>
    <v-app>
      <v-container fluid>
        <v-row>
          <v-col cols="6">
            <v-row>
              <v-col cols="6">
                <v-card class="pa-4">
                  <MonthSaleAll/>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card class="pa-4">
                  <MealTypeAll/>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card class="pa-4">
                  <MealTimeAll/>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card class="pa-4">
                  <MenuPreferAll/>
                </v-card>
              </v-col>
            </v-row>
          </v-col>
          <v-col cols="6">
            <v-card class="pa-4 mb-4">
              <DeliveryFrequencyAll/>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-app>
  </template>
  
  <script setup>
  import MonthSaleAll from "@/components/Insight/MonthSaleAll.vue";
  import MealTypeAll from "@/components/Insight/MealTypeAll.vue";
  import MenuPreferAll from "@/components/Insight/MenuPreferAll.vue";
  import MealTimeAll from "@/components/Insight/MealTimeAll.vue";
  import DeliveryFrequencyAll from "@/components/Insight/DeliveryFrequencyAll.vue";
  
  </script>
  
  <style scoped>
  .v-card {
    border: 1px solid #ccc;
    box-sizing: border-box;
  }
  </style>
  