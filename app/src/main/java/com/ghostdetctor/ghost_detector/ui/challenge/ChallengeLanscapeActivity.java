package com.ghostdetctor.ghost_detector.ui.challenge;

import static android.view.View.GONE;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityChallengeLanscapeBinding;

import java.util.Random;

public class ChallengeLanscapeActivity extends BaseActivity<ActivityChallengeLanscapeBinding> {
    private boolean isSoundOn;
    MediaPlayer mediaPlayerBackground;
    Handler handler = new Handler();
    private int degrees = 0;
    int plusDegree = 60;
    int timeStop, timeEnd;
    Runnable runnableRotatePencil;
    private ObjectAnimator scaleDown;
    private boolean isPencilRolate;

    @Override
    public ActivityChallengeLanscapeBinding getBinding() {
        return ActivityChallengeLanscapeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        isSoundOn = SPUtils.getBoolean(this,SPUtils.SOUND_CHALLENGE,true);
        if (isSoundOn){
            binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_on);
            playBackgroundSound();
        } else {
            binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_off);
            stopBackgroundSound();
        }
        isPencilRolate = false;
        createAnimationTalking();
    }

    @Override
    public void bindView() {
        binding.ivBackChallenge.setOnClickListener(view -> onBackPressed());
        binding.ivStoryChallenge.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(ChallengeLanscapeActivity.this, ChallengeStoryActivity.class));
        });
        binding.ivSoundChallenge.setOnClickListener(view -> {
            if (isSoundOn) {
                isSoundOn = false;
                binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_off);
                stopBackgroundSound();
                SPUtils.setBoolean(this, SPUtils.SOUND_CHALLENGE, isSoundOn);
            } else {
                isSoundOn = true;
                binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_on);
                SPUtils.setBoolean(this, SPUtils.SOUND_CHALLENGE, isSoundOn);
                playBackgroundSound();
            }
        });
        binding.ivScreenChallenge.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(ChallengeLanscapeActivity.this, ChallengePortraitActivity.class));
            finish();
        });
        binding.ivTalk.setOnClickListener(view -> {
            if (isPencilRolate){
                Toast.makeText(this, "Just some seconds to view result!", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.clTalk.setVisibility(GONE);
            binding.clStop.setVisibility(View.VISIBLE);
            scaleDown.start();
        });
        binding.ivStop.setOnClickListener(view -> {
            scaleDown.cancel();
            binding.clTalk.setVisibility(View.VISIBLE);
            binding.clStop.setVisibility(GONE);
            startSecondHandAnimation();
        });
        binding.ivReset.setOnClickListener(view -> {
            handler.removeCallbacks(runnableRotatePencil);
            isPencilRolate = false;
            if (scaleDown!=null) scaleDown.cancel();
            binding.clTalk.setVisibility(View.VISIBLE);
            binding.clStop.setVisibility(GONE);
            binding.ivPencilPortrait.clearAnimation();
            binding.ivPencilPortrait.setRotation(0);
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
    private void createAnimationTalking() {
        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                binding.ivStop,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.17f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.17f));
        scaleDown.setDuration(500);
        scaleDown.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleDown.setRepeatCount(ValueAnimator.INFINITE);
        scaleDown.setRepeatMode(ValueAnimator.REVERSE);
    }
    private void startSecondHandAnimation() {
        Random random = new Random();
        plusDegree = 50 +  random.nextInt(41);
        timeEnd = 3 + random.nextInt(5);
        timeEnd *=10;
        timeStop = 10+ random.nextInt(31);
        isPencilRolate = true;
        runnableRotatePencil = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                if (timeEnd==0||timeStop==0){
                    handler.removeCallbacks(this);
                    isPencilRolate = false;
                } else {
                    timeEnd --;
                    if (plusDegree<=1||plusDegree*2/3==0) timeStop--;
                    else plusDegree = plusDegree*2/3 +  random.nextInt(plusDegree*2/3);
                    Log.d("challengeCheck","plusDegree"+plusDegree);
                    Log.d("challengeCheck","timeEnd"+timeEnd);
                    Log.d("challengeCheck","timeStop"+timeStop);
                    RotateAnimation rotate = new RotateAnimation(degrees, degrees + plusDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setFillAfter(true);
                    rotate.setInterpolator(new LinearInterpolator());
                    binding.ivPencilPortrait.startAnimation(rotate);
                    degrees += plusDegree;
                    if (degrees >= 360) {
                        degrees = degrees%360;
                    }
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.postDelayed(runnableRotatePencil, 100);
    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Log.e("activityCheck", "homeChallenge");
        }
    });

    private void stopBackgroundSound() {
        if (mediaPlayerBackground != null) {
            mediaPlayerBackground.release();
            mediaPlayerBackground = null;
        }
    }

    private void playBackgroundSound() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBackgroundSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSoundOn) playBackgroundSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopBackgroundSound();
    }
}