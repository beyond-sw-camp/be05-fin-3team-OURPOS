<template>
  <v-container>
    <h1><v-icon>mdi-chevron-left</v-icon>주문내역</h1>
    <v-card v-if="isLoading">
      <v-card-text>Loading...</v-card-text>
    </v-card>
    <v-card v-else>
      <v-row>
        <v-col cols="12" md="6">
          <v-card>
            <v-card-title>
              <h2> Hall Orders </h2>
            </v-card-title>
            <v-list>
              <v-list-item v-for="hallOrder in hallOrders" :key="hallOrder.orderId">
                <v-list-item-avatar>
                  <v-icon>mdi-food</v-icon> <!-- 홀 주문 아이콘 -->
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title class="custom-text">{{ hallOrder.hallOrderStatus }}</v-list-item-title>
                  <v-list-item-title v-if="hallOrder.orderDetailResponseDtos && hallOrder.orderDetailResponseDtos.length > 0">
                    {{ hallOrder.orderDetailResponseDtos[0].menuName }}
                  </v-list-item-title>
                  <v-list-item-subtitle>주문 ID: {{ hallOrder.orderId }}</v-list-item-subtitle>
                  <v-list-item-subtitle>주문 시간: {{ hallOrder.orderCreatedDateTime }}</v-list-item-subtitle>
                  <v-list-item-subtitle>가격: {{ hallOrder.price }}</v-list-item-subtitle>
                  <!-- 다른 주문 정보들을 여기에 표시 -->
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card>
        </v-col>
        <v-col cols="12" md="6">
          <v-card>
            <v-card-title>
              <h2> Delivery Orders </h2>
            </v-card-title>
            <v-list>
              <v-list-item v-for="deliveryOrder in deliveryOrders" :key="deliveryOrder.orderId">
                <v-list-item-avatar>
                  <v-icon>mdi-motorbike</v-icon> <!-- 배달 주문 아이콘 -->
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title class="custom-text">{{ deliveryOrder.deliveryOrderStatus }}</v-list-item-title>
                  <v-list-item-title v-if="deliveryOrder.orderDetailResponseDtos && deliveryOrder.orderDetailResponseDtos.length > 0">
                    {{ deliveryOrder.orderDetailResponseDtos[0].menuName }}
                  </v-list-item-title>
                  <v-list-item-subtitle>주문 ID: {{ deliveryOrder.orderId }}</v-list-item-subtitle>
                  <v-list-item-subtitle>주문 시간: {{ deliveryOrder.orderCreatedDateTime }}</v-list-item-subtitle>
                  <v-list-item-subtitle>가격: {{ deliveryOrder.price }}</v-list-item-subtitle>
                  <!-- 다른 주문 정보들을 여기에 표시 -->
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
  <BottomNav/>
</template>

<script>
import BottomNav from "@/components/BottomNav.vue";
import axios from 'axios';

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', 
  withCredentials: true, // 쿠키를 전송하기 위해 필요
});

export default {
  components: {
    BottomNav
  },
  data() {
    return {
      isLoading: true,
      hallOrders: [],
      deliveryOrders: []
    };
  },
  mounted() {
    this.fetchOrders();
  },
  methods: {
    async fetchOrders() {
      try {
        
        // Axios 인스턴스를 사용하여 백엔드 API에 요청을 보냄
        const responseHall = await axiosInstance.get('/api/v1/customers/my/orders/hall');
        this.hallOrders = responseHall.data.data.content;
        
        const responseDelivery = await axiosInstance.get('/api/v1/customers/my/orders/delivery');
        this.deliveryOrders = responseDelivery.data.data.content;

        this.isLoading = false;
      } catch (error) {
        console.error('Error fetching orders:', error);
        this.isLoading = false;
      }
    }
  }
};
</script>

<style scoped>
.custom-text {
  font-weight: bold;
  color: rgb(246, 162, 28); /* 적용하고자 하는 색상 */
}
</style>