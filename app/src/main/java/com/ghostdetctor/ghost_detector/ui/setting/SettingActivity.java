package com.ghostdetctor.ghost_detector.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.dialog.rate.IClickDialogRate;
import com.ghostdetctor.ghost_detector.dialog.rate.RatingDialog;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.ui.language.LanguageActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import org.intellij.lang.annotations.Language;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {


    @Override
    public ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"setting_view");
        binding.headerSetting.tvTitle.setText(R.string.settings);
        binding.tvLangCurrent.setText(SPUtils.getString(this,SPUtils.LANGUAGE,""));
        if (SharePrefUtils.isRated(this)) {
            binding.clRate.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView() {
        binding.clLanguage.setOnClickListener(view -> {
            EventTracking.logEvent(this,"setting_language_click");
            startNextActivity(LanguageActivity.class,null);
        });
        binding.headerSetting.ivBack.setOnClickListener(view -> onBackPressed());
        binding.clRate.setOnClickListener(view -> onRate());
        binding.clAbout.setOnClickListener(view -> {
            EventTracking.logEvent(this,"setting_about_click");
            startNextActivity(AboutActivity.class,null);
        });
        binding.clShare.setOnClickListener(view -> onShare());
    }
    private void onRate() {
        EventTracking.logEvent(this,"setting_rate_us_click");
        RatingDialog ratingDialog = new RatingDialog(SettingActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                binding.clRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    int star = SPUtils.getInt(SettingActivity.this,SPUtils.RATE_STAR,0);
                    EventTracking.logEvent(SettingActivity.this,"rate_submit","rate_star"+star,String.valueOf(star));
                    //AppOpenManager.getInstance().disableAppResumeWithActivity(SettingActivity.class);
                    SharePrefUtils.forceRated(SettingActivity.this);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                EventTracking.logEvent(SettingActivity.this,"rate_submit");
                ReviewManager manager = ReviewManagerFactory.create(SettingActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            int star = SPUtils.getInt(SettingActivity.this,SPUtils.RATE_STAR,0);
                            EventTracking.logEvent(SettingActivity.this,"rate_submit","rate_star"+star,String.valueOf(star));
                            binding.clRate.setVisibility(View.GONE);
                            SharePrefUtils.forceRated(SettingActivity.this);
                            ratingDialog.dismiss();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(SettingActivity.this,"rate_not_now");
                ratingDialog.dismiss();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(this,"rate_show");
    }

    private void onShare() {
        EventTracking.logEvent(this,"setting_share_click");
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT, "Download application :" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intentShare, "Share with"));
        //AppOpenManager.getInstance().disableAppResumeWithActivity(SettingActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}