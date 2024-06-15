<template>
  <div class="py-4 container-fluid">
    <div class="row mb-4">
      <div class="col-lg-12 position-relative z-index-2">


        <div class="row">
          <div class="col-lg-3 col-md-6 col-sm-6 mt-lg-0 mt-4">
            <mini-statistics-card :title="{ text: 'New Clients', value: '3,462' }"
              detail="<span class='text-danger text-sm font-weight-bolder'>-2%</span> than yesterday" :icon="{
                name: 'person',
                color: 'text-white',
                background: 'success',
              }" />
          </div>
          <div class="col-lg-3 col-md-6 col-sm-6 mt-lg-0 mt-4">
            <mini-statistics-card :title="{ text: 'Sales', value: '$103,430' }"
              detail="<span class='text-success text-sm font-weight-bolder'>+5%</span> Just updated" :icon="{
                name: 'weekend',
                color: 'text-white',
                background: 'info',
              }" />
              <button @click="goToStore">지점 이동</button>
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
          :headers="['Branch', 'DashBoard', 'Address']" :projects="[
            {
              logo: logoXD,
              title: '강남역점',
              dashboard: '강남역점 대시보드',
              address: '서울 특별시 서초구 강남대로 435'
            },
            {
              logo: logoAtlassian,
              title: '고속터미널점',
              dashboard: '고속터미널점 대시보드',
              address: '서울특별시 서초구 신반포로 176'
            },
            {
              logo: logoSlack,
              title: '서울역점',
              dashboard: '서울역점 대시보드',
              address: '서울특별시 중구 한강대로 405 2층'
            },
            {
              logo: logoSpotify,
              title: '여의도점',
              dashboard: '여의도점 대시보드',
              address: '서울특별시 영등포구 여의대로 108 더현대 서울 B1'
            },
            {
              logo: logoJira,
              title: '신대방삼거리점',
              dashboard: '신대방삼거리점 대시보드',
              address: '서울특별시 동작구 보라매로 87'
            },
          ]" />
      </div>
      <div class="col-lg-4 col-md-6">
        <timeline-list class="h-100" title="Orders overview" description="<i class='fa fa-arrow-up text-success' aria-hidden='true'></i>
        <span class='font-weight-bold'>24%</span> this month">
          <timeline-item :icon="{
            component: 'notifications',
            class: 'text-success',
          }" title="$2400 Design changes" date-time="22 DEC 7:20 PM" />
          <TimelineItem :icon="{
            component: 'code',
            class: 'text-danger',
          }" title="New order #1832412" date-time="21 DEC 11 PM" />
          <TimelineItem :icon="{
            component: 'shopping_cart',
            class: 'text-info',
          }" title="Server payments for April" date-time="21 DEC 9:34 PM" />
          <TimelineItem :icon="{
            component: 'credit_card',
            class: 'text-warning',
          }" title="New card added for order #4395133" date-time="20 DEC 2:20 AM" />
          <TimelineItem :icon="{
            component: 'vpn_key',
            class: 'text-primary',
          }" title="Unlock packages for development" date-time="18 DEC 4:54 AM" class="pb-1" />
        </timeline-list>
      </div>
    </div>
  </div>
</template>

<script setup>

  import MiniStatisticsCard from "./components/MiniStatisticsCard.vue";
  import ProjectCard from "./components/ProjectCard.vue";
  import TimelineList from "@/examples/Cards/TimelineList.vue";
  import TimelineItem from "@/examples/Cards/TimelineItem.vue";
  import MonthlySaleAll from "./components/Insight/MonthlySaleAll.vue";
  import HourlySaleAll from "./components/Insight/HourlySaleAll.vue";
  import TypeAll from "./components/Insight/TypeAll.vue";
  import DeliveryAll from "./components/Insight/DeliveryAll.vue";
  import MenuAll from "./components/Insight/MenuAll.vue";
  import { useRouter } from 'vue-router';
  
  const router = useRouter();

  const goToStore = () => {
    router.push({ name: "SubDash" });
  };

</script>
