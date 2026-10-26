package vn.edu.hcmute.qaute.service.system;

import java.util.List;
import vn.edu.hcmute.qaute.dto.view.SettingView;

// Xử lý nghiệp vụ Settings trong hệ thống.
public interface SettingsService {

    // Lấy giá trị thiết lập dạng chuỗi theo khóa.
    String getString(String key);

    // Lấy giá trị thiết lập dạng số nguyên theo khóa.
    int getInt(String key);

    // Lấy giá trị thiết lập dạng số nguyên dài theo khóa.
    long getLong(String key);

    // Lấy giá trị thiết lập dạng boolean theo khóa.
    boolean getBoolean(String key);

    // Liệt kê tất cả thiết lập theo thứ tự nhóm và khóa.
    List<SettingView> listAll();

    // Cập nhật giá trị thiết lập và ghi nhận người thực hiện.
    void update(String key, String value, Long actorUserId);
}
