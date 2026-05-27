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
  // Đăng nhập bằng SĐT + OTP
  phoneLogin: (data) => api.post('/api/auth/phone-login', data),
  verifyOtp: (data) => api.post('/api/auth/verify-otp', data),
  // Đăng nhập bằng Google Authenticator (TOTP)
  qrLogin: (data) => api.post('/api/auth/qr-login', data),
  totpActivate: (data) => api.post('/api/auth/totp/activate', data),
  totpVerify: (data) => api.post('/api/auth/totp/verify', data),
}

export const userApi = {
  getAll: () => api.get('/api/protected/admin/users'),
}

export default api
