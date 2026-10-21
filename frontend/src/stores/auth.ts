import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api, type ApiResponse } from '../api/client'

export interface User { id: number; email: string; displayName: string; roles: string[] }
interface AuthPayload { token: string; user: User }
export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('history-token') || '')
  const user = ref<User | null>(null)
  const authenticated = computed(() => Boolean(token.value))
  async function login(email: string, password: string) {
    const { data } = await api.post<ApiResponse<AuthPayload>>('/api/auth/login', { email, password })
    token.value = data.data.token; user.value = data.data.user; localStorage.setItem('history-token', token.value)
  }
  async function register(email: string, displayName: string, password: string) {
    const { data } = await api.post<ApiResponse<AuthPayload>>('/api/auth/register', { email, displayName, password })
    token.value = data.data.token; user.value = data.data.user; localStorage.setItem('history-token', token.value)
  }
  async function loadMe() {
    if (!token.value) return
    const { data } = await api.get<ApiResponse<User>>('/api/auth/me'); user.value = data.data
  }
  function logout() { token.value = ''; user.value = null; localStorage.removeItem('history-token') }
  return { token, user, authenticated, login, register, loadMe, logout }
})
