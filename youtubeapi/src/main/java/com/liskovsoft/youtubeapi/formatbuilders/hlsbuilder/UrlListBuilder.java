package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaFormat;

import java.util.List;

public interface UrlListBuilder {
    void append(MediaFormat mediaItem);
    boolean isEmpty();
    List<String> buildUriList();
}
