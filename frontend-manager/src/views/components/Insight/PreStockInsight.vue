<template>
  <div class = "pre-stock">
    <table class="stock-table">
      <thead>
      <tr>
        <th>Stock Name</th>
        <th>Stock Amount</th>
        <th>Status</th>
        <th>Order Date</th>
        <th>Delivery Date</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item, index) in incomingStockList" :key="index">
        <td>{{ item.stockName }}</td>
        <td>{{ item.stockAmount }}</td>
        <td>{{ item.status }}</td>
        <td>{{ item.orderdate }}</td>
        <td>{{ item.deliverydate ? item.deliverydate : '-' }}</td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button v-for="pageNumber in totalPages" :key="pageNumber" @click="fetchIncomingStock(pageNumber - 1)">
        {{ pageNumber }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const incomingStockList = ref([]);
const totalPages = ref(0);

const fetchIncomingStock = async (page) => {
  try {
    const response = await fetch(`http://localhost:8080/api/v1/store/incoming-stock/my?page=${page}`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    const data = await response.json();
    incomingStockList.value = data.data.content;
    totalPages.value = data.data.totalPages;
    console.log("totalPage : ", totalPages.value);
  } catch (error) {
    console.error('Error fetching incoming stock:', error);
  }
};

onMounted(() => {
  fetchIncomingStock(0); // 초기 페이지 로딩
});
</script>

<style>
.stock-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.stock-table th, .stock-table td {
  padding: 8px;
  text-align: center;
  border: 1px solid #dddddd;
}

.stock-table th {
  background-color: #f2f2f2;
}

.pagination {
  margin-top: 10px;
}

.pagination button {
  margin-right: 5px;
  cursor: pointer;
}
</style>
