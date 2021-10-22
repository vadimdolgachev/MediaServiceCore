package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.RemoteManager;
import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import com.liskovsoft.youtubeapi.service.data.YouTubeCommand;
import io.reactivex.Observable;

import java.io.InterruptedIOException;

public class YouTubeRemoteManager implements RemoteManager {
    private static final String TAG = YouTubeRemoteManager.class.getSimpleName();
    private static YouTubeRemoteManager sInstance;

    private YouTubeRemoteManager() {
        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeRemoteManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeRemoteManager();
        }

        return sInstance;
    }

    @Override
    public String getPairingCode() {
        return "";
    }

    @Override
    public Observable<String> getPairingCodeObserve() {
        return ObservableHelper.fromNullable(this::getPairingCode);
    }

    @Override
    public Observable<Command> getCommandObserve() {
        return Observable.create(emitter -> {
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Void> postStartPlayingObserve(String videoId, long positionMs, long durationMs, boolean isPlaying) {
        return ObservableHelper.fromVoidable(() -> {});
    }

    @Override
    public Observable<Void> postStateChangeObserve(long positionMs, long durationMs, boolean isPlaying) {
        return ObservableHelper.fromVoidable(() -> {});
    }

    @Override
    public Observable<Void> postVolumeChangeObserve(int volume) {
        return ObservableHelper.fromVoidable(() -> {});
    }

    @Override
    public Observable<Void> resetDataObserve() {
        return ObservableHelper.fromVoidable(()->{});
    }
}
