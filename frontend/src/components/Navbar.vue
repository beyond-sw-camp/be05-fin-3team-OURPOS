<template>
    <div>
    <nav>
        <v-toolbar class="cyan lighten-1" dark prominent height="65">

            <v-toolbar-title class="text-uppercase gray--text">
                <span class="font-weight-light">Stock</span>
                <span>Dashboard</span>
            </v-toolbar-title>

            <v-spacer></v-spacer>
            <v-btn text color="black">
                <span>Log Out</span>
                <v-icon right>exit_to_app</v-icon>
            </v-btn>
        </v-toolbar>

     </nav>
        </div>
</template>

<script>

    export default {
        data(){
          return {

          }
        }
    }
</script>

<style>
.cyan lighten-1 {
    background-color: #00bcd4 ;
}

</style>