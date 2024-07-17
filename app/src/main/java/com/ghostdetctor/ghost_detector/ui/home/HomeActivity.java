package com.ghostdetctor.ghost_detector.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.dialog.exit.ExitAppDialog;
import com.ghostdetctor.ghost_detector.dialog.exit.IClickDialogExit;
import com.ghostdetctor.ghost_detector.dialog.rate.IClickDialogRate;
import com.ghostdetctor.ghost_detector.dialog.rate.RatingDialog;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityHomeBinding;
//import com.google.android.gms.tasks.Task;
//import com.google.android.play.core.review.ReviewInfo;
//import com.google.android.play.core.review.ReviewManager;
//import com.google.android.play.core.review.ReviewManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));


    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {

    }
//
//    private void rateApp() {
//        RatingDialog ratingDialog = new RatingDialog(HomeActivity.this, true);
//        ratingDialog.init(new IClickDialogRate() {
//            @Override
//            public void send() {
//                binding.rlRate.setVisibility(View.GONE);
//                ratingDialog.dismiss();
//                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
//                Uri uri = Uri.parse(uriText);
//                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
//                sendIntent.setData(uri);
//                try {
//                    finishAffinity();
//                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
//                    SharePrefUtils.forceRated(HomeActivity.this);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(HomeActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void rate() {
//                ReviewManager manager = ReviewManagerFactory.create(HomeActivity.this);
//                Task<ReviewInfo> request = manager.requestReviewFlow();
//                request.addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        ReviewInfo reviewInfo = task.getResult();
//                        Task<Void> flow = manager.launchReviewFlow(HomeActivity.this, reviewInfo);
//                        flow.addOnSuccessListener(result -> {
//                            binding.rlRate.setVisibility(View.GONE);
//                            SharePrefUtils.forceRated(HomeActivity.this);
//                            ratingDialog.dismiss();
//                            finishAffinity();
//                        });
//                    } else {
//                        ratingDialog.dismiss();
//                    }
//                });
//            }
//
//            @Override
//            public void later() {
//                ratingDialog.dismiss();
//                finishAffinity();
//            }
//
//        });
//        ratingDialog.show();
//    }

    private void exitApp() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this, true);
        exitAppDialog.init(new IClickDialogExit() {
            @Override
            public void cancel() {
                exitAppDialog.dismiss();
            }

            @Override
            public void quit() {
                exitAppDialog.dismiss();
                finishAffinity();
            }
        });

        try {
            exitAppDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!SharePrefUtils.isRated(this)) {
            if (exitRate.contains(String.valueOf(SharePrefUtils.getCountOpenApp(this)))) {
                //rateApp();
                exitApp();
            } else {
                exitApp();
            }
        } else {
            exitApp();
        }
    }
}
