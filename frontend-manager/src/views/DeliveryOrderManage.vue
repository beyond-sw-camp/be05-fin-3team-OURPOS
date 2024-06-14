<template>
  <div>
    <nav class="navbar navbar-dark bg-dark navigation-bar">
      <span class="navbar-brand">OUR POS</span>
      <router-link to="/store-landing" class="ml-auto">
        <button class="btn btn-outline-light">
          <i class="mdi mdi-export"></i>
        </button>
      </router-link>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-9">
          <div class="card p-4 order-list">
            <div class="row">
              <div class="col">
                <button
                  :class="['btn', selectedTab === '대기' ? 'btn-success' : 'btn-light']"
                  @click="selectTab('대기')"
                >
                  대기
                </button>
              </div>
              <div class="col">
                <button
                  :class="['btn', selectedTab === '조리중' ? 'btn-success' : 'btn-light']"
                  @click="selectTab('조리중')"
                >
                  조리중
                </button>
              </div>
              <div class="col">
                <button
                  :class="['btn', selectedTab === '배달중' ? 'btn-success' : 'btn-light']"
                  @click="selectTab('배달중')"
                >
                  배달중
                </button>
              </div>
              <div class="col">
                <button
                  :class="['btn', selectedTab === '배달완료' ? 'btn-success' : 'btn-light']"
                  @click="selectTab('배달완료')"
                >
                  배달완료
                </button>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col">
                <div class="order-title">주문번호</div>
              </div>
              <div class="col">
                <div class="order-title">주문일시</div>
              </div>
              <div class="col">
                <div class="order-title">경과시간</div>
              </div>
              <div class="col">
                <div class="order-title">결제수단</div>
              </div>
              <div class="col">
                <div class="order-title">주문 금액</div>
              </div>
              <div class="col">
                <div class="order-title">주문 상태</div>
              </div>
            </div>
            <hr class="my-2">
            <div
              v-for="order in filteredOrders"
              :key="order.id"
              @click="selectOrder(order)"
              class="row order-card"
            >
              <div class="col">{{ order.id }}</div>
              <div class="col">{{ order.date }}</div>
              <div class="col">{{ order.elapsedTime }}</div>
              <div class="col">{{ order.payment }}</div>
              <div class="col">{{ order.amount }}</div>
              <div class="col">{{ order.status }}</div>
            </div>
          </div>
        </div>
        <div class="col-3">
          <div class="card p-4 order-detail">
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
            <hr class="my-2">
            <div v-if="selectedTab === '대기'">
              <div class="row">
                <div class="col">
                  <button class="btn btn-primary" @click="acceptOrder">주문수락</button>
                </div>
                <div class="col">
                  <button class="btn btn-danger" @click="rejectOrder">주문거절</button>
                </div>
              </div>
            </div>
            <div v-else-if="selectedTab === '조리중'">
              <button class="btn btn-primary" @click="callRider">기사호출</button>
            </div>
            <div v-else-if="selectedTab === '배달중'">
              <button class="btn btn-primary" @click="trackRider">라이더 위치확인</button>
            </div>
          </div>
        </div>
      </div>
    </div>
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
