package com.ghostdetctor.ghost_detector.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.language.LanguageActivity;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivitySettingBinding;

import org.intellij.lang.annotations.Language;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {


    @Override
    public ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.clLanguage.setOnClickListener(view -> {
            startNextActivity(LanguageActivity.class,null);
        });

    }
}