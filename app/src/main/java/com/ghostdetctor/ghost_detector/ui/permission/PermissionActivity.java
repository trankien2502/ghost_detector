package com.ghostdetctor.ghost_detector.ui.permission;


import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetector.ghost_detector.databinding.ActivityPermissionBinding;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
