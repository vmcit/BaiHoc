import { useState } from 'react'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi } from '../services/api'

export default function TotpVerify() {
  const { state } = useLocation()
  const phone = state?.phone
  const [code, setCode] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  if (!phone) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <p>Dữ liệu không hợp lệ. <Link to="/qr-login">Quay lại</Link></p>
        </div>
      </div>
    )
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await authApi.totpVerify({ phone, code })
      login(res.data.data)
      navigate('/home')
    } catch (err) {
      setError(err.response?.data?.message || 'Mã không đúng hoặc đã hết hạn (30 giây).')
      setCode('')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">🔐</div>
        <h2>Xác Thực Google Authenticator</h2>

        <p style={{ color: '#555', textAlign: 'center', marginBottom: '0.5rem' }}>
          Mở ứng dụng Google Authenticator và nhập mã 6 số hiện tại cho số <b>{phone}</b>.
        </p>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Mã xác thực (6 số)</label>
            <input
              value={code}
              onChange={(e) => setCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
              placeholder="000000"
              inputMode="numeric"
              autoComplete="one-time-code"
              maxLength={6}
              style={{ fontSize: '1.6rem', letterSpacing: '0.6rem', textAlign: 'center' }}
              autoFocus
              required
            />
          </div>
          <button type="submit" className="btn-primary" disabled={loading || code.length !== 6}>
            {loading ? 'Đang xác thực...' : 'Xác Nhận & Đăng Nhập'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '1rem', fontSize: '0.85rem', color: '#888' }}>
          Mã mới sau mỗi 30 giây — dùng mã đang hiển thị trong app
        </div>

        <div className="auth-links" style={{ marginTop: '1rem' }}>
          <Link to="/qr-login">← Quay lại</Link>
        </div>
      </div>
    </div>
  )
}
