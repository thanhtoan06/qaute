package vn.edu.hcmute.qaute.common.response;

// Đóng gói dữ liệu phản hồi API dùng chung qua FieldErrorItem.
public record FieldErrorItem(String field, String message) {
}
