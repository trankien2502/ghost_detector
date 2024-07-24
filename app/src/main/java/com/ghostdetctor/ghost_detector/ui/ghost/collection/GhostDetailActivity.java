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
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityGhostDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GhostDetailActivity extends BaseActivity<ActivityGhostDetailBinding> {
    public static Bitmap finalBitmap;
    private final String AUTHORITY = "com.ghostdetctor.ghost_detector.fileprovider";
    File photoFile;
    int idGhost;

    @Override
    public ActivityGhostDetailBinding getBinding() {
        return ActivityGhostDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        idGhost = getIntent().getIntExtra(SPUtils.GHOST_ID,0);
        Ghost ghost = AppDatabase.getInstance(this).ghostDAO().findByIds(idGhost);
        if (idGhost==0 || ghost == null){
            onBackPressed();
        }
        assert ghost != null;
        photoFile = new File(ghost.getImagePath());
        if (!photoFile.exists()) onBackPressed();
        else {
            Bitmap bitmap = BitmapFactory.decodeFile(ghost.getImagePath());
            binding.imgImageCapture.setImageBitmap(bitmap);
            binding.tvName.setText(ghost.getName());
            if (ghost.getAlias()!=null){
                binding.tvAlias.setText(ghost.getAlias());
                binding.tvAlias.setVisibility(View.VISIBLE);
            }
            else binding.tvAlias.setVisibility(View.GONE);
            binding.tvDie.setText(ghost.getDie());
            binding.tvDanger.setText(ghost.getDanger());
            binding.tvDescription.setText(ghost.getDescription());
        }
        binding.header.tvTitle.setText(R.string.ghost_detail);
//        binding.cardView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                binding.cardView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                finalBitmap = getBitmapFromView(binding.cardView1);
//                saveToCollection();
//            }
//        });

//        ghostImage[GhostActivity.appearedGhostImage].setImageResource(ghost[GhostActivity.appearedGhost]);
//        ghostImage[GhostActivity.appearedGhostImage].setVisibility(View.VISIBLE);
        //EventTracking.logEvent(this, "ghost_result_view");
        //saveToCollection();
    }



    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EventTracking.logEvent(ImageCaptureActivity.this, "ghost_result_share_click");
//                finalBitmap = getBitmapFromView(binding.cardView1);
//                Bitmap bitmap = getBitmapFromView(binding.cardView1);
//                try {
//                    File cachePath = new File(getExternalCacheDir(), "images");
//                    cachePath.mkdirs(); // tạo thư mục nếu chưa tồn tại
//                    File file = new File(cachePath, "shared_image.png");
//                    FileOutputStream fOut = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//                    fOut.flush();
//                    fOut.close();
//                    shareImage(FileProvider.getUriForFile(ImageCaptureActivity.this, AUTHORITY, file));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                try {
                    shareImage(FileProvider.getUriForFile(GhostDetailActivity.this, AUTHORITY, photoFile));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GhostDetailActivity.this, "fail!", Toast.LENGTH_SHORT).show();
                }
            }
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

            Ghost ghost1 = AppDatabase.getInstance(this).ghostDAO().findByIds(GhostActivity.appearedGhost);
            Toast.makeText(GhostDetailActivity.this, "Ảnh đã được lưu vào bộ nhớ: " + ghost1.getImagePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    private void showImage() {
        Ghost ghost = AppDatabase.getInstance(this).ghostDAO().findByIds(GhostActivity.appearedGhost);
        Bitmap bitmap = BitmapFactory.decodeFile(ghost.getImagePath());
        binding.imgImageCapture.setImageBitmap(bitmap);
        binding.tvName.setText(ghost.getName());
        if (ghost.getAlias()!=null){
            binding.tvAlias.setText(ghost.getAlias());
            binding.tvAlias.setVisibility(View.VISIBLE);
        }
        else binding.tvAlias.setVisibility(View.GONE);
        binding.tvDie.setText(ghost.getDie());
        binding.tvDanger.setText(ghost.getDanger());
        binding.tvDescription.setText(ghost.getDescription());
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

//    private void initData() {
//        ghostImage = new ImageView[]{
//                binding.imgGhost11, binding.imgGhost22, binding.imgGhost33,
//                binding.imgGhost44, binding.imgGhost55, binding.imgGhost4,
//                binding.imgGhost5
//        };
//        ghost = new int[]{
//                R.drawable.img_ghost_11, R.drawable.img_ghost_22, R.drawable.img_ghost_33, R.drawable.img_ghost_44, R.drawable.img_ghost_55,
//                R.drawable.img_ghost_1, R.drawable.img_ghost_2, R.drawable.img_ghost_3, R.drawable.img_ghost_4, R.drawable.img_ghost_5
//        };
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}