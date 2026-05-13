import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
})

// Tự động đính kèm Access Token vào mỗi request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const authApi = {
  login: (data) => api.post('/api/auth/login', data),
  register: (data) => api.post('/api/users/register', data),
  logout: (data) => api.post('/api/auth/logout', data),
  forgotPassword: (data) => api.post('/api/users/forgot-password', data),
  resetPassword: (data) => api.post('/api/users/reset-password', data),
}

export default api
