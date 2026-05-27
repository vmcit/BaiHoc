import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../services/api'

export default function QrLogin() {
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
      const res = await authApi.qrLogin(form)
      const data = res.data.data
      if (data.status === 'SETUP_REQUIRED') {
        // Lần đầu: chuyển sang trang setup QR
        navigate('/totp-setup', {
          state: { phone: data.phone, qrDataUri: data.qrDataUri, secret: data.secret },
        })
      } else if (data.status === 'OTP_REQUIRED') {
        // Đã kích hoạt: chuyển sang nhập mã
        navigate('/totp-verify', { state: { phone: data.phone } })
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Xác thực thất bại. Kiểm tra lại SĐT/mật khẩu.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">🔑</div>
        <h2>Đăng Nhập Bằng Google Authenticator</h2>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Số điện thoại</label>
            <input
              name="phone"
              value={form.phone}
              onChange={handleChange}
              placeholder="VD: 0936352582"
              inputMode="numeric"
              autoComplete="off"
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
            {loading ? 'Đang xác thực...' : 'Tiếp tục'}
          </button>
        </form>

        <div className="auth-links" style={{ marginTop: '1rem' }}>
          <Link to="/login">← Quay lại đăng nhập</Link>
        </div>

        <div style={{ marginTop: '1rem', padding: '12px', background: '#f5f5f5', borderRadius: '8px', fontSize: '0.85rem', color: '#666' }}>
          <b>Tài khoản test:</b><br />
          SĐT: <code>0936352582</code> &nbsp;|&nbsp; MK: <code>123456</code>
        </div>
      </div>
    </div>
  )
}
