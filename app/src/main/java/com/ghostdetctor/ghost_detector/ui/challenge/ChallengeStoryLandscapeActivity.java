package com.ghostdetctor.ghost_detector.ui.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityChallengeStoryLandscapeBinding;

public class ChallengeStoryLandscapeActivity extends BaseActivity<ActivityChallengeStoryLandscapeBinding> {


    @Override
    public ActivityChallengeStoryLandscapeBinding getBinding() {
        return ActivityChallengeStoryLandscapeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
       //rotateTextView(binding.tvTitleStory);
    }
    @Override
    public void bindView() {
        binding.ivBackChallenge.setOnClickListener(view -> onBackPressed());
    }
}