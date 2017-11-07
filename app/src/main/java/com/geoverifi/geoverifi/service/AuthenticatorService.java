package com.geoverifi.geoverifi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.geoverifi.geoverifi.account.Authenticator;

/**
 * Created by chriz on 10/14/2017.
 */

public class AuthenticatorService extends Service {
    Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
