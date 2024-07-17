package com.ghostdetctor.ghost_detector.ui.splash;

import android.os.Handler;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.language.LanguageStartActivity;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.databinding.ActivitySplashBinding;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);

        new Handler().postDelayed(() -> {
            startNextActivity(LanguageStartActivity.class, null);
            finishAffinity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBackPressed() {
    }
}
