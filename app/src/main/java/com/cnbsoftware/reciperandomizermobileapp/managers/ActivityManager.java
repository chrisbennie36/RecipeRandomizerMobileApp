package com.cnbsoftware.reciperandomizermobileapp.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ActivityManager {
    private final Activity currentActivity;
    private final Context context;
    public ActivityManager(Activity currentActivity) {
        this.currentActivity = currentActivity;
        this.context = currentActivity.getApplicationContext();
    }

    public void OpenActivity(Class<?> activityToOpen, Bundle bundle) {
        Intent intent = new Intent(context, activityToOpen);

        if(bundle != null) {
            intent.putExtras(bundle);
        }

        currentActivity.startActivity(intent);
    }

    public void OpenActivity(Class<?> activityToOpen) {
        OpenActivity(activityToOpen, null);
    }

    public void OpenUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        currentActivity.startActivity(intent);
    }
}
