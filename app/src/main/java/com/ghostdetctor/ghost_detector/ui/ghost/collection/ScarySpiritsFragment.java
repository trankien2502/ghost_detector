package com.ghostdetctor.ghost_detector.ui.ghost.collection;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ghostdetctor.ghost_detector.base.BaseFragment;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.ui.ghost.GhostActivity;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
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

        if (getListScarySpirits().size()==0){
            binding.clNoGhost.setVisibility(View.VISIBLE);
            binding.rcvScarySpirits.setVisibility(View.GONE);
        } else {
            binding.clNoGhost.setVisibility(View.INVISIBLE);
            binding.rcvScarySpirits.setVisibility(View.VISIBLE);
            GhostAdapter ghostAdapter = new GhostAdapter(requireContext(),getListScarySpirits(),this::goToGhostDetail);
            binding.rcvScarySpirits.setAdapter(ghostAdapter);
        }

    }

    private List<Ghost> getListScarySpirits() {
        List<Ghost> scary = new ArrayList<>();
        for (Ghost ghost : AppDatabase.getInstance(requireContext()).ghostDAO().getAllGhost()){
            if (!ghost.isHorror()){
                if (ghost.isCaptured()){
                    scary.add(ghost);
                }
            }
        }
        return scary;
    }
    private void goToGhostDetail(Ghost ghost) {
        EventTracking.logEvent(requireContext(),"collection_ghost_click");
        Intent intent = new Intent(getContext(), GhostDetailActivity.class);
        intent.putExtra(SPUtils.GHOST_ID,ghost.getId());
        startActivity(intent);
    }

    @Override
    public void bindView() {
        binding.clScan.setOnClickListener(view -> {
            EventTracking.logEvent(getContext(),"collection_start_scan_click");
            int ghostType = 2;
            Intent intent = new Intent(getActivity(), GhostActivity.class);
            intent.putExtra("RESTART_SCAN_WITH_GHOST_TYPE",ghostType);
            startActivity(new Intent(getActivity(), HomeActivity.class));
            requireActivity().finishAffinity();
            startActivity(intent);
        });
    }
}