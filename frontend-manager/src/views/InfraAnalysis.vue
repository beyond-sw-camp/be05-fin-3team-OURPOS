<template>
  <div class="row mb-4 col-lg-12">
    <div class="col-lg-12 position-relative z-index-2">
      <div class="row mt-4">
        <div class="col-lg-6 col-md-6 mt-4">
          <div class="col-lg-6 col-md-6 col-sm-6 mt-lg-0 mt-4">
            <mini-statistics-card :title="{ text: '현 지점수', value: '5' }"
                                  detail="<span class='text-danger text-sm font-weight-bolder'></span>" :icon="{
                name: 'person',
                color: 'text-white',
                background: 'success'
              }" />
          </div>

          <div class = "box">

          <br/>
          <h5>1. 입점하고자 하는 주소를 선택합니다.</h5>
          <button @click="openPostcode">신규 점포 입점 지역 선택</button>

          <br/>
          <div v-if="address">{{ address }} : 해당 지역의 예상 매출액을 산정합니다.</div>
          <br/>

          <h5>2. 예상 매출액 산정 서비스가 가능한 지역인지 확인합니다.</h5>
          <button @click="getCode">서비스 이용 지역 여부 확인</button>

            <div v-if="isServiceAvailable === true">
              <br/>
              <h5 style="color: green">서비스 이용 가능 지역입니다.</h5>
              <br/>
              <h5>3. 아래의 pdf 다운로드 버튼을 누르면, 1분30초 ~ 2분 후, </h5>
              <h5> 컴퓨터에 예상매출액보고서 pdf가 다운로드 됩니다.</h5>
              <br/>
              <button @click="downloadPDF">Download PDF</button>
            </div>
            <div v-else-if="isServiceAvailable === false">
              <br/>
              <h5 style="color: red">서비스 이용 불가능 지역입니다.</h5>
            </div>
            <h5 class = "footer">입점 하고자 하는 건물의 주소 선택 후, 예상 매출액 다운로드 시 입점 하고자 하는 건물의 예상 매출액을 분석하여 다음과 같은 pdf로 제공됩니다.</h5>
          </div>
        </div>
        <div class="col-lg-5 col-md-6 mt-4">

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
import { ref, onMounted, watchEffect } from 'vue';
import MiniStatisticsCard from "@/views/components/MiniStatisticsCard.vue";
import axios from "axios";
import Papa from 'papaparse';

const address = ref('');
const code = ref('');
const codes = ref([]);
const csvData = ref([]);
const isServiceAvailable = ref(null);
const isLoading = ref(false);
const downloadUrl = ref('');



watchEffect(() => {
  downloadUrl.value = `http://127.0.0.1:5000/api/v1/calculator?dong_code=${encodeURIComponent(code.value)}`;
});
const downloadPDF = () => {
  window.location.href = downloadUrl.value;
};

const loadCsvData = async () => {
  try {
    const response = await axios.get('/serviceDong.csv', {
      responseType: 'arraybuffer' // 바이너리 데이터로 받음
    });

    const decoder = new TextDecoder('utf-8');
    const csvContent = decoder.decode(new Uint8Array(response.data));

    Papa.parse(csvContent, {
      header: true,
      dynamicTyping: true,
      skipEmptyLines: true,
      delimiter: ',',
      complete: (results) => {
        console.log("PapaParse Results:", results);  // 전체 결과 로그 추가
        if (results.errors.length > 0) {
          console.error("CSV Parsing Errors:", results.errors);
        } else {
          console.log("Parsed Data Fields:", Object.keys(results.data[0]));
          csvData.value = results.data.map(row => row['dong'] || row['dong']);
          console.log("CSV Data Loaded:", csvData.value);

        }
      }
    });
  } catch (error) {
    console.error("Error loading CSV data:", error);
  }
};

const getCode = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/maps', {
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
    console.log("백엔드", response.data);

    codes.value = response.data.data;
    codes.value.forEach(record => {
      if (record.region_type == 'H') {
        code.value = record.code.slice(0, -2);
      }
    });
    console.log("행정동 코드:", code.value);

    isServiceAvailable.value = csvData.value.includes(parseInt(code.value));
    console.log("프론트 사용가능 지역 값:", isServiceAvailable.value);

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
button {
  background-color: #4CAF50; /* Green */
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 12px;
  transition-duration: 0.4s;
}

/* 마우스 호버 시 버튼 스타일 */
button:hover {
  background-color: white;
  color: black;
  border: 2px solid #4CAF50;
}
.box{
  margin : 20px;
}
.footer {
  position: fixed;
  bottom: 10px;
  left: 100px;
  width: 100%;
  background-color: white; /* 배경 색상 */
  padding: 10px; /* 패딩 */
  text-align: center; /* 텍스트 가운데 정렬 */
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  z-index: 1000; /* 다른 요소들보다 위에 표시 */
}

</style>
