import { useState } from 'react'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi } from '../services/api'

export default function OtpVerify() {
  const location = useLocation()
  const phone = location.state?.phone || ''
  const [otp, setOtp] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await authApi.verifyOtp({ phone, otp })
      login(res.data.data)
      navigate('/home')
    } catch (err) {
      setError(err.response?.data?.message || 'OTP không đúng hoặc đã hết hạn.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">🔢</div>
        <h2>Nhập Mã OTP</h2>

        <p style={{ color: '#555', textAlign: 'center', marginBottom: '0.5rem', fontSize: '0.9rem' }}>
          Mã OTP đã được gửi đến số <strong>{phone}</strong>
        </p>

        {phone && (
          <div style={{ textAlign: 'center', marginBottom: '1.2rem' }}>
            <a
              href={`http://localhost:8080/api/auth/otp-inbox/${phone}`}
              target="_blank"
              rel="noreferrer"
              style={{
                display: 'inline-flex', alignItems: 'center', gap: '6px',
                background: '#e3f2fd', color: '#1565c0', padding: '8px 16px',
                borderRadius: '20px', textDecoration: 'none', fontSize: '0.88rem',
                border: '1px solid #90caf9', fontWeight: 500
              }}
            >
              📬 Mở hộp thư SMS giả lập (xem OTP)
            </a>
          </div>
        )}

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Mã OTP (6 chữ số)</label>
            <input
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              placeholder="______"
              maxLength={6}
              inputMode="numeric"
              pattern="[0-9]{6}"
              autoComplete="one-time-code"
              required
              style={{ letterSpacing: '0.3rem', fontSize: '1.4rem', textAlign: 'center' }}
            />
          </div>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Đang xác minh...' : 'Xác Nhận OTP'}
          </button>
        </form>

        <div className="auth-links" style={{ marginTop: '1rem' }}>
          <Link to="/phone-login">← Gửi lại OTP</Link>
          <span className="divider"> · </span>
          <Link to="/login">Đăng nhập thường</Link>
        </div>
      </div>
    </div>
  )
}
