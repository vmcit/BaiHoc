# Frontend - React App

Ứng dụng React kết nối với Spring Boot backend tại `http://localhost:8080`.

---

## ✅ Yêu cầu hệ thống

- **Node.js** phiên bản mới nhất (v20+ hoặc v22 LTS)
- Tải tại: https://nodejs.org/ → chọn **"LTS" (Recommended)**

---

## 🚀 Cài đặt & Chạy

### Bước 1 — Vào thư mục frontend
```powershell
cd D:\Book\ocp\baihoc\BaiHoc\frontend
```

### Bước 2 — Kéo node_modules về (chạy 1 lần duy nhất)
```powershell
npm install
```

### Bước 3 — Chạy dự án trên cổng 3000
```powershell
npm run dev
```

Sau đó mở trình duyệt: **http://localhost:3000**

---

## 📦 Nâng cấp Node.js lên bản mới nhất

### Cách 1: Tải thủ công
1. Vào https://nodejs.org/
2. Chọn bản **Current** (mới nhất)
3. Tải file `.msi`, chạy và cài đặt

### Cách 2: Dùng `nvm-windows` (khuyên dùng)
```powershell
# 1. Tải nvm-windows từ https://github.com/coreybutler/nvm-windows/releases
# 2. Sau khi cài:
nvm install latest        # cài bản mới nhất
nvm use latest            # chuyển sang dùng bản mới nhất
node -v                   # kiểm tra version
```

---

## 🗂 Cấu trúc thư mục

```
frontend/
├── index.html              # HTML gốc
├── package.json            # Dependencies & scripts
├── vite.config.js          # Cấu hình Vite (port 3000)
├── src/
│   ├── main.jsx            # Entry point
│   ├── App.jsx             # Router chính
│   ├── index.css           # Style toàn cục
│   ├── context/
│   │   └── AuthContext.jsx # Quản lý trạng thái đăng nhập
│   ├── pages/
│   │   ├── Login.jsx       # Trang đăng nhập
│   │   ├── Register.jsx    # Trang đăng ký
│   │   ├── ForgotPassword.jsx # Trang quên mật khẩu
│   │   └── Home.jsx        # Trang chủ (sau khi đăng nhập)
│   └── services/
│       └── api.js          # Axios calls đến backend
```

---

## 🔗 Danh sách trang

| Trang | URL | Mô tả |
|-------|-----|-------|
| Đăng nhập | `/login` | Nhập username + password |
| Đăng ký | `/register` | Tạo tài khoản mới |
| Quên mật khẩu | `/forgot-password` | Gửi email + nhập mã reset |
| Trang chủ | `/home` | Hiển thị sau khi đăng nhập *(yêu cầu auth)* |

---

## 🔧 Backend cần chạy trước

Khởi động Spring Boot trước khi dùng app:
```powershell
cd D:\Book\ocp\baihoc\BaiHoc
set JAVA_HOME=C:\Users\cuongmanh.vu\.jdks\ms-17.0.18
mvn spring-boot:run
```
Backend chạy tại: **http://localhost:8080**

---

## 🧪 Tài khoản test (xem REGISTER_PASSWORD_RESET_GUIDE.md)

```
ADMIN:  admin / admin123
USER:   user1 / user1234
```

---

## 📌 Lệnh thường dùng

| Lệnh | Mục đích |
|------|----------|
| `npm install` | Cài dependencies lần đầu |
| `npm run dev` | Chạy development server (port 3000) |
| `npm run build` | Build production |
| `npm run preview` | Preview bản build (port 3000) |
