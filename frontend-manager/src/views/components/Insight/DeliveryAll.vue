<template>
    <chart-holder-card
      color="white"
      update="실시간 업데이트"
      title="배달 발생 빈도수 히트맵"
      subtitle="행정구역 기준으로 배달 위치를 분석하여 시각화한 자료"
    >
    <div id="map" style="height: 796px;"></div>
    </chart-holder-card>
  </template>
  
  <script setup>
  import "leaflet/dist/leaflet.css";
  import * as L from 'leaflet';
  import axios from 'axios';
  import ChartHolderCard from "../ChartHolderCard.vue";
  
  import { defineProps } from "vue"

// eslint-disable-next-line no-unused-vars
const props = defineProps(["storeId"]);

  const loadMap = async () => {
    const map = L.map('map').setView([37.559819, 126.963895], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
    }).addTo(map);
  
    // Fetching administrative boundary data
    const response = await axios.get('/seoul_geojson_wgs84.geojson');
    const boundaryData = response.data;
  
    // Fetching location data with frequency information
    const locationResponse = await axios.get('https://api.ourpos.org/api/v1/orders/delivery/frequency', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
        },
      params: {
        storeId: props.storeId,
      }
    });
    const locationData = locationResponse.data.data;
  
    // Function to find which polygon a point is inside
    const isPointInPolygon = (point, vs) => {
      const x = point[0], y = point[1];
      let inside = false;
      for (let i = 0, j = vs.length - 1; i < vs.length; j = i++) {
        const xi = vs[i][0], yi = vs[i][1];
        const xj = vs[j][0], yj = vs[j][1];
        const intersect = ((yi > y) !== (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
      }
      return inside;
    };
  
    // Counting frequencies within each administrative boundary
    const frequencyMap = {};
  
    boundaryData.features.forEach(feature => {
      const properties = feature.properties;
      const boundaryName = properties.ADSTRD_NM; // 올바른 키 사용
      if (!frequencyMap[boundaryName]) {
        frequencyMap[boundaryName] = 0;
      }
  
      // 디버깅을 위해 경계 좌표를 출력
      const polygons = feature.geometry.coordinates;
      // console.log('Boundary Name:', boundaryName);
      // console.log('Polygons:', polygons);
  
      locationData.forEach(location => {
        const point = [location.longitude, location.latitude];
        let inPolygon = false;
        
        // 폴리곤의 각 부분을 검사
        for (const polygon of polygons) {
          // MultiPolygon 구조의 경우도 확인
          for (const ring of polygon) {
            if (isPointInPolygon(point, ring)) {
              inPolygon = true;
              break;
            }
          }
          if (inPolygon) break;
        }
  
        if (inPolygon) {
          frequencyMap[boundaryName] += 1; // 개수를 세므로 1씩 더함
          // console.log(`Location ${point} is inside ${boundaryName}`);
        }
      });
    });
  
    // console.log('Frequency Map:', frequencyMap); // 디버깅을 위해 frequencyMap 출력
  
    // Function to get color based on frequency
    const getColor = (frequency) => {
      return frequency >= 6 ? '#800026' :
             frequency >= 5  ? '#BD0026' :
             frequency >= 4  ? '#E31A1C' :
             frequency >= 3  ? '#FC4E2A' :
             frequency >= 2  ? '#FD8D3C' :
             frequency >= 1  ? '#FEB24C' :
                               '#FFFFFF';
    };
  
    // Adding the boundaries with color based on frequency
    L.geoJson(boundaryData, {
      style: (feature) => {
        const boundaryName = feature.properties.ADSTRD_NM;
        const frequency = frequencyMap[boundaryName];
        return {
          fillColor: getColor(frequency),
          weight: 2,
          opacity: 1,
          color: 'white',
          dashArray: '3',
          fillOpacity: 0.7
        };
      }
    }).addTo(map);
  
  };
  
  import { onMounted } from 'vue';
  
  onMounted(() => {
    loadMap();
  });
  </script>
  
  <style>
  #map {
    height: 100vh;
    width: 100%;
  }
  </style>
  