package com.geoverifi.geoverifi.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by chriz on 10/15/2017.
 */

public class SyncAuditingService extends Service {
    // Storage for an instance of the sync adapter
    private static SyncAuditingAdapter sSyncAuditingAdapter = null;

    // Object to use as a thread-safe lock
    private static final Object sSyncAuditingLock = new Object();

    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAuditingLock) {
            if (sSyncAuditingAdapter == null) {
                sSyncAuditingAdapter = new SyncAuditingAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAuditingAdapter.getSyncAdapterBinder();
    }
}
