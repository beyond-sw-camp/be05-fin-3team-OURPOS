<template>
    <div>
      <v-list dense>
        <v-list-item-group>
          <v-list-item v-for="(order, index) in orders" :key="order.storeOrderId" align="center">
            <!-- 주문 상태, 주문번호, 주문일시, 주문금액, 상점 ID 행 -->
            <v-row>
              <v-col><div>주문상태</div></v-col>
              <v-col><div>주문번호</div></v-col>
              <v-col><div>주문일시</div></v-col>
               <v-col><div>주문 금액</div></v-col>
               <v-col><div>지점명</div></v-col>
            </v-row>

            <!-- chips, storeOrderId, storeOrderDate, storeOrderPrice, storeId -->
            <v-row>
              <v-col>
                <v-chip :color="statusColor(order.storeOrderStatus)" dark @click="openDialog(order)">{{ order.storeOrderStatus }}</v-chip>
              </v-col>
              <v-col><div>{{ order.storeOrderId }}</div></v-col>
              <v-col><div>{{ order.storeOrderDate }}</div></v-col>
              <v-col><div>{{ order.storeOrderPrice }}</div></v-col>
              <v-col><div>{{ order.storeName }}</div></v-col>
            </v-row>

            <!-- 간격조정 -->
            <v-divider v-if="index !== orders.length - 1" :key="'divider' + index"></v-divider>
          </v-list-item>
        </v-list-item-group>
      </v-list>

      <v-dialog v-model="dialog" max-width="600px">
        <v-card>
              <v-card-title>
                  <span class="headline">Order Details</span>
              </v-card-title>
              <v-card-text>
                  <v-list-item>
                      <v-list-item-content>
                          <v-list-item-title>Order Content: {{ selectedOrder.storeCommName }}</v-list-item-title>
                          <v-list-item-title>Order Date: {{ selectedOrder.storeOrderDate }}</v-list-item-title>
                          <v-list-item-title>Order Price: {{ selectedOrder.storeOrderPrice }}</v-list-item-title>
                          <v-list-item-title>Store Name: {{ selectedOrder.storeName }}</v-list-item-title>
                          <v-list-item-title>Store Address: {{ selectedOrder.addressDetail }}</v-list-item-title>
                          <v-list-item-title>Store Phone: {{ selectedOrder.storePhone }}</v-list-item-title>
                      </v-list-item-content>
                  </v-list-item>
              </v-card-text>
              <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn v-if="selectedOrder.storeOrderStatus === 'WAITING'" color="blue darken-1" @click="updateOrderStatus('accepted')">주문 승인</v-btn>
                  <v-btn v-if="selectedOrder.storeOrderStatus === 'ACCEPTED'" color="blue darken-1" @click="updateOrderStatus('delivering')">배송 시작</v-btn>
                  <v-btn v-if="selectedOrder.storeOrderStatus === 'DELIVERING'" color="blue darken-1" @click="updateOrderStatus('complete')">배송 완료</v-btn>
                  <v-btn color="blue darken-1" text @click="dialog = false">Close</v-btn>
              </v-card-actions>
          </v-card>
      </v-dialog>

      <v-tabs v-model="tab">
        <v-tab v-for="status in statuses" :key="status.value">{{ status.label }}</v-tab>
      </v-tabs>
      <v-tabs-items v-model="tab">
        <v-tab-item v-for="status in statuses" :key="status.value">
          <OrderTable :orders="filteredOrders" @refresh-orders="fetchOrders" />
        </v-tab-item>
      </v-tabs-items>
    </div>
  </template>

  <script>
  import axios from 'axios';

  export default {
    props: {
      orders: {
        type: Array,
        required: true,
      },
      statuses: {
        type: Array,
        required: true,
      }
    },
    data() {
      return {
        dialog: false,
        selectedOrder: {},
      };
    },
    methods: {
      statusColor(status) {
        switch (status) {
          case 'WAITING': return 'orange';
          case 'ACCEPTED': return 'blue';
          case 'DELIVERING': return 'purple';
          case 'COMPLETED': return 'green';
          default: return 'grey';
        }
      },
      openDialog(order) {
        this.selectedOrder = order;
        this.dialog = true;
      },
      async updateOrderStatus(action) {
        const actions = {
          accepted: `https://ourpos.org/api/v1/storeorder/${this.selectedOrder.storeOrderId}/accepted`,
          delivering: `https://ourpos.org/api/v1/storeorder/${this.selectedOrder.storeOrderId}/delivering`,
          complete: `https://ourpos.org/api/v1/storeorder/${this.selectedOrder.storeOrderId}/complete`
        };

        try {
          const response = await axios.put(actions[action]);
          if (response.status !== 200) throw new Error('Status update failed');
          // Refresh orders after successful update
          this.$emit('refresh-orders');
          this.dialog = false;
        } catch (error) {
          console.error('Error updating order status:', error);
        }
      },
      filteredOrders() {
        return this.orders;
      }
    }
  };
  </script>
