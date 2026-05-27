import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import Login from './pages/Login'
import Register from './pages/Register'
import ForgotPassword from './pages/ForgotPassword'
import Home from './pages/Home'
import UserList from './pages/UserList'
import OAuth2Callback from './pages/OAuth2Callback'
import PhoneLogin from './pages/PhoneLogin'
import OtpVerify from './pages/OtpVerify'
import QrLogin from './pages/QrLogin'
import TotpSetup from './pages/TotpSetup'
import TotpVerify from './pages/TotpVerify'

function PrivateRoute({ children }) {
  const { user } = useAuth()
  return user ? children : <Navigate to="/login" replace />
}

function PublicRoute({ children }) {
  const { user } = useAuth()
  return user ? <Navigate to="/home" replace /> : children
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
          <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />
          <Route path="/forgot-password" element={<PublicRoute><ForgotPassword /></PublicRoute>} />
          <Route path="/phone-login" element={<PublicRoute><PhoneLogin /></PublicRoute>} />
          <Route path="/otp-verify" element={<PublicRoute><OtpVerify /></PublicRoute>} />
          <Route path="/qr-login" element={<PublicRoute><QrLogin /></PublicRoute>} />
          <Route path="/totp-setup" element={<PublicRoute><TotpSetup /></PublicRoute>} />
          <Route path="/totp-verify" element={<PublicRoute><TotpVerify /></PublicRoute>} />
          <Route path="/home" element={<PrivateRoute><Home /></PrivateRoute>} />
          <Route path="/users" element={<PrivateRoute><UserList /></PrivateRoute>} />
          <Route path="/oauth2/callback" element={<OAuth2Callback />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
