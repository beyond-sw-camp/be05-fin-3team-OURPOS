<template>
  <div class="row mb-4 col-lg-12">
    <div class="col-lg-12 position-relative z-index-2">
      <div class="row mt-4">
        <div class="col-lg-6 col-md-6 mt-4">
          <div class="col-lg-6 col-md-6 col-sm-6 mt-lg-0 mt-4">
            <mini-statistics-card :title="{ text: '현재 지점수', value: '5' }"
                                  detail="<span class='text-danger text-sm font-weight-bolder'></span>" :icon="{
                name: 'logo-ct',
                color: 'text-white',
                background: 'success',
              }" />
          </div>
          <button @click="openPostcode">주소 선택</button>
          <div v-if="address">선택한 주소: {{ address }}</div>

          <button @click="getCode">행정동 코드 받기</button>

          <div v-if="isServiceAvailable">
            서비스 이용 가능 지역입니다.
            <a :href="downloadUrl">Download PDF</a>
          </div>

        </div>
        <div class="col-lg-5 col-md-6 mt-4">
          <h5>입점 하고자 하는 건물의 주소 선택 후, 예상 매출액 다운로드 시 입점 하고자 하는 건물의 예상 매출액을 분석하여 다음과 같은 pdf로 제공됩니다.</h5>
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
import axios from "axios";
import Papa from 'papaparse';

const address = ref('');
const code = ref('');
const codes = ref([]);
const csvData = ref([]);
const isServiceAvailable = ref(false);
const isLoading = ref(false);

const downloadUrl = `http://127.0.0.1:5000/api/v1/calculator?dong_code=${encodeURIComponent(code.value)}`;

const loadCsvData = async () => {
  const response = await axios.get('/filtered_sample_rfr_all.csv');
  Papa.parse(response.data, {
    header: true,
    complete: (results) => {
      csvData.value = results.data.map(row => row['행정동_코드']);
    }
  });
};

const getCode = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get('http://localhost:8080/api/v1/maps', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      },
      params: {
        addressBase: address.value
      },
      paramsSerializer: params => {
        return new URLSearchParams(params).toString();
      }
    });
    console.log(response.data);

    codes.value = response.data.data;
    codes.value.forEach(record => {
      if (record.region_type == 'H') {
        code.value = record.code.slice(0, -2);
      }
    });
    console.log(code.value);

    isServiceAvailable.value = csvData.value.includes(code.value);
  } catch (error) {
    console.error('데이터를 가져오는 중 에러가 발생했습니다:', error);
  } finally {
    isLoading.value = false;
  }
};

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
  loadCsvData();
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
