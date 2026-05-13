# BaiHoc — Spring Boot + React

```
BaiHoc/
├── backend/    ← Spring Boot (Java 17, MariaDB, JWT)
└── frontend/   ← React (Vite, port 3000)
```

---

## 🔧 Chạy Backend (Spring Boot)

```powershell
cd D:\Book\ocp\baihoc\BaiHoc\backend
set JAVA_HOME=C:\Users\cuongmanh.vu\.jdks\ms-17.0.18
mvn spring-boot:run
```
Backend chạy tại: **http://localhost:8080**

---

## 🌐 Chạy Frontend (React)

```powershell
# Lần đầu — cài node_modules
cd D:\Book\ocp\baihoc\BaiHoc\frontend
npm install

# Chạy dev server
npm run dev
```
Frontend chạy tại: **http://localhost:3000**

---

> Khởi động **backend trước**, sau đó mới chạy frontend.
