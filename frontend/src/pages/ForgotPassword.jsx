import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { authApi } from '../services/api'

export default function ForgotPassword() {
  const [step, setStep] = useState(1) // 1: nhập email, 2: nhập mã + mật khẩu mới
  const [email, setEmail] = useState('')
  const [form, setForm] = useState({ resetToken: '', newPassword: '', confirmPassword: '' })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleSendEmail = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await authApi.forgotPassword({ email })
      setSuccess('Email đặt lại mật khẩu đã được gửi! Kiểm tra hộp thư của bạn.')
      setStep(2)
    } catch (err) {
      setError(err.response?.data?.message || 'Không tìm thấy email hoặc gửi mail thất bại.')
    } finally {
      setLoading(false)
    }
  }

  const handleReset = async (e) => {
    e.preventDefault()
    setError('')
    if (form.newPassword !== form.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp!')
      return
    }
    setLoading(true)
    try {
      await authApi.resetPassword(form)
      setSuccess('Đặt lại mật khẩu thành công! Đang chuyển đến đăng nhập...')
      setTimeout(() => navigate('/login'), 2000)
    } catch (err) {
      setError(err.response?.data?.message || 'Mã reset không hợp lệ hoặc đã hết hạn.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">🔑</div>
        <h2>Quên Mật Khẩu</h2>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        {/* Bước 1: Nhập email */}
        {step === 1 && (
          <form onSubmit={handleSendEmail}>
            <p className="hint">
              Nhập địa chỉ email đã đăng ký. Chúng tôi sẽ gửi mã đặt lại mật khẩu.
            </p>
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="email@example.com"
                required
              />
            </div>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Đang gửi...' : 'Gửi Mã Đặt Lại'}
            </button>
            <div style={{ textAlign: 'center', marginTop: '12px' }}>
              <button type="button" className="btn-link" onClick={() => setStep(2)}>
                Tôi đã có mã reset →
              </button>
            </div>
          </form>
        )}

        {/* Bước 2: Nhập mã + mật khẩu mới */}
        {step === 2 && (
          <form onSubmit={handleReset}>
            <p className="hint">
              Nhập mã reset từ email và mật khẩu mới của bạn. Mã có hiệu lực trong 15 phút.
            </p>
            <div className="form-group">
              <label>Mã Reset (từ email)</label>
              <input
                value={form.resetToken}
                onChange={(e) => setForm({ ...form, resetToken: e.target.value })}
                placeholder="Dán mã từ email"
                required
              />
            </div>
            <div className="form-group">
              <label>Mật khẩu mới</label>
              <input
                type="password"
                value={form.newPassword}
                onChange={(e) => setForm({ ...form, newPassword: e.target.value })}
                placeholder="Tối thiểu 6 ký tự"
                required
              />
            </div>
            <div className="form-group">
              <label>Xác nhận mật khẩu mới</label>
              <input
                type="password"
                value={form.confirmPassword}
                onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })}
                placeholder="Nhập lại mật khẩu"
                required
              />
            </div>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Đang xử lý...' : 'Đặt Lại Mật Khẩu'}
            </button>
            <div style={{ textAlign: 'center', marginTop: '12px' }}>
              <button type="button" className="btn-link" onClick={() => { setStep(1); setError(''); setSuccess('') }}>
                ← Quay lại nhập email
              </button>
            </div>
          </form>
        )}

        <div className="auth-links">
          <Link to="/login">← Quay lại đăng nhập</Link>
        </div>
      </div>
    </div>
  )
}
