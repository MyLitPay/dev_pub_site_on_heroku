package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsRequest {
    @JsonProperty("MULTIUSER_MODE")
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;

    public SettingsRequest() {
    }

    public SettingsRequest(boolean multiuserMode, boolean postPremoderation, boolean statisticsIsPublic) {
        this.multiuserMode = multiuserMode;
        this.postPremoderation = postPremoderation;
        this.statisticsIsPublic = statisticsIsPublic;
    }

    public String getStringMultiuserMode() {
        if (isMultiuserMode()) {
            return "YES";
        }
        return "NO";
    }

    public String getStringPostPremoderation() {
        if (isPostPremoderation()) {
            return "YES";
        }
        return "NO";
    }

    public String getStringStatisticsIsPublic() {
        if (isStatisticsIsPublic()) {
            return "YES";
        }
        return "NO";
    }

    public boolean isMultiuserMode() {
        return multiuserMode;
    }

    public void setMultiuserMode(boolean multiuserMode) {
        this.multiuserMode = multiuserMode;
    }

    public boolean isPostPremoderation() {
        return postPremoderation;
    }

    public void setPostPremoderation(boolean postPremoderation) {
        this.postPremoderation = postPremoderation;
    }

    public boolean isStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public void setStatisticsIsPublic(boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }
}
