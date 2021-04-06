package main.service;

import main.api.response.TagResponse;
import main.model.Tag;

import java.util.List;

public interface TagService {
    TagResponse getTagResponse(String tag);
    Tag getTagByName(String name);
}
