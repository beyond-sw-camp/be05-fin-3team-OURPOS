<template>
    <v-app>
      <v-main>
        <v-container fluid>
          <!-- Hero Section -->
          <v-row class="hero-section" justify="center" align="center">
            <v-col class="text-center">
              <h1 class="display-1">Welcome to Food Ordering Service</h1>
              <p class="subheading">Order your favorite meals quickly and easily</p>
              <div class="footer-section">
                <v-container>
                  <v-row justify="center">
                    <v-col cols="12" md="8" class="footer-tab">
                      <v-btn color="red" @click="navigateTo('store')" class="footer-btn">매장</v-btn>
                      <v-btn color="warning" @click="navigateTo('delivery')" class="footer-btn">배달</v-btn>
                    </v-col>
                  </v-row>
                </v-container>
              </div>
            </v-col>
          </v-row>
          <!-- Content Section -->
        </v-container>
      </v-main>
    </v-app>
  </template>
  
  <script>
  export default {
    name: 'App',
    methods: {
      navigateTo(type) {
        if (type === 'store') {
          this.$router.push('/store');
        } else if (type === 'delivery') {
          this.$router.push('/delivery');
        }
      },
    },
  };
  </script>
  
  <style>
  .hero-section {
    height: 100vh;
    background: url('https://cambridgeleisure.co.uk/sites/cambridge_leisure/files/styles/whats_on_gallery_small/public/images/shops/gallery/five_guys.jpg?itok=YKUdHJuQ') center center / cover no-repeat;
    color: white;
  }
  .subheading {
    font-size: 1.5rem;
    margin-top: 1rem;
  }
  .footer-section {
    position: absolute;
    bottom: 20px;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .footer-tab {
    background-color: rgba(255, 255, 255, 0.8);
    padding: 40px;
    border-radius: 10px;
    display: flex;
    justify-content: center;
  }
  .footer-btn {
    margin: 0 10px;
  }
  </style>