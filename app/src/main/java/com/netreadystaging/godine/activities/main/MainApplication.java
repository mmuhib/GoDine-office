package com.netreadystaging.godine.activities.main;

import android.app.Application;

import com.helpshift.All;
import com.helpshift.Core;
import com.helpshift.InstallConfig;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.Support;

public class MainApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Core.init(All.getInstance());
    InstallConfig installConfig = new InstallConfig.Builder()
                               .setEnableInAppNotification(true)
                               .build();
    try {
      Core.install(this,
                   "8ae760ba7bdb44f70c51bbb3478b45bd",
                   "godine.helpshift.com",
                   "godine_platform_20160725103841299-3d95fc273f90df5",
                   installConfig);
    } catch (InstallException e) {
      android.util.Log.e("Helpshift", "install call : ", e);
    }

    android.util.Log.d("Helpshift", Support.libraryVersion + " - is the version for gradle");

  }
}
