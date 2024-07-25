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
    private Disposable mRefreshCacheAction;
    private boolean mVisitorDataReset;

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
        AppService.instance().invalidateCache();
        YouTubeMediaItemService.instance().invalidateCache();
        YouTubeSignInService.instance().invalidateCache(); // sections infinite loading fix (request timed out fix)
        VideoInfoService.instance().invalidateCache();
        LocaleManager.unhold();
        mVisitorDataReset = false;
    }

    @Override
    public void refreshCacheIfNeeded() {
        if (RxHelper.isAnyActionRunning(mRefreshCacheAction)) {
            return;
        }

        mRefreshCacheAction = RxHelper.execute(refreshCacheIfNeededObserve());
    }

    private Observable<Void> refreshCacheIfNeededObserve() {
        return RxHelper.fromVoidable(AppService.instance()::refreshCacheIfNeeded);
    }

    @Override
    public void applyNoPlaybackFix() {
        if (mVisitorDataReset) {
            VideoInfoService.instance().applyVideoInfoFix();
        } else {
            AppService.instance().invalidateVisitorData();
            mVisitorDataReset = true;
        }
    }

    @Override
    public void invalidatePlaybackCache() {
        AppService.instance().invalidateCache();
        YouTubeMediaItemService.instance().invalidateCache();
    }
}
