<template>
    <v-app>
      <v-navigation-drawer
        app
        permanent
        image="https://www.colorpalettestore.com/cdn/shop/products/EDA768_800x.png?v=1614620266"
        dark
      >
        <v-list nav>
          <v-list-item
            prepend-icon="mdi-email"
            title="Inbox"
            value="inbox"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-account-supervisor-circle"
            title="Supervisors"
            value="supervisors"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-clock-start"
            title="Clock-in"
            value="clockin"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-account-supervisor-circle"
            title="Supervisors"
            value="supervisors"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-account-supervisor-circle"
            title="Supervisors"
            value="supervisors"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-account-supervisor-circle"
            title="Supervisors"
            value="supervisors"
          ></v-list-item>
        </v-list>
      </v-navigation-drawer>
  
      <v-main>
        <v-container fluid style="height: 250px">
          <!-- Your main content goes here -->
        </v-container>
      </v-main>
    </v-app>
  </template>
  
  <script>
  export default {
    name: "MyComponent",
  };
  </script>
  