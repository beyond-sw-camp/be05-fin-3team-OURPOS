<template>
  <div class="now-stock">
    <h5>현재 재고량</h5>
    <table class="stock-table">
      <thead>
      <tr>
        <th>식자재/비품명</th>
        <th>수량</th>
        <th>입고일</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item, index) in incomingStockList" :key="index">
        <td>{{ item.stockName }}</td>
        <td>{{ item.stockAmount }}</td>
        <td>{{ item.stockingTime }}</td>
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
import { ref, onMounted, watch } from 'vue';

const incomingStockList = ref([]);
const totalPages = ref(0);

const fetchIncomingStock = async (page) => {
  try {
    const response = await fetch(`https://api.ourpos.org/api/v1/storestocks/my?page=${page}`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    const data = await response.json();
    incomingStockList.value = data.data.content;
    totalPages.value = data.data.totalPages;
    console.log("Total Pages: ", totalPages.value);
  } catch (error) {
    console.error('Error fetching incoming stock:', error);
  }
};

onMounted(() => {
  fetchIncomingStock(0); // Initial page load
});

watch(incomingStockList, (newList) => {
  newList.forEach(item => {
    if (item.stockAmount <= 50) {
      alert(`${item.stockName}의 재고가 50개 이하입니다.`);
    }
  });
}, { deep: true });
</script>

<style scoped>
.stock-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.stock-table th,
.stock-table td {
  padding: 8px;
  text-align: center;
  border: 1px solid #dddddd;
}

.stock-table th {
  background-color: #f2f2f2;
}

.pagination {
  margin-top: 10px;
  justify-content: center;
  align-items: center;
}

.pagination button {
  margin-right: 5px;
  cursor: pointer;
}

.now-stock {
  height: calc(40vh - 8px);
}
</style>
