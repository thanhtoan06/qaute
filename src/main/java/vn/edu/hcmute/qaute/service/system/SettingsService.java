package vn.edu.hcmute.qaute.service.system;

import java.util.List;
import vn.edu.hcmute.qaute.dto.view.SettingView;

public interface SettingsService {

    String getString(String key);

    int getInt(String key);

    long getLong(String key);

    boolean getBoolean(String key);

    List<SettingView> listAll();

    void update(String key, String value, Long actorUserId);
}
