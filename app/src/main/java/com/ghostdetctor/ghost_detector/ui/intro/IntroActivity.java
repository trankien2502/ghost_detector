package com.ghostdetctor.ghost_detector.ui.intro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;


import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityIntroBinding;

import org.jetbrains.annotations.Nullable;


public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    ImageView[] dots = null;
    int[] listIntroTitle = null;
    IntroAdapter introAdapter;

    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        dots = new ImageView[]{binding.ivCircle01, binding.ivCircle02, binding.ivCircle03};
        listIntroTitle = new int[]{R.string.prank_your_friends, R.string.scan_to_detect_ghosts, R.string.discover_secret_of_ghost};
        introAdapter = new IntroAdapter(this);

        binding.viewPager2.setAdapter(introAdapter);

        binding.viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeContentInit(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        loadNativeIntro();
//        loadInterIntro();
    }
//
//    private void loadInterIntro() {
//
//        if (ConstantIdAds.mInterIntro == null && IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsInterIntro.size() != 0 && ConstantRemote.inter_intro) {
//            ConstantIdAds.mInterIntro = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterIntro);
//        }
//    }

//    private void showInterIntro() {
//        if (IsNetWork.haveNetworkConnectionUMP(IntroActivity.this) && ConstantIdAds.listIDAdsInterIntro.size() != 0 && ConstantRemote.inter_intro) {
//            try {
//                if (ConstantIdAds.mInterIntro != null) {
//                    CommonAd.getInstance().forceShowInterstitial(this, ConstantIdAds.mInterIntro, new CommonAdCallback() {
//                        @Override
//                        public void onNextAction() {
//                            startNextActivity();
////                            ConstantRemote.show_inter_intro = true;
//                            ConstantIdAds.mInterIntro = null;
//                            loadInterIntro();
//                        }
//                    }, true);
//                } else {
//                    startNextActivity();
//                }
//            } catch (Exception e) {
//                startNextActivity();
//            }
//        } else {
//            startNextActivity();
//        }
//    }

    @Override
    public void bindView() {
        binding.clNext.setOnClickListener(view -> {
            switch (binding.viewPager2.getCurrentItem()) {
                case 0:
                    EventTracking.logEvent(this, "intro1_next_click");
                    break;
                case 1:
                    EventTracking.logEvent(this, "intro2_next_click");
                    break;
                case 2:
                    EventTracking.logEvent(this, "intro3_next_click");
                    break;

            }
            if (binding.viewPager2.getCurrentItem() < 2) {
                binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
            } else {
//                showInterIntro();
                startNextActivity();
            }
        });
        binding.btnNext2.setOnClickListener(v -> startNextActivity());

    }

    private void changeContentInit(int position) {
        binding.tvIntro.setText(listIntroTitle[position]);
        for (int i = 0; i < 3; i++) {
            if (i == position) {
                dots[i].setImageResource(R.drawable.ic_intro_s);

            }
            else dots[i].setImageResource(R.drawable.ic_intro_sn);
        }
        switch (position) {
            case 0:
                binding.clNext.setVisibility(View.VISIBLE);
                binding.btnNext2.setVisibility(View.INVISIBLE);
                binding.nativeLoad.setVisibility(View.GONE);
                binding.nativeIntro.setVisibility(View.GONE);
                EventTracking.logEvent(this, "intro1_view");
                break;
            case 1:
                binding.clNext.setVisibility(View.VISIBLE);
                binding.btnNext2.setVisibility(View.INVISIBLE);
                binding.nativeLoad.setVisibility(View.GONE);
                binding.nativeIntro.setVisibility(View.GONE);
                EventTracking.logEvent(this, "intro2_view");
                break;
            case 2:
                binding.clNext.setVisibility(View.GONE);
                binding.btnNext2.setVisibility(View.VISIBLE);
//                if (IsNetWork.haveNetworkConnection(IntroActivity.this) && ConstantIdAds.listIDAdsNativeIntro.size() != 0 && ConstantRemote.native_intro) {
//                    binding.nativeLoad.setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(() -> {
//                        binding.nativeLoad.setVisibility(View.GONE);
//                        binding.nativeIntro.setVisibility(View.VISIBLE);
//
//                    }, 500);
//                }
                EventTracking.logEvent(this, "intro3_view");
                break;

        }
    }

    public void startNextActivity() {
//        if (SharePrefUtils.getCountOpenApp(IntroActivity.this) == 1) {
//            startNextActivity(PermissionActivity.class, null);
//        } else {
//            if (ConstantRemote.show_permission.contains(String.valueOf(SharePrefUtils.getCountOpenApp(IntroActivity.this)))) {
//                startNextActivity(PermissionActivity.class, null);
//            } else {
//                if (!PermissionManager.checkAllPermission(this)) {
//                    startNextActivity(PermissionActivity.class, null);
//                } else
//                    startNextActivity(MainActivity.class, null);
//            }
//        }
        startNextActivity(HomeActivity.class,null);
        finishAffinity();
    }

//    public void loadNativeIntro() {
//        try {
//            if (IsNetWork.haveNetworkConnection(IntroActivity.this) && ConstantIdAds.listIDAdsNativeIntro.size() != 0 && ConstantRemote.native_intro) {
//                Admob.getInstance().loadNativeAd(IntroActivity.this, ConstantIdAds.listIDAdsNativeIntro, new AdCallback() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
//                        NativeAdView adView = (NativeAdView) LayoutInflater.from(IntroActivity.this).inflate(R.layout.layout_native_show_small, null);
//                        binding.nativeIntro.removeAllViews();
//                        binding.nativeIntro.addView(adView);
//                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                        binding.nativeIntro.setVisibility(View.GONE);
//                    }
//                });
//            } else {
//                binding.nativeIntro.setVisibility(View.GONE);
//            }
//
//        } catch (Exception e) {
//            binding.nativeIntro.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(binding.viewPager2.getCurrentItem());
    }
}