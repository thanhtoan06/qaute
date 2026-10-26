package vn.edu.hcmute.qaute.config.seed;

// Khai báo hợp đồng chung cho các bộ khởi tạo dữ liệu mẫu.
public interface DemoSeeder {

    // Trả về thứ tự chạy của seeder.
    int order();

    // Thực hiện nạp dữ liệu demo.
    void seed();
}
