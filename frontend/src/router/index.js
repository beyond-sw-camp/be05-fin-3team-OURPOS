/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from "@/pages/LoginPage.vue";
import FindStoreHallPage from "@/pages/FindStoreHallPage.vue";
import MyPage from "@/pages/MyPage.vue";
import StoreOrder from "@/pages/StoreOrder.vue";
import StoreOrderCheck_company from "@/pages/StoreOrderCheck_company.vue";
import StoreOrderCheck_store from "@/pages/StoreOrderCheck_store.vue";
import CustomerMainPage from "@/pages/CustomerMainPage.vue";
import CustomerAddressManage from "@/pages/CustomerAddressManage.vue";
import CustomerOrderCheckPage from "@/pages/CustomerOrderCheckPage.vue";

import MenuPage from "@/pages/MenuPage.vue";
import MenuOnePage from "@/pages/MenuOnePage.vue";
import MainPage from "@/pages/MainPage.vue";
import CartPage from "@/pages/CartPage.vue";
import StoreLandingPage from '@/pages/StoreLandingPage.vue';
import HallOrderManagePage from '@/pages/HallOrderManagePage.vue';
import DeliveryOrderManagePage from '@/pages/DeliveryOrderManagePage.vue';
import MenuStatusManagePage from '@/pages/MenuStatusManagePage.vue';
import AdminPage from "@/pages/AdminPage.vue";
import {checkUserRole} from "@/utils/auth";
import AdminLoginPage from "@/pages/AdminLoginPage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: 'home',
      path: '/',
      component: MainPage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
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
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'store',
      path: '/stores/:id/menus',
      component: MenuPage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'storeMenu',
      path: '/stores/:storeId/menus/:menuId',
      component: MenuOnePage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'cart',
      path: '/cart',
      component: CartPage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'storeLanding',
      path: '/storeLanding',
      component: StoreLandingPage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'hallOrderManage',
      path: '/storeLanding/hallOrderManage',
      component: HallOrderManagePage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'deliveryOrderManage',
      path : '/storeLanding/deliveryOrderManage',
      component: DeliveryOrderManagePage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'menuStatusManage',
      path : '/storeLanding/menuStatusManage',
      component: MenuStatusManagePage,
      meta: { requiredRoles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'admin-login',
      path: '/admin/login',
      component: AdminLoginPage
    },
    {
      name: 'admin',
      path: '/admin',
      component: AdminPage,
      meta: { requiredRoles: ['ROLE_ADMIN', 'ROLE_SUPER_ADMIN'] }
    },
    {
      name: 'mypage',
      path: '/mypage',
      component: MyPage
    },
    {
      name: 'storeorder',
      path: '/storeorder',
      component: StoreOrder
    }, 
    {
      name: 'storeordercheck_store',
      path: '/storeorder/:storeId/checkforstore',
      component: StoreOrderCheck_store
    },
    {
      name: 'storeordercheck_company',
      path: '/storeorder/:storeId/check',
      component: StoreOrderCheck_company
    },
    {
      name: 'nfhome',
      path: '/nfhome',
      component: CustomerMainPage
    },
    {
      name: 'addressmanage',
      path: '/change-address',
      component: CustomerAddressManage
    },
    {
      name: 'ordercheck',
      path: '/order-check',
      component: CustomerOrderCheckPage
    },

  ]
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiredRoles) {
    const hasAccess = checkUserRole(to.meta.requiredRoles);
    if (hasAccess) {
      next();
    } else {
      alert('접근 권한이 없습니다.');
      next({ name: 'login' });
    }
  } else {
    next();
  }
});

export default router;
