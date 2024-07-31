package com.ghostdetctor.ghost_detector.ui.ghost.story;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ghostdetctor.ghost_detector.base.BaseFragment;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.databinding.FragmentHauntedStorytellerBinding;

public class HauntedStorytellerFragment extends BaseFragment<FragmentHauntedStorytellerBinding> {


    @Override
    public FragmentHauntedStorytellerBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentHauntedStorytellerBinding.inflate(inflater,container,false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.tvSeeMore1.setOnClickListener(view -> startToStoryActivity(11));
        binding.tvSeeMore2.setOnClickListener(view -> startToStoryActivity(12));
        binding.tvSeeMore3.setOnClickListener(view -> startToStoryActivity(13));
        binding.tvSeeMore4.setOnClickListener(view -> startToStoryActivity(14));
        binding.tvSeeMore5.setOnClickListener(view -> startToStoryActivity(15));
    }

    private void startToStoryActivity(int i) {
        EventTracking.logEvent(requireContext(), "scary_stories_see_more_click");
        Bundle bundle = new Bundle();
        bundle.putInt(SPUtils.SCARY_STORIES,i);
        Intent intent = new Intent(getActivity(),StoryActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}