<template>

  <nav class="navbar navbar-expand-lg navbar-dark bg-dark navigation-bar">
    <a class="navbar-brand" href="#">OUR POS</a>
    <div class="ml-auto">
      <router-link to="/manager">
        <button class="btn btn-outline-light">
          <i class="mdi mdi-export"></i>
        </button>
      </router-link>
    </div>

  </nav>

  <div class="py-4 container-fluid">
    <div class="row mb-4">
      <div class="col-lg-12 position-relative z-index-2">
        <div class="row mb-4 col-lg-12">
          <div class="col-lg-6 position-relative z-index-2">
            <div class="row mt-4">
              <div class="col-lg-8 col-md-6 mt-4">
                <MonthlySale/>
              </div>
              <div class="col-lg-4 col-md-6 mt-4">
                <Type/>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-lg-8 col-md-6 mt-4">
                <HourlySale />
              </div>
              <div class="col-lg-4 col-md-6 mt-4">
                <Menu/>
              </div>
            </div>
          </div>

          <div class="col-lg-6 position-relative z-index-2">
            <div class="row mt-4">
              <div class="col-lg-12 mt-4">
                <Delivery />
              </div>
            </div>
          </div>
        </div>







      </div>
    </div>

  </div>
</template>

<script setup>

import Delivery from "./components/Insight/Delivery.vue";
import Menu from "./components/Insight/Menu.vue";
import Type from "./components/Insight/Type.vue";
import HourlySale from "@/views/components/Insight/HourlySale.vue";
import MonthlySale from "@/views/components/Insight/MonthlySale.vue";

</script>

<style>
.navigation-bar {
  height: 10vh;
  margin-bottom: 10px;
}
</style>
