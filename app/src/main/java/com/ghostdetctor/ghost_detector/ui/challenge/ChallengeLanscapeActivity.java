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
import com.ghostdetctor.ghost_detector.dialog.reset.IClickDialogReset;
import com.ghostdetctor.ghost_detector.dialog.reset.ResetDialog;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityChallengeLanscapeBinding;

import java.util.Random;

public class ChallengeLanscapeActivity extends BaseActivity<ActivityChallengeLanscapeBinding> {
    private boolean isSoundOn;
    MediaPlayer mediaPlayerBackground;
    public static boolean isChallengeLandscape;
    boolean soundCheck = true;
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
        isChallengeLandscape = true;
        Log.e("challengeCheck","on create");
        isSoundOn = SPUtils.getBoolean(this,SPUtils.SOUND_CHALLENGE,true);
        Log.e("challengeCheck","isSoundOn "+isSoundOn);
        if (!isSoundOn) {
            binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_off);
            stopBackgroundSound();
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    soundCheck = SPUtils.getBoolean(ChallengeLanscapeActivity.this, SPUtils.SOUND_CHECK, true);
                    if (!soundCheck) {
                        handler.postDelayed(this, 100);
                    } else {
                        binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_on);
                        playBackgroundSound();
                        handler.removeCallbacks(this);
                        SPUtils.setBoolean(ChallengeLanscapeActivity.this, SPUtils.SOUND_CHECK, false);
                    }
                }
            });
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
            resultLauncher.launch(new Intent(ChallengeLanscapeActivity.this, ChallengeLanscapeActivity.class));
            finish();
        });
        binding.ivTalk.setOnClickListener(view -> {
            if (isPencilRolate){
                Toast.makeText(this, getString(R.string.please_waiting_to_view_result), Toast.LENGTH_SHORT).show();                return;
            }
            binding.clTalk.setVisibility(GONE);
            binding.clStop.setVisibility(View.VISIBLE);
            scaleDown.start();
        });
        binding.ivStop.setOnClickListener(view -> {
            scaleDown.cancel();
            binding.clTalk.setVisibility(View.VISIBLE);
            binding.clStop.setVisibility(GONE);
            isPencilRolate = true;
            startPencilAnimation(isPencilRolate,0,0,0);
        });
        binding.ivReset.setOnClickListener(view -> {
            if (!isPencilRolate && degrees==0){
                Toast.makeText(this, getString(R.string.pencil_already_reset), Toast.LENGTH_SHORT).show();
                return;
            }
            resetChallenge();
        });
    }


    private void resetChallenge() {
        ResetDialog resetDialog = new ResetDialog(this, true);
        resetDialog.init(new IClickDialogReset() {
            @Override
            public void cancel() {
                resetDialog.dismiss();
            }

            @Override
            public void reset() {
                resetDialog.dismiss();
                handler.removeCallbacks(runnableRotatePencil);
                isPencilRolate = false;
                if (scaleDown!=null) scaleDown.cancel();
                binding.clTalk.setVisibility(View.VISIBLE);
                binding.clStop.setVisibility(GONE);
                degrees=0;
                binding.ivPencilPortrait.clearAnimation();
                binding.ivPencilPortrait.setRotation(0);
            }
        });

        try {
            resetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        isChallengeLandscape = false;
        Log.e("challengeCheck","on back");
        startNextActivity(ChallengeHomeActivity.class,null);
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
    private void startPencilAnimation(boolean isPencilRolateLocal,int plusDegreeLocal,int timeEndLocal,int timeStopLocal) {
        if (!isPencilRolateLocal) return;
        Random random = new Random();
        if (plusDegreeLocal!=0) plusDegree = plusDegreeLocal;
        else
            plusDegree = 45 +  random.nextInt(31);
        if (timeEndLocal!=0) timeEnd = timeEndLocal;
        else {
            timeEnd = 3 + random.nextInt(5);
            timeEnd *=10;
        }
        if (timeStopLocal!=0) timeStop = timeStopLocal;
        else timeStop = 10+ random.nextInt(11);
        runnableRotatePencil = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                if (timeEnd==0||timeStop==0){
                    handler.removeCallbacks(this);
                    isPencilRolate = false;
                } else {
                    timeEnd --;
                    if (plusDegree<=1||plusDegree/4==0) {
                        timeStop--;
                        plusDegree=1;
                    }
                    else {
                        if (random.nextInt(5)==2) plusDegree = plusDegree +  random.nextInt(plusDegree/2);
                        else plusDegree = plusDegree*3/4 +  random.nextInt(plusDegree/4);
                    }
                    Log.d("challengeCheck","plusDegree"+plusDegree);
                    Log.d("challengeCheck","timeEnd"+timeEnd);
                    Log.d("challengeCheck","timeStop"+timeStop);
                    RotateAnimation rotate = new RotateAnimation(degrees, degrees + plusDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(600);
                    rotate.setFillAfter(true);
                    rotate.setInterpolator(new LinearInterpolator());
                    binding.ivPencilPortrait.startAnimation(rotate);
                    degrees += plusDegree;
                    if (degrees >= 360) {
                        degrees = degrees%360;
                    }
                    handler.postDelayed(this, 600);
                }
            }
        };
        handler.post(runnableRotatePencil);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Log.e("activityCheck", "homeChallenge");
        }
    });
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChallengeLandscape = false;
        handler.removeCallbacks(runnableRotatePencil);
        isPencilRolate = false;
        if (scaleDown!=null) scaleDown.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChallengeLandscape = true;
        Log.e("challengeCheck","on resume");
        if (isSoundOn){
            binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_on);
            playBackgroundSound();
        } else {
            binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_off);
            stopBackgroundSound();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isChallengeLandscape = false;
        Log.e("challengeCheck","on stop");
        stopBackgroundSound();
        SPUtils.setBoolean(ChallengeLanscapeActivity.this,SPUtils.SOUND_CHECK,true);
    }
}