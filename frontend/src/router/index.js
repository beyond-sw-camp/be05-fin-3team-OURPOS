/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from "@/pages/LoginPage.vue";
import FindStoreHallPage from "@/pages/FindStoreHallPage.vue";
import MenuPage from "@/pages/MenuPage.vue";
import MenuOnePage from "@/pages/MenuOnePage.vue";
import MainPage from "@/pages/MainPage.vue";
import CartPage from "@/pages/CartPage.vue";
import StoreLandingPage from '@/pages/StoreLandingPage.vue';
import HallOrderManagePage from '@/pages/HallOrderManagePage.vue';
import DeliveryOrderManagePage from '@/pages/DeliveryOrderManagePage.vue';

const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
};

const requireAuth = (to, from, next) => {
  const token = getCookie('Authorization');
  if (token) {
    return next();
  }
  next('/login');
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: 'home',
      path: '/',
      component: MainPage,
      beforeEnter: requireAuth
    },
    {
      name: 'login',
      path: '/login',
      component: LoginPage
    },
    {
      name: 'findStore',
      path: '/stores',
      component: FindStoreHallPage,
      beforeEnter: requireAuth
    },
    {
      name: 'store',
      path: '/stores/:id/menus',
      component: MenuPage,
    },
    {
      name: 'storeMenu',
      path: '/stores/:storeId/menus/:menuId',
      component: MenuOnePage,
    },
    {
      name: 'cart',
      path: '/cart',
      component: CartPage
    },
    {
      name: 'storeLanding',
      path: '/storeLanding',
      component: StoreLandingPage
    },
    {
      name: 'hallOrderManage',
      path: '/storeLanding/hallOrderManage',
      component: HallOrderManagePage
    },
    {
      name: 'deliveryOrderManage',
      path : '/storeLanding/deliveryOrderManage',
      component : DeliveryOrderManagePage
    }
  ]
});

export default router;
