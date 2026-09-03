<template>
    <div id="nav" style="display: flex;align-items: center; justify-content: center">
        <div v-html="topTitle" style="padding-top: 20px"></div>
    </div>
</template>

<script>
export default {
    name: "App",
    data() {
        return {
            topTitle: "一对一英语直播教学平台"
        }
    }
}
</script>