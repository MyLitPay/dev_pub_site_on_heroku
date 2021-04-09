package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    final InitResponse initResponse;
    final SettingsService settingsService;
    final TagService tagService;
    final PostService postService;
    final ProfileService profileService;
    final CommentService commentService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, TagService tagService, PostService postService, ProfileService profileService, CommentService commentService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagService = tagService;
        this.postService = postService;
        this.profileService = profileService;
        this.commentService = commentService;
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
    public TagResponse getTags(@RequestParam(defaultValue = "") String query) {
        return tagService.getTagResponse(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(@RequestParam(defaultValue = "0") int year) {
        return postService.getCalendar(year);
    }

    @PostMapping("/image")
    @PreAuthorize("hasAuthority('WRITE_AUTHORITY')")
    public String uploadImage(@RequestParam MultipartFile image) {
        return profileService.uploadImage(image);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasAuthority('WRITE_AUTHORITY')")
    public CommentResponse addComment(@RequestBody CommentRequest request) {
        return commentService.addComment(request);
    }

    @PostMapping("/moderation")
    @PreAuthorize("hasAuthority('MODERATE_AUTHORITY')")
    public ResultResponse moderate(@RequestBody ModerationRequest request) {
        return postService.moderate(request);
    }

    @PostMapping(value = "/profile/my", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('READ_AUTHORITY')")
    public ResultResponse editProfile(@RequestParam(required = false) MultipartFile photo,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(required = false) String password,
                                      @RequestParam(required = false) String removePhoto,
                                      @RequestBody(required = false) String request) throws IOException {
        return profileService.editProfile(photo, name, email, password, removePhoto, request);
    }

    @GetMapping("/statistics/my")
    @PreAuthorize("hasAuthority('READ_AUTHORITY')")
    public StatisticsResponse getMyStatistics() {
        return settingsService.getMyStatistics();
    }

    @GetMapping("/statistics/all")
    public StatisticsResponse getAllStatistics() {
        return settingsService.getAllStatistics();
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('MODERATE_AUTHORITY')")
    public void updateGlobalSettings(@RequestBody SettingsRequest request) {
        settingsService.updateGlobalSettings(request);
    }

}
