<template>
    <div class="py-4 container-fluid">
      <div class="row mb-4">
        <div class="col-lg-12 position-relative z-index-2">
  
  
          <div class="row">
            <div class="col-lg-3 col-md-6 col-sm-6 mt-lg-0 mt-4">
              <mini-statistics-card :title="{ text: '지점명', value: '신대방삼거리' }"
                detail="서울시 동작구 보라매로 87" :icon="{
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
    import { useRouter } from 'vue-router';

    const router = useRouter();

    const storeId = "1";

    const goToBack = () => {
        router.push({ name: "OwnerHome" });
    };

</script>
