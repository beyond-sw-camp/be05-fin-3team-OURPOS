<template>
  
  <v-container>
    <v-tabs v-model="tab">
      <v-tab v-for="status in statuses" :key="status.value">
        {{ status.label }}
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <OrderTable :orders="filteredOrders" />
      </v-tab-item>
    </v-tabs-items>
  </v-container>
  
</template>

<script>

import OrderTable from '../components/OrderTable.vue';

export default {
  components: { OrderTable },
  data() {
    return {
      tab: 0,
      orders: [],
      statuses: [
        { label: '대기중', value: 'WAITING' },
        { label: '주문 승인', value: 'ACCEPTED' },
        { label: '배송중', value: 'DELIVERING' },
        { label: '배송 완료', value: 'COMPLETED' },
      ],
    };
  },
  created() {
    this.fetchOrders();
  },
  computed: {
    filteredOrders() {
      const status = this.statuses[this.tab].value;
      return this.orders.filter(order => order.storeOrderStatus === status);
    }
  },
  methods: {
    async fetchOrders() {
      try {
        const response = await fetch('http://localhost:8080/api/v1/storeorder/1/check', {credentials: 'include'}); 
        const result = await response.json();
        this.orders = result.data;
      } catch (error) {
        console.error('Error fetching orders:', error);
      }
    }
  }
};
</script>