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
import com.ghostdetector.ghost_detector.databinding.FragmentHororGhostsBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HororGhostsFragment extends BaseFragment<FragmentHororGhostsBinding> {


    @Override
    public FragmentHororGhostsBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentHororGhostsBinding.inflate(inflater,container,false);
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcvHorrorGhosts.setLayoutManager(gridLayoutManager);

        GhostAdapter ghostAdapter = new GhostAdapter(requireContext(),getListHorrorGhosts(),this::goToGhostDetail);
        binding.rcvHorrorGhosts.setAdapter(ghostAdapter);
    }

    private void goToGhostDetail(Ghost ghost) {
        Intent intent = new Intent(getContext(), GhostDetailActivity.class);
        intent.putExtra(SPUtils.GHOST_ID,ghost.getId());
        startActivity(intent);
    }


    private List<Ghost> getListHorrorGhosts() {
        List<Ghost> horror = new ArrayList<>();
        for (Ghost ghost : AppDatabase.getInstance(requireContext()).ghostDAO().getAllGhost()){
            if (ghost.isHorror()){
                if (ghost.getImagePath()!=null && !ghost.getImagePath().isEmpty()){
                    File file = new File(ghost.getImagePath());
                    if (file.exists()) horror.add(ghost);
                }
            }
        }
        return horror;
    }

    @Override
    public void bindView() {

    }
}