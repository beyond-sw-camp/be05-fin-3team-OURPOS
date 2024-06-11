<template>
    <v-container class="text-center"> 
      <v-row>
        <v-col cols="12">
          
          <v-card>
            <v-card-title>주문 내역</v-card-title>
            <v-card-text>
              <v-list>
                <!-- 칼럼 헤더  -->
                <v-list-item>
                  <v-list-item-content class="list-item-content">
                    <v-list-item-title style="width: 20%;">주문 번호</v-list-item-title>
                    <v-list-item-title style="width: 20%;">주문 일자</v-list-item-title>
                    <v-list-item-title style="width: 20%;">주문 금액</v-list-item-title>
                    <v-list-item-title style="width: 20%;">주문 상태</v-list-item-title>
                    <v-list-item-title style="width: 20%;">기타</v-list-item-title> 
                  </v-list-item-content>
                </v-list-item>
                <!-- 주문 내역 표시 -->
                <v-list-item v-for="(order, index) in orders" :key="index">
                  <v-list-item-content class="list-item-content">
                    <!-- 주문 번호 -->
                    <v-list-item-title style="width: 20%;">{{ order.storeOrderId }}</v-list-item-title>
                    <!-- 주문 일자 -->
                    <v-list-item-title style="width: 20%;">{{ order.storeOrderDate }}</v-list-item-title>
                    <!-- 주문 금액 -->
                    <v-list-item-title style="width: 20%;">{{ order.storeOrderPrice }}</v-list-item-title>
                    <!-- 주문 상태 -->
                    <v-list-item-title style="width: 20%;">{{ order.storeOrderStatus }}</v-list-item-title>
                    <!-- 기타 -->
                    <v-list-item-title style="width: 20%;">
                      <v-btn color="red" @click="cancelOrder(order.storeOrderId)">취소</v-btn>
                      <v-btn color="primary" @click="viewOrderDetails(order.storeOrderId)">상세</v-btn>
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
  
      <!-- 주문 상세 정보  -->
      <v-dialog v-model="orderDetailDialog" max-width="600px">
        <v-card>
          <v-card-title class="headline">주문 상세 정보</v-card-title>
          <v-card-text>
            <v-list>
              <v-list-item-title>주문 항목</v-list-item-title>
              <v-list-item-content class="item-content">
                <v-row>
                  <v-col cols="4">
                    <v-list-item>
                      <v-list-item-content class="item-content">
                        <v-list-item-title>이름</v-list-item-title>
                        <v-list-item-title>가격</v-list-item-title>
                        <v-list-item-title>수량</v-list-item-title>
                      </v-list-item-content>
                    </v-list-item>
                  </v-col>
                  <v-col v-for="(item, index) in selectedOrderDetails" :key="index" cols="2.5">
                    <v-list-item>
                      <v-list-item-content class="item-content">
                        <v-list-item-title>{{ item.storeCommName }}</v-list-item-title>
                        <v-list-item-title>{{ item.storeOrderDetailPrice }}</v-list-item-title>
                        <v-list-item-title>{{ item.storeOrderDetailQuantity }}</v-list-item-title>
                      </v-list-item-content>
                    </v-list-item>
                  </v-col>
                </v-row>
              </v-list-item-content>
            </v-list>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" @click="orderDetailDialog = false">닫기</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-container>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        orders: [],
        orderDetailDialog: false,
        selectedOrderDetails: [],
      };
    },
    created() {
      this.fetchOrders();
    },
    methods: {
      async fetchOrders() {
        try {
          const response = await axios.get('http://localhost:8080/api/v1/storeorder/1/check');
          this.orders = response.data.data;
        } catch (error) {
          console.error('Failed to fetch orders:', error);
        }
      },
      async cancelOrder(storeOrderId) {
        try {
          const response = await axios.delete(`http://localhost:8080/api/v1/storeorder/1`);
          if (response.status === 200) {
            this.orders = this.orders.filter(order => order.storeOrderId !== storeOrderId);
            alert('주문이 성공적으로 취소되었습니다.');
          }
        } catch (error) {
          console.error('Failed to cancel order:', error);
          alert('주문 취소에 실패했습니다.');
        }
      },
      async viewOrderDetails(storeOrderId) {
        try {
          const response = await axios.get(`http://localhost:8080/api/v1/storeorder/${storeOrderId}`);
          this.selectedOrderDetails = response.data;
          this.orderDetailDialog = true;
        } catch (error) {
          console.error('Failed to fetch order details:', error);
        }
      }
    },
  };
  </script>
  
  <style scoped>
  .list-item-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  </style>