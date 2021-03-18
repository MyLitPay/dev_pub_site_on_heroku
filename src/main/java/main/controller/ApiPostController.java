package main.controller;

import main.api.response.PostResponse;
import main.api.response.dto.PostByIdDTO;
import main.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_AUTHORITY')")
    public PostResponse getPosts(@RequestParam(defaultValue = "0") int offset,
                                 @RequestParam(defaultValue = "10") int limit,
                                 @RequestParam(defaultValue = "recent") String mode) {
        return postService.getPostResponse(offset, limit, mode);
    }

    @GetMapping("/search")
    public PostResponse getPostsByQuery(@RequestParam(defaultValue = "0") int offset,
                                           @RequestParam(defaultValue = "10") int limit,
                                           @RequestParam(defaultValue = "") String query) {
        return postService.getPostResponseByQuery(offset, limit, query);
    }

    @GetMapping("/byDate")
    public PostResponse getPostsByDate(@RequestParam(defaultValue = "0") int offset,
                                       @RequestParam(defaultValue = "10") int limit,
                                       @RequestParam String date) {
        return postService.getPostResponseByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    public PostResponse getPostsByTag(@RequestParam(defaultValue = "0") int offset,
                                      @RequestParam(defaultValue = "10") int limit,
                                      @RequestParam String tag) {
        return postService.getPostResponseByTag(offset, limit, tag);
    }

    @GetMapping("/{id}")
    public PostByIdDTO getPostById(@PathVariable("id") int id) {
        return postService.getPostByIdDTO(id);
    }
}
