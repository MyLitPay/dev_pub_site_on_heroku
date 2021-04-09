package main.service.implementation;

import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.api.response.StatisticsResponse;
import main.exception.StatisticsNotPublicException;
import main.model.*;
import main.repo.PostRepository;
import main.repo.SettingsRepository;
import main.security.UserDetailsServiceImpl;
import main.service.SettingsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {

    final SettingsRepository settingsRepository;
    final PostRepository postRepository;
    final UserDetailsServiceImpl userDetailsService;

    public SettingsServiceImpl(SettingsRepository settingsRepository, PostRepository postRepository, UserDetailsServiceImpl userDetailsService) {
        this.settingsRepository = settingsRepository;
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();

        Iterable<GlobalSettings> settings = settingsRepository.findAll();

        for (GlobalSettings s : settings) {
            if (s.getCode().equals("MULTIUSER_MODE")) {
                settingsResponse.setMultiuserMode(s.getValue().equals("YES"));
            }

            if (s.getCode().equals("POST_PREMODERATION")) {
                settingsResponse.setPostPremoderation(s.getValue().equals("YES"));
            }

            if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
                settingsResponse.setStatisticsIsPublic(s.getValue().equals("YES"));
            }
        }

        return settingsResponse;
    }

    @Override
    public void updateGlobalSettings(SettingsRequest request) {
        Iterable<GlobalSettings> settings = settingsRepository.findAll();
        for (GlobalSettings s : settings) {
            if (s.getCode().equals("MULTIUSER_MODE")) {
                s.setValue(request.getStringMultiuserMode());
            }
            if (s.getCode().equals("POST_PREMODERATION")) {
                s.setValue(request.getStringPostPremoderation());
            }
            if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
                s.setValue(request.getStringStatisticsIsPublic());
            }
        }

        settingsRepository.saveAll(settings);
    }

    @Override
    public StatisticsResponse getMyStatistics() {
        User user = userDetailsService.getUserFromContextHolder();
        List<Post> postList = postRepository.findByUserAndIsActiveAndModerationStatusAndTimeBefore(
                user, (byte) 1, ModerationStatus.ACCEPTED, new Date());

        return getStatisticsResponse(postList);
    }

    @Override
    public StatisticsResponse getAllStatistics() {
        User user = userDetailsService.getUserFromContextHolder();

        SettingsResponse settingsResponse = getGlobalSettings();
        if (!settingsResponse.isStatisticsIsPublic() && user.getIsModerator() != 1) {
            throw new StatisticsNotPublicException("Statistics not public");
        }

        List<Post> postList = postRepository.findByIsActiveAndModerationStatusAndTimeBefore(
                (byte) 1, ModerationStatus.ACCEPTED, new Date());

        return getStatisticsResponse(postList);
    }

    private StatisticsResponse getStatisticsResponse(List<Post> postList) {
        int postCount = 0;
        int likesCount = 0;
        int dislikesCount = 0;
        int viewsCount = 0;
        long firstPublication = new Date().getTime();

        for (Post post : postList) {
            postCount++;
            List<PostVote> postVoteList = post.getPostVoteList();
            int likes = (int) postVoteList.stream().filter(v -> v.getValue() == 1).count();
            int dislikes = (int) postVoteList.stream().filter(v -> v.getValue() == -1).count();
            likesCount += likes;
            dislikesCount += dislikes;
            viewsCount += post.getViewCount();
            if (post.getTime().before(new Date(firstPublication))) {
                firstPublication = post.getTime().getTime();
            }
        }

        return new StatisticsResponse(postCount, likesCount, dislikesCount,
                viewsCount, firstPublication / 1000);
    }
}
