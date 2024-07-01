<template>
  <chart-holder-card
      title="식사유형 선호도"
      subtitle="배달/매장 소비 트랜드 분석자료"
      update="실시간 업데이트"
      color="dark"
  >
    <div class="canvas">
      <canvas id="mealTypeAllChart"></canvas>
    </div>
  </chart-holder-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import ChartHolderCard from "../ChartHolderCard.vue";
import { defineProps } from "vue";

const props = defineProps(["storeId"]);

const sales = ref([]);
const isLoading = ref(false);
const myChart = ref(null);

const fetchData = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/orders/meal-type', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      },
      params: {
        storeId: props.storeId,
      }
    });
    sales.value = response.data.data;

    const salesData = {
      '홀주문': 0,
      '배달주문': 0
    };

    sales.value.forEach(record => {
      if (record.type === 'hall') {
        salesData['홀주문'] += record.total;
      } else if (record.type === 'delivery') {
        salesData['배달주문'] += record.total;
      }
    });

    updateChart(salesData);
  } catch (error) {
    console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
  } finally {
    isLoading.value = false;
  }
};

const updateChart = (salesData) => {
  const ctx = document.getElementById('mealTypeAllChart').getContext('2d');
  if (myChart.value) {
    myChart.value.data.labels = Object.keys(salesData);
    myChart.value.data.datasets[0].data = Object.values(salesData);
    myChart.value.update();
  } else {
    myChart.value = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: Object.keys(salesData),
        datasets: [{
          label: '식사 유형별 매출량',
          data: Object.values(salesData),
          backgroundColor: [
            '#00215E',
            '#FFD1E3',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
          ],
          borderColor: [
            '#ffffff',
            '#ffffff',
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
            display: false,
          },
          y: {
            display: false,
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: (tooltipItem) => {
                return tooltipItem.label + ': ' + tooltipItem.raw.toLocaleString() + '원';
              }
            }
          },
          legend: {
            labels: {
              font: {
                size: 14, // 폰트 크기 설정
              },
              color: '#ffffff' // 폰트 색상 설정
            }
          }
        },
        animation: {
          animateRotate: true,
          animateScale: true
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
