import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../services/api'

export default function Register() {
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    fullName: '',
    phone: '',
  })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    if (form.password !== form.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp!')
      return
    }
    setLoading(true)
    try {
      await authApi.register(form)
      setSuccess('Đăng ký thành công! Đang chuyển đến trang đăng nhập...')
      setTimeout(() => navigate('/login'), 1800)
    } catch (err) {
      setError(err.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-logo">📝</div>
        <h2>Đăng Ký</h2>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>Tên đăng nhập *</label>
              <input
                name="username"
                value={form.username}
                onChange={handleChange}
                placeholder="username"
                required
              />
            </div>
            <div className="form-group">
              <label>Email *</label>
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="email@example.com"
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Họ và tên</label>
              <input
                name="fullName"
                value={form.fullName}
                onChange={handleChange}
                placeholder="Nguyễn Văn A"
              />
            </div>
            <div className="form-group">
              <label>Số điện thoại</label>
              <input
                name="phone"
                value={form.phone}
                onChange={handleChange}
                placeholder="0901234567"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Mật khẩu *</label>
              <input
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Tối thiểu 6 ký tự"
                required
              />
            </div>
            <div className="form-group">
              <label>Xác nhận mật khẩu *</label>
              <input
                type="password"
                name="confirmPassword"
                value={form.confirmPassword}
                onChange={handleChange}
                placeholder="Nhập lại mật khẩu"
                required
              />
            </div>
          </div>

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Đang đăng ký...' : 'Đăng Ký'}
          </button>
        </form>

        <div className="auth-links">
          <Link to="/login">← Đã có tài khoản? Đăng nhập</Link>
        </div>
      </div>
    </div>
  )
}
