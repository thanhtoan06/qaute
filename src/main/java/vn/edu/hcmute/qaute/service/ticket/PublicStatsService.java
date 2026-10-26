package vn.edu.hcmute.qaute.service.ticket;

// Xử lý nghiệp vụ PublicStats trong hệ thống.
public interface PublicStatsService {

    // Tính thời gian phản hồi đầu tiên trung vị trong số ngày chỉ định.
    Double getMedianFirstResponseMinutes(int days);

    // Tính thời gian phản hồi đầu tiên trung vị trong 30 ngày gần nhất.
    default Double getMedianFirstResponseMinutes() {
        return getMedianFirstResponseMinutes(30);
    }



    // Trả về thời gian phản hồi đầu tiên trung vị trong 30 ngày gần nhất.
    default Double medianFirstResponseMinutes() {
        return getMedianFirstResponseMinutes(30);
    }
}
