import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../services/api'

export default function PhoneLogin() {
  const [form, setForm] = useState({ phone: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await authApi.phoneLogin(form)
      navigate('/otp-verify', { state: { phone: form.phone } })
    } catch (err) {
      setError(err.response?.data?.message || 'Số điện thoại hoặc mật khẩu không đúng.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">📱</div>
        <h2>Đăng Nhập Bằng SĐT</h2>
        <p style={{ color: '#888', fontSize: '0.9rem', marginBottom: '1rem', textAlign: 'center' }}>
          Nhập số điện thoại và mật khẩu để nhận mã OTP
        </p>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Số điện thoại</label>
              <input
                name="phone"
                value={form.phone}
                onChange={handleChange}
                placeholder="Ví dụ: 0968965682"
                autoComplete="off"
                inputMode="numeric"
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
              {loading ? 'Đang xử lý...' : 'Gửi Mã OTP'}
            </button>
          </form>

        <div className="auth-links" style={{ marginTop: '1rem' }}>
          <Link to="/login">← Quay lại đăng nhập thường</Link>
        </div>
      </div>
    </div>
  )
}
