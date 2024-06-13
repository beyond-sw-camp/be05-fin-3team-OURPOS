<template>
    <chart-holder-card
      title="Monthly Sales Volume Report"
      subtitle="Sales Trends Across All Locations"
      update="real-time update"
      color="light"
    >
    <div class = "canvas">
        <canvas id="monthAllChart"></canvas>
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

const colorMap = {
    '맛집': 'rgba(255, 99, 132, 1)',
    '강남치킨': 'rgba(54, 162, 235, 1)',
};

// const props = defineProps({
//   // 한국어임.
//   storeId: String
// });



const fetchData = async () => {   
  isLoading.value = true;
//   console.log("storeId" , this.storeId);
//  storeId: 1
  try {
    const response = await axios.get('http://localhost:8080/api/v1/orders/meal-time', {
        params: {
            storeId: props.storeId,
        },
        withCredentials: true
    });

    sales.value = response.data.data;
    const labels = new Set();
    const groupedData = {};
    sales.value.forEach(record => {
    
        if(!groupedData[record.storeName]){
            groupedData[record.storeName] = {};
        }
        const monthYear = `${record.year}-${String(record.month).padStart(2, '0')}`;
        labels.add(monthYear);
        groupedData[record.storeName][monthYear] = record.total;
      });
      const sortedLabels = Array.from(labels).sort();

      const datasets = Object.keys(groupedData).map(storeName => {
        const salesData = groupedData[storeName];
        console.log("salesData", salesData);
        return {
            label : storeName,
            data: sortedLabels.map(label => salesData[label] || 0),
            backgroundColor: colorMap[storeName] || 'rgba(75, 192, 192, 0.2)',
            borderColor: colorMap[storeName] || 'rgba(75, 192, 192, 1)',
          borderWidth: 1,
        };
      });

    updateChart(sortedLabels,datasets);
  } catch (error) {
    console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
  } finally {
    isLoading.value = false;
  }
};

const updateChart = (labels,datasets) => {
  const ctx = document.getElementById('monthAllChart').getContext('2d');
  if (myChart.value) {
    myChart.value.data.label=labels;
    myChart.value.data.datasets = datasets;
    myChart.value.update();
  } else {
    // 차트가 아직 없으면 생성
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
.canvas {
    height: 300px;
}
</style>