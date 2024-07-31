package com.ghostdetctor.ghost_detector.ui.challenge;

import static android.view.View.GONE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.dialog.reset.IClickDialogReset;
import com.ghostdetctor.ghost_detector.dialog.reset.ResetDialog;
import com.ghostdetctor.ghost_detector.ui.challenge.service.SoundService;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityChallengePortraitBinding;

import java.util.Random;

public class ChallengePortraitActivity extends BaseActivity<ActivityChallengePortraitBinding> {
    private boolean isSoundOn;
    public static boolean isChallengePortrait;
    Handler handler = new Handler();
    private int degrees = 0;
    int plusDegree = 60;
    int timeStop, timeEnd;
    Runnable runnableRotatePencil;
    private ObjectAnimator scaleDown;
    private boolean isPencilRolate;

    @Override
    public ActivityChallengePortraitBinding getBinding() {
        return ActivityChallengePortraitBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"challenge_view","","PORTRAIT");
        isChallengePortrait = true;
        SharePrefUtils.increaseCountOpenChallenge(this);
        if (SharePrefUtils.getCountOpenChallenge(this)!=1) viewStartGone();
        else {
            animateTextView(binding.tvClick);
        }
        String language = SPUtils.getString(this,SPUtils.LANGUAGE,"");
        switch (language){
            case "China":
                binding.ivTalk.setImageResource(R.drawable.img_talk_zh);
                binding.ivStop.setImageResource(R.drawable.img_stop_zh);
                break;
            case "French":
                binding.ivTalk.setImageResource(R.drawable.img_talk_fr);
                binding.ivStop.setImageResource(R.drawable.img_stop_fr);
                break;
            case "German":
                binding.ivTalk.setImageResource(R.drawable.img_talk_de);
                binding.ivStop.setImageResource(R.drawable.img_stop_de);
                break;
            case "Hindi":
                binding.ivTalk.setImageResource(R.drawable.img_talk_hi);
                binding.ivStop.setImageResource(R.drawable.img_stop_hi);
                break;
            case "Indonesia":
                binding.ivTalk.setImageResource(R.drawable.img_talk_in);
                binding.ivStop.setImageResource(R.drawable.img_stop_in);
                break;
            case "Portuguese":
                binding.ivTalk.setImageResource(R.drawable.img_talk_pt);
                binding.ivStop.setImageResource(R.drawable.img_stop_pt);
                break;
            case "Spanish":
                binding.ivTalk.setImageResource(R.drawable.img_talk_es);
                binding.ivStop.setImageResource(R.drawable.img_stop_es);
                break;
            default:
                binding.ivTalk.setImageResource(R.drawable.img_talk_en);
                binding.ivStop.setImageResource(R.drawable.img_stop_en);
                break;

        }
        isSoundOn = SPUtils.getBoolean(this,SPUtils.SOUND_CHALLENGE,true);
        Log.e("challengeCheck","on create po + issound "+isSoundOn);
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
            EventTracking.logEvent(this,"challenge_stories_click");
            resultLauncher.launch(new Intent(ChallengePortraitActivity.this, ChallengeStoryActivity.class));
        });
        binding.ivSoundChallenge.setOnClickListener(view -> {
            EventTracking.logEvent(this,"challenge_sound_click");
            if (isSoundOn) {
                EventTracking.logEvent(this,"challenge_sound_click","","OFF");
                isSoundOn = false;
                binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_off);
                stopBackgroundSound();
                SPUtils.setBoolean(this, SPUtils.SOUND_CHALLENGE, isSoundOn);
            } else {
                EventTracking.logEvent(this,"challenge_sound_click","","ON");
                isSoundOn = true;
                binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_on);
                SPUtils.setBoolean(this, SPUtils.SOUND_CHALLENGE, isSoundOn);
                playBackgroundSound();
            }
        });
        binding.ivScreenChallenge.setOnClickListener(view -> {
            EventTracking.logEvent(this,"challenge_rotate_click");
            if (!isPencilRolate){
                resultLauncher.launch(new Intent(ChallengePortraitActivity.this, ChallengeLanscapeActivity.class));
                finish();
                return;
            }
            resetByScreenChange();
        });
        binding.ivTalk.setOnClickListener(view -> {
            EventTracking.logEvent(this,"challenge_talk_click");
            viewStartGone();
            if (isPencilRolate){
                Toast.makeText(this, "Just some seconds to view result!", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.clTalk.setVisibility(GONE);
            binding.clStop.setVisibility(View.VISIBLE);
            scaleDown.start();
        });
        binding.ivStop.setOnClickListener(view -> {
            EventTracking.logEvent(this,"challenge_stop_click");
            scaleDown.cancel();
            binding.clTalk.setVisibility(View.VISIBLE);
            binding.clStop.setVisibility(GONE);
            isPencilRolate = true;
            startPencilAnimation(true,0,0,0);
        });
        binding.ivReset.setOnClickListener(view -> {
            EventTracking.logEvent(this,"challenge_reset_click");
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
    private void resetByScreenChange() {
        ResetDialog resetDialog = new ResetDialog(this, true);
        resetDialog.init(new IClickDialogReset() {
            @Override
            public void cancel() {
                resetDialog.dismiss();
            }

            @Override
            public void reset() {
                resultLauncher.launch(new Intent(ChallengePortraitActivity.this, ChallengeLanscapeActivity.class));
                finish();
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
        EventTracking.logEvent(this,"challenge_back_click");
        setResult(RESULT_OK);
        finish();
    }
    private void viewStartGone() {
        binding.tvClick.setVisibility(GONE);
        binding.amClick.setVisibility(GONE);
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
//    private void stopBackgroundSound() {
//        if (mediaPlayerBackground != null) {
//            mediaPlayerBackground.release();
//            mediaPlayerBackground = null;
//        }
//    }
//    private void playBackgroundSound() {
//        if (mediaPlayerBackground != null) {
//            mediaPlayerBackground.release();
//        }
//        mediaPlayerBackground = MediaPlayer.create(this, R.raw.background_sound);
//        mediaPlayerBackground.start();
//        mediaPlayerBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.seekTo(0);
//                mediaPlayer.start();
//            }
//        });
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("challengeCheck","on destroy po + issound "+isSoundOn);
        isChallengePortrait = false;
        handler.removeCallbacks(runnableRotatePencil);
        isPencilRolate = false;
        if (scaleDown!=null) scaleDown.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChallengePortrait = true;
        isSoundOn =  SPUtils.getBoolean(this,SPUtils.SOUND_CHALLENGE,true);
        Log.e("challengeCheck","on resume po + issound "+isSoundOn);
        if (isSoundOn) playBackgroundSound();
        else stopBackgroundSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isChallengePortrait = false;
        if (!checkChallengeExist()&&isSoundOn)
            stopBackgroundSound();
    }
}