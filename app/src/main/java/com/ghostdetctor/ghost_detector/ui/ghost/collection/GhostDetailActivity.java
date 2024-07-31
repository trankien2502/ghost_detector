package com.ghostdetctor.ghost_detector.ui.ghost.collection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.ui.ghost.GhostActivity;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityGhostDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GhostDetailActivity extends BaseActivity<ActivityGhostDetailBinding> {
    int idGhost;
    Ghost ghost;

    @Override
    public ActivityGhostDetailBinding getBinding() {
        return ActivityGhostDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"ghost_detail_view");
        idGhost = getIntent().getIntExtra(SPUtils.GHOST_ID,0);
        ghost = AppDatabase.getInstance(this).ghostDAO().findByIds(idGhost);
        if (idGhost==0 || ghost == null){
            Toast.makeText(this, getString(R.string.load_ghost_detail_failed), Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            binding.imgImageCapture.setImageResource(ghost.getGhostDetailImage());
            binding.tvName.setText(ghost.getName());
            if (ghost.getAlias()!=null){
                binding.tvAlias.setText(ghost.getAlias());
                binding.tvAlias.setVisibility(View.VISIBLE);
            }
            else binding.tvAlias.setVisibility(View.GONE);
            binding.tvDie.setText(ghost.getDie());
            binding.tvDanger.setText(ghost.getDanger());
            binding.tvDescription.setText(ghost.getDescription());
            binding.header.tvTitle.setText(R.string.ghost_detail);
        }

        binding.header.tvTitle.setText(R.string.ghost_detail);
        //EventTracking.logEvent(this, "ghost_result_view");
    }



    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.header.ivScan.setOnClickListener(view -> {
            EventTracking.logEvent(this,"ghost_detail_scan_click");
            int ghostType;
            if (ghost.getId()<=10) ghostType = 1;
            else ghostType = 2;
            //ghostType = SPUtils.getInt(this,SPUtils.GHOST_TYPE,0);
            Intent intent = new Intent(this, GhostActivity.class);
            intent.putExtra("RESTART_SCAN_WITH_GHOST_TYPE",ghostType);
            startNextActivity(HomeActivity.class,null);
            finishAffinity();
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        EventTracking.logEvent(this, "ghost_detail_back_click");
        finish();
    }


//    private void loadInterGhostCapture() {
//        new Thread(() -> {
//            if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.mInterGhostCaptureBack == null && !ConstantIdAds.listIDAdsInterGhostCaptureBack.isEmpty() && ConstantRemote.inter_ghost_capture_back) {
//                runOnUiThread(() -> ConstantIdAds.mInterGhostCaptureBack = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterGhostCaptureBack));
//            }
//        }).start();
//    }

//    private void reLoadAds() {
//        if (IsNetWork.haveNetworkConnection(this)) {
//            binding.nativeGhostCapture.setVisibility(View.VISIBLE);
//            @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_loading, null, false);
//            binding.nativeGhostCapture.removeAllViews();
//            binding.nativeGhostCapture.addView(adViewLoad);
//            loadAds();
//        } else {
//            binding.nativeGhostCapture.setVisibility(View.INVISIBLE);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}