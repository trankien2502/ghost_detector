package com.ghostdetctor.ghost_detector.ui.ghost;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.LifecycleOwner;


import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.database.GhostDAO;
import com.ghostdetctor.ghost_detector.ui.ghost.collection.CollectionActivity;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.ui.ghost.story.ScaryStoryActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityGhostBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class GhostActivity extends BaseActivity<ActivityGhostBinding> {
    private final int TYPE_HORROR_GHOSTS = 1;
    private final int TYPE_SCARY_SPIRITS = 2;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ProcessCameraProvider cameraProvider;
    Preview preview;
    CameraSelector cameraSelector;
    private final Handler handler = new Handler();
    Handler ghostHandler = new Handler();
    private int degrees = 0;
    public static Bitmap capturedBitmap, capturedGhost;
    ImageCapture imageCapture;
    Runnable ghostAppearRunable, ghostLeaveRunnable;
    ImageView[] evp = null;
    int[] evpLevel = null;
    int[] ghost = null;
    ImageView[] ghostImage = null;
    ImageView[] pointSign = null;
    boolean isGhostAppeared = false;
    int timeToSee = 30;
    public static int appearedGhost;
    public static int appearedGhostImage;
    boolean isLightSign = false, isSoundOn = true, isScanGhost = false, isCameraBack = true;
    boolean isStartGetGhost, isActivityGhost;
    MediaPlayer mediaPlayerBackground, mediaPlayerGhost;
    int width,height;
    int ghostType;
    GhostDAO ghostDAO;

    @Override
    public ActivityGhostBinding getBinding() {
        return ActivityGhostBinding.inflate(getLayoutInflater());
    }
    @Override
    public void initView() {
        EventTracking.logEvent(this,"scanning_view");
        int typeGhost = getIntent().getIntExtra("RESTART_SCAN_WITH_GHOST_TYPE",0);
        if (typeGhost!=0) SPUtils.setInt(this,SPUtils.GHOST_TYPE,typeGhost);
        binding.previewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = binding.cardView2.getWidth();
                height = binding.cardView2.getHeight();
                //startCameraBack();
            }
        });
        ghostDAO = AppDatabase.getInstance(this).ghostDAO();
        //EventTracking.logEvent(this, "ghost_view");
        //loadBanner();
        isStartGetGhost = true;
        isActivityGhost = true;
        isCameraBack = true;
        isSoundOn = SPUtils.getBoolean(this,SPUtils.SOUND_GHOST_SCAN,true);
        isScanGhost = false;
        initData();
        initUI();
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        Log.e("isCheck", "ghostType"+SPUtils.getInt(this,SPUtils.GHOST_TYPE,0));
    }
    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.ivCamera.setOnClickListener(view -> {
            if (!isScanGhost){
                Toast.makeText(this, getString(R.string.tap_the_radar_to_scan_first), Toast.LENGTH_SHORT).show();
                return;
            }
            if (isCameraBack){
                isCameraBack = false;
                startCameraFront();
            } else {
                isCameraBack = true;
                startCameraBack();
            }
        });
        binding.ivSound.setOnClickListener(view -> {
            if (!isScanGhost){
                EventTracking.logEvent(this,"scanning_sound_click");
                Toast.makeText(this, getString(R.string.tap_the_radar_to_scan_first), Toast.LENGTH_SHORT).show();
                return;
            }
            if (isSoundOn){
                EventTracking.logEvent(this,"scanning_sound_click","","OFF");
                isSoundOn = false;
                binding.ivSound.setImageResource(R.drawable.img_ghost_sound_off);
                stopBackgroundGhostSound();
                stopGhostAppearSound();
                SPUtils.setBoolean(this,SPUtils.SOUND_GHOST_SCAN,isSoundOn);
            }
            else {
                EventTracking.logEvent(this,"scanning_sound_click","","ON");
                isSoundOn = true;
                binding.ivSound.setImageResource(R.drawable.img_ghost_sound_on);
                SPUtils.setBoolean(this,SPUtils.SOUND_GHOST_SCAN,isSoundOn);
                if (isScanGhost){
                    playBackgroundGhostSound();
                    playGhostAppearSound();
                }
            }
        });
        binding.layoutRadarScan.setOnClickListener(view -> {
            EventTracking.logEvent(this,"scanning_scan_click");
            if (!isScanGhost){
                isScanGhost = true;
                binding.amClick.setVisibility(View.GONE);
                startCameraBack();
                binding.tvGhostScan.setVisibility(View.GONE);
                binding.ivStartScanGhost.setVisibility(View.GONE);
                binding.imgRadarScan.setVisibility(View.VISIBLE);
                startSecondHandAnimation();
                getGhost();
                if (isSoundOn) playBackgroundGhostSound();
            }
        });
        binding.clGhostType.setOnClickListener(view -> {
            EventTracking.logEvent(this,"scanning_type_click");
            startNextActivity(TypeGhostActivity.class,null);
        });
        binding.clCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventTracking.logEvent(GhostActivity.this, "scanning_collection_click");
                resultLauncher.launch(new Intent(GhostActivity.this, CollectionActivity.class));
            }
        });
        binding.clScaryStory.setOnClickListener(view -> {
            EventTracking.logEvent(GhostActivity.this, "scanning_stories_click");
            resultLauncher.launch(new Intent(GhostActivity.this, ScaryStoryActivity.class));
        });
    }
//    private void loadBanner() {
//        new Thread(() -> runOnUiThread(() -> {
//            if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner) {
//                CommonAd.getInstance().loadCollapsibleBannerFloor(this, ConstantIdAds.listIDAdsBanner, "bottom");
//                findViewById(R.id.banner).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.banner).setVisibility(View.GONE);
//            }
//        })).start();
//    }


    private void initUI() {
        SharePrefUtils.increaseCountOpenGhost(this);
        if (SharePrefUtils.getCountOpenGhost(this)==1){
            binding.tvGhostScan.setVisibility(View.VISIBLE);
            animateTextView(binding.tvGhostScan);
            binding.amClick.setVisibility(View.VISIBLE);
        }
        else {
            binding.tvGhostScan.setVisibility(View.GONE);
            binding.amClick.setVisibility(View.GONE);
        }
        animateTextView(binding.tvLoading);
        binding.header.tvTitle.setText(R.string.horror_ghost);
        if (isSoundOn){
            binding.ivSound.setImageResource(R.drawable.img_ghost_sound_on);
        } else {
            binding.ivSound.setImageResource(R.drawable.img_ghost_sound_off);
        }
        showEvpLevel();
    }
    private void startCameraBack() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cameraProvider != null) {
                        cameraProvider.unbindAll();
                    }
                    cameraProvider = cameraProviderFuture.get();
                    preview = new Preview.Builder().build();
                    cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();
                    preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
                    //bindPreviewBack();
                    bindPreViewToCaptureImage();
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private void startCameraFront() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cameraProvider != null) {
                        cameraProvider.unbindAll();
                    }
                    cameraProvider = cameraProviderFuture.get();
                    preview = new Preview.Builder().build();
                    cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                            .build();
                    preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
                    //bindPreviewBack();
                    bindPreViewToCaptureImage();
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreViewToCaptureImage() {
        cameraProvider.unbindAll();
        imageCapture = new ImageCapture.Builder().setTargetResolution(new Size(width,height)).build();
        cameraProvider.bindToLifecycle((LifecycleOwner) GhostActivity.this, cameraSelector, preview, imageCapture);
        //binding.vScreen.setVisibility(View.GONE);
    }
    private void randomToSeeGhost() {
        Random random = new Random();
        boolean isSoonToSee = random.nextBoolean();
        int timeRandomSee = random.nextInt(6);
        timeToSee = 20;
        if (isSoonToSee) timeToSee -= timeRandomSee;
        else timeToSee += timeRandomSee;
    }
    private void getGhost() {
        randomToSeeGhost();
        Random random = new Random();
        appearedGhostImage = random.nextInt(7);
        ghostAppearRunable = new Runnable() {
            @Override
            public void run() {
                if (isStartGetGhost) {
                    timeToSee--;
                    if (timeToSee > 0) {
                        ghostHandler.postDelayed(this, 1000);
                        if (timeToSee==2) {
                            isGhostAppeared = true;
                            pointSign[appearedGhostImage].setVisibility(View.VISIBLE);
                        }
                    } else {
                        ghostHandler.removeCallbacks(this);
                        ghostAppear();
                    }
                } else {
                    ghostHandler.postDelayed(this, 1000);
                }
                Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost + " " + timeToSee);
            }
        };
        ghostHandler.postDelayed(ghostAppearRunable, 1000);

    }
    private void ghostAppear() {
        Log.d("isCheckGhost", "isGhostAppeared: " + isGhostAppeared);
        Random random = new Random();
        ghostType = SPUtils.getInt(this,SPUtils.GHOST_TYPE,TYPE_SCARY_SPIRITS);
        if (ghostType != TYPE_SCARY_SPIRITS){
            appearedGhost = 1 + random.nextInt(10);
        } else {
            appearedGhost = 11 +  random.nextInt(10);
        }
        Log.d("isCheckGhost", "appearedGhost: " + appearedGhost + " ");
        //appearedGhost = random.nextInt(10);
        if (isSoundOn) playGhostAppearSound();
        ghostImage[appearedGhostImage].setImageResource(ghost[appearedGhost]);
        ghostImage[appearedGhostImage].setVisibility(View.VISIBLE);

        capturePictureGhost();

    }

    private void capturePictureGhost() {
        showLoadingDialog();
        isStartGetGhost = false;
        isActivityGhost = false;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
        if (imageCapture == null) {
            Toast.makeText(GhostActivity.this, getString(R.string.camera_is_not_available), Toast.LENGTH_SHORT).show();
            dismissLoadingDialog();
            return;
        }
        if (imageCapture != null) {
            File cachePath = getExternalCacheDir();
            if (cachePath != null) {
                File imageDir = new File(cachePath, "images");
                if (!imageDir.exists()) {
                    imageDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }
                File photoFile = new File(imageDir, "IMG_" + appearedGhost + ".jpg");
                ImageCapture.OutputFileOptions outputFileOptions =
                        new ImageCapture.OutputFileOptions.Builder(photoFile).build();

                imageCapture.takePicture(outputFileOptions,
                        ContextCompat.getMainExecutor(this),
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                try {
                                    ExifInterface exif = new ExifInterface(photoFile.getAbsolutePath());
                                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                    int rotationInDegrees = exifToDegrees(rotation);
                                    if (rotationInDegrees != 0) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                                        bitmap = rotateBitmap(bitmap, rotationInDegrees);
                                        FileOutputStream out = new FileOutputStream(photoFile);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                capturedGhost = getImageLayout(binding.layoutGetImage);
                                Ghost ghost2 = AppDatabase.getInstance(GhostActivity.this).ghostDAO().findByIds(appearedGhost);
                                if (ghost2 != null) {
                                    ghost2.setImagePath(photoFile.getAbsolutePath());
                                    AppDatabase.getInstance(GhostActivity.this).ghostDAO().update(ghost2);
                                } else {
                                    Log.e("GhostActivity", "Ghost not found in database for ID: " + appearedGhost);
                                }
                                resultLauncher.launch(new Intent(GhostActivity.this, ImageCaptureActivity.class));
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                dismissLoadingDialog();
                                Log.e("GhostActivity", "Image capture failed", exception);
                                Toast.makeText(GhostActivity.this, getString(R.string.capture_image_failed), Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Log.e("GhostActivity", "External cache directory is null");
                Toast.makeText(GhostActivity.this, getString(R.string.external_directory_is_not_available), Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
            }
        }
    }
    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void showLoadingDialog() {
        binding.clLoading.setVisibility(View.VISIBLE);
    }

    private void checkToBackGhostActivity() {
        isStartGetGhost = true;
        if (isSoundOn && isScanGhost) playBackgroundGhostSound();
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
    }
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            isActivityGhost = true;
            isGhostAppeared = false;
            Log.d("isCheckGhost", "isGhostAppeared: " + isGhostAppeared);
            stopGhostAppearSound();
            getGhost();
            dismissLoadingDialog();
            Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
            checkToBackGhostActivity();
        }
    });
    private void dismissLoadingDialog(){
        binding.clLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.setInt(this,SPUtils.GHOST_TYPE,0);
        Log.e("isCheck", "ghostType"+SPUtils.getInt(this,SPUtils.GHOST_TYPE,0));
        Log.d("isCheckGhost", "onDestroy: ");
        ghostHandler.removeCallbacks(ghostAppearRunable);
        ghostHandler.removeCallbacks(ghostLeaveRunnable);
        stopBackgroundGhostSound();
        stopGhostAppearSound();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        isStartGetGhost = false;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        Log.d("isCheckGhost", "onStop: ");
        stopBackgroundGhostSound();
        stopGhostAppearSound();
    }
    @Override
    protected void onPause() {
        super.onPause();
        isStartGetGhost = false;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        Log.d("isCheckGhost", "onPause: ");
        stopBackgroundGhostSound();
        stopGhostAppearSound();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkToBackGhostActivity();
        Log.d("isCheckGhost", "onResume: ");
    }
    @Override
    public void onBackPressed() {
        EventTracking.logEvent(this,"scanning_back_click");
        setResult(RESULT_OK);
        finish();
    }

    private void showEvpLevel() {
        Handler handler1 = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!isScanGhost){
                    for (int i=0;i<40;i++){
                        evp[i].setImageResource(evpLevel[0]);
                    }
                } else {
                    Random randomEvp = new Random();
                    if (!isGhostAppeared) {
                        for (int i = 0; i < 7; i++) ghostImage[i].setVisibility(View.GONE);
                        for (int i = 0; i < 7; i++) pointSign[i].setVisibility(View.GONE);
                        binding.layoutPointSign.setVisibility(View.GONE);
                        Random randomLine = new Random();
                        int line = randomLine.nextInt(5);
                        if (line == 1 || line == 3) {
                            for (int i = 0; i < 40; i++) {
                                if (i <= 7 || i >= 32) {
                                    evp[i].setImageResource(evpLevel[0]);
                                } else {
                                    int x = 1 + randomEvp.nextInt(3);
                                    evp[i].setImageResource(evpLevel[x]);
                                }
                            }
                        } else {
                            for (int i = 0; i < 40; i++) {
                                if (i < 3 || i > 37 || i > 8 && i < 14 || i > 27 && i < 33) {
                                    evp[i].setImageResource(evpLevel[0]);
                                } else {
                                    if(i<=8||i>=27){
                                        int x = 1 + randomEvp.nextInt(4);
                                        evp[i].setImageResource(evpLevel[x]);
                                    }else {
                                        int x = 2 + randomEvp.nextInt(5);
                                        evp[i].setImageResource(evpLevel[x]);
                                    }
                                }
                            }
                        }
                    } else {
                        if (isLightSign) {
                            binding.layoutPointSign.setVisibility(View.VISIBLE);
                        } else {
                            binding.layoutPointSign.setVisibility(View.GONE);
                        }
                        isLightSign = !isLightSign;
                        for (int i = 0; i < 40; i++) {
                            if (i <= 5 || i >= 33) {
                                int x = randomEvp.nextInt(2);
                                evp[i].setImageResource(evpLevel[x]);
                            } else if (i <= 7 || i >= 30) {
                                int x = 2 + randomEvp.nextInt(3);
                                evp[i].setImageResource(evpLevel[x]);
                            } else if (i <= 16 || i >= 26) {
                                int x = 5 + randomEvp.nextInt(3);
                                evp[i].setImageResource(evpLevel[x]);
                            } else {
                                int x = 9 + randomEvp.nextInt(3);
                                evp[i].setImageResource(evpLevel[x]);
                            }
                        }
                    }
                }
                handler1.postDelayed(this, 250);
            }
        };
        handler1.postDelayed(runnable, 250);
    }


    private void startSecondHandAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotate = new RotateAnimation(degrees, degrees + 15,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new LinearInterpolator());
                binding.imgRadarScan.startAnimation(rotate);

                degrees += 15;
                if (degrees == 360) {
                    degrees = 0;
                }

                handler.postDelayed(this, 100);
            }
        }, 100);
    }
    private void playBackgroundGhostSound() {
        if (mediaPlayerBackground != null) {
            mediaPlayerBackground.release();
        }
        mediaPlayerBackground = MediaPlayer.create(this, R.raw.background_sound);
        mediaPlayerBackground.start();
        mediaPlayerBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });
    }
    private void playGhostAppearSound() {
        if (mediaPlayerGhost != null) {
            mediaPlayerGhost.release();
        }
        mediaPlayerGhost = MediaPlayer.create(this, R.raw.ghost_appear_sound);
        if (isGhostAppeared)
            mediaPlayerGhost.start();
        mediaPlayerGhost.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isGhostAppeared) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }

            }
        });

    }
    private void stopGhostAppearSound() {
        if (mediaPlayerGhost != null) {
            mediaPlayerGhost.release();
            mediaPlayerGhost = null;
        }
    }
    private void stopBackgroundGhostSound() {
        if (mediaPlayerBackground != null) {
            mediaPlayerBackground.release();
            mediaPlayerBackground = null;
        }
    }
    private void initData() {
        evp = new ImageView[]{
                binding.evp0, binding.evp1, binding.evp2, binding.evp3, binding.evp4, binding.evp5,
                binding.evp6, binding.evp7, binding.evp8, binding.evp9, binding.evp10, binding.evp11,
                binding.evp12, binding.evp13, binding.evp14, binding.evp15, binding.evp16, binding.evp17,
                binding.evp18, binding.evp19, binding.evp20, binding.evp21, binding.evp22, binding.evp23,
                binding.evp24, binding.evp25, binding.evp26, binding.evp27, binding.evp28, binding.evp29,
                binding.evp30, binding.evp31, binding.evp32, binding.evp33, binding.evp34, binding.evp35,
                binding.evp36, binding.evp37, binding.evp38, binding.evp39
        };
        evpLevel = new int[]{
                R.drawable.img_evp_0, R.drawable.img_evp_1, R.drawable.img_evp_2, R.drawable.img_evp_3, R.drawable.img_evp_4,
                R.drawable.img_evp_5, R.drawable.img_evp_6, R.drawable.img_evp_7, R.drawable.img_evp_8, R.drawable.img_evp_9,
                R.drawable.img_evp_10, R.drawable.img_evp_11
        };
        ghost = new int[]{
                R.drawable.img_ghost_1, R.drawable.img_ghost_1, R.drawable.img_ghost_2,
                R.drawable.img_ghost_3, R.drawable.img_ghost_4,
                R.drawable.img_ghost_5, R.drawable.img_ghost_6, R.drawable.img_ghost_7, R.drawable.img_ghost_8, R.drawable.img_ghost_9,
                R.drawable.img_ghost_10, R.drawable.img_ghost_11, R.drawable.img_ghost_12,
                R.drawable.img_ghost_13, R.drawable.img_ghost_14,
                R.drawable.img_ghost_15, R.drawable.img_ghost_16, R.drawable.img_ghost_17, R.drawable.img_ghost_18, R.drawable.img_ghost_19, R.drawable.img_ghost_20
        };
        ghostImage = new ImageView[]{
                binding.imgGhost11, binding.imgGhost22, binding.imgGhost33,
                binding.imgGhost44, binding.imgGhost55, binding.imgGhost4,
                binding.imgGhost5
        };
        pointSign = new ImageView[]{
                binding.imgPointSign1, binding.imgPointSign7, binding.imgPointSign3,
                binding.imgPointSign4, binding.imgPointSign5, binding.imgPointSign6,
                binding.imgPointSign2
        };
    }
    private Bitmap rotateBitmap(Bitmap source, int rotationAngle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private Bitmap imageToBitmap(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public Bitmap getImageLayout(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public Bitmap getViewGhostInsideLayout(View layoutGhost, View ghost) {
        Bitmap bitmap = Bitmap.createBitmap(layoutGhost.getWidth(), layoutGhost.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layoutGhost.draw(canvas);
        int[] location = new int[2];
        ghost.getLocationInWindow(location);
        canvas.translate(-location[0], -location[1]);
        ghost.draw(canvas);
        return bitmap;
    }
    public Bitmap combineBitmaps(Bitmap bitmapCamera, Bitmap bitmapGhost) {
        Bitmap combinedBitmap = Bitmap.createBitmap(bitmapCamera.getWidth(), bitmapCamera.getHeight(), bitmapCamera.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(bitmapCamera, 0, 0, null);
        canvas.drawBitmap(bitmapGhost, 0, 0, null);

        return combinedBitmap;
    }
    private void x(){
        isStartGetGhost = false;
        isActivityGhost = false;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
        if (imageCapture == null) {
            Toast.makeText(GhostActivity.this, "ImageCapture fail!", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadingDialog();
        try{
            imageCapture.takePicture(ContextCompat.getMainExecutor(GhostActivity.this),
                    new ImageCapture.OnImageCapturedCallback() {
                        @Override
                        public void   onCaptureSuccess(@NonNull ImageProxy image) {
                            Log.e("CameraXApp", "Error capturing image1");
                            @ExperimentalGetImage Image media = image.getImage();
                            Log.e("CameraXApp", "Error capturing image2");
                            @OptIn(markerClass = ExperimentalGetImage.class) Bitmap bitmap = imageToBitmap(media);
                            Log.e("CameraXApp", "Error capturing image3");
                            int rotationDegrees = image.getImageInfo().getRotationDegrees();
                            if (rotationDegrees != 0) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(rotationDegrees);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            }
                            Log.e("CameraXApp", "Error capturing image4");
                            image.close();
                            Log.e("CameraXApp", "Error capturing image5");
                            capturedGhost = getImageLayout(binding.layoutGetImage);
                            capturedBitmap = bitmap;
                            //AppOpenManager.getInstance().disableAppResumeWithActivity(GhostActivity.class);
                            resultLauncher.launch(new Intent(GhostActivity.this, ImageCaptureActivity.class));
                            dismissLoadingDialog();
                            Log.e("CameraXApp", "Error capturing image8" + width+" " + height );
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            dismissLoadingDialog();
                            Log.e("CameraXApp", "Error capturing image6" + exception);
                            Toast.makeText(GhostActivity.this, "Error capturing image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("CameraXApp", "Error capturing image7" + e);
            Toast.makeText(GhostActivity.this, "Camera is not available!", Toast.LENGTH_SHORT).show();
            isStartGetGhost = true;
            isActivityGhost = true;
            Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
            Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
            dismissLoadingDialog();
        }

    }


}