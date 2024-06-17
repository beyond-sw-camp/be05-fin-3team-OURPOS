<template>
  <div>
    <div class="row">
      <!-- 주문 목록 -->
      <div class="col-8">
        <ul class="list-group">
          <li class="list-group-item" v-for="(order, index) in orders" :key="order.storeOrderId">
            <!-- 주문 상태, 주문번호, 주문일시, 주문금액, 상점 ID 행 -->
            <div class="row">
              <div class="col"><div>주문상태</div></div>
              <div class="col"><div>주문번호</div></div>
              <div class="col"><div>주문일시</div></div>
              <div class="col"><div>주문금액</div></div>
              <div class="col"><div>지점명</div></div>
            </div>
            <!-- chips, storeOrderId, storeOrderDate, storeOrderPrice, storeId -->
            <div class="row">
              <div class="col">
                <button :class="statusColor(order.storeOrderStatus)" @click="openOrderDetails(order)">
                  {{ order.storeOrderStatus }}
                </button>
              </div>
              <div class="col"><div>{{ order.storeOrderId }}</div></div>
              <div class="col"><div>{{ order.storeOrderDate }}</div></div>
              <div class="col"><div>{{ order.storeOrderPrice }}</div></div>
              <div class="col"><div>{{ order.storeName }}</div></div>
            </div>
            <!-- 간격조정 -->
            <hr v-if="index !== orders.length - 1" :key="'divider' + index" />
          </li>
        </ul>
      </div>

      <!-- 주문 상세 정보 및 상태 변경 버튼 -->
      <div class="col-4" v-if="selectedOrder.storeOrderId">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Order Details</h5>
            <ul class="list-group">
              <li class="list-group-item">Order Content: {{ selectedOrder.storeCommName }}</li>
              <li class="list-group-item">Order Date: {{ selectedOrder.storeOrderDate }}</li>
              <li class="list-group-item">Order Price: {{ selectedOrder.storeOrderPrice }}</li>
              <li class="list-group-item">Store Name: {{ selectedOrder.storeName }}</li>
              <li class="list-group-item">Store Address: {{ selectedOrder.addressDetail }}</li>
              <li class="list-group-item">Store Phone: {{ selectedOrder.storePhone }}</li>
            </ul>
            <div class="mt-3">
              <button v-if="selectedOrder.storeOrderStatus === 'WAITING'" type="button" class="btn btn-primary" @click="updateOrderStatus('accepted')">주문 승인</button>
              <button v-if="selectedOrder.storeOrderStatus === 'ACCEPTED'" type="button" class="btn btn-primary" @click="updateOrderStatus('delivering')">배송 시작</button>
              <button v-if="selectedOrder.storeOrderStatus === 'DELIVERING'" type="button" class="btn btn-primary" @click="updateOrderStatus('complete')">배송 완료</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'; 

export default {
  props: {
    orders: {
      type: Array,
      required: true,
    }
  },
  data() {
    return {
      selectedOrder: {},
    };
  },
  methods: {
    statusColor(status) {
      switch (status) {
        case 'WAITING': return 'badge badge-warning';
        case 'ACCEPTED': return 'badge badge-primary';
        case 'DELIVERING': return 'badge badge-secondary';
        case 'COMPLETED': return 'badge badge-success';
        default: return 'badge badge-light';
      }
    },
    openOrderDetails(order) {
      this.selectedOrder = order;
    },
    async updateOrderStatus(action) {
  const actions = {
    accepted: `/api/v1/storeorder/${this.selectedOrder.storeOrderId}/accepted`,
    delivering: `/api/v1/storeorder/${this.selectedOrder.storeOrderId}/delivering`,
    complete: `/api/v1/storeorder/${this.selectedOrder.storeOrderId}/complete`
  };

  try {
    const token = localStorage.getItem('token'); 
    const response = await axios.put(actions[action], {}, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` 
      },
      withCredentials: true
    });

    if (response.status !== 200) {
      throw new Error('Status update failed');
    }

   
    this.selectedOrder = {};

   
    this.$emit('refresh-orders');
  } catch (error) {
    console.error('Error updating order status:', error);
  }
}
  }
}

</script>

<style scoped>
.table-responsive {
  overflow-x: auto;
}
.card {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.text-dark {
  color: #000 !important; 
}
</style>
