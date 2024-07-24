package com.ghostdetctor.ghost_detector.ui.ghost.story;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StoryAdapter extends FragmentStateAdapter {
    public StoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new HauntedStorytellerFragment();
        else return new HorrorStoryWorldwideFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
