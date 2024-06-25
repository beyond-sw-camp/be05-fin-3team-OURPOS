<template>
  <v-container>
    <h1><v-icon @click="goToMypage">mdi-chevron-left</v-icon> 주문 확인 </h1>
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
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                <v-card-text v-else>
                  <v-icon>mdi-alpha-x-box-outline</v-icon> 주문내역이 존재하지 않습니다
                </v-card-text>
                <v-card-actions justify="center">
                  <v-btn @click="fetchHallOrders(currentHallPage - 1)" :disabled="currentHallPage === 0">prev</v-btn>
                  <v-btn @click="fetchHallOrders(currentHallPage + 1)" :disabled="currentHallPage === hallTotalPages - 1">next</v-btn>
                </v-card-actions>
              </v-col>
            </v-row>
          </v-container>
        </v-tabs-window-item>
        <v-tabs-window-item :value="2">
          <v-container fluid>
            <v-row>
              <v-col cols="12">
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
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                <v-card-text v-else>
                  <v-icon>mdi-alpha-x-box-outline</v-icon> 주문내역이 존재하지 않습니다
                </v-card-text>
                <v-card-actions justify="center">
                  <v-btn @click="fetchDeliveryOrders(currentDeliveryPage - 1)" :disabled="currentDeliveryPage === 0">prev</v-btn>
                  <v-btn @click="fetchDeliveryOrders(currentDeliveryPage + 1)" :disabled="currentDeliveryPage === deliveryTotalPages - 1">next</v-btn>
                </v-card-actions>
              </v-col>
            </v-row>
          </v-container>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card>
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title> 주문 상세 정보 </v-card-title>
        <v-card-text>
          <v-list>
            <v-list-item v-for="(detail, index) in selectedOrder.orderDetailResponseDtos" :key="index">
              <v-list-item-content>
                <v-list-item-title> 메뉴: {{ detail.menuName }} </v-list-item-title>
                <v-list-item-subtitle v-if="detail.orderOptionGroupResponseDtos.length > 0">
                  옵션:
                  <template v-for="(optionGroup, groupIndex) in detail.orderOptionGroupResponseDtos">
                    <span v-for="(option, optionIndex) in optionGroup.orderOptionResponseDtos" :key="optionIndex">
                      {{ optionGroup.optionGroupName }}: {{ option.optionName }}
                      <span v-if="groupIndex !== detail.orderOptionGroupResponseDtos.length - 1 || optionIndex !== optionGroup.orderOptionResponseDtos.length - 1">,</span>
                    </span>
                  </template>
                </v-list-item-subtitle>
                <v-list-item-subtitle> 수량: {{ detail.quantity }} </v-list-item-subtitle>
                <v-list-item-subtitle> 가격: {{ detail.price }} </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="dialog = false"> 닫기 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
  <BottomNav/>
</template>

<script>
import BottomNav from "@/components/BottomNav.vue";
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'https://ourpos.org',
  withCredentials: true,
});

export default {
  components: {
    BottomNav
  },
  data() {
    return {
      isLoading: true,
      tab: 1,
      hallOrders: [],
      deliveryOrders: [],
      dialog: false,
      selectedOrder: null,
      error: null,
      currentPage: 0,
      pageSize: 5,
      // 각 탭별 페이지와 총 페이지 수를 따로 관리합니다.
      currentHallPage: 0,
      hallTotalPages: 0,
      currentDeliveryPage: 0,
      deliveryTotalPages: 0,
    };
  },
  mounted() {
    this.fetchHallOrders();
    this.fetchDeliveryOrders();
  },
  methods: {
    async fetchHallOrders(page = 0) {
      try {
        const responseHall = await axiosInstance.get(`/api/v1/customers/my/orders/hall?page=${page}&size=${this.pageSize}`);
        this.hallOrders = responseHall.data.data.content;
        this.currentHallPage = responseHall.data.data.pageable.pageNumber;
        this.hallTotalPages = responseHall.data.data.totalPages;
      } catch (error) {
        console.error('Error fetching hall orders:', error);
        this.error = 'Failed to load hall orders. Please try again.';
      } finally {
        this.isLoading = false;
      }
    },
    async fetchDeliveryOrders(page = 0) {
      try {
        const responseDelivery = await axiosInstance.get(`/api/v1/customers/my/orders/delivery?page=${page}&size=${this.pageSize}`);
        this.deliveryOrders = responseDelivery.data.data.content;
        this.currentDeliveryPage = responseDelivery.data.data.pageable.pageNumber;
        this.deliveryTotalPages = responseDelivery.data.data.totalPages;
      } catch (error) {
        console.error('Error fetching delivery orders:', error);
        this.error = 'Failed to load delivery orders. Please try again.';
      } finally {
        this.isLoading=false;
      }
    },
    goToMypage(){
      this.$router.push('/mypage');
    },
    showOrderDetails(order){
      this.selectedOrder=order;
      this.dialog =true;
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
