package com.ghostdetctor.ghost_detector.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.policy.PolicyActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    public ActivityAboutBinding getBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(AboutActivity.this,"view_about_app");
    }

    @Override
    public void bindView() {
        binding.privacy.setOnClickListener(view -> {
            startNextActivity(PolicyActivity.class,null);
        });
        binding.ivGone.setOnClickListener(view -> onBackPressed());
    }
}