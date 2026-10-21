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

## Kiểm tra nhanh Manager/Admin

Thực hiện trên dữ liệu demo, dùng các tài khoản ở bảng trên. Ghi lại mã ticket/FAQ để đối chiếu kết quả và audit.

1. **Đăng nhập Manager:** dùng `manager1@qaute.edu.vn`, mở dashboard và hàng đợi. Thử mở URL ticket, lịch hẹn hoặc API chat của Manager khác ngoài phạm vi: phải nhận 404.
2. **Nhận ticket:** chọn yêu cầu NEW trong phạm vi rồi bấm “Nhận”. Yêu cầu xuất hiện ở “Của tôi”; dùng cửa sổ khác thử nhận lại cùng ticket và kiểm tra thông báo xung đột.
3. **Trả lời:** mở workspace, nhập nội dung, đính kèm tệp của mình và gửi. Kiểm tra nội dung, tệp tải về và nháp được xóa; ghi chú nội bộ chỉ xuất hiện ở tab riêng.
4. **Giải quyết:** nhập tóm tắt giải pháp hợp lệ rồi đánh dấu đã giải quyết. Kiểm tra trạng thái và timeline; thử thao tác với lý do quá ngắn hoặc sửa ID sang ticket ngoài quyền xem.
5. **Chat desk:** đặt Manager ONLINE, mở thêm cửa sổ sinh viên để yêu cầu tư vấn. Nhận phiên, gửi tin/tệp, kiểm tra đang gõ/đã xem, chuyển hoặc kết thúc; thử tải lại để đồng bộ. Thu nhỏ dưới 992px: ba khối xếp thành một cột.
6. **Lịch làm việc:** thêm quy tắc, kiểm tra slot trong tuần; thêm/bỏ khung chặn. Dùng tài khoản sinh viên đặt lịch rồi xác nhận Manager khác không xem được lịch đó.
7. **Đăng nhập Admin:** kiểm tra danh sách người dùng và các trang cấu hình. Thử tự khóa/vô hiệu hóa chính mình và hạ vai trò Admin cuối cùng đang ACTIVE: hệ thống phải từ chối. Gửi POST thiếu CSRF qua công cụ phát triển: phải nhận 403.
8. **Duyệt FAQ:** tạo bản nháp, gửi duyệt và duyệt/từ chối với lý do hợp lệ. Kiểm tra lịch sử phiên bản và nội dung xem trước; nội dung HTML nguy hiểm phải bị loại bỏ.
9. **Đổi SLA:** lưu chính sách bốn mức ưu tiên, giờ làm việc và ngày nghỉ; thử giờ chồng lấn hoặc thời gian giải quyết nhỏ hơn phản hồi đầu tiên. Chỉ dữ liệu hợp lệ được lưu; ticket mới dùng chính sách mới.
10. **Xem audit:** mở `/admin/audit-logs`, lọc các thao tác vừa thực hiện, xem trước/sau và xuất CSV. Kiểm tra dấu tiếng Việt, ô bắt đầu bằng `= + - @` được vô hiệu hóa công thức và có bản ghi `REPORT_EXPORT` sau xuất.

Chi tiết rà soát T2-50 và phạm vi kiểm chứng: [Rà soát Manager/Admin](docs/RA_SOAT_TV2_T2_50.md).
