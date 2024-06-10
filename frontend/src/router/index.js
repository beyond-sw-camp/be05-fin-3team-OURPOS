
/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import {createRouter, createWebHistory} from 'vue-router';
import LoginPage from "@/pages/LoginPage.vue";
import FindStoreHallPage from "@/pages/FindStoreHallPage.vue";
import MyPage from "@/pages/MyPage.vue";
import StoreOrder from "@/pages/StoreOrder.vue";
import StoreOrderCheck_company from "@/pages/StoreOrderCheck_company.vue";
import StoreOrderCheck_store from "@/pages/StoreOrderCheck_store.vue";
import CustomerMainPage from "@/pages/CustomerMainPage.vue";
import CustomerAddressManage from "@/pages/CustomerAddressManage.vue";
import CustomerOrderCheckPage from "@/pages/CustomerOrderCheckPage.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: 'login',
      path: '/login',
      component: LoginPage
    },
    {
      name: 'findStore',
      path: '/stores',
      component: FindStoreHallPage
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
})

export default router
