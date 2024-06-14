import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "../views/Dashboard.vue";
import Tables from "../views/Tables.vue";
import Billing from "../views/Billing.vue";
import RTL from "../views/Rtl.vue";
import Notifications from "../views/Notifications.vue";
import Profile from "../views/Profile.vue";
import SignUp from "../views/SignUp.vue";
import StoreLanding from '../views/StoreLanding.vue';
import DeliveryOrderManage from '../views/DeliveryOrderManage.vue';
import MenuStatusManage from "../views/MenuStatusManage.vue";
import HeadOfficeLanding from "../views/HeadOfficeLanding.vue";
import MenuManage from "../views/MenuManage.vue";
import MenuOptionGroupManage from "../views/MenuOptionGroupManage.vue";

const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/dashboard",
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
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
    path:"/store-landing",
    name:"StoreLanding",
    component:StoreLanding, 
  },
  {
    path:"/store-landing/delivery-order-manage",
    name:"DeliveryOrderManage",
    component:DeliveryOrderManage, 
  },
  {
    path:"/store-landing/menu-status-manage",
    name:"MenuStatusManage",
    component:MenuStatusManage, 
  },
  {
    path:"/head-office-landing",
    name:"HeadOfficeLanding",
    component:HeadOfficeLanding, 
  },
  {
    path:"/head-office-landing/menu-manage",
    name:"MenuManage",
    component:MenuManage, 
  },
  {
    path:"/head-office-landing/menu-option-group-manage",
    name:"MenuOptionGroupManage",
    component:MenuOptionGroupManage, 
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

export default router;
