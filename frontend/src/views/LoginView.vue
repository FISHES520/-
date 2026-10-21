<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import AppShell from '../components/AppShell.vue'
const form=reactive({email:'',password:''}); const error=ref(''); const loading=ref(false); const auth=useAuthStore(); const router=useRouter()
async function submit(){loading.value=true;error.value='';try{await auth.login(form.email,form.password);await router.push('/')}catch(e:any){error.value=e.response?.data?.error?.message||'登录失败，请检查邮箱和密码'}finally{loading.value=false}}
</script>
<template><AppShell><section class="auth-page"><div class="auth-aside"><span class="eyebrow">WELCOME BACK</span><h1>让每一次学习<br/>都回到历史现场</h1><p>继续你的课程探索，沿着问题、证据与解释，读懂现代世界。</p></div><el-card class="auth-card" shadow="never"><span class="eyebrow">学习账户</span><h2>登录平台</h2><el-alert v-if="error" :title="error" type="error" :closable="false"/><el-form :model="form" label-position="top" @submit.prevent="submit"><el-form-item label="邮箱"><el-input v-model="form.email" type="email" autocomplete="email" placeholder="name@university.edu" @keyup.enter="submit"/></el-form-item><el-form-item label="密码"><el-input v-model="form.password" type="password" show-password autocomplete="current-password" placeholder="请输入密码" @keyup.enter="submit"/></el-form-item><el-button class="form-submit" type="primary" :loading="loading" @click="submit">登录</el-button></el-form><p class="form-foot">还没有账户？ <RouterLink to="/register">创建学生账户</RouterLink></p></el-card></section></AppShell></template>
