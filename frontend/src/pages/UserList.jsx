import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { userApi, authApi } from '../services/api'

export default function UserList() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    userApi.getAll()
      .then((res) => setUsers(res.data.data || []))
      .catch((err) => {
        if (err.response?.status === 403) {
          setError('Bạn không có quyền xem danh sách user (yêu cầu ADMIN).')
        } else {
          setError('Không thể tải danh sách user.')
        }
      })
      .finally(() => setLoading(false))
  }, [])

  const handleLogout = async () => {
    try {
      if (user?.refreshToken) {
        await authApi.logout({ refreshToken: user.refreshToken })
      }
    } catch (_) {}
    logout()
    navigate('/login')
  }

  return (
    <div className="userlist-container">
      <div className="userlist-card">
        {/* Header */}
        <div className="userlist-header">
          <div>
            <h1>👥 Danh sách User</h1>
            <p className="userlist-sub">Đăng nhập với tài khoản: <strong>{user?.username}</strong></p>
          </div>
          <div className="userlist-actions">
            <button className="btn-back" onClick={() => navigate('/home')}>
              ← Quay lại
            </button>
            <button className="btn-logout" onClick={handleLogout}>
              🚪 Đăng Xuất
            </button>
          </div>
        </div>

        {/* Content */}
        {loading && <div className="userlist-loading">Đang tải...</div>}

        {error && <div className="alert alert-error">{error}</div>}

        {!loading && !error && (
          <>
            <p className="userlist-count">Tổng cộng: <strong>{users.length}</strong> user</p>
            <div className="userlist-table-wrap">
              <table className="userlist-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Họ tên</th>
                    <th>Email</th>
                    <th>Vai trò</th>
                    <th>Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((u) => (
                    <tr key={u.id}>
                      <td className="td-id">#{u.id}</td>
                      <td className="td-bold">{u.username}</td>
                      <td>{u.fullName || '—'}</td>
                      <td className="td-email">{u.email}</td>
                      <td>
                        {(u.roles || []).map((r) => (
                          <span key={r} className={`badge ${r === 'ADMIN' ? 'badge-admin' : ''}`}>
                            {r}
                          </span>
                        ))}
                      </td>
                      <td>
                        <span className={`status-badge status-${u.status?.toLowerCase()}`}>
                          {u.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}
      </div>
    </div>
  )
}
