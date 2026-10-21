import axios from 'axios'
import router from '../router'

export interface ApiResponse<T> { success: boolean; data: T; error: { code: string; message: string } | null; timestamp: string }
export const api = axios.create({ baseURL: import.meta.env.VITE_API_BASE_URL || '', timeout: 15000, headers: { 'Content-Type': 'application/json' } })
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('history-token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})
api.interceptors.response.use((response) => response, async (error) => {
  if (error.response?.status === 401 && !String(error.config?.url).includes('/auth/')) {
    localStorage.removeItem('history-token')
    await router.push({ name: 'login' })
  }
  return Promise.reject(error)
})
