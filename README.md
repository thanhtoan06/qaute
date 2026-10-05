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
