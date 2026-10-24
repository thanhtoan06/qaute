# QAUTE

QAUTE là website tư vấn sinh viên HCMUTE, hỗ trợ tra cứu câu hỏi thường gặp, gửi ticket tư vấn, trò chuyện và đặt lịch hẹn.

## Chạy ứng dụng trên Windows

Cần Java 25, Maven và MySQL đang hoạt động. Trong PowerShell, mở đúng thư mục dự án rồi chạy:

```powershell
cd C:\projects\qaute
mvn spring-boot:run "-Dspring-boot.run.profiles=dev,demo"
```

Chờ log `Tomcat started on port 8090`, sau đó mở `http://localhost:8090/auth/login`.
Giữ terminal này mở trong khi dùng ứng dụng. Để dừng, nhấn **Ctrl+C** trong chính terminal đã chạy.
Chỉ chạy một phiên QAUTE trên cổng 8090; không chạy thêm từ VS Code khi một phiên Maven khác vẫn hoạt động.

### Khi báo `Port 8090 was already in use`

Đây là xung đột cổng giữa hai tiến trình. Kiểm tra tiến trình đang lắng nghe:

```powershell
Get-NetTCPConnection -LocalPort 8090 -State Listen |
    Select-Object LocalPort, OwningProcess
```

Nếu đó là phiên QAUTE đang chạy bình thường, dùng ngay địa chỉ trên hoặc dừng phiên cũ bằng **Ctrl+C** trước khi khởi động lại.
Nếu không còn terminal cũ, kiểm tra PID bằng `Get-Process -Id <PID>` và xác nhận đúng phiên QAUTE trước khi dùng `Stop-Process -Id <PID>`.
Không dừng toàn bộ tiến trình Java vì VS Code và các ứng dụng khác cũng có thể đang dùng Java.

Nếu cần chạy song song ở cổng khác:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev,demo" "-Dspring-boot.run.arguments=--server.port=8091 --app.base-url=http://localhost:8091"
```

Khi đó truy cập `http://localhost:8091/auth/login`. Dừng các phiên chạy thử sau khi kiểm tra để giải phóng cổng.

### Kiểm thử và đóng gói

Trong một terminal khác, tại thư mục dự án:

```powershell
mvn test
mvn -DskipTests package
```

Kiểm thử cần MySQL vì có test tích hợp. Kết quả đạt khi Maven báo `BUILD SUCCESS`, không có failure/error.
File đóng gói nằm tại `target/qaute-0.0.1-SNAPSHOT.war`.

### Kiểm tra đổi mật khẩu và hồ sơ Admin

1. Đăng nhập Admin, mở **Người dùng**, chọn **Đổi mật khẩu** ở dòng người dùng hoặc trang chi tiết. Nhập mật khẩu mới và xác nhận (8–64 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt; tối đa 72 byte UTF-8).
2. Thử mật khẩu yếu hoặc xác nhận khác nhau: trang báo lỗi, không thay đổi mật khẩu. Sau khi đổi thành công, mật khẩu cũ, các phiên/token đăng nhập và liên kết khôi phục cũ của người dùng bị vô hiệu hóa. Trạng thái và vai trò tài khoản được giữ nguyên. Nếu Admin chọn chính mình ở danh sách này thì phải đăng nhập lại.
3. Vào **Hồ sơ admin** (`/admin/profile`) từ sidebar/menu tài khoản: sửa họ tên, email, điện thoại hoặc chọn ảnh đại diện. Email trùng với tài khoản khác bị từ chối; ảnh được cắt vuông 400 × 400 trước khi tải lên.
4. Trong **Đổi mật khẩu của tôi**, nhập mật khẩu hiện tại, mật khẩu mới và xác nhận. Nhập sai mật khẩu hiện tại phải báo lỗi; đổi đúng sẽ đăng xuất các thiết bị khác, giữ phiên hiện tại.
5. Đăng nhập Student hoặc Manager rồi mở `/admin/profile` hay `/admin/users/<id>/password`: phải bị từ chối. Kiểm tra `/admin/audit-logs`: có hành động đổi mật khẩu/cập nhật hồ sơ, không có mật khẩu hoặc hash trong dữ liệu trước/sau.

Kiểm thử riêng các chức năng này:

```powershell
mvn "-Dtest=AdminUserPasswordServiceTest,AdminAccountAuthorizationTest,AdminProfileServiceTest,AdminProfileControllerTest" test
```

Không cần migration hoặc cài thêm dependency cho hai chức năng này; dùng các cột User, BCrypt và cơ chế phiên sẵn có.

## Tài khoản demo

Bật dữ liệu demo bằng profile `demo`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev,demo
```

| Vai trò         | Email hoặc MSSV        | Mật khẩu    |
| ---------------- | ----------------------- | ------------- |
| Quản trị viên | admin@qaute.edu.vn      | Admin@12345   |
| Tư vấn viên   | manager1@qaute.edu.vn   | Manager@12345 |
| Tư vấn viên   | manager2@qaute.edu.vn   | Manager@12345 |
| Tư vấn viên   | manager3@qaute.edu.vn   | Manager@12345 |
| Tư vấn viên   | manager4@qaute.edu.vn   | Manager@12345 |
| Sinh viên       | 24162101 đến 24162130 | Student@12345 |
