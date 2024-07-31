package com.ghostdetctor.ghost_detector.ui.challenge.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) ||
                Intent.ACTION_USER_PRESENT.equals(intent.getAction()) ||
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {

            // Stop the service when the screen is turned off or the app is sent to the background
            Intent serviceIntent = new Intent(context, SoundService.class);
            context.stopService(serviceIntent);
        }
    }
}
