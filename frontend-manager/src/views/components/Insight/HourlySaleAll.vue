<template>
    <chart-holder-card
      title="시간대별 매출량"
      subtitle="시간 단위 소비 트랜드 분석자료"
      update="실시간 업데이트"
      color="light"
    >
    <div class = "canvas">
      <canvas id="mealTimeAllChart"></canvas>
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
    const response = await axios.get('https://api.ourpos.org/api/v1/orders/meal-time', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      },
      params: {
        storeId: props.storeId,
      }
    });
    sales.value = response.data.data;
    // const salesData = {};
    const groupedData = {};
    sales.value.forEach(record =>{
      if(!groupedData[record.storeName]){
        groupedData[record.storeName] = {};
      }
      groupedData[record.storeName][record.hour] = record.total;
    });
    
    const datasets = Object.keys(groupedData).map(storeName => {
      const salesData = groupedData[storeName];
      return {label: storeName,
      data: generateTimeRangeData(salesData),
      backgroundColor: [
            '#102C57',
            '#C80036',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
          ],
          borderColor: [
            '#102C57',
            '#C80036',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
          ],
          borderWidth: 2,
          // pointRadius: 1
      }
    })

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
            },
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
.canvas {
    height: 300px;
}
</style>
