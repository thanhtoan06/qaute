# QAUTE

QAUTE là website tư vấn sinh viên HCMUTE, hỗ trợ tra cứu câu hỏi thường gặp, gửi ticket tư vấn, trò chuyện và đặt lịch hẹn.

## Tài khoản demo

Bật dữ liệu demo bằng profile `demo`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev,demo
```

| Vai trò | Email hoặc MSSV | Mật khẩu |
|---|---|---|
| Quản trị viên | admin@qaute.edu.vn | Admin@12345 |
| Tư vấn viên | manager1@qaute.edu.vn | Manager@12345 |
| Tư vấn viên | manager2@qaute.edu.vn | Manager@12345 |
| Tư vấn viên | manager3@qaute.edu.vn | Manager@12345 |
| Tư vấn viên | manager4@qaute.edu.vn | Manager@12345 |
| Sinh viên | 24162101 đến 24162115 | Student@12345 |

Có thể bật profile khi chạy file JAR bằng:

```bash
java -jar qaute.jar --spring.profiles.active=dev,demo
```

### Dữ liệu demo Manager/Admin (TV2)

Các seeder chạy sau dữ liệu tài khoản, tổ chức, FAQ và ticket. Lần nạp đầu bổ sung:

- **Lịch làm việc:** 40 quy tắc cho manager1–manager4, từ thứ Hai đến thứ Sáu, mỗi ngày 09:00–11:00 và 14:00–16:00; mỗi khung hẹn dài 30 phút, hỗ trợ trực tiếp và trực tuyến. Địa điểm theo phòng ban, liên kết họp mẫu là `https://meet.example.com/qaute-<n>` (thay `<n>` bằng 1–4).
- **10 lịch hẹn:** 4 lịch đã xác nhận trong tương lai, 2 lịch đã hoàn thành có kết luận, 2 lịch sinh viên không đến và 2 lịch đã hủy. Khung giờ tương lai được sinh qua `SlotGenerationService`; các lịch quá khứ có khung giờ lịch sử riêng.
- **10 phiên chat trong 30 ngày qua:** 8 phiên đã kết thúc, mỗi phiên có 4–10 tin nhắn sinh viên/tư vấn viên đúng chuyên mục phụ trách, 6 phiên có đánh giá; 2 phiên hết hạn. Thời gian chờ và thời lượng khác nhau để kiểm tra báo cáo.
- **4 thông báo tiếng Việt:** lịch đăng ký học phần được ghim cho mọi người, thông báo học phí dành cho sinh viên, quy trình xử lý yêu cầu dành cho Manager và một thông báo đã hết hạn.

Có thể kiểm tra tại `/manager/schedule`, `/manager/appointments`, `/manager/students`, `/admin/announcements` và các trang báo cáo. Chat mẫu là lịch sử đã kết thúc nên không xuất hiện trong hàng chờ đang trực.

Ba seeder TV2 chỉ chạy với profile `demo`, nhận diện từng bản ghi đã có và không tạo trùng khi khởi động lại. Các quy tắc hiện có được giữ nguyên; bộ sinh slot tiếp tục bổ sung khung giờ tương lai còn thiếu. Thông báo mẫu không gửi email/thông báo đẩy; các liên kết họp là địa chỉ minh họa.
