<template>
  <v-container>
    <h1><v-icon @click="goToMypage">mdi-chevron-left</v-icon>
    주문 확인
    </h1>
    <v-card v-if="isLoading">
      <v-card-text>Loading...</v-card-text>
    </v-card>
    <v-card v-else>
      <v-tabs v-model="tab" align-tabs="center" color="rgb(246, 162, 28)">
        <v-tab :value="1">Hall Orders</v-tab>
        <v-tab :value="2">Delivery Orders</v-tab>
      </v-tabs>

      <v-tabs-window v-model="tab">
        <v-tabs-window-item :value="1">
          <v-container fluid>
            <v-row>
              <v-col cols="12">
               
                  <v-card-title>
                    
                  </v-card-title>
                  <v-list v-if="hallOrders.length">
                    <v-list-item v-for="hallOrder in hallOrders" :key="hallOrder.orderId" @click="showOrderDetails(hallOrder)">
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
                  <v-card-text v-else><v-icon>mdi-alpha-x-box-outline</v-icon>주문내역이 존재하지 않습니다</v-card-text>
                
              </v-col>
            </v-row>
          </v-container>
        </v-tabs-window-item>
        <v-tabs-window-item :value="2">
          <v-container fluid>
            <v-row>
              <v-col cols="12">
                
                  <v-card-title>
                    
                  </v-card-title>
                  <v-list v-if="deliveryOrders.length">
                    <v-list-item v-for="deliveryOrder in deliveryOrders" :key="deliveryOrder.orderId" @click="showOrderDetails(deliveryOrder)">
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
                  <v-card-text v-else><v-icon>mdi-alpha-x-box-outline</v-icon>주문내역이 존재하지 않습니다</v-card-text>
               
              </v-col>
            </v-row>
          </v-container>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card>
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title>
          주문 상세 정보
        </v-card-title>
        <v-card-text>
          <!-- 주문 상세 정보 표시 -->
          <v-list>
            <v-list-item v-for="(detail, index) in selectedOrder.orderDetailResponseDtos" :key="index">
              <v-list-item-content>
                <v-list-item-title>메뉴: {{ detail.menuName }}</v-list-item-title>
                <v-list-item-subtitle v-if="detail.orderOptionGroupResponseDtos.length > 0">
                  옵션:
                  <template v-for="(optionGroup, groupIndex) in detail.orderOptionGroupResponseDtos">
                    <span v-for="(option, optionIndex) in optionGroup.orderOptionResponseDtos" :key="optionIndex">
                      {{ optionGroup.optionGroupName }}: {{ option.optionName }}
                      <span v-if="groupIndex !== detail.orderOptionGroupResponseDtos.length - 1 || optionIndex !== optionGroup.orderOptionResponseDtos.length - 1">,</span>
                    </span>
                  </template>
                </v-list-item-subtitle>
                <v-list-item-subtitle>수량: {{ detail.quantity }}</v-list-item-subtitle>
                <v-list-item-subtitle>가격: {{ detail.price }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="dialog = false">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
      tab: 1, // 탭 초기 값을 1로 설정
      hallOrders: [],
      deliveryOrders: [],
      dialog: false,
      selectedOrder: null
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
    },
    goToMypage() {
      // mypage로 이동하는 코드 추가
      this.$router.push('/mypage');
    },
    showOrderDetails(order) {
      this.selectedOrder = order; // 선택된 주문 정보 저장
      this.dialog = true; // 다이얼로그 표시
    }
  }
};
</script>

<style scoped>
.title {
  font-size: 1rem; /* 글씨 크기 조정 */
}
.custom-text {
  font-weight: bold;
  color: rgb(246, 162, 28); /* 적용하고자 하는 색상 */
}
</style>
