package com.ghostdetctor.ghost_detector.ui.ghost.story;

import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Typeface;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityScaryStoryBinding;

public class ScaryStoryActivity extends BaseActivity<ActivityScaryStoryBinding> {

    @Override
    public ActivityScaryStoryBinding getBinding() {
        return ActivityScaryStoryBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        StoryAdapter storyAdapter = new StoryAdapter(this);
        binding.frameContentPager.setAdapter(storyAdapter);
        binding.frameContentPager.setCurrentItem(0);
    }

    @Override
    public void bindView() {
        binding.header.tvTitle.setText(R.string.scary_stories);
        binding.header.ivBack.setOnClickListener(view -> onBackPressed());
        binding.tvHauntedStoryteller.setOnClickListener(view -> {
            binding.frameContentPager.setCurrentItem(0);
        });
        binding.tvHorrorStoriesWorldwide.setOnClickListener(view -> {
            binding.frameContentPager.setCurrentItem(1);
        });
        binding.frameContentPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position==0){
                    binding.tvHauntedStoryteller.setBackgroundResource(R.drawable.img_border_select);
                    binding.tvHauntedStoryteller.setTextColor(getResources().getColor(R.color.text_select));
                    binding.tvHorrorStoriesWorldwide.setBackgroundResource(R.drawable.img_border_unselect);
                    binding.tvHorrorStoriesWorldwide.setTextColor(getResources().getColor(R.color.text_unselect));
                    binding.tvHorrorStoriesWorldwide.setTypeface(null,Typeface.NORMAL);
                    binding.tvHauntedStoryteller.setTypeface(null,Typeface.BOLD);
                }
                else {
                    binding.tvHauntedStoryteller.setBackgroundResource(R.drawable.img_border_unselect);
                    binding.tvHauntedStoryteller.setTextColor(getResources().getColor(R.color.text_unselect));
                    binding.tvHorrorStoriesWorldwide.setBackgroundResource(R.drawable.img_border_select);
                    binding.tvHorrorStoriesWorldwide.setTextColor(getResources().getColor(R.color.text_select));
                    binding.tvHorrorStoriesWorldwide.setTypeface(null,Typeface.BOLD);
                    binding.tvHauntedStoryteller.setTypeface(null,Typeface.NORMAL);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}