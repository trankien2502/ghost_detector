package com.ghostdetctor.ghost_detector.ui.splash;

import android.media.metrics.Event;
import android.os.Handler;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.database.GhostDAO;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.ui.language.LanguageStartActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetctor.ghost_detector.util.SharePrefUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivitySplashBinding;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"splash_open");
        initData();
        SharePrefUtils.increaseCountOpenApp(this);
        SPUtils.setInt(this,SPUtils.GHOST_TYPE,0);
        new Handler().postDelayed(() -> {
            startNextActivity(LanguageStartActivity.class, null);
            finishAffinity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBackPressed() {
    }
    public void initData(){
        GhostDAO ghostDAO = AppDatabase.getInstance(this).ghostDAO();

        ghostDAO.insertAll(new Ghost(1, getString(R.string.horror_name_1),getString(R.string.horror_description_1),getString(R.string.danger_7),getString(R.string.horror_die_1),R.drawable.img_border_ghost_item_1,R.drawable.img_ghost_detail_1,true));
        ghostDAO.insertAll(new Ghost(2, getString(R.string.horror_name_2),getString(R.string.horror_alias_2),getString(R.string.horror_description_2),getString(R.string.danger_8),getString(R.string.horror_die_2),R.drawable.img_border_ghost_item_2,R.drawable.img_ghost_detail_2,true));
        ghostDAO.insertAll(new Ghost(3, getString(R.string.horror_name_3),getString(R.string.horror_alias_3),getString(R.string.horror_description_3),getString(R.string.danger_8),getString(R.string.horror_die_3),R.drawable.img_border_ghost_item_3,R.drawable.img_ghost_detail_3,true));
        ghostDAO.insertAll(new Ghost(4, getString(R.string.horror_name_4),getString(R.string.horror_description_4),getString(R.string.danger_7),getString(R.string.horror_die_4),R.drawable.img_border_ghost_item_4,R.drawable.img_ghost_detail_4,true));
        ghostDAO.insertAll(new Ghost(5, getString(R.string.horror_name_5),getString(R.string.horror_alias_5),getString(R.string.horror_description_5),getString(R.string.danger_7),getString(R.string.horror_die_5),R.drawable.img_border_ghost_item_5,R.drawable.img_ghost_detail_5,true));
        ghostDAO.insertAll(new Ghost(6, getString(R.string.horror_name_6),getString(R.string.horror_description_6),getString(R.string.danger_7),getString(R.string.horror_die_6),R.drawable.img_border_ghost_item_6,R.drawable.img_ghost_detail_6,true));
        ghostDAO.insertAll(new Ghost(7, getString(R.string.horror_name_7),getString(R.string.horror_description_7),getString(R.string.danger_6),getString(R.string.horror_die_7),R.drawable.img_border_ghost_item_7,R.drawable.img_ghost_detail_7,true));
        ghostDAO.insertAll(new Ghost(8, getString(R.string.horror_name_8),getString(R.string.horror_description_8),getString(R.string.danger_9),getString(R.string.horror_die_8),R.drawable.img_border_ghost_item_8,R.drawable.img_ghost_detail_8,true));
        ghostDAO.insertAll(new Ghost(9, getString(R.string.horror_name_9),getString(R.string.horror_description_9),getString(R.string.danger_9),getString(R.string.horror_die_9),R.drawable.img_border_ghost_item_9,R.drawable.img_ghost_detail_9,true));
        ghostDAO.insertAll(new Ghost(10, getString(R.string.horror_name_10),getString(R.string.horror_description_10),getString(R.string.danger_8),getString(R.string.horror_die_10),R.drawable.img_border_ghost_item_10,R.drawable.img_ghost_detail_10,true));
        ghostDAO.insertAll(new Ghost(11, getString(R.string.scary_name_1),getString(R.string.scary_description_1),getString(R.string.danger_2),getString(R.string.scary_die_1),R.drawable.img_border_ghost_item_11,R.drawable.img_ghost_detail_11,false));
        ghostDAO.insertAll(new Ghost(12, getString(R.string.scary_name_2),getString(R.string.scary_description_2),getString(R.string.danger_0),getString(R.string.scary_die_2),R.drawable.img_border_ghost_item_12,R.drawable.img_ghost_detail_12,false));
        ghostDAO.insertAll(new Ghost(13, getString(R.string.scary_name_3),getString(R.string.scary_description_3),getString(R.string.danger_1),getString(R.string.scary_die_3),R.drawable.img_border_ghost_item_13,R.drawable.img_ghost_detail_13,false));
        ghostDAO.insertAll(new Ghost(14, getString(R.string.scary_name_4),getString(R.string.scary_description_4),getString(R.string.danger_0),getString(R.string.scary_die_4),R.drawable.img_border_ghost_item_14,R.drawable.img_ghost_detail_14,false));
        ghostDAO.insertAll(new Ghost(15, getString(R.string.scary_name_5),getString(R.string.scary_description_5),getString(R.string.danger_1),getString(R.string.scary_die_5),R.drawable.img_border_ghost_item_15,R.drawable.img_ghost_detail_15,false));
        ghostDAO.insertAll(new Ghost(16, getString(R.string.scary_name_6),getString(R.string.scary_description_6),getString(R.string.danger_2),getString(R.string.scary_die_6),R.drawable.img_border_ghost_item_16,R.drawable.img_ghost_detail_16,false));
        ghostDAO.insertAll(new Ghost(17, getString(R.string.scary_name_7),getString(R.string.scary_description_7),getString(R.string.danger_0),getString(R.string.scary_die_7),R.drawable.img_border_ghost_item_17,R.drawable.img_ghost_detail_17,false));
        ghostDAO.insertAll(new Ghost(18, getString(R.string.scary_name_8),getString(R.string.scary_description_8),getString(R.string.danger_1),getString(R.string.scary_die_8),R.drawable.img_border_ghost_item_18,R.drawable.img_ghost_detail_18,false));
        ghostDAO.insertAll(new Ghost(19, getString(R.string.scary_name_9),getString(R.string.scary_description_9),getString(R.string.danger_0),getString(R.string.scary_die_9),R.drawable.img_border_ghost_item_19,R.drawable.img_ghost_detail_19,false));
        ghostDAO.insertAll(new Ghost(20, getString(R.string.scary_name_10),getString(R.string.scary_description_10),getString(R.string.danger_0),getString(R.string.scary_die_10),R.drawable.img_border_ghost_item_20,R.drawable.img_ghost_detail_20,false));
    }
}
