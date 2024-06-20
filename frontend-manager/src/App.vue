<!--
=========================================================
* Vue Material Dashboard 2 - v3.1.0
=========================================================

* Product Page: https://creative-tim.com/product/vue-material-dashboard-2
* Copyright 2023 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
-->
<template>
  <sidenav
    :custom_class="color"
    :class="[isRTL ? 'fixed-end' : 'fixed-start']"
    v-if="showSidenav && !$route.meta.hideSidenav"
  />
  <main
    class="main-content position-relative max-height-vh-100 h-100 overflow-x-hidden"
  >
<!--    <navbar-->
<!--      :class="[isNavFixed ? navbarFixed : '', isAbsolute ? absolute : '']"-->
<!--      :color="isAbsolute ? 'text-white opacity-8' : ''"-->
<!--      :minNav="navbarMinimize"-->
<!--      v-if="showNavbar"-->
<!--    />-->
<!--    <Navbar2/>-->
    <router-view />
<!--    <configurator-->
<!--      :toggle="toggleConfigurator"-->
<!--      :class="[showConfig ? 'show' : '', hideConfigButton ? 'd-none' : '']"-->
<!--    />-->
  </main>
</template>
<script>
import Sidenav from "./examples/Sidenav";
// import Configurator from "@/examples/Configurator.vue";
// import Navbar from "@/examples/Navbars/Navbar.vue";
// import Navbar2 from "@/examples/Navbars/Navbar2.vue";
import { mapMutations, mapState } from "vuex";

export default {
  name: "App",
  components: {
    // Navbar2,
    Sidenav,
    // Configurator,
    // Navbar,
  },
  methods: {
    ...mapMutations(["toggleConfigurator", "navbarMinimize"]),
  },
  computed: {
    ...mapState([
      "isRTL",
      "color",
      "isAbsolute",
      "isNavFixed",
      "navbarFixed",
      "absolute",
      "showSidenav",
      "showNavbar",
      "showFooter",
      "showConfig",
      "hideConfigButton",
    ]),
  },
  beforeMount() {
    this.$store.state.isTransparent = "bg-transparent";

    const sidenav = document.getElementsByClassName("g-sidenav-show")[0];

    if (window.innerWidth > 1200) {
      sidenav.classList.add("g-sidenav-pinned");
    }
  },
};
</script>
