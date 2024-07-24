package com.ghostdetctor.ghost_detector.ui.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityChallengeStoryBinding;

public class ChallengeStoryActivity extends BaseActivity<ActivityChallengeStoryBinding> {


    @Override
    public ActivityChallengeStoryBinding getBinding() {
        return ActivityChallengeStoryBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.ivBackChallenge.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}