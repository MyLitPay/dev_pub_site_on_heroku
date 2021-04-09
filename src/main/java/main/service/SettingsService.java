package main.service;

import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.api.response.StatisticsResponse;

public interface SettingsService {

    SettingsResponse getGlobalSettings();
    StatisticsResponse getMyStatistics();
    StatisticsResponse getAllStatistics();
    void updateGlobalSettings(SettingsRequest request);
}
