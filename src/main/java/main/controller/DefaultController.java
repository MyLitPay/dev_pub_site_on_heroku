package main.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET},
            value = "/**/{path:[^\\.]*}")
    public String redirectToIndex() {
        return "index";
    }

    @GetMapping("/upload/{first}/{second}/{third}/{name}")
    @ResponseBody
    public FileSystemResource getImage(@PathVariable("first") String first,
                         @PathVariable("second") String second,
                         @PathVariable("third") String third,
                         @PathVariable("name") String name) {
        File file = new File("upload/" + first + "/" + second + "/" + third + "/" + name);
        return new FileSystemResource(file);
    }
}
