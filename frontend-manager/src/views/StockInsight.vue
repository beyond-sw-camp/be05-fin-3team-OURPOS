<template>
  <div>
    <table>
      <thead>
      <tr>
        <th>비품/재고명</th>
        <th>재고수량</th>
        <th>상태</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in paginatedData" :key="item.stockName">
        <td>{{ item.stockName }}</td>
        <td>{{ item.stockAmount }}</td>
        <td>{{ item.status }}</td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
      <button
          v-for="page in totalPages"
          :key="page"
          @click="currentPage = page"
          :class="{ active: currentPage === page }"
      >
        {{ page }}
      </button>
      <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const stockData = ref([]);
const isLoading = ref(true);
const currentPage = ref(1);
const itemsPerPage = 5;

const fetchData = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get('http://localhost:8080/api/v1/store/1/incoming-stock', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.data.code === 200) {
      stockData.value = response.data.data;
    }
  } catch (error) {
    console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const totalPages = computed(() => Math.ceil(stockData.value.length / itemsPerPage));

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return stockData.value.slice(start, end);
});

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f9f9f9;
  text-align: left;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 5px;
  padding: 5px 10px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination button.active {
  background-color: #0056b3;
}
</style>
