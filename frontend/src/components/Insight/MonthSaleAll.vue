<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" md="8">
          <v-card>
            <v-card-title>본사 월별 매출량</v-card-title>
            <v-card-text>
              <div class="canvas-container">
                <div class="canvas">
                  <canvas id="monthAllChart"></canvas>
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
  
  
  const colorMap = {
    '맛집': 'rgba(255, 99, 132, 1)',
    '강남치킨': 'rgba(54, 162, 235, 1)',
  };
  
  const fetchData = async () => {
    isLoading.value = true;
    try {
      const response = await axios.get('http://localhost:8080/api/v1/orders/monthly', {
      withCredentials: true
    });
      sales.value = response.data.data;
      const labels = new Set();
      const groupedData = {};
      sales.value.forEach(record => {
        if (!groupedData[record.storeName]) {
          groupedData[record.storeName] = {};
        }
        const monthYear = `${record.year}-${String(record.month).padStart(2, '0')}`;
        labels.add(monthYear);
        groupedData[record.storeName][monthYear] = record.total;
      });
      const sortedLabels = Array.from(labels).sort();
      const datasets = Object.keys(groupedData).map(storeName => {
        const salesData = groupedData[storeName];
        return {
          label: storeName,
          data: sortedLabels.map(label => salesData[label] || 0),
          backgroundColor: colorMap[storeName] || 'rgba(75, 192, 192, 0.2)',
          borderColor: colorMap[storeName] || 'rgba(75, 192, 192, 1)',
          borderWidth: 1,
        };
      });
      updateChart(sortedLabels, datasets);
    } catch (error) {
      console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
    } finally {
      isLoading.value = false;
    }
  };
  
  const updateChart = (labels, datasets) => {
    const ctx = document.getElementById('monthAllChart').getContext('2d');
    if (myChart.value) {
      myChart.value.data.labels = labels;
      myChart.value.data.datasets = datasets;
      myChart.value.update();
    } else {
      myChart.value = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: datasets
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
  