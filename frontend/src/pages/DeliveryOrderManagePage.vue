<template>
  <div>
    <v-toolbar dark prominent class="navigation-bar">
      <v-toolbar-title>OUR POS</v-toolbar-title>
      <v-spacer></v-spacer>
      <router-link to="/storeLanding">
        <v-btn icon>
          <v-icon>mdi-export</v-icon>
        </v-btn>
      </router-link>
    </v-toolbar>

    <v-container fluid>
      <v-row>
        <v-col cols="9">
          <v-card class="pa-4 order-list">
            <v-row>
              <v-col>
                <v-btn :color="selectedTab === '대기' ? 'light-green' : 'white'" @click="selectTab('대기')">
                  대기
                </v-btn>
              </v-col>
              <v-col>
                <v-btn :color="selectedTab === '조리중' ? 'light-green' : 'white'" @click="selectTab('조리중')">
                  조리중
                </v-btn>
              </v-col>
              <v-col>
                <v-btn :color="selectedTab === '배달중' ? 'light-green' : 'white'" @click="selectTab('배달중')">
                  배달중
                </v-btn>
              </v-col>
              <v-col>
                <v-btn :color="selectedTab === '배달완료' ? 'light-green' : 'white'" @click="selectTab('배달완료')">
                  배달완료
                </v-btn>
              </v-col>
            </v-row>
            <v-row class="mt-4">
              <v-col>
                <div class="order-title">주문번호</div>
              </v-col>
              <v-col>
                <div class="order-title">주문일시</div>
              </v-col>
              <v-col>
                <div class="order-title">경과시간</div>
              </v-col>
              <v-col>
                <div class="order-title">결제수단</div>
              </v-col>
              <v-col>
                <div class="order-title">주문 금액</div>
              </v-col>
              <v-col>
                <div class="order-title">주문 상태</div>
              </v-col>
            </v-row>
            <v-divider class="my-2"></v-divider>
            <v-row v-for="order in filteredOrders" :key="order.id" @click="selectOrder(order)" class="order-card">
              <v-col>{{ order.id }}</v-col>
              <v-col>{{ order.date }}</v-col>
              <v-col>{{ order.elapsedTime }}</v-col>
              <v-col>{{ order.payment }}</v-col>
              <v-col>{{ order.amount }}</v-col>
              <v-col>{{ order.status }}</v-col>
            </v-row>
          </v-card>
        </v-col>
        <v-col cols="3">
          <v-card class="pa-4 order-detail">
            <div v-if="selectedOrder">
              <div>
                <strong>총 금액:</strong> {{ selectedOrder.totalAmount }}
              </div>
              <div>
                <strong>주문 일시:</strong> {{ selectedOrder.date }}
              </div>
              <div>
                <strong>주문 주소:</strong> {{ selectedOrder.address }}
              </div>
              <div>
                <strong>연락처:</strong> {{ selectedOrder.contact }}
              </div>
              <div>
                <strong>고객요청:</strong> {{ selectedOrder.customerRequest }}
              </div>
              <div>
                <strong>라이더요청:</strong> {{ selectedOrder.riderRequest }}
              </div>
            </div>
            <v-divider class="my-2"></v-divider>
            <div v-if="selectedTab === '대기'">
              <v-row>
                <v-col>
                  <v-btn color="primary" @click="acceptOrder">주문수락</v-btn>
                </v-col>
                <v-col>
                  <v-btn color="error" @click="rejectOrder">주문거절</v-btn>
                </v-col>
              </v-row>
            </div>
            <div v-else-if="selectedTab === '조리중'">
              <v-btn color="primary" @click="callRider">기사호출</v-btn>
            </div>
            <div v-else-if="selectedTab === '배달중'">
              <v-btn color="primary" @click="trackRider">라이더 위치확인</v-btn>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const orders = ref([
  {
    id: 1, date: '2023-06-09 12:00', elapsedTime: '10분', amount: '10,000원', status: '대기', payment :'카드',
    totalAmount: '10,000원', address: '123 Main St', contact: '010-1234-5678', customerRequest: 'Extra ketchup', riderRequest: 'Call upon arrival'
  },
  {
    id: 2, date: '2023-06-09 12:05', elapsedTime: '5분', amount: '8,000원', status: '대기', payment : '카드',
    totalAmount: '8,000원', address: '456 Elm St', contact: '010-8765-4321', customerRequest: 'No onions', riderRequest: 'Leave at door'
  }
]);

const selectedOrder = ref(null);
const selectedTab = ref('대기');

const filteredOrders = computed(() => {
  return orders.value.filter(order => order.status === selectedTab.value);
});

const selectOrder = (order) => {
  selectedOrder.value = order;
};

const selectTab = (tab) => {
  selectedTab.value = tab;
  selectedOrder.value = null;
};

const acceptOrder = () => {
  if (selectedOrder.value) {
    selectedOrder.value.status = '조리중';
    const orderIndex = orders.value.findIndex(order => order.id === selectedOrder.value.id);
    orders.value[orderIndex].status = '조리중';
  }
};

const rejectOrder = () => {
  if (selectedOrder.value) {
    orders.value = orders.value.filter(order => order.id !== selectedOrder.value.id);
    selectedOrder.value = null;
  }
};

const callRider = () => {
  if (selectedOrder.value) {
    selectedOrder.value.status = '배달중';
    const orderIndex = orders.value.findIndex(order => order.id === selectedOrder.value.id);
    orders.value[orderIndex].status = '배달중';
  }
};

const trackRider = () => {
  alert("Tracking rider for order: " + selectedOrder.value.id);
};
</script>


<style scoped>
.order-title {
  font-weight: bold;
  background-color: lightgray;
  padding: 8px 0;
}

.order-card {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.order-card:hover {
  background-color: #f5f5f5;
}

.order-list {
  min-height: 80vh;
}

.order-detail {
  height: 80vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
