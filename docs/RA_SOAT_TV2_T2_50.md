# T2-50 — Rà soát Manager/Admin

## Các chỗ đã sửa

| Nhóm | File chính | Thay đổi |
|---|---|---|
| Phạm vi báo cáo | `TicketReportService` | Tự lấy Manager đang đăng nhập, giao phạm vi được phép với bộ lọc; không chấp nhận ID Manager khác hoặc yêu cầu báo cáo toàn hệ thống từ Manager/Student. Giới hạn khoảng ngày ở tầng service. |
| Quyền chat | `ChatDeskService`, `ChatAccessCheckerImpl`, `ChatMessagingService`, `ChatServiceImpl` | Kiểm tra phạm vi trước trạng thái khi nhận phiên; trả `NotFoundException` khi chuyển phiên của người khác; từ chối người dùng null; khóa tư vấn viên đích khi kiểm tra sức chứa để tránh hai yêu cầu chuyển cùng vượt giới hạn. |
| Ticket liên quan lịch hẹn | `ManagerAppointmentService` | Chỉ trả `ticketId`/`ticketCode` nếu Manager có quyền xem ticket, kể cả khi lịch hẹn thuộc chính Manager. |
| Audit còn thiếu | `AdminUserService`, `AdminHealthController`, `AuditActions`, `AdminAuditService` | Ghi `USER_INVITATION_RESENT` và `SYSTEM_TEST_EMAIL_QUEUED`, thêm nhãn tiếng Việt. Audit chỉ ghi việc xếp hàng, không ghi token/mật khẩu. |
| Tắt phòng ban | `AdminDepartmentService` | Dùng cùng rào kiểm tra ticket đang mở cho cả nút bật/tắt và đường sửa form, tránh tắt phòng ban qua form chỉnh sửa. |
| Form và lý do | Các controller FAQ, ticket Admin, tag, synonym, ghi chú, hồ sơ Manager, SLA; các DTO tương ứng | Bổ sung `@Valid`, bắt lỗi binding trước khi gọi service; lý do can thiệp ticket được kiểm tra thêm tại service, dài 10–500 ký tự. Form gửi duyệt FAQ dùng DTO riêng để không nhận các trường chỉnh sửa bài viết. |
| Giới hạn trang | `BoundedPages` và các service danh sách | Chặn yêu cầu unpaged hoặc size quá lớn ở các điểm nhận Pageable; tối đa 100 dòng/trang. Các trang thường dùng 15, 20 hoặc 25 dòng. |
| Các bảng chưa phân trang | Tag, synonym, phòng ban, Manager, mẫu trả lời, phiên bản FAQ, Student 360 | Thêm truy vấn trang và nút chuyển trang. Ba lịch sử Student 360 giữ nguyên điều kiện phạm vi/người phụ trách trong cả truy vấn dữ liệu lẫn đếm tổng. Tìm Manager dùng LIKE đã escape. |
| Hàng chờ chat | `ChatDeskService`, `ChatApiController`, `chat-desk.js`, dashboard Manager | Truy vấn theo scope ngay trong DB, 25 phiên/trang; có Trước/Sau; vị trí chờ được tính trên toàn hàng chờ chuyên mục. Dashboard dùng truy vấn đếm riêng để không chỉ đếm trang đầu. Tin chat Manager đồng bộ bằng cursor, tối đa 100 tin/lượt. |
| HTML và liên kết | `AdminFaqService`, `AnnouncementQueryServiceImpl`, `ManagerScheduleService` | Sanitize khi dựng bản xem trước FAQ/thông báo, bổ sung kiểm tra HTTPS với mọi liên kết họp được nhập. Danh sách thông báo công khai cũng chặn limit trên 100. |
| Giao diện | Hai decorator TV2, `tv2-layout.js`, các trang workspace/review, chat desk | Bổ sung phân trang 25 mục cho bảng cấu hình/kết quả import và lịch sử đã tải sẵn; bảng có vùng cuộn ngang, modal cuộn được. Chat desk khai báo rõ một cột dưới 992px và cho thanh hành động xuống dòng. |
| Điều hướng chat | `ChatServiceImpl` | Sửa liên kết thông báo tới `/manager/chat`. |
| Hướng dẫn | `README.md` | Bổ sung “Kiểm tra nhanh Manager/Admin” gồm 10 bước. |

## Đối chiếu checklist

- **(a), (d):** các trang ticket dùng `TicketAccessService`; lịch hẹn dùng `requireOwn`; chat dùng kiểm tra thành viên/chủ phiên; Student 360 kiểm tra quan hệ trước khi trả dữ liệu. Các hồi quy mới bao phủ chat ngoài scope, chuyển phiên người khác, báo cáo vượt scope và ticket ngoài quyền xem được gắn vào lịch hẹn.
- **(b):** các thay đổi dữ liệu qua controller Admin gọi service ghi audit tương ứng; bổ sung hai thao tác còn thiếu ở trên. Xuất audit/báo cáo vẫn ghi `REPORT_EXPORT`.
- **(c), (k):** kiểm tra 42 JSP Admin/Manager: có title, POST có CSRF; form dùng `qa:confirmModal` nhận CSRF từ tag. Dữ liệu văn bản dùng `c:out`/escape; vùng HTML được sanitize trước hiển thị. Các request API thay đổi dữ liệu dùng CSRF của `Qaute.fetchJson`.
- **(e):** form có DTO dùng `@Valid`; quy tắc quyền, trạng thái, ID và độ dài lý do tiếp tục được kiểm tra tại service. Cài đặt hệ thống dạng map và file CSV dùng bộ kiểm tra chuyên biệt theo kiểu/biên hoặc từng dòng tại service.
- **(f):** giữ rào chống tự khóa/vô hiệu hóa, hạ vai trò chính mình, khóa/vô hiệu hóa/hạ vai trò Admin ACTIVE cuối cùng; bộ test `AdminUserServiceTest` kiểm tra các rào này.
- **(g):** danh sách tra cứu lớn phân trang tại DB; các bảng cấu hình và lịch sử workspace đã tải sẵn phân trang tại trình duyệt, 25 mục/trang. Lịch làm việc là lưới cố định bảy ngày. Dropdown cấu hình giữ đủ lựa chọn; CSV xuất audit giữ giới hạn riêng 5.000 dòng. Hợp đồng JSON sinh viên của TV1 được giữ nguyên, gồm giới hạn 200 tin chat/lượt đã quy định ở T2-38.
- **(h):** các CSV xuất dùng `CsvUtil.row`, hàm này gọi `safeCell` cho từng ô và escape dấu ngoặc kép; phản hồi có BOM UTF-8.
- **(i):** kiểm tra cấu trúc Bootstrap, thêm lớp một cột cho chat desk và hỗ trợ cuộn modal/bảng trong decorator TV2.
- **(j):** không tìm thấy `System.out`/`printStackTrace` trong controller/service thuộc phạm vi; các thay đổi mới không ghi log hoặc audit mật khẩu, OTP, token.

## Kiểm chứng

- `mvnw.cmd -q test`: **166 kiểm thử, 0 lỗi, 0 thất bại, 0 bỏ qua**.
- Jasper JspC: biên dịch 42 JSP Admin/Manager và hai decorator; lỗi EL phát hiện trong quá trình sửa đã được xử lý, lần biên dịch lại trang sửa báo 0 lỗi. Jasper dùng mức Java 19 cho servlet JSP sinh ra; ứng dụng và kiểm thử dùng JDK 25 của dự án.
- `node --check`: đạt với `manager/chat-desk.js` và `tv2-layout.js`.
- Kiểm tra diff để giữ thay đổi trong phần TV2 và phần README bổ sung.

Chưa thực hiện chuỗi kiểm tra bằng trình duyệt với hai tài khoản, WebSocket thực tế, thiết bị di động hay gửi email thật trong đợt này. Dùng 10 bước trong README để kiểm tra tích hợp trên môi trường chạy ứng dụng; kết quả unit test/JSP compilation không thay thế bước đó.
