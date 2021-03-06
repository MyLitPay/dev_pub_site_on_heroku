package main.service.implementation;

import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.repo.SettingsRepository;
import main.service.SettingsService;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    final SettingsRepository settingsRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();

        Iterable<GlobalSettings> settings = settingsRepository.findAll();

        for (GlobalSettings s : settings) {
            if (s.getCode().equals("MULTIUSER_MODE")) {
                settingsResponse.setMultiuserMode(s.getValue().equals("YES"));
            }

            if (s.getCode().equals("POST_PREMODERATION")) {
                settingsResponse.setPostPremoderation(s.getValue().equals("YES"));
            }

            if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
                settingsResponse.setStatisticsIsPublic(s.getValue().equals("YES"));
            }
        }

        return settingsResponse;
    }
}
