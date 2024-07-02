<template>
  <div class="py-4 container-fluid">
    <div class="row mb-4">
      <div class="col-lg-12 position-relative z-index-2">


        <div class="row">
          <div class="col-lg-3 col-md-6 col-sm-6 mt-lg-0 mt-4">
            <mini-statistics-card :title="{ text: '현재 지점수', value: '5' }"
              detail="<span class='text-danger text-sm font-weight-bolder'></span>" :icon="{
                name: 'person',
                color: 'text-white',
                background: 'success',
              }" />
          </div>
        </div>


        <div class="row mb-4 col-lg-12">
          <div class="col-lg-6 position-relative z-index-2">
            <div class="row mt-4">
              <div class="col-lg-8 col-md-6 mt-4">
                <MonthlySaleAll/>
              </div>
              <div class="col-lg-4 col-md-6 mt-4">
                <TypeAll/>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-lg-8 col-md-6 mt-4">
                <HourlySaleAll />
              </div>
              <div class="col-lg-4 col-md-6 mt-4">
                <MenuAll/>
              </div>
            </div>
          </div>

          <div class="col-lg-6 position-relative z-index-2">
            <div class="row mt-4">
              <div class="col-lg-12 mt-4">
                <DeliveryAll />
              </div>
            </div>
          </div>
        </div>







      </div>
    </div>

    <div class="row">
      <div class="col-lg-8 col-md-6 mb-md-0 mb-4">
        <project-card title="Current States Of Franchise"
          description="<i class='fa fa-check text-info' aria-hidden='true'></i> <span class='font-weight-bold ms-1'>5 franchies</span> real time"
                      :headers="['Branch', 'DashBoard', 'Address']"
                      :projects="franchises"
        />
      </div>

    </div>
  </div>
</template>

<script setup>

  import MiniStatisticsCard from "./components/MiniStatisticsCard.vue";
  import ProjectCard from "./components/ProjectCard.vue";
  import MonthlySaleAll from "./components/Insight/MonthlySaleAll.vue";
  import HourlySaleAll from "./components/Insight/HourlySaleAll.vue";
  import TypeAll from "./components/Insight/TypeAll.vue";
  import DeliveryAll from "./components/Insight/DeliveryAll.vue";
  import MenuAll from "./components/Insight/MenuAll.vue";
  import {ref} from "vue";
  import logo from "@/assets/img/logos/store-check-outline.png";


  const franchises = ref([
    {
      logo: logo,
      title: "강남역점",
      dashboard: "강남역점 대시보드",
      address: "서울 특별시 서초구 강남대로 435",
      storeId: "1", // 각 프랜차이즈에 해당하는 storeId 설정
    },
    {
      logo: logo,
      title: "고속터미널점",
      dashboard: "고속터미널점 대시보드",
      address: "서울특별시 서초구 신반포로 176",
      storeId: "2",
    },{
      logo: logo,
      title: '서울역점',
      dashboard: '서울역점 대시보드',
      address: '서울특별시 중구 한강대로 405 2층',
      storeId: "3",
    },
    {
      logo: logo,
      title: '여의도점',
      dashboard: '여의도점 대시보드',
      address: '서울특별시 영등포구 여의대로 108 더현대 서울 B1',
      storeId: "4",
    },
    {
      logo: logo,
      title: '신대방삼거리점',
      dashboard: '신대방삼거리점 대시보드',
      address: '서울특별시 동작구 보라매로 87',
      storeId: "5",
    },
  ]);

</script>
