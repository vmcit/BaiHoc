import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

/**
 * OAuth2Callback - Trang xử lý redirect từ backend sau khi đăng nhập Google
 * URL: /oauth2/callback?token=xxx
 * Lưu token vào localStorage và điều hướng về trang chủ
 */
export default function OAuth2Callback() {
  const [searchParams] = useSearchParams()
  const { login } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState('')

  useEffect(() => {
    const token = searchParams.get('token')
    if (token) {
      // Giải mã payload từ JWT để lấy thông tin user
      try {
        const payload = JSON.parse(atob(token.split('.')[1]))
        const userData = {
          username: payload.sub,
          userId: payload.userId,
          roles: payload.roles || [],
          accessToken: token,
        }
        login(userData)
        navigate('/home', { replace: true })
      } catch {
        setError('Token không hợp lệ. Vui lòng thử lại.')
      }
    } else {
      setError('Đăng nhập Google thất bại. Vui lòng thử lại.')
    }
  }, [searchParams, login, navigate])

  if (error) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <div className="auth-logo">❌</div>
          <h2>Lỗi đăng nhập</h2>
          <div className="alert alert-error">{error}</div>
          <a href="/login" className="btn-primary" style={{ display: 'block', textAlign: 'center', marginTop: '1rem' }}>
            Quay lại đăng nhập
          </a>
        </div>
      </div>
    )
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">⏳</div>
        <h2>Đang xử lý đăng nhập...</h2>
        <p style={{ textAlign: 'center', color: '#666' }}>Vui lòng chờ trong giây lát</p>
      </div>
    </div>
  )
}
