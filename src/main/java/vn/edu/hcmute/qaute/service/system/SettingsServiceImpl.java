package vn.edu.hcmute.qaute.service.system;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.SettingView;
import vn.edu.hcmute.qaute.entity.system.SystemSetting;
import vn.edu.hcmute.qaute.repository.system.SystemSettingRepository;

@Service
@Transactional(readOnly = true)
public class SettingsServiceImpl implements SettingsService {

    private static final String TYPE_INT = "INT";
    private static final String TYPE_BOOL = "BOOL";
    private static final String TYPE_STRING = "STRING";

    private final SystemSettingRepository settingRepository;
    private final ConcurrentMap<String, SettingView> cache = new ConcurrentHashMap<>();

    public SettingsServiceImpl(SystemSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public String getString(String key) {
        SettingView setting = getSetting(key);
        if (!TYPE_STRING.equals(setting.valueType())) {
            throw new IllegalStateException("Thiết lập không có kiểu STRING: " + key);
        }
        return setting.value();
    }

    @Override
    public int getInt(String key) {
        long value = getLong(key);
        try {
            return Math.toIntExact(value);
        } catch (ArithmeticException exception) {
            throw new IllegalStateException("Giá trị thiết lập vượt giới hạn số nguyên: " + key, exception);
        }
    }

    @Override
    public long getLong(String key) {
        SettingView setting = getSetting(key);
        if (!TYPE_INT.equals(setting.valueType())) {
            throw new IllegalStateException("Thiết lập không có kiểu INT: " + key);
        }
        try {
            return Long.parseLong(setting.value());
        } catch (NumberFormatException exception) {
            throw new IllegalStateException("Giá trị thiết lập không phải số nguyên: " + key, exception);
        }
    }

    @Override
    public boolean getBoolean(String key) {
        SettingView setting = getSetting(key);
        if (!TYPE_BOOL.equals(setting.valueType())
                || (!"true".equals(setting.value()) && !"false".equals(setting.value()))) {
            throw new IllegalStateException("Giá trị thiết lập không phải BOOL hợp lệ: " + key);
        }
        return Boolean.parseBoolean(setting.value());
    }

    @Override
    public List<SettingView> listAll() {
        return settingRepository.findAllByOrderByGroupNameAscSettingKeyAsc().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    @Transactional
    public void update(String key, String value, Long actorUserId) {
        SystemSetting setting = settingRepository.findBySettingKey(key)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy thiết lập: " + key));
        validateValue(setting, value);
        setting.setSettingValue(value);
        setting.setUpdatedBy(actorUserId);
        settingRepository.save(setting);
        cache.remove(key);
    }

    private SettingView getSetting(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("Khóa thiết lập không hợp lệ");
        }
        return cache.computeIfAbsent(key, this::loadSetting);
    }

    private SettingView loadSetting(String key) {
        return settingRepository.findBySettingKey(key)
                .map(this::toView)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy thiết lập: " + key));
    }

    private SettingView toView(SystemSetting setting) {
        return new SettingView(
                setting.getSettingKey(),
                setting.getSettingValue(),
                setting.getValueType(),
                setting.getMinValue(),
                setting.getMaxValue(),
                setting.getGroupName(),
                setting.getDescription());
    }

    private void validateValue(SystemSetting setting, String value) {
        if (value == null) {
            throw invalidValue("Giá trị thiết lập không được để trống");
        }
        switch (setting.getValueType()) {
            case TYPE_INT -> validateInteger(setting, value);
            case TYPE_BOOL -> {
                if (!"true".equals(value) && !"false".equals(value)) {
                    throw invalidValue("Giá trị BOOL phải là true hoặc false");
                }
            }
            case TYPE_STRING -> {
                if (value.isEmpty()) {
                    throw invalidValue("Giá trị STRING không được để trống");
                }
                if (value.length() > 500) {
                    throw invalidValue("Giá trị STRING không được vượt quá 500 ký tự");
                }
            }
            default -> throw new IllegalStateException("Kiểu thiết lập không được hỗ trợ: "
                    + setting.getValueType());
        }
    }

    private void validateInteger(SystemSetting setting, String value) {
        final long number;
        try {
            number = Long.parseLong(value);
        } catch (NumberFormatException exception) {
            throw invalidValue("Giá trị INT phải là số nguyên");
        }
        if (setting.getMinValue() != null && number < setting.getMinValue()) {
            throw invalidValue("Giá trị INT nhỏ hơn giới hạn cho phép");
        }
        if (setting.getMaxValue() != null && number > setting.getMaxValue()) {
            throw invalidValue("Giá trị INT lớn hơn giới hạn cho phép");
        }
    }

    private BadRequestException invalidValue(String message) {
        return new BadRequestException(message);
    }
}
