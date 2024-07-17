//package com.ghostdetctor.ghost_detector.ads;
//
//import android.os.Handler;
//
//import com.ghostdetector.ghost_detector.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//
//public class ConstantRemote {
//    public static boolean open_splash = true;
//    public static ArrayList<String> show_language = new ArrayList<>(Collections.singletonList("1,2,3"));
//    public static ArrayList<String> rate_aoa_inter_splash = new ArrayList<>(Arrays.asList("30", "70"));
//
//
//
//    public static void initRemoteConfig(OnCompleteListener listener) {
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        mFirebaseRemoteConfig.reset();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build();
//        new Handler().postDelayed(() -> {
//            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
//            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(listener);
//        }, 2000);
//    }
//
//
//    public static boolean getRemoteConfigBoolean(String adUnitId) {
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        return mFirebaseRemoteConfig.getBoolean(adUnitId);
//    }
//
//    public static ArrayList<String> getRemoteConfigString(String adUnitId) {
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        String object = mFirebaseRemoteConfig.getString(adUnitId);
//        String[] arStr = object.split(",");
//        return new ArrayList<>(Arrays.asList(arStr));
//    }
//
//    public static ArrayList<String> getRemoteConfigOpenSplash(String adUnitId) {
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        String object = mFirebaseRemoteConfig.getString(adUnitId);
//        String[] arStr = object.split("_");
//        return new ArrayList<>(Arrays.asList(arStr));
//    }
//}
