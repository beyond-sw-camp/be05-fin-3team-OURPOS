<template>
  <chart-holder-card
      color="white"
      update="실시간 업데이트"
      title="배달 발생 지역 시각화"
  >
    <div id="map" style="height: 796px;"></div>
    <p>Store ID: {{ storeId }}</p>
  </chart-holder-card>
</template>

<script setup>
import ChartHolderCard from "@/views/components/ChartHolderCard.vue";
import "leaflet/dist/leaflet.css";
import { ref, onMounted } from 'vue';
import * as L from 'leaflet';
import axios from 'axios';

const loadMap = async () => {
  const map = L.map('map').setView([37.559819, 126.963895], 11);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
  }).addTo(map);

  // 행정동 구분선 추가
  const response = await fetch('/seoul_geojson_wgs84.geojson');
  const data = await response.json();

  // Choropleth layer example - customize based on your data
  const getColor = d => {
    return d > 1000 ? '#800026' :
        d > 500  ? '#BD0026' :
            d > 200  ? '#E31A1C' :
                d > 100  ? '#FC4E2A' :
                    d > 50   ? '#FD8D3C' :
                        d > 20   ? '#FEB24C' :
                            d > 10   ? '#FED976' :
                                '#FFEDA0';
  };

  const style = feature => {
    return {
      fillColor: getColor(feature.properties.your_property), // Adjust this line based on your data
      weight: 1,
      opacity: 1,
      color: 'red',
      dashArray: '3',
      fillOpacity: 0.7
    };
  };

  L.geoJson(data, { style }).addTo(map);

  const locations = ref([]);

  const getLocations = async () => {
    try {
      const response = await axios.get('https://api.ourpos.org/api/v1/orders/delivery/frequency/my', {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        }
      });
      locations.value = response.data.data
          .filter(location => location.latitude !== null && location.longitude !== null)
          .map(location => [location.latitude, location.longitude]);

      console.log("getLocations 함수 내의 locations 상태 ", locations.value)
      addLocations(map, locations);
    } catch (error) {
      console.error('Error fetching location data:', error);
    }
  };

  const addLocations = (map, locations) => {
    locations.value.forEach(function (location) {
      L.marker(location).addTo(map); // 기본 마커를 사용합니다.
    });
  };

  // Ensure map is loaded before fetching locations
  await getLocations();
};

onMounted(() => {
  loadMap();
});


</script>
