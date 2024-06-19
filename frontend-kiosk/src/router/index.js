/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import {createRouter, createWebHistory} from 'vue-router';
import MainPage from "@/pages/MainPage.vue";
import MenusPage from "@/pages/MenusPage.vue";
import {checkUserRole} from "@/utils/auth";
import LoginPage from "@/pages/LoginPage.vue";
import HallCheckoutView from "@/pages/hallpayments/HallCheckoutView.vue";
import HallSuccessView from "@/pages/hallpayments/HallSuccessView.vue";
import HallFailView from "@/pages/hallpayments/HallFailView.vue";

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/',
    name: 'MainPage',
    component: MainPage,
    meta: {requiredRoles: ['ROLE_ADMIN']}
  },
  {
    path: '/menus',
    name: 'menus',
    component: MenusPage,
    meta: {requiredRoles: ['ROLE_ADMIN']}
  },
  {
    path: '/hall/pay',
    name: 'hall-pay',
    component: HallCheckoutView,
    meta: {requiredRoles: ['ROLE_ADMIN']}
  },
  {
    path: '/hall/success',
    name: 'hall-success',
    component: HallSuccessView,
    meta: {requiredRoles: ['ROLE_ADMIN']}
  },
  {
    path: '/hall/fail',
    name: 'hall-fail',
    component: HallFailView,
    meta: {requiredRoles: ['ROLE_ADMIN']}
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiredRoles) {
    const hasAccess = checkUserRole(to.meta.requiredRoles);
    if (hasAccess) {
      next();
    } else {
      alert('로그인이 필요합니다.');
      next({name: 'Login'});
    }
  } else {
    next();
  }
});

export default router
