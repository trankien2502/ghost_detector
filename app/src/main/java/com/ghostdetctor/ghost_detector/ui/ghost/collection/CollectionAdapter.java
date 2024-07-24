package com.ghostdetctor.ghost_detector.ui.ghost.collection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CollectionAdapter extends FragmentStateAdapter {
    public CollectionAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new HororGhostsFragment();
        else return new ScarySpiritsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
