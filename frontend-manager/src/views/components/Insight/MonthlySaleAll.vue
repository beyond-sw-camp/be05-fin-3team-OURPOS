<template>

  <chart-holder-card
      title="월별 매출량"
      subtitle="월 단위 소비 트랜드 분석자료"
      update="실시간 업데이트"
      color="light"
  >
    <div class="canvas">
      <canvas id="monthAllChart"></canvas>
    </div>
  </chart-holder-card>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import {defineProps} from "vue";
import ChartHolderCard from "@/views/components/ChartHolderCard.vue";

const props = defineProps(["storeId"]);

const sales = ref([]);
const isLoading = ref(false);
const myChart = ref(null);

const colorMap = {
  '강남점': '#102C57',
  '고속터미널점': '#C80036',
  '서울역점' : '#850F8D',
  '여의도점' : '#E88D67',
  '신대방삼거리점': '#FFDB00'
};

const fetchData = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/orders/monthly', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      },
      params: {
        storeId: props.storeId,
      }
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
        backgroundColor: colorMap[storeName] || '#E88D67',
        borderColor: colorMap[storeName] || '#E88D67',
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
    myChart.value.data.labels = labels;  // 여기를 수정하여 labels가 올바르게 설정되도록 함
    myChart.value.data.datasets = datasets;
    myChart.value.update();
  } else {
    myChart.value = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,  // 여기를 수정하여 labels가 올바르게 설정되도록 함
        datasets: datasets
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function (value) {
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
  height: 300px;
}
</style>
