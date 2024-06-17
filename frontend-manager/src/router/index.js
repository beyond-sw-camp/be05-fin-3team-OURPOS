import { createRouter, createWebHistory } from "vue-router";
import Tables from "../views/Tables.vue";
import Billing from "../views/Billing.vue";
import RTL from "../views/Rtl.vue";
import Notifications from "../views/Notifications.vue";
import Profile from "../views/Profile.vue";
import SignUp from "../views/SignUp.vue";
import SubDash from "../views/SubDashboard.vue";
import {checkUserRole} from "@/utils/auth";
import ManagerHome from "@/views/ManagerHome.vue";
import OwnerHome from "@/views/OwnerHome.vue";
import StoreOrderCheck_company from "../views/StoreOrderCheck_company.vue";
import HallOrderManage from "../views/HallOrderManage.vue";
import StoreOrder from "../views/StoreOrder.vue";
import StoreOrderCheck_store from "../views/StoreOrderCheck_store.vue";


const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/sign-up",
  },
  {
    path: "/owner",
    name: "OwnerHome",
    component: OwnerHome,
    meta: { requiredRoles: ['ROLE_SUPER_ADMIN'] }
  },
  {
    path: "/tables",
    name: "Tables",
    component: Tables,
  },
  {
    path: "/billing",
    name: "Billing",
    component: Billing,
  },
  {
    path: "/rtl-page",
    name: "RTL",
    component: RTL,
  },
  {
    path: "/notifications",
    name: "Notifications",
    component: Notifications,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/sign-up",
    name: "SignUp",
    component: SignUp,
  },
  {
    path: "/subdash",
    name: "SubDash",
    component: SubDash,
    meta: { requiredRoles: ['ROLE_SUPER_ADMIN'] }
  },
  {
    path: "/manager",
    name: "ManagerHome",
    component: ManagerHome,
    meta: { requiredRoles: ['ROLE_ADMIN'] , hideSidenav: true}
  },
  {
    path: "/sign-up",
    name: "SignUp",
    component: SignUp,
  },
  {
    name: 'storeordercheck_company',
    path: '/storeorder/:storeId/check',
    component: StoreOrderCheck_company
  },
  {
    name: 'storeordercheck_store',
    path: '/storeorder/:storeId/checkforstore',
    component: StoreOrderCheck_store
  },
  {
    name: 'HallOrderManage',
    path: '/hallordermanage',
    component: HallOrderManage
  },
  {
    name: 'StoreOrder',
    path: '/storeorder',
    component: StoreOrder
  },

];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});
router.beforeEach((to, from, next) => {
  if (to.meta.requiredRoles) {
    const hasAccess = checkUserRole(to.meta.requiredRoles);
    console.log(hasAccess);
    if (hasAccess) {
      next();
    } else {
      alert('접근 권한이 없습니다.');
      next({ name: 'SignUp' });
    }
  } else {
    next();
  }
});
export default router;
