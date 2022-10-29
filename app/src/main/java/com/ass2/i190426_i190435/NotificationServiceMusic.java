package com.ass2.i190426_i190435;


import android.content.Context;
import android.content.Intent;


public class NotificationServiceMusic {

    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("TRACKS_TRACKS")
                .putExtra("actionname", intent.getAction()));
    }
}
