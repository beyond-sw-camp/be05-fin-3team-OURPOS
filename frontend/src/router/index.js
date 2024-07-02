/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

import {createRouter, createWebHistory} from 'vue-router';
import LoginPage from "@/pages/login/LoginPage.vue";
import FindStoreHallPage from "@/pages/hallorder/FindStoreHallPage.vue";
import MyPage from "@/pages/MyPage.vue";
import CustomerAddressManage from "@/pages/CustomerAddressManage.vue";
import CustomerOrderCheckPage from "@/pages/CustomerOrderCheckPage.vue";

import MenusPage from "@/pages/hallorder/MenusPage.vue";
import MenuOnePage from "@/pages/hallorder/MenuOnePage.vue";
import MainPage from "@/pages/MainPage.vue";
import CartPage from "@/pages/hallorder/CartPage.vue";
import {checkUserRole} from "@/utils/auth";
import SignupSuccess from "@/pages/SignupSuccess.vue";

import FindStoreDeliveryPage from "@/pages/deliveryorder/FindStoreDeliveryPage.vue";
import DeliveryAddress from "@/pages/deliveryorder/DeliveryAddress.vue";
import DeliveryMenusPage from "@/pages/deliveryorder/DeliveryMenusPage.vue";
import DeliveryMenuOnePage from "@/pages/deliveryorder/DeliveryMenuOnePage.vue";
import DeliveryCartPage from "@/pages/deliveryorder/DeliveryCartPage.vue";
import SuccessView from "@/pages/deliveryorder/payments/SuccessView.vue";
import FailView from "@/pages/deliveryorder/payments/FailView.vue";
import CheckoutView from "@/pages/deliveryorder/payments/CheckoutView.vue";
import HallCheckoutView from "@/pages/hallorder/hallpayments/HallCheckoutView.vue";
import HallSuccessView from "@/pages/hallorder/hallpayments/HallSuccessView.vue";
import HallFailView from "@/pages/hallorder/hallpayments/HallFailView.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: 'home',
      path: '/',
      component: MainPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'login',
      path: '/login',
      component: LoginPage
    },
    {
      name: 'findHallStore',
      path: '/stores',
      component: FindStoreHallPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'findDeliveryStore',
      path: '/stores/delivery',
      component: FindStoreDeliveryPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'deliveryAddress',
      path: '/stores/delivery/address',
      component: DeliveryAddress,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'deliveryStoreMenus',
      path: '/stores/:id/delivery/menus',
      component: DeliveryMenusPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'deliveryStoreMenu',
      path: '/stores/:storeId/delivery/menus/:menuId',
      component: DeliveryMenuOnePage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'deliveryCart',
      path: '/cart/delivery',
      component: DeliveryCartPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'store',
      path: '/stores/:id/menus',
      component: MenusPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'storeMenu',
      path: '/stores/:storeId/menus/:menuId',
      component: MenuOnePage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'cart',
      path: '/cart',
      component: CartPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },

    {
      name: 'signup-success',
      path: '/signup-success',
      component: SignupSuccess
    },

    {
      name: 'mypage',
      path: '/mypage',
      component: MyPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },




    {
      name: 'addressmanage',
      path: '/change-address',
      component: CustomerAddressManage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'ordercheck',
      path: '/order-check',
      component: CustomerOrderCheckPage,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      name: 'pay',
      path: '/pay',
      component: CheckoutView,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      path: '/success',
      name: 'success',
      component: SuccessView,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      path: '/fail',
      name: 'fail',
      component: FailView,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      path: '/hall/pay',
      name: 'hall-pay',
      component: HallCheckoutView,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      path: '/hall/success',
      name: 'hall-success',
      component: HallSuccessView,
      meta: {requiredRoles: ['ROLE_USER']}
    },
    {
      path: '/hall/fail',
      name: 'hall-fail',
      component: HallFailView,
      meta: {requiredRoles: ['ROLE_USER']}
    }

  ]
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiredRoles) {
    const hasAccess = checkUserRole(to.meta.requiredRoles);
    if (hasAccess) {
      next();
    } else {
      alert('접근 권한이 없습니다.');
      next({name: 'login'});
    }
  } else {
    next();
  }
});

export default router;
