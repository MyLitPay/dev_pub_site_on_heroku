package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.PostService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    final PostService postService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, TagService tagService, PostService postService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagService = tagService;
        this.postService = postService;
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
    @PreAuthorize("hasAuthority('MODERATE_AUTHORITY')")
    public TagResponse getTags(@RequestParam(defaultValue = "") String query) {
        return tagService.getTagResponse(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(@RequestParam(defaultValue = "0") int year) {
        return postService.getCalendar(year);
    }
}
