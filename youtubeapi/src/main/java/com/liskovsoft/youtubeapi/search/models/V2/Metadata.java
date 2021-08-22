package com.liskovsoft.youtubeapi.search.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class Metadata {
    @JsonPath("$.title.simpleText")
    private String mTitle;
    @JsonPath("$.lines[0].lineRenderer.items[0].lineItemRenderer.text.runs[0].text")
    private String mUserName;
    @JsonPath("$.lines[1].lineRenderer.items[1].lineItemRenderer.text.simpleText")
    private String mViewCountText;
    @JsonPath("$.lines[3].lineRenderer.items[1].lineItemRenderer.text.simpleText")
    private String mPublishedTime;
    @JsonPath("$.lines[1].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.style")
    private String mBadgeStyle;

    public String getTitle() {
        return mTitle;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getViewCountText() {
        return mViewCountText;
    }

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public String getBadgeStyle() {
        return mBadgeStyle;
    }
}