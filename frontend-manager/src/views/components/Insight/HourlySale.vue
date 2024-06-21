<template>
  <chart-holder-card
      title="시간대별 매출량"
      subtitle="시간 단위 소비 트랜드 분석자료"
      update="실시간 업데이트"
      color="light"
  >
    <div class = "canvas">
      <canvas id="mealTimeChart"></canvas>
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
    const response = await axios.get('http://localhost:8080/api/v1/orders/meal-time/my', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    sales.value = response.data.data;
    const salesData = {};
    sales.value.forEach(record => {
      salesData[record.hour] = record.total;
    });
    const timeRangeData = generateTimeRangeData(salesData);
    updateChart(timeRangeData);
  } catch (error) {
    console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
  } finally {
    isLoading.value = false;
  }
};

const updateChart = (salesData) => {
  const ctx = document.getElementById('mealTimeChart').getContext('2d');
  if (myChart.value) {
    myChart.value.data.labels = Object.keys(salesData);
    myChart.value.data.datasets[0].data = Object.values(salesData);
    myChart.value.update();
  } else {
    myChart.value = new Chart(ctx, {
      type: 'line',
      data: {
        labels: Object.keys(salesData),
        datasets: [{
          label: '시간대별 매출량 추이',
          data: Object.values(salesData),
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
          ],
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          x: {
            min : '9',
            ticks: {
              stepSize: 1,
              autoSkip: false,
              maxTicksLimit: undefined
            }
          },
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

const generateTimeRangeData = (salesData) => {
  const timeRangeData = {};
  for (let i = 9; i < 23; i++) {
    timeRangeData[i] = salesData[i] || 0;
  }
  return timeRangeData;
};

onMounted(() => {
  fetchData();
});

</script>

<style scoped>
.canvas {
  height: 300px;
}
</style>
