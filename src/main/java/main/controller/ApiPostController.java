package main.controller;

import main.api.response.PostResponse;
import main.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    // Дописать для limit и сортировки
    @GetMapping
    public PostResponse getPosts(@RequestParam(defaultValue = "0") int offset,
                                 @RequestParam(defaultValue = "10") int limit,
                                 @RequestParam(defaultValue = "recent") String mode) {
        return postService.getPostResponse(offset, limit, mode);
    }
}
