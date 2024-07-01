<template>
  <chart-holder-card
      title="시간대별 매출량"
      subtitle="시간 단위 소비 트렌드 분석자료"
      update="실시간 업데이트"
      color="light"
  >
    <div class="canvas">
      <canvas id="mealTimeAllChart"></canvas>
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

// 지점별 색상 매핑
const colorMap = {
  '강남점': '#102C57',
  '고속터미널점': '#C80036',
  '서울역점': '#850F8D',
  '여의도점': '#E88D67',
  '신대방삼거리점': '#FFDB00'
};

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

    const groupedData = {};
    sales.value.forEach(record => {
      if (!groupedData[record.storeName]) {
        groupedData[record.storeName] = {};
      }
      groupedData[record.storeName][record.hour] = record.total;
    });

    const datasets = Object.keys(groupedData).map(storeName => {
      const salesData = groupedData[storeName];
      return {
        label: storeName,
        data: generateTimeRangeData(salesData),
        backgroundColor: colorMap[storeName] || '#E88D67',
        borderColor: colorMap[storeName] || '#E88D67',
        borderWidth: 2,
      }
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
