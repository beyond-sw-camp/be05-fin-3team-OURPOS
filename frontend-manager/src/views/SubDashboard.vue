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
  
      <div class="row">
        <div class="col-lg-8 col-md-6 mb-md-0 mb-4">
          <project-card title="Projects"
            description="<i class='fa fa-check text-info' aria-hidden='true'></i> <span class='font-weight-bold ms-1'>30 done</span> this month"
            :headers="['Companies', 'Members', 'Budget', 'Progress']" :projects="[
              {
                logo: logoXD,
                title: 'Material XD Material XD Version',
                members: [team1, team2, team3, team4],
                budget: '$14,000',
                progress: { percentage: 60, color: 'info' },
              },
              {
                logo: logoAtlassian,
                title: 'Add Progress Track',
                members: [team2, team4],
                budget: '$3,000',
                progress: { percentage: 10, color: 'info' },
              },
              {
                logo: logoSlack,
                title: 'Fix Platform Errors',
                members: [team3, team1],
                budget: 'Not set',
                progress: { percentage: 100, color: 'success' },
              },
              {
                logo: logoSpotify,
                title: 'Launch our Mobile App',
                members: [team4, team3, team4, team1],
                budget: '$20,500',
                progress: { percentage: 100, color: 'success' },
              },
              {
                logo: logoJira,
                title: 'Add the New Pricing Page',
                members: [team4],
                budget: '$500',
                progress: { percentage: 25, color: 'info' },
              },
              {
                logo: logoJira,
                title: 'Redesign New Online Shop',
                members: [team1, team4],
                budget: '$2,000',
                progress: { percentage: 40, color: 'info' },
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

    const storeId = "1";

    const goToBack = () => {
        router.push({ name: "OwnerHome" });
    };

</script>
