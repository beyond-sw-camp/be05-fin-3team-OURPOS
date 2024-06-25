<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" md="8">
          <v-card>
            <v-card-title>본사 시간대별 매출액</v-card-title>
            <v-card-text>
              <div class="canvas-container">
                <div class="canvas">
                  <canvas id="mealTimeAllChart"></canvas>
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
  Chart.register(...registerables);


  const sales = ref([]);
  const isLoading = ref(false);
  const myChart = ref(null);

  const fetchData = async () => {
    isLoading.value = true;
    try {
      const response = await axios.get('https://ourpos.org/api/v1/orders/meal-time', {
      withCredentials: true
    });
      sales.value = response.data.data;
      const groupedData = {};
      sales.value.forEach(record => {
        if(!groupedData[record.storeName]){
          groupedData[record.storeName] = {};
        }
        groupedData[record.storeName][record.hour] = record.total;
      });

      const datasets = Object.keys(groupedData).map(storeName => {
        const salesData = groupedData[storeName];
        return {
          label: storeName,
          data: generateTimeRangeData(salesData),
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
          borderWidth: 1,
          pointRadius: 0
        };
      });

      updateChart(datasets);
    } catch (error) {
      console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
    } finally {
      isLoading.value = false;
    }
  };

  const updateChart = (datasets) => {
    const ctx = document.getElementById('mealTimeAllChart').getContext('2d');
    if (myChart.value) {
      myChart.value.data.datasets = datasets;
      myChart.value.update();
    } else {
      myChart.value = new Chart(ctx, {
        type: 'line',
        data: {
          labels: generateTimeLabels(),
          datasets: datasets
        },
        options: {
          scales: {
            x: {
              min: '9',
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

  const generateTimeLabels = () => {
    // 0-23시간 레이블 생성 함수
    return Array.from({ length: 24 }, (_, i) => i);
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
