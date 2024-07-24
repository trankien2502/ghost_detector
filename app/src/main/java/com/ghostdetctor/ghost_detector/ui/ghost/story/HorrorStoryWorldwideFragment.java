package com.ghostdetctor.ghost_detector.ui.ghost.story;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ghostdetctor.ghost_detector.base.BaseFragment;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.databinding.FragmentHorrorStoryWorldwideBinding;


public class HorrorStoryWorldwideFragment extends BaseFragment<FragmentHorrorStoryWorldwideBinding> {

    @Override
    public FragmentHorrorStoryWorldwideBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentHorrorStoryWorldwideBinding.inflate(inflater,container,false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.tvSeeMore1.setOnClickListener(view -> startToStoryActivity(21));
        binding.tvSeeMore2.setOnClickListener(view -> startToStoryActivity(22));
        binding.tvSeeMore3.setOnClickListener(view -> startToStoryActivity(23));
        binding.tvSeeMore4.setOnClickListener(view -> startToStoryActivity(24));
        binding.tvSeeMore5.setOnClickListener(view -> startToStoryActivity(25));
    }

    private void startToStoryActivity(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(SPUtils.SCARY_STORIES,i);
        Intent intent = new Intent(getActivity(),StoryActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}