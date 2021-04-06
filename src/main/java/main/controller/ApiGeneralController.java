package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.ProfileRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;
import java.sql.SQLOutput;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    final InitResponse initResponse;
    final SettingsService settingsService;
    final TagService tagService;
    final PostService postService;
    final ProfileService profileService;
    final CommentService commentService;

    final EmailService emailService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, TagService tagService, PostService postService, ProfileService profileService, CommentService commentService, EmailService emailService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagService = tagService;
        this.postService = postService;
        this.profileService = profileService;
        this.commentService = commentService;
        this.emailService = emailService;
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
}
