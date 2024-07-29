package com.ghostdetctor.ghost_detector.ui.challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.dialog.camera_access.CameraAccessDialog;
import com.ghostdetctor.ghost_detector.dialog.camera_access.IClickDialogCameraAccess;
import com.ghostdetctor.ghost_detector.dialog.micro_access.IClickDialogMicroAccess;
import com.ghostdetctor.ghost_detector.dialog.micro_access.MicroAccessDialog;
import com.ghostdetctor.ghost_detector.ui.challenge.service.SoundService;
import com.ghostdetctor.ghost_detector.ui.ghost.OptionActivity;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityChallengeHomeBinding;

public class ChallengeHomeActivity extends BaseActivity<ActivityChallengeHomeBinding> {

    private boolean isSoundOn;
    MediaPlayer mediaPlayerBackground;
    Handler handler = new Handler();
    boolean soundCheck = true;
    private final int REQUEST_CODE_AUDIO_PERMISSION = 129;
    private int countAudio = 0;
    @Override
    public ActivityChallengeHomeBinding getBinding() {
        return ActivityChallengeHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
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
                    soundCheck = SPUtils.getBoolean(ChallengeHomeActivity.this, SPUtils.SOUND_CHECK, true);
                    if (!soundCheck) {
                        handler.postDelayed(this, 100);
                    } else {
                        binding.ivSoundChallenge.setImageResource(R.drawable.img_challenge_sound_on);
                        playBackgroundSound();
                        handler.removeCallbacks(this);
                        SPUtils.setBoolean(ChallengeHomeActivity.this, SPUtils.SOUND_CHECK, false);
                    }
                }
            });
        }

    }

    @Override
    public void bindView() {
        binding.ivBackChallenge.setOnClickListener(view -> onBackPressed());
        binding.ivStoryChallenge.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(ChallengeHomeActivity.this, ChallengeStoryActivity.class));
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
        binding.ivStartChallenge.setOnClickListener(view -> {
            if (checkMicroPermission()){
                resultLauncher.launch(new Intent(ChallengeHomeActivity.this, ChallengePortraitActivity.class));
                finish();
            }
            else accessMicro();
        });
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
    private void accessMicro(){
        MicroAccessDialog microAccessDialog = new MicroAccessDialog(this,false);
        microAccessDialog.init(new IClickDialogMicroAccess() {
            @Override
            public void deny() {
                microAccessDialog.dismiss();
            }

            @Override
            public void allow() {
                ActivityCompat.requestPermissions(ChallengeHomeActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO_PERMISSION);
                microAccessDialog.dismiss();
            }
        });
        try {
            microAccessDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkMicroPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                resultLauncher.launch(new Intent(ChallengeHomeActivity.this, ChallengePortraitActivity.class));
            }

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                        SPUtils.getInt(this,SPUtils.MICRO,0);
                        countAudio++;
                        SPUtils.setInt(this,SPUtils.MICRO, countAudio);
                        if (countAudio > 1) {
                            showDialogGotoSetting();
                        }
                    }
                }
            }
        }
    }

    private void showDialogGotoSetting() {
        Toast.makeText(this, getString(R.string.permission_denied_please_go_to_setting_to_enable), Toast.LENGTH_SHORT).show();
    }



    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Log.e("activityCheck", "homeChallenge");
        }
    });
//private void stopBackgroundSound() {
//    if (mediaPlayerBackground != null) {
//        mediaPlayerBackground.release();
//        mediaPlayerBackground = null;
//    }
//}
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
        Log.e("challengeCheck","on destroy");
        stopBackgroundSound();
        SPUtils.setBoolean(this, SPUtils.SOUND_CHECK, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        Log.e("challengeCheck","on stop");
        stopBackgroundSound();
    }
}