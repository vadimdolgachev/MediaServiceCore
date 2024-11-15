package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.yt.CommentsService;
import com.liskovsoft.mediaserviceinterfaces.yt.ContentService;
import com.liskovsoft.mediaserviceinterfaces.yt.LiveChatService;
import com.liskovsoft.mediaserviceinterfaces.yt.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.yt.NotificationsService;
import com.liskovsoft.mediaserviceinterfaces.yt.RemoteControlService;
import com.liskovsoft.mediaserviceinterfaces.yt.ServiceManager;
import com.liskovsoft.mediaserviceinterfaces.yt.SignInService;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class YouTubeServiceManager implements ServiceManager {
    private static final String TAG = YouTubeServiceManager.class.getSimpleName();
    private static YouTubeServiceManager sInstance;
    private final YouTubeSignInService mSignInManager;
    private final ContentService mMediaGroupManager;
    private final MediaItemService mMediaItemManager;
    private final YouTubeLiveChatService mLiveChatService;
    private final YouTubeCommentsService mCommentsService;
    private Disposable mRefreshCoreDataAction;
    private Disposable mRefreshPoTokenAction;

    private YouTubeServiceManager() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInService.instance();
        mMediaGroupManager = YouTubeContentService.instance();
        mMediaItemManager = YouTubeMediaItemService.instance();
        mLiveChatService = YouTubeLiveChatService.instance();
        mCommentsService = YouTubeCommentsService.instance();
    }

    public static ServiceManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeServiceManager();
        }

        return sInstance;
    }

    @Override
    public SignInService getSignInService() {
        return mSignInManager;
    }

    @Override
    public RemoteControlService getRemoteControlService() {
        return null;
    }

    @Override
    public LiveChatService getLiveChatService() {
        return mLiveChatService;
    }

    @Override
    public CommentsService getCommentsService() {
        return mCommentsService;
    }

    @Override
    public ContentService getContentService() {
        return mMediaGroupManager;
    }

    @Override
    public MediaItemService getMediaItemService() {
        return mMediaItemManager;
    }

    @Override
    public NotificationsService getNotificationsService() {
        return YouTubeNotificationsService.INSTANCE;
    }

    @Override
    public void invalidateCache() {
        invalidatePlaybackCache();
        VideoInfoService.instance().resetInfoType();
    }

    @Override
    public void refreshCacheIfNeeded() {
        refreshCoreDataIfNeeded();
        //refreshPoTokenIfNeeded();
    }

    private void refreshCoreDataIfNeeded() {
        if (RxHelper.isAnyActionRunning(mRefreshCoreDataAction)) {
            return;
        }

        mRefreshCoreDataAction = RxHelper.execute(refreshCoreDataIfNeededObserve());
    }

    private void refreshPoTokenIfNeeded() {
        if (RxHelper.isAnyActionRunning(mRefreshPoTokenAction)) {
            return;
        }

        mRefreshPoTokenAction = RxHelper.execute(refreshPoTokenIfNeededObserve());
    }

    private Observable<Void> refreshCoreDataIfNeededObserve() {
        return RxHelper.fromVoidable(AppService.instance()::refreshCoreDataIfNeeded);
    }

    private Observable<Void> refreshPoTokenIfNeededObserve() {
        return RxHelper.fromVoidable(AppService.instance()::refreshPoTokenIfNeeded);
    }

    @Override
    public void applyNoPlaybackFix() {
        invalidatePlaybackCache();
        VideoInfoService.instance().fixPlaybackErrors();
    }

    @Override
    public void invalidatePlaybackCache() {
        LocaleManager.unhold();
        YouTubeSignInService.instance().invalidateCache(); // sections infinite loading fix (request timed out fix)
        AppService.instance().invalidateCache();
        AppService.instance().invalidateVisitorData();
        YouTubeMediaItemService.instance().invalidateCache();
    }
}
