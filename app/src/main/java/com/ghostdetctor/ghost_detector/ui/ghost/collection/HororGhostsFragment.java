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

        if (getListHorrorGhosts().size()==0){
            binding.clNoGhost.setVisibility(View.VISIBLE);
            binding.rcvHorrorGhosts.setVisibility(View.GONE);
        } else {
            binding.clNoGhost.setVisibility(View.INVISIBLE);
            binding.rcvHorrorGhosts.setVisibility(View.VISIBLE);
            GhostAdapter ghostAdapter = new GhostAdapter(requireContext(),getListHorrorGhosts(),this::goToGhostDetail);
            binding.rcvHorrorGhosts.setAdapter(ghostAdapter);
        }



    }

    private void goToGhostDetail(Ghost ghost) {
        EventTracking.logEvent(requireContext(),"collection_ghost_click");
        Intent intent = new Intent(getContext(), GhostDetailActivity.class);
        intent.putExtra(SPUtils.GHOST_ID,ghost.getId());
        startActivity(intent);
    }


    private List<Ghost> getListHorrorGhosts() {
        List<Ghost> horror = new ArrayList<>();
        for (Ghost ghost : AppDatabase.getInstance(requireContext()).ghostDAO().getAllGhost()){
            if (ghost.isHorror()){
                if (ghost.isCaptured()){
                    horror.add(ghost);
                }
            }
        }
        return horror;
    }

    @Override
    public void bindView() {
        binding.clScan.setOnClickListener(view -> {
            EventTracking.logEvent(getContext(),"collection_start_scan_click");
            int ghostType = 1;
            Intent intent = new Intent(getActivity(), GhostActivity.class);
            intent.putExtra("RESTART_SCAN_WITH_GHOST_TYPE",ghostType);
            startActivity(new Intent(getActivity(), HomeActivity.class));
            requireActivity().finishAffinity();
            startActivity(intent);
        });
    }
}