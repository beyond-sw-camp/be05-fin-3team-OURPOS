<template>
    <chart-holder-card title="메뉴 선호도"
                       subtitle="메뉴별 소비 트랜드 분석자료"
                       update="실시간 업데이트"  color="dark">
        <div class="canvas">
            <canvas id="menuPreferAllChart"></canvas>
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
        const response = await axios.get('https://api.ourpos.org/api/v1/orders/menu-prefer', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
          },
          params: {
            storeId: props.storeId,
          }
        });
        console.log("menuprefer 통신 성공");
        sales.value = response.data.data;
        const salesData = {};
        sales.value.forEach(record => {
            salesData[record.name] = record.quantity;
        });
        console.log(salesData);
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
                        '#7469B6',
                        '#AD88C6',
                        '#E1AFD1',
                        '#F27BBD',
                        '#CAF4FF',
                        '#0A6847',
                        '#1E90FF',
                        '#2C4E80',
                        '#1E0342',
                        '#B3C8CF',
                        '#A1C398',
                        '#627254',
                    ],
                    borderColor: [
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                      '#ffffff',
                    ],
                    borderWidth: 1
                }]
            },
            options: {
              scales: {
                x: {
                  display: false, // x 축 눈금선을 숨김
                },
                y: {
                  display: false, // y 축 눈금선을 숨김
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