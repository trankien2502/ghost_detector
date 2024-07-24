package com.ghostdetctor.ghost_detector.ui.ghost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.ui.ghost.collection.GhostDetailActivity;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityImageCaptureBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCaptureActivity extends BaseActivity<ActivityImageCaptureBinding> {
    public static Bitmap finalBitmap;
    private final String AUTHORITY = "com.ghostdetctor.ghost_detector.fileprovider";
    File photoFile;
    String oldPath;

    @Override
    public ActivityImageCaptureBinding getBinding() {
        return ActivityImageCaptureBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(R.string.result);
        showImage();
        binding.imgGhostFilter.setImageBitmap(GhostActivity.capturedGhost);
        binding.cardView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.cardView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                finalBitmap = getBitmapFromView(binding.cardView1);
                saveToCollection();
            }
        });
        //EventTracking.logEvent(this, "ghost_result_view");

    }



    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.clCaptureShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    shareImage(FileProvider.getUriForFile(ImageCaptureActivity.this, AUTHORITY, photoFile));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ImageCaptureActivity.this, "fail!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.clCaptureDetail.setOnClickListener(view -> {
            Intent intent = new Intent(ImageCaptureActivity.this, GhostDetailActivity.class);
            intent.putExtra("GHOST_ID",GhostActivity.appearedGhost);
            resultLauncher.launch(intent);
        });
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK || result.getResultCode() == RESULT_CANCELED) {
            //reLoadAds();
        }
    });
    private void saveToCollection() {
        File cachePath = new File(getExternalCacheDir(), "images");
        cachePath.mkdirs(); // tạo thư mục nếu chưa tồn tại
        photoFile = new File(cachePath, "IMG_" + GhostActivity.appearedGhost + ".png");
        try {
            FileOutputStream fOut = new FileOutputStream(photoFile);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Ghost ghost = AppDatabase.getInstance(this).ghostDAO().findByIds(GhostActivity.appearedGhost);
            ghost.setImagePath(photoFile.getAbsolutePath());
            AppDatabase.getInstance(this).ghostDAO().update(ghost);
            //Ghost ghost1 = AppDatabase.getInstance(this).ghostDAO().findByIds(GhostActivity.appearedGhost);
            //Toast.makeText(ImageCaptureActivity.this, "Ảnh đã được lưu vào bộ nhớ: " + ghost1.getImagePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    private void showImage() {
        Ghost ghost = AppDatabase.getInstance(this).ghostDAO().findByIds(GhostActivity.appearedGhost);
        Bitmap bitmap = BitmapFactory.decodeFile(ghost.getImagePath());
        binding.imgImageCapture.setImageBitmap(bitmap);
        oldPath = ghost.getImagePath();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        //EventTracking.logEvent(ImageCaptureActivity.this, "ghost_result_back_click");
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

    private void shareImage(Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //AppOpenManager.getInstance().disableAppResumeWithActivity(ImageCaptureActivity.class);
        Intent chooser = Intent.createChooser(shareIntent, "Share Image via");
        //startActivity(chooser);
        resultLauncher.launch(chooser);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            File file = new File(oldPath);
            if (file.delete())
                Log.e("ischeck", "delete");
            else Log.e("ischeck", "fail");

        } catch (Exception e) {
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}