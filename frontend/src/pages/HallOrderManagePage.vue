<template>
    <div>
        <v-toolbar dark prominent class="navigation-bar">
        <v-toolbar-title>OUR POS</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon>
            <v-icon>mdi-export</v-icon>
        </v-btn>
        </v-toolbar>

        <v-container fluid>
        <v-row>
            <v-col cols="9">
            <v-card class="pa-4 order-list">
                <v-row class="status-cards">
                    <v-col>
                        <v-btn class="status-card" :color="selectedTab === '대기' ? 'light-green' : 'white'" @click="selectTab('대기')">
                        대기
                        </v-btn>
                    </v-col>
                    <v-col>
                        <v-btn class="status-card" :color="selectedTab === '조리중' ? 'light-green' : 'white'" @click="selectTab('조리중')">
                        조리중
                        </v-btn>
                    </v-col>
                    <v-col>
                        <v-btn class="status-card" :color="selectedTab === '완료' ? 'light-green' : 'white'" @click="selectTab('완료')">
                        완료
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
                <v-col>{{ order.amount }}</v-col>
                <v-col>{{ order.status }}</v-col>
                </v-row>
            </v-card>
            </v-col>
            <v-col cols="3">
            <v-card class="pa-4 order-detail">
                <div v-if="selectedOrder">
                <div v-for="item in selectedOrder.items" :key="item.foodName">
                    {{ item.foodName }}*{{ item.count }} = {{ item.price }}
                </div>
                </div>
                <v-divider class="my-2"></v-divider>
                <div v-if="selectedOrder">
                <span>총 금액:</span>
                <span>{{ selectedOrder.totalAmount }}</span>
                </div>
                <v-btn color="primary" class="mt-2" @click="startCooking">조리시작</v-btn>
                <v-btn color="error" class="mt-2" @click="rejectOrder">주문거절</v-btn>
            </v-card>
            </v-col>
        </v-row>
        </v-container>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const orders = ref([
  { id: 1, date: '2023-06-09 12:00', elapsedTime: '10분', amount: '10,000원', status: '대기', items: [{ foodName: 'Burger', count: 2, price: '5,000원' }], totalAmount: '10,000원' },
  { id: 2, date: '2023-06-09 12:05', elapsedTime: '5분', amount: '8,000원', status: '대기', items: [{ foodName: 'Fries', count: 4, price: '2,000원' }], totalAmount: '8,000원' }
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

const startCooking = () => {
  if (selectedOrder.value) {
    selectedOrder.value.status = '조리중';
    // Update the order status in the orders list
    const orderIndex = orders.value.findIndex(order => order.id === selectedOrder.value.id);
    orders.value[orderIndex].status = '조리중';
  }
};

const rejectOrder = () => {
  if (selectedOrder.value) {
    // Remove the selected order from the orders list
    orders.value = orders.value.filter(order => order.id !== selectedOrder.value.id);
    selectedOrder.value = null;
  }
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

.status-cards {
    text-align: center;
}
.status-card {
    padding : 30px;
    border:1px solid #ccc;
    display: flex;
    align-items: center;
    justify-content: center;
}

.order-list {
  min-height: 80vh;
}

.order-detail {
  height: 80vh;
  display: flex;
  flex-direction: column;
  /* justify-content: space-between; */
}
</style>