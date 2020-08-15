package com.liskovsoft.mediaserviceinterfaces.data;

import java.util.ArrayList;

public interface MediaItemFormatInfo {
    ArrayList<MediaFormat> getAdaptiveFormats();
    ArrayList<MediaFormat> getRegularFormats();
    // video metadata
    String getLengthSeconds();
    void setLengthSeconds(String lengthSeconds);
    String getTitle();
    void setTitle(String title);
    String getAuthor();
    void setAuthor(String author);
    String getViewCount();
    void setViewCount(String viewCount);
    String getTimestamp();
    void setTimestamp(String timestamp);
    String getDescription();
    void setDescription(String description);
    String getVideoId();
    void setVideoId(String videoId);
    String getChannelId();
    void setChannelId(String channelId);
    boolean containsDashInfo();
    boolean containsUrlListInfo();
}