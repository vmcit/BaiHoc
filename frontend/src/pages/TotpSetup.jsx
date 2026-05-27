import { useState } from 'react'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi } from '../services/api'

export default function TotpSetup() {
  const { state } = useLocation()
  const { phone, qrDataUri, secret } = state || {}
  const [code, setCode] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  if (!phone || !qrDataUri) {
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
      const res = await authApi.totpActivate({ phone, code })
      login(res.data.data)
      navigate('/home')
    } catch (err) {
      setError(err.response?.data?.message || 'Mã không đúng, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card" style={{ maxWidth: '440px' }}>
        <div className="auth-logo">📲</div>
        <h2>Thiết Lập Google Authenticator</h2>

        <div style={{ padding: '12px', background: '#e3f2fd', borderRadius: '8px', marginBottom: '1.2rem', fontSize: '0.9rem', color: '#1565c0' }}>
          <b>Lần đầu đăng nhập</b> — Quét mã QR bên dưới bằng ứng dụng Google Authenticator để kích hoạt xác thực 2 bước.
        </div>

        <ol style={{ textAlign: 'left', paddingLeft: '1.2rem', color: '#555', fontSize: '0.9rem', lineHeight: 1.8 }}>
          <li>Mở ứng dụng <b>Google Authenticator</b> trên điện thoại</li>
          <li>Nhấn dấu <b>+</b> → chọn <b>Quét mã QR</b></li>
          <li>Quét mã bên dưới</li>
          <li>Nhập mã 6 số từ app để xác nhận</li>
        </ol>

        <div style={{ textAlign: 'center', margin: '1.2rem 0' }}>
          <img
            src={qrDataUri}
            alt="QR Code Google Authenticator"
            style={{ width: 200, height: 200, border: '4px solid #1976d2', borderRadius: '12px', padding: '4px', background: '#fff' }}
          />
        </div>

        <details style={{ marginBottom: '1rem', fontSize: '0.82rem', color: '#888' }}>
          <summary style={{ cursor: 'pointer', color: '#1976d2' }}>Không quét được QR? Nhập thủ công</summary>
          <div style={{ marginTop: '8px', background: '#f5f5f5', padding: '8px', borderRadius: '6px', wordBreak: 'break-all', fontFamily: 'monospace' }}>
            {secret}
          </div>
        </details>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Mã xác thực (6 số từ Google Authenticator)</label>
            <input
              value={code}
              onChange={(e) => setCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
              placeholder="000000"
              inputMode="numeric"
              autoComplete="one-time-code"
              maxLength={6}
              style={{ fontSize: '1.6rem', letterSpacing: '0.6rem', textAlign: 'center' }}
              required
            />
          </div>
          <button type="submit" className="btn-primary" disabled={loading || code.length !== 6}>
            {loading ? 'Đang kích hoạt...' : 'Kích Hoạt & Đăng Nhập'}
          </button>
        </form>

        <div className="auth-links" style={{ marginTop: '1rem' }}>
          <Link to="/qr-login">← Quay lại</Link>
        </div>
      </div>
    </div>
  )
}
