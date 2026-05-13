import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi } from '../services/api'

export default function Home() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = async () => {
    try {
      if (user?.refreshToken) {
        await authApi.logout({ refreshToken: user.refreshToken })
      }
    } catch (_) {
      // Vẫn logout local dù API lỗi
    }
    logout()
    navigate('/login')
  }

  return (
    <div className="home-container">
      <div className="home-card">
        <div className="home-avatar">
          {(user?.fullName || user?.username || '?')[0].toUpperCase()}
        </div>
        <h1>👋 Hello, {user?.fullName || user?.username || 'bạn'}!</h1>
        <p className="home-sub">Chào mừng bạn đã đăng nhập thành công.</p>

        <div className="home-info">
          {user?.email && (
            <div className="info-row">
              <span className="info-label">Email</span>
              <span className="info-value">{user.email}</span>
            </div>
          )}
          {user?.username && (
            <div className="info-row">
              <span className="info-label">Username</span>
              <span className="info-value">{user.username}</span>
            </div>
          )}
          {user?.roles && user.roles.length > 0 && (
            <div className="info-row">
              <span className="info-label">Vai trò</span>
              <span className="info-value">
                {user.roles.map((r) => (
                  <span key={r} className="badge">{r}</span>
                ))}
              </span>
            </div>
          )}
        </div>

        <button className="btn-logout" onClick={handleLogout}>
          🚪 Đăng Xuất
        </button>
      </div>
    </div>
  )
}
