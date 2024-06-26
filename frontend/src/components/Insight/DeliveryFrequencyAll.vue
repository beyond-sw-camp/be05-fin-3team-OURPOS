<template>
    <div id="map" style="height: 100vh;"></div>
  </template>

  <script setup>
  import "leaflet/dist/leaflet.css";
  import * as L from 'leaflet';
  import 'leaflet.heat/dist/leaflet-heat.js';
  import axios from 'axios';

  const loadMap = async () => {
    const map = L.map('map').setView([37.559819, 126.963895], 11);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
    }).addTo(map);

    // Fetching administrative boundary data
    const response = await axios.get('/seoul_geojson_wgs84.geojson');
    const boundaryData = response.data;

    // Adding administrative boundaries to the map
    L.geoJson(boundaryData).addTo(map);

    // Fetching location data with frequency information
    const locationResponse = await axios.get('https://api.ourpos.org/api/v1/orders/delivery/frequency?storeId=1');
    const locationData = locationResponse.data.data;

    // Extracting locations with frequency information
    const heatData = locationData
      .filter(location => location.latitude !== null && location.longitude !== null)
      .map(location => [location.latitude, location.longitude, location.frequency]);

    // Adding heatmap layer
    L.heatLayer(heatData, { radius: 25 }).addTo(map);
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
