<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
const auth = useAuthStore()
const router = useRouter()
function signOut() { auth.logout(); router.push({ name: 'home' }) }
</script>

<template>
  <div class="app-shell">
    <header class="topbar">
      <RouterLink class="brand" to="/"><span class="brand-mark">史</span><span><strong>现代历史学</strong><small>智慧教学平台</small></span></RouterLink>
      <nav class="main-nav" aria-label="主导航">
        <RouterLink to="/">首页</RouterLink><RouterLink to="/course/introduction">课程导览</RouterLink><RouterLink to="/knowledge/main-map">问题图谱</RouterLink><RouterLink to="/learning/challenge">学习中心</RouterLink><RouterLink to="/resources/search">资源中心</RouterLink><RouterLink to="/interaction/poll">课堂互动</RouterLink><RouterLink to="/ai/tutor">智能伴学</RouterLink>
      </nav>
      <div class="account-actions"><template v-if="auth.authenticated"><span class="account-name">{{ auth.user?.displayName || '学习者' }}</span><button class="text-button" @click="signOut">退出</button></template><template v-else><RouterLink to="/login" class="text-button">登录</RouterLink><RouterLink to="/register" class="small-cta">加入课程</RouterLink></template></div>
    </header>
    <main><slot /></main>
    <footer class="site-footer"><span>现代历史学 · 国家级一流本科课程智慧教学平台</span><span>以史料为据 · 以问题为径</span></footer>
  </div>
</template>
