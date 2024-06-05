
/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import {createRouter, createWebHistory} from 'vue-router';
import LoginPage from "@/pages/LoginPage.vue";
import StoreLandingPage from '@/pages/StroreLandingPage.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: 'login',
      path: '/login',
      component: LoginPage
    },
    {
      name: 'storeLanding',
      path: '/storeLanding',
      component: StoreLandingPage
    }
  ]
})

export default router
