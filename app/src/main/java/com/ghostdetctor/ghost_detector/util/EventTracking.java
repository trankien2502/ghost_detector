package com.ghostdetctor.ghost_detector.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EventTracking {

    public static void logEvent(Context context, String nameEvent) {
        if (context != null) {
            Bundle bundle = new Bundle();
            bundle.putString(nameEvent, nameEvent);
            FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
        }
    }

    public static void logEvent(Context context, String nameEvent, String param, String value) {
        if (context != null) {
            Bundle bundle = new Bundle();
            bundle.putString(param, value);
            FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
        }
    }

    public static void logEvent(Context context, String nameEvent, String param, Long value) {
        if (context != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(param, value);
            FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
        }
    }

    public static void logEvent(Context context, String nameEvent, Bundle bundle) {
        if (context != null) {
            FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
        }
    }

}
