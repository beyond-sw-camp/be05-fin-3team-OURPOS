<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" md="8">
          <v-card>
            <v-card-title>메뉴별 주문 빈도</v-card-title>
            <v-card-text>
              <div class="canvas-container">
                <div class="canvas">
                  <canvas id="menuPreferAllChart"></canvas>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>

  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { Chart, registerables } from 'chart.js';

  const sales = ref([]);
  const isLoading = ref(false);
  const myChart = ref(null);
  Chart.register(...registerables);

  const fetchData = async () => {
    isLoading.value = true;
    try {
      const response = await axios.get('https://ourpos.org/api/v1/orders/menu-prefer', {
      withCredentials: true
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
    const ctx = document.getElementById('menuPreferAllChart').getContext('2d');
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
  .canvas-container {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .canvas {
    height: 300px;
  }
  </style>
