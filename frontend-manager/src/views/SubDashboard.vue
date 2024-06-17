<template>
    <div class="py-4 container-fluid">
      <div class="row mb-4">
        <div class="col-lg-12 position-relative z-index-2">
  
  
          <div class="row">
            <div class="col-lg-3 col-md-6 col-sm-6 mt-lg-0 mt-4">
              <mini-statistics-card :title="{ text: '지점명', value: branchName }"
                                    :detail=" branchAddress" :icon="{
                name: 'weekend',
                color: 'text-white',
                background: 'info',
              }" />
            </div>
          </div>

          <div class="row mb-4 col-lg-12">
            <div class="col-lg-6 position-relative z-index-2">
              <div class="row mt-4">
                <div class="col-lg-8 col-md-6 mt-4">
                  <MonthlySaleAll :storeId='storeId'/>
                </div>
                <div class="col-lg-4 col-md-6 mt-4">
                  <TypeAll :storeId='storeId'/>
                </div>
              </div>
              <div class="row mt-4">
                <div class="col-lg-8 col-md-6 mt-4">
                  <HourlySaleAll :storeId='storeId'/>
                </div>
                <div class="col-lg-4 col-md-6 mt-4">
                  <MenuAll :storeId='storeId'/>
                </div>
              </div>
            </div>
  
            <div class="col-lg-6 position-relative z-index-2">
              <div class="row mt-4">
                <div class="col-lg-12 mt-4">
                  <DeliveryAll :storeId='storeId'/>
                </div>
              </div>
              <button @click="goToBack">GO TO BACK</button>
            </div>
          </div>
  
        </div>
      </div>
    </div>
</template>

<script setup>

    import MiniStatisticsCard from "./components/MiniStatisticsCard.vue";
    import MonthlySaleAll from "./components/Insight/MonthlySaleAll.vue";
    import HourlySaleAll from "./components/Insight/HourlySaleAll.vue";
    import TypeAll from "./components/Insight/TypeAll.vue";
    import DeliveryAll from "./components/Insight/DeliveryAll.vue";
    import MenuAll from "./components/Insight/MenuAll.vue";
    import { useRouter, useRoute } from 'vue-router';
    import {computed, ref} from "vue";

    const router = useRouter();
    const route = useRoute();


    // 지점 데이터 객체 정의
    const branchData = {
      1: { name: '강남역점', address: '서울특별시 서초구 강남대로 435'},
      2: { name: '고속터미널점', address: '서울특별시 서초구 신반포로 176'},
      3: { name: '서울역점', address: '서울특별시 중구 한강대로 405'},
      4: { name: '여의도점', address: '서울특별시 영등포구 여의대로 108 더현대 서울 B1'},
      5: { name: '신대방삼거리점', address: '서울시 동작구 보라매로 87'}
    };



    const storeId = ref(route.query.storeId || "1");
    console.log("subDash storeId" , storeId);


    // storeId에 따라 지점 데이터 가져오기
    const branchName = computed(() => branchData[storeId.value].name);
    const branchAddress = computed(() => branchData[storeId.value].address);

    const goToBack = () => {
        router.push({ name: "OwnerHome" });
    };

</script>
