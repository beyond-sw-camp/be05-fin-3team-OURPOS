<template>
  <chart-holder-card
      title="메뉴 선호도"
      subtitle="메뉴별 소비 트랜드 분석자료"
      update="실시간 업데이트"
      color="dark">
    <div class="canvas">
      <canvas id="menuPreferChart"></canvas>
    </div>
  </chart-holder-card>
</template>

<script setup>
import ChartHolderCard from "../ChartHolderCard.vue";
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';

const sales = ref([]);
const isLoading = ref(false);
const myChart = ref(null);

const fetchData = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/orders/menu-prefer/my', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    sales.value = response.data.data;
    const salesData = {};
    sales.value.forEach(record => {
      salesData[record.name] = record.quantity;
    });
    // 변환된 데이터를 차트에 적용
    updateChart(salesData);
  } catch (error) {
    console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
  } finally {
    isLoading.value = false;
  }
};

const updateChart = (salesData) => {
  const ctx = document.getElementById('menuPreferChart').getContext('2d');
  if (myChart.value) {
    // 차트가 이미 생성되었다면 데이터만 업데이트
    myChart.value.data.labels = Object.keys(salesData);
    myChart.value.data.datasets[0].data = Object.values(salesData);
    myChart.value.update();
  } else {
    // 차트가 아직 없으면 생성
    myChart.value = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: Object.keys(salesData),
        datasets: [{
          label: '식사 유형별 매출량',
          data: Object.values(salesData),
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            '#1E90FF',
            '#A52A2A',
            '#7FFF00'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            '#1E90FF',
            '#A52A2A',
            '#7FFF00'
          ],
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function(value) {
                return value;
              }
            }
          }
        }
      }
    });
  }
};

onMounted(() => {
  fetchData();
});

</script>

<style scoped>
.canvas {
  height: 293px;
}
</style>