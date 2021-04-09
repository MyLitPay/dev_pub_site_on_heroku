package main.service.implementation;

import main.api.response.TagResponse;
import main.api.response.dto.TagDTO;
import main.exception.TagNotFoundException;
import main.model.ModerationStatus;
import main.model.Tag;
import main.repo.PostRepository;
import main.repo.TagRepository;
import main.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    final TagRepository tagRepository;
    final PostRepository postRepository;

    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findFirstByNameLike(name).orElseThrow(
                () -> new TagNotFoundException("Tag not found"));
    }

    @Override
    public TagResponse getTagResponse(String tag) {

        TagResponse tagResponse = new TagResponse();
        List<TagDTO> tagDTOList = new ArrayList<>();

        List<Tag> tagList;
        if (tag.isEmpty()) {
            tagList = tagRepository.findActiveTags((byte) 1, ModerationStatus.ACCEPTED.toString(), new Date());
        } else {
            tagList = tagRepository.findByNameStartingWith(tag);
        }

//        tagList.forEach(t -> t.getPostSet().removeIf(p -> p.getIsActive() != 1 ||
//                !(p.getModerationStatus().equals(ModerationStatus.ACCEPTED)) ||
//                p.getTime().after(new Date())));

//        List<Tag> sortedTagList = tagList.stream()
//                .sorted((t1, t2) -> Integer.compare(t2.getPostSet().size(),
//                        t1.getPostSet().size()))
//                .collect(Collectors.toList());

        if (tagList.size() > 0) {

            int countOfPostsMostPopularTag = tagRepository.countOfPostsMostPopularTag(
                    (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date());
            int countOfAllPosts = postRepository.findByIsActiveAndModerationStatusAndTimeBefore(
                    (byte) 1, ModerationStatus.ACCEPTED, new Date())
                    .size();
            double k = 1 / ((double) countOfPostsMostPopularTag / countOfAllPosts);

            for (Tag t : tagList) {
                int countOfPosts = tagRepository.countOfPostsWithTagById(
                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), t.getId());
                double dWeight = (double) countOfPosts / countOfAllPosts;
                double weight = dWeight * k;

                TagDTO tagDTO = new TagDTO(t, weight);
                tagDTOList.add(tagDTO);
            }
        }

        tagResponse.setTags(tagDTOList);

        return tagResponse;
    }
}
