import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: () => import('../views/LoginView.vue'), meta: { guest: true, title: '登录' } },
    { path: '/register', name: 'register', component: () => import('../views/RegisterView.vue'), meta: { guest: true, title: '注册' } },
    { path: '/', name: 'home', component: () => import('../views/HomeView.vue'), meta: { title: '首页' } },
    { path: '/:pathMatch(.*)*', name: 'not-found', component: () => import('../views/NotFoundView.vue'), meta: { title: '页面不存在' } },
  ],
  scrollBehavior: () => ({ top: 0 }),
})
router.beforeEach(async (to) => {
  document.title = `${String(to.meta.title || '智慧教学平台')} · 现代历史学`
  const auth = useAuthStore()
  if (auth.authenticated && !auth.user) {
    try { await auth.loadMe() } catch { auth.logout() }
  }
  if (to.meta.guest && auth.authenticated) return { name: 'home' }
})
export default router
