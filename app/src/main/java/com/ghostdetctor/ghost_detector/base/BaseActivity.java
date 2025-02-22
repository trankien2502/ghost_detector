package com.ghostdetctor.ghost_detector.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewbinding.ViewBinding;

import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.database.GhostDAO;
import com.ghostdetctor.ghost_detector.ui.challenge.ChallengeHomeActivity;
import com.ghostdetctor.ghost_detector.ui.challenge.ChallengeLanscapeActivity;
import com.ghostdetctor.ghost_detector.ui.challenge.ChallengePortraitActivity;
import com.ghostdetctor.ghost_detector.ui.challenge.service.SoundService;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.ui.intro.IntroActivity;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetctor.ghost_detector.util.SystemUtil;
import com.ghostdetector.ghost_detector.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    public VB binding;

    public abstract VB getBinding();

    public abstract void initView();

    public abstract void bindView();

    //public abstract void onBack();

    Animation animation;
    AlertDialog alertDialog;
    MediaPlayer mediaPlayerBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        binding = getBinding();
        setContentView(binding.getRoot());

        animation = AnimationUtils.loadAnimation(this, R.anim.onclick);
        //make fully Android Transparent Status bar
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Thiết lập màu trong suốt cho thanh điều hướng
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                onBack();
//            }
//        });

        if (!(this instanceof IntroActivity)) {
            // Padding bottom with the value of status bar
            binding.getRoot().setPadding(
                    binding.getRoot().getPaddingLeft(),
                    binding.getRoot().getPaddingTop() + getStatusBarHeight(),
                    binding.getRoot().getPaddingRight(),
                    binding.getRoot().getPaddingBottom()
            );
        }
        hideNavigation();
        createLoadingDialog();
        initView();
        bindView();
    }
    private void createLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();

    }
//    public void showLoadingDialog(){
//        alertDialog.show();
//    }
//    public void dismissLoadingDialog(){
//        alertDialog.dismiss();
//    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void startNextActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }
    public void updateLanguageDatabase(){
        SystemUtil.setLocale(this);
        GhostDAO ghostDAO = AppDatabase.getInstance(getBaseContext()).ghostDAO();
        List<Ghost> currentListGhost,updateListGhost = new ArrayList<>();
        currentListGhost = ghostDAO.getAllGhost();

        
//        AppDatabase.getInstance(getBaseContext()).ghostDAO().deleteAllGhost();
       
        updateListGhost.add(new Ghost(1, getString(R.string.horror_name_1),getString(R.string.horror_description_1),getString(R.string.danger_7),getString(R.string.horror_die_1),R.drawable.img_border_ghost_item_1,R.drawable.img_ghost_detail_1,true));
        updateListGhost.add(new Ghost(2, getString(R.string.horror_name_2),getString(R.string.horror_alias_2),getString(R.string.horror_description_2),getString(R.string.danger_8),getString(R.string.horror_die_2),R.drawable.img_border_ghost_item_2,R.drawable.img_ghost_detail_2,true));
        updateListGhost.add(new Ghost(3, getString(R.string.horror_name_3),getString(R.string.horror_alias_3),getString(R.string.horror_description_3),getString(R.string.danger_8),getString(R.string.horror_die_3),R.drawable.img_border_ghost_item_3,R.drawable.img_ghost_detail_3,true));
        updateListGhost.add(new Ghost(4, getString(R.string.horror_name_4),getString(R.string.horror_description_4),getString(R.string.danger_7),getString(R.string.horror_die_4),R.drawable.img_border_ghost_item_4,R.drawable.img_ghost_detail_4,true));
        updateListGhost.add(new Ghost(5, getString(R.string.horror_name_5),getString(R.string.horror_alias_5),getString(R.string.horror_description_5),getString(R.string.danger_7),getString(R.string.horror_die_5),R.drawable.img_border_ghost_item_5,R.drawable.img_ghost_detail_5,true));
        updateListGhost.add(new Ghost(6, getString(R.string.horror_name_6),getString(R.string.horror_description_6),getString(R.string.danger_7),getString(R.string.horror_die_6),R.drawable.img_border_ghost_item_6,R.drawable.img_ghost_detail_6,true));
        updateListGhost.add(new Ghost(7, getString(R.string.horror_name_7),getString(R.string.horror_description_7),getString(R.string.danger_6),getString(R.string.horror_die_7),R.drawable.img_border_ghost_item_7,R.drawable.img_ghost_detail_7,true));
        updateListGhost.add(new Ghost(8, getString(R.string.horror_name_8),getString(R.string.horror_description_8),getString(R.string.danger_9),getString(R.string.horror_die_8),R.drawable.img_border_ghost_item_8,R.drawable.img_ghost_detail_8,true));
        updateListGhost.add(new Ghost(9, getString(R.string.horror_name_9),getString(R.string.horror_description_9),getString(R.string.danger_9),getString(R.string.horror_die_9),R.drawable.img_border_ghost_item_9,R.drawable.img_ghost_detail_9,true));
        updateListGhost.add(new Ghost(10, getString(R.string.horror_name_10),getString(R.string.horror_description_10),getString(R.string.danger_8),getString(R.string.horror_die_10),R.drawable.img_border_ghost_item_10,R.drawable.img_ghost_detail_10,true));
        updateListGhost.add(new Ghost(11, getString(R.string.scary_name_1),getString(R.string.scary_description_1),getString(R.string.danger_2),getString(R.string.scary_die_1),R.drawable.img_border_ghost_item_11,R.drawable.img_ghost_detail_11,false));
        updateListGhost.add(new Ghost(12, getString(R.string.scary_name_2),getString(R.string.scary_description_2),getString(R.string.danger_0),getString(R.string.scary_die_2),R.drawable.img_border_ghost_item_12,R.drawable.img_ghost_detail_12,false));
        updateListGhost.add(new Ghost(13, getString(R.string.scary_name_3),getString(R.string.scary_description_3),getString(R.string.danger_1),getString(R.string.scary_die_3),R.drawable.img_border_ghost_item_13,R.drawable.img_ghost_detail_13,false));
        updateListGhost.add(new Ghost(14, getString(R.string.scary_name_4),getString(R.string.scary_description_4),getString(R.string.danger_0),getString(R.string.scary_die_4),R.drawable.img_border_ghost_item_14,R.drawable.img_ghost_detail_14,false));
        updateListGhost.add(new Ghost(15, getString(R.string.scary_name_5),getString(R.string.scary_description_5),getString(R.string.danger_1),getString(R.string.scary_die_5),R.drawable.img_border_ghost_item_15,R.drawable.img_ghost_detail_15,false));
        updateListGhost.add(new Ghost(16, getString(R.string.scary_name_6),getString(R.string.scary_description_6),getString(R.string.danger_2),getString(R.string.scary_die_6),R.drawable.img_border_ghost_item_16,R.drawable.img_ghost_detail_16,false));
        updateListGhost.add(new Ghost(17, getString(R.string.scary_name_7),getString(R.string.scary_description_7),getString(R.string.danger_0),getString(R.string.scary_die_7),R.drawable.img_border_ghost_item_17,R.drawable.img_ghost_detail_17,false));
        updateListGhost.add(new Ghost(18, getString(R.string.scary_name_8),getString(R.string.scary_description_8),getString(R.string.danger_1),getString(R.string.scary_die_8),R.drawable.img_border_ghost_item_18,R.drawable.img_ghost_detail_18,false));
        updateListGhost.add(new Ghost(19, getString(R.string.scary_name_9),getString(R.string.scary_description_9),getString(R.string.danger_0),getString(R.string.scary_die_9),R.drawable.img_border_ghost_item_19,R.drawable.img_ghost_detail_19,false));
        updateListGhost.add(new Ghost(20, getString(R.string.scary_name_10),getString(R.string.scary_description_10),getString(R.string.danger_0),getString(R.string.scary_die_10),R.drawable.img_border_ghost_item_20,R.drawable.img_ghost_detail_20,false));

        for (Ghost ghost : updateListGhost){
            Ghost currentGhost = ghostDAO.findByIds(ghost.getId());
            if (currentGhost.isCaptured()){
                ghost.setCaptured(true);
            }
            currentGhost = ghost;
            ghostDAO.update(currentGhost);
        }
    }

    public void animateTextView(TextView textView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 1.15f);
        scaleX.setDuration(500);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 1.15f);
        scaleY.setDuration(500);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(textView, "scaleX", 1f);
        scaleXBack.setDuration(500);
        scaleXBack.setRepeatCount(ValueAnimator.INFINITE);
        scaleXBack.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(textView, "scaleY", 1f);
        scaleYBack.setDuration(500);
        scaleYBack.setRepeatCount(ValueAnimator.INFINITE);
        scaleYBack.setRepeatMode(ValueAnimator.REVERSE);
        // Create an AnimatorSet to play the animations sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.play(scaleXBack).with(scaleYBack).after(scaleX);

        animatorSet.start();
    }
//    public void stopBackgroundSound() {
//        if (mediaPlayerBackground != null) {
//            SPUtils.setInt(getBaseContext(),SPUtils.SOUND_POSITION,mediaPlayerBackground.getCurrentPosition());
//            mediaPlayerBackground.release();
//            mediaPlayerBackground = null;
//        }
//    }
//    public void playBackgroundSound() {
//        if (mediaPlayerBackground != null) {
//            mediaPlayerBackground.release();
//        }
//        mediaPlayerBackground = MediaPlayer.create(this, R.raw.background_sound);
//        int position = SPUtils.getInt(this,SPUtils.SOUND_POSITION,0);
//        mediaPlayerBackground.seekTo(position);
//        mediaPlayerBackground.start();
//        mediaPlayerBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.seekTo(0);
//                mediaPlayer.start();
//            }
//        });
//    }
    public void destroyService(){
        Intent intent = new Intent(this, SoundService.class);
        stopService(intent);
        Log.e("challengeCheck","destroy music");
    }
    public void stopBackgroundSound() {
        Intent intent = new Intent(this, SoundService.class);
        intent.setAction("STOP_SOUND");
        startService(intent);
        Log.e("challengeCheck","stop music");

    }
    public void playBackgroundSound() {
        Intent intent = new Intent(this, SoundService.class);
        intent.setAction("START_SOUND");
        startService(intent);
        Log.e("challengeCheck","start music");
    }
    public boolean checkChallengeExist(){
        return ChallengeHomeActivity.isChallengeHome || ChallengePortraitActivity.isChallengePortrait || ChallengeLanscapeActivity.isChallengeLandscape;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //tắt ads resume all
//        if (RemoteConfig.remote_resume) {
//            AppOpenManager.getInstance().enableAppResumeWithActivity(getClass());
//        } else {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
//        }
    }

    public void finishThisActivity() {
        finish();
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    public void hideStatusBarAndNavigation(){
        WindowInsetsControllerCompat windowInsetsController;
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        } else {
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
        }

        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
    }

    public void hideNavigation() {
        WindowInsetsControllerCompat windowInsetsController;
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        } else {
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
        }

        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(i -> {
            if (i == 0) {
                new Handler().postDelayed(() -> {
                    WindowInsetsControllerCompat windowInsetsController1;
                    if (Build.VERSION.SDK_INT >= 30) {
                        windowInsetsController1 = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
                    } else {
                        windowInsetsController1 = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
                    }
                    Objects.requireNonNull(windowInsetsController1).hide(WindowInsetsCompat.Type.navigationBars());
                }, 3000);
            }
        });
    }

}
