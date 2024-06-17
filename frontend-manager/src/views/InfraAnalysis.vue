<template>

  <div class="row mb-4 col-lg-12">
    <div class="col-lg-12 position-relative z-index-2">
      <div class="row mt-4">
        <div class="col-lg-6 col-md-6 mt-4">
          <div class="col-lg-6 col-md-6 col-sm-6 mt-lg-0 mt-4">
            <mini-statistics-card :title="{ text: '현재 지점수', value: '5' }"
                                  detail="<span class='text-danger text-sm font-weight-bolder'></span>" :icon="{
                name: 'person',
                color: 'text-white',
                background: 'success',
              }" />
          </div>
          <button @click="openPostcode">주소 선택</button>
          <div v-if="address">선택한 주소: {{ address }}</div>

          <a href="http://127.0.0.1:5000/api/v1/calculator" download>Download PDF</a>
        </div>
        <div class="col-lg-5 col-md-6 mt-4">
          <h5>입점 하고자 하는 건물의 주소 선택 후, 예상 매출액 다운로드 시 아래와 같은 pdf 가 다운로드 됩니다.</h5>
          <chart-holder-card
              title="예상 매출액 추정 보고서 예시"
              subtitle="서울시 행정동 공공데이터 분석 결과"
              color="white"
          >
            <img src = "/예상매출액보고서예시.png" />
          </chart-holder-card>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>

import ChartHolderCard from "@/views/components/ChartHolderCard.vue";
import { ref, onMounted } from 'vue';
import MiniStatisticsCard from "@/views/components/MiniStatisticsCard.vue";
const address = ref('');



const loadDaumPostcodeScript = () => {
  return new Promise((resolve, reject) => {
    if (document.getElementById('daum-postcode-script')) {
      resolve();
      return;
    }

    const script = document.createElement('script');
    script.id = 'daum-postcode-script';
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.onload = resolve;
    script.onerror = reject;
    document.head.appendChild(script);
  });
};

const openPostcode = async () => {
  try {
    await loadDaumPostcodeScript();

    new window.daum.Postcode({
      oncomplete: function(data) {
        address.value = data.address;
      }
    }).open();
  } catch (error) {
    console.error('Failed to load Daum Postcode script:', error);
  }
};

onMounted(() => {
  loadDaumPostcodeScript().catch((error) => {
    console.error('Failed to load Daum Postcode script:', error);
  });
});
</script>


<style scoped>
 img {
  display: flex;
  justify-content: center; /* 수평 정렬 */
  align-items: center; /* 수직 정렬 */
  height: 100vh; /* 전체 뷰포트 높이 */
}
</style>