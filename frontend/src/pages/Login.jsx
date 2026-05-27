import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi } from '../services/api'

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await authApi.login(form)
      login(res.data.data)
      navigate('/home')
    } catch (err) {
      setError(err.response?.data?.message || 'Đăng nhập thất bại. Kiểm tra lại tài khoản/mật khẩu.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">🔐</div>
        <h2>Đăng Nhập</h2>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tên đăng nhập</label>
            <input
              name="username"
              value={form.username}
              onChange={handleChange}
              placeholder="Nhập username"
              autoComplete="username"
              required
            />
          </div>
          <div className="form-group">
            <label>Mật khẩu</label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              placeholder="••••••••"
              autoComplete="current-password"
              required
            />
          </div>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Đang đăng nhập...' : 'Đăng Nhập'}
          </button>
        </form>

        <div className="auth-divider">
          <span>hoặc</span>
        </div>

        <Link to="/phone-login" className="btn-google" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px', textDecoration: 'none', marginBottom: '0.6rem' }}>
          📱 Đăng nhập bằng Số Điện Thoại
        </Link>

        <Link to="/qr-login" className="btn-google" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px', textDecoration: 'none', marginBottom: '0.6rem' }}>
          🔑 Đăng nhập bằng Google Authenticator
        </Link>

        <a
          href="http://localhost:8080/oauth2/authorization/google"
          className="btn-google"
        >
          <img
            src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"
            alt="Google"
            width="20"
            height="20"
          />
          Đăng nhập bằng Google
        </a>

        <div className="auth-links">
          <Link to="/forgot-password">Quên mật khẩu?</Link>
          <span className="divider"> · </span>
          <Link to="/register">Tạo tài khoản mới</Link>
        </div>
      </div>
    </div>
  )
}
