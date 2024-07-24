package com.ghostdetctor.ghost_detector.ui.ghost.collection;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ghostdetctor.ghost_detector.base.BaseFragment;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.databinding.FragmentScarySpiritsBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScarySpiritsFragment extends BaseFragment<FragmentScarySpiritsBinding> {

    @Override
    public FragmentScarySpiritsBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentScarySpiritsBinding.inflate(inflater,container,false);
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcvScarySpirits.setLayoutManager(gridLayoutManager);

        GhostAdapter ghostAdapter = new GhostAdapter(requireContext(),getListScarySpirits(),this::goToGhostDetail);
        binding.rcvScarySpirits.setAdapter(ghostAdapter);
    }

    private List<Ghost> getListScarySpirits() {
        List<Ghost> scary = new ArrayList<>();
        for (Ghost ghost : AppDatabase.getInstance(requireContext()).ghostDAO().getAllGhost()){
            if (!ghost.isHorror()){
                if (ghost.getImagePath()!=null && !ghost.getImagePath().isEmpty()){
                    File file = new File(ghost.getImagePath());
                    if (file.exists()) scary.add(ghost);
                }
            }
        }
        return scary;
    }
    private void goToGhostDetail(Ghost ghost) {
        Intent intent = new Intent(getContext(), GhostDetailActivity.class);
        intent.putExtra(SPUtils.GHOST_ID,ghost.getId());
        startActivity(intent);
    }

    @Override
    public void bindView() {

    }
}