package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    final InitResponse initResponse;
    final SettingsService settingsService;
    final TagService tagService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, TagService tagService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagService = tagService;
    }

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    public SettingsResponse getSettings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    public TagResponse getTags(@RequestParam(defaultValue = "") String tag) {
        return tagService.getTagResponse(tag);
    }
}
