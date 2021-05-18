package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import io.reactivex.Observable;

public interface RemoteManager {
    String getPairingCode();

    // RxJava interfaces
    Observable<String> getPairingCodeObserve();
    Observable<Command> getCommandObserve();
    Observable<Void> postStartPlayingObserve(String videoId, long positionMs, long durationMs, boolean isPlaying);
    Observable<Void> postStateChangeObserve(long positionMs, long durationMs, boolean isPlaying);
    Observable<Void> resetDataObserve();
}
