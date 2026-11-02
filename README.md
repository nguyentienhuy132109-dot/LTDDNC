# RentConnect - Hệ Thống Hỗ Trợ Người Thuê và Cho Thuê Nhà (Android Native - Java)

Dự án Android Studio được xây dựng dựa trên đề tài **BÀI TẬP LỚN RENTCONNECT** và tổng hợp kiến thức từ giáo trình **LẬP TRÌNH DI ĐỘNG** (Android SDK, Java, SQLite, View & ViewGroup, RecyclerView Adapter, Intents, SharedPreferences, Camera API, Biometrics, Hardware Sensors, VietQR).

---

## 📌 1. Cấu Trúc Dự Án (Android Studio Standard Structure)

```text
RentConnect/
├── .gitignore                      # Cấu hình bỏ qua file rác Gradle/Android Studio
├── build.gradle                    # Gradle Root
├── settings.gradle                 # Cấu hình Modules
├── README.md                       # Tài liệu hướng dẫn & Push GitHub
└── app/
    ├── build.gradle                # Cấu hình SDK (compileSdk 34, minSdk 24) & Dependencies
    └── src/main/
        ├── AndroidManifest.xml     # Cấu hình Permissions & Activities
        ├── java/com/example/rentconnect/
        │   ├── activities/
        │   │   ├── LoginActivity.java           # FR01: Đăng nhập & Xác thực Vân tay / FaceID
        │   │   ├── RegisterActivity.java        # FR01: Đăng ký & Phân quyền (Tenant / Landlord)
        │   │   ├── MainActivity.java            # FR03: Dashboard & Danh sách phòng trọ (Tích hợp Cảm biến)
        │   │   ├── UtilityRecordActivity.java   # FR07: Ghi điện nước Offline & Chụp ảnh công tơ
        │   │   ├── InvoiceActivity.java         # FR08 & FR09: Lập hóa đơn & Sinh mã VietQR
        │   │   └── TicketActivity.java          # FR10 & FR11: Cổng Báo cáo & Xử lý sự cố
        │   ├── adapters/
        │   │   ├── RoomAdapter.java             # RecyclerView Adapter danh sách phòng
        │   │   ├── InvoiceAdapter.java          # RecyclerView Adapter hóa đơn & QR payload
        │   │   └── TicketAdapter.java           # RecyclerView Adapter danh sách Ticket sự cố
        │   ├── database/
        │   │   └── RentConnectDbHelper.java     # SQLite Storage (Bảng users, rooms, utility, invoices, tickets)
        │   ├── models/
        │   │   ├── User.java
        │   │   ├── Room.java
        │   │   ├── UtilityRecord.java
        │   │   ├── Invoice.java
        │   │   └── Ticket.java
        │   └── utils/
        │       ├── SharedPrefManager.java       # Quản lý Session đăng nhập (SharedPreferences)
        │       ├── SensorHelper.java            # [NEW] Quản lý Cảm biến Ánh sáng & Gia tốc
        │       └── VietQRHelper.java            # Sinh payload mã thanh toán VietQR tự động
        └── res/
            ├── layout/                          # Giao diện XML (activity_*.xml, item_*.xml)
            └── values/                          # colors.xml, strings.xml, themes.xml, arrays.xml
```

---

## 🛠️ 2. Công Nghệ Áp Dụng (Theo Giáo Trình Lập Trình Di Động)

1. **Giao diện & UI Controls:**
   - Sử dụng **LinearLayout, ConstraintLayout, CardView, RecyclerView** với mô hình `ViewHolder` & `LayoutInflater`.
   - Các widget tương tác: **EditText, RadioGroup/RadioButton, Spinner (ArrayAdapter), Button, ImageView**.
   - Phản hồi người dùng bằng **Toast Message**, Dialog thông báo.
2. **Lưu trữ dữ liệu ngoại tuyến (Offline-to-Online Local Storage):**
   - **`SharedPreferences`** (`MODE_PRIVATE`): Lưu thông tin phiên đăng nhập (`user_id`, `user_name`, `user_role`).
   - **`SQLiteOpenHelper` (`RentConnectDbHelper`)**: Quản trị CSDL cục bộ trên máy di động. Hỗ trợ chốt số điện nước **ngoại tuyến (Offline)** tại nơi mất sóng WiFi (tầng hầm, phòng kín) và lưu trữ hợp đồng, hóa đơn, sự cố.
3. **Phân quyền người dùng (Role-Based Access Control):**
   - Phân biệt vai trò giữa **Chủ trọ (Landlord)** và **Người thuê (Tenant)** để tùy biến giao diện và quyền thao tác.
4. **Tích hợp Cảm biến & Phần cứng (Hardware Sensors & Camera & Biometrics):**
   - **Cảm biến Ánh sáng (`Sensor.TYPE_LIGHT`):** Nhận biết độ sáng môi trường xung quanh (Lux) khi người dùng đi xem phòng trực tiếp (ngoài trời nắng gắt hoặc phòng tối thiếu sáng) để tự động gợi ý/chuyển đổi chế độ **Light/Dark Theme**, giúp dễ đọc thông tin hợp đồng và phòng trọ.
   - **Cảm biến Gia tốc (`Sensor.TYPE_ACCELEROMETER`):** Thiết lập cử chỉ **"Lắc thiết bị (Shake Gesture)"** để tự động làm mới (Refresh) danh sách phòng trọ mới nhất và kích hoạt nhanh cửa sổ **Báo cáo sự cố khẩn cấp (Quick Ticket)**.
   - **Cảm biến Máy ảnh (Camera Intent):** Chụp ảnh công tơ điện/nước (FR07) và chụp ảnh hiện trường hư hỏng (FR10) qua Intent Camera.
   - **Cảm biến Sinh trắc học (Biometric API):** Xác thực vân tay / khuôn mặt nâng cao bảo mật khi đăng nhập.
5. **Thanh toán VietQR:**
   - Tự động hợp nhất tiền phòng, tiền điện, tiền nước và phí dịch vụ để sinh chuỗi mã VietQR thanh toán 1-touch.

---

## 🚀 3. Hướng Dẫn Push Dự Án Lên GitHub

Bạn có thể mở Terminal tại thư mục gốc của dự án `RentConnect` và thực hiện các lệnh Git sau:

```bash
# 1. Khởi tạo kho lưu trữ Git cục bộ
git init

# 2. Thêm toàn bộ file dự án vào Staging Area (file rác đã được .gitignore tự động bỏ qua)
git add .

# 3. Tạo bản Commit đầu tiên
git commit -m "Update RentConnect v2: Added Light Sensor (TYPE_LIGHT) & Accelerometer (TYPE_ACCELEROMETER)"

# 4. Đổi tên nhánh mặc định thành main
git branch -M main

# 5. Liên kết tới Repo của bạn trên GitHub
git remote add origin git@github.com:YOUR_USERNAME/RentConnect-Android.git

# 6. Push code lên GitHub
git push -u origin main
```

---

## 📱 4. Hướng Dẫn Mở Dự Án Trong Android Studio

1. Mở **Android Studio** (phiên bản Hedgehog | Jellyfish trở lên).
2. Chọn **Open** -> Duyệt tới thư mục `RentConnect`.
3. Chờ Android Studio đồng bộ **Gradle** (Gradle Sync).
4. Chọn máy ảo Android (Emulator API 24+) hoặc kết nối điện thoại thật qua USB Debugging.
5. Nhấn nút **Run (Shift + F10)** để trải nghiệm ứng dụng!
