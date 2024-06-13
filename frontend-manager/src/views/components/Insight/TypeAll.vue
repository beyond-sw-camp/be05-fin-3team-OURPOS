<template>

    <chart-holder-card
      title="Deliver/Hall Prefer Report"
      subtitle="Sales Trends Across All Locations"
      update="real-time update"
      color="dark"
    >
    <div class = "canvas">
      <canvas id="mealTypeAllChart"></canvas>
      <p>Store ID: {{ storeId }}</p>
    </div>
    </chart-holder-card>
</template>

<script setup>

  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import Chart from 'chart.js/auto';
  import ChartHolderCard from "../ChartHolderCard.vue";
  import { defineProps } from "vue"
  
  // eslint-disable-next-line no-unused-vars
  const props = defineProps(["storeId"]);

  const sales = ref([]);
  const isLoading = ref(false);
  const myChart = ref(null);

  const fetchData = async () => {   
    isLoading.value = true;
    try {
      const response = await axios.get('http://localhost:8080/api/v1/orders/meal-type', {
        params: {
            storeId: props.storeId,
        },
        withCredentials: true
    });
      sales.value = response.data.data;
      const salesData = {};
      sales.value.forEach(record => {
          salesData[record.type] = record.total;
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
    const ctx = document.getElementById('mealTypeAllChart').getContext('2d');
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
