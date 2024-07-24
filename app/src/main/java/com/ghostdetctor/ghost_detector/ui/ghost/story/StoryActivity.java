package com.ghostdetctor.ghost_detector.ui.ghost.story;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityStoryBinding;

public class StoryActivity extends BaseActivity<ActivityStoryBinding> {

    @Override
    public ActivityStoryBinding getBinding() {
        return ActivityStoryBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(R.string.stories);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            int storyId = bundle.getInt(SPUtils.SCARY_STORIES);
            switch (storyId){
                case 11:
                    binding.storyTitle.setText(R.string.haunted_title_1);
                    binding.storyContent.setText(R.string.haunted_content_1);
                    binding.storyFooter.setText(R.string.haunted_footer_1);
                    binding.storyTeller.setText(R.string.haunted_teller_1);
                    binding.storyTeller.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    binding.storyTitle.setText(R.string.haunted_title_2);
                    binding.storyContent.setText(R.string.haunted_content_2);
                    binding.storyFooter.setText(R.string.haunted_footer_2);
                    binding.storyTeller.setText(R.string.haunted_teller_2);
                    binding.storyTeller.setVisibility(View.VISIBLE);
                    break;
                case 13:
                    binding.storyTitle.setText(R.string.haunted_title_3);
                    binding.storyContent.setText(R.string.haunted_content_3);
                    binding.storyFooter.setText(R.string.haunted_footer_3);
                    binding.storyTeller.setText(R.string.haunted_teller_3);
                    binding.storyTeller.setVisibility(View.VISIBLE);
                    break;
                case 14:
                    binding.storyTitle.setText(R.string.haunted_title_4);
                    binding.storyContent.setText(R.string.haunted_content_4);
                    binding.storyFooter.setText(R.string.haunted_footer_4);
                    binding.storyTeller.setText(R.string.haunted_teller_4);
                    binding.storyTeller.setVisibility(View.VISIBLE);
                    break;
                case 15:
                    binding.storyTitle.setText(R.string.haunted_title_5);
                    binding.storyContent.setText(R.string.haunted_content_5);
                    binding.storyFooter.setText(R.string.haunted_footer_5);
                    binding.storyTeller.setText(R.string.haunted_teller_5);
                    binding.storyTeller.setVisibility(View.VISIBLE);
                    break;
                case 21:
                    binding.storyTitle.setText(R.string.horror_title_1);
                    binding.storyContent.setText(R.string.horror_content_1);
                    binding.storyFooter.setText(R.string.horror_footer_1);
                    binding.storyTeller.setVisibility(View.GONE);

                    break;
                case 22:
                    binding.storyTitle.setText(R.string.horror_title_2);
                    binding.storyContent.setText(R.string.horror_content_2);
                    binding.storyFooter.setText(R.string.horror_footer_2);
                    binding.storyTeller.setVisibility(View.GONE);

                    break;
                case 23:
                    binding.storyTitle.setText(R.string.horror_title_3);
                    binding.storyContent.setText(R.string.horror_content_3);
                    binding.storyFooter.setText(R.string.horror_footer_3);
                    binding.storyTeller.setVisibility(View.GONE);

                    break;
                case 24:
                    binding.storyTitle.setText(R.string.horror_title_4);
                    binding.storyContent.setText(R.string.horror_content_4);
                    binding.storyFooter.setText(R.string.horror_footer_4);
                    binding.storyTeller.setVisibility(View.GONE);

                    break;
                case 25:
                    binding.storyTitle.setText(R.string.horror_title_5);
                    binding.storyContent.setText(R.string.horror_content_5);
                    binding.storyFooter.setText(R.string.horror_footer_5);
                    binding.storyTeller.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}