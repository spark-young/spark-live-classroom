<template>
  <div  id="app">
    <router-view id="content" />
    <div id="footer">&copy;copy by spark</div>
  </div>
</template>

<style>
* {
  margin: 0px;
  padding: 0px;
  font-style: dengxian;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#content{
  min-height: 94vh !important;
}
#footer {
  height: 6vh;
  line-height: 6vh !important;
  background-color:rgb(32, 34, 38);
  color: wheat;
}
</style>
