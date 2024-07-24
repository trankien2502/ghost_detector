package com.ghostdetctor.ghost_detector.ui.splash;

import android.os.Handler;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.database.AppDatabase;
import com.ghostdetctor.ghost_detector.database.GhostDAO;
import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetctor.ghost_detector.ui.language.LanguageStartActivity;
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
//    public void insertOrUpdate(Ghost ghost) {
//        GhostDAO ghostDAO = AppDatabase.getInstance(this).ghostDAO();
//        new Thread(() -> {
//            Ghost existingGhost = ghostDAO.getGhostById(ghost.getId());
//            if (existingGhost != null) {
//                ghostDao.update(ghost);
//            } else {
//                ghostDao.insert(ghost);
//            }
//        }).start();
//    }
    public void initData(){
        GhostDAO ghostDAO = AppDatabase.getInstance(this).ghostDAO();

        ghostDAO.insertAll(new Ghost(1, getString(R.string.horror_name_1),getString(R.string.horror_description_1),getString(R.string.danger_7),getString(R.string.horror_die_1),true));
        ghostDAO.insertAll(new Ghost(2, getString(R.string.horror_name_2),getString(R.string.horror_alias_2),getString(R.string.horror_description_2),getString(R.string.danger_8),getString(R.string.horror_die_2),true));
        ghostDAO.insertAll(new Ghost(3, getString(R.string.horror_name_3),getString(R.string.horror_alias_3),getString(R.string.horror_description_3),getString(R.string.danger_8),getString(R.string.horror_die_3),true));
        ghostDAO.insertAll(new Ghost(4, getString(R.string.horror_name_4),getString(R.string.horror_description_4),getString(R.string.danger_7),getString(R.string.horror_die_4),true));
        ghostDAO.insertAll(new Ghost(5, getString(R.string.horror_name_5),getString(R.string.horror_alias_5),getString(R.string.horror_description_5),getString(R.string.danger_7),getString(R.string.horror_die_5),true));
        ghostDAO.insertAll(new Ghost(6, getString(R.string.horror_name_6),getString(R.string.horror_description_6),getString(R.string.danger_7),getString(R.string.horror_die_6),true));
        ghostDAO.insertAll(new Ghost(7, getString(R.string.horror_name_7),getString(R.string.horror_description_7),getString(R.string.danger_6),getString(R.string.horror_die_7),true));
        ghostDAO.insertAll(new Ghost(8, getString(R.string.horror_name_8),getString(R.string.horror_description_8),getString(R.string.danger_9),getString(R.string.horror_die_8),true));
        ghostDAO.insertAll(new Ghost(9, getString(R.string.horror_name_9),getString(R.string.horror_description_9),getString(R.string.danger_9),getString(R.string.horror_die_9),true));
        ghostDAO.insertAll(new Ghost(10, getString(R.string.horror_name_10),getString(R.string.horror_description_10),getString(R.string.danger_8),getString(R.string.horror_die_10),true));
        ghostDAO.insertAll(new Ghost(11, getString(R.string.scary_name_1),getString(R.string.scary_description_1),getString(R.string.danger_2),getString(R.string.scary_die_1),false));
        ghostDAO.insertAll(new Ghost(12, getString(R.string.scary_name_2),getString(R.string.scary_description_2),getString(R.string.danger_0),getString(R.string.scary_die_2),false));
        ghostDAO.insertAll(new Ghost(13, getString(R.string.scary_name_3),getString(R.string.scary_description_3),getString(R.string.danger_1),getString(R.string.scary_die_3),false));
        ghostDAO.insertAll(new Ghost(14, getString(R.string.scary_name_4),getString(R.string.scary_description_4),getString(R.string.danger_0),getString(R.string.scary_die_4),false));
        ghostDAO.insertAll(new Ghost(15, getString(R.string.scary_name_5),getString(R.string.scary_description_5),getString(R.string.danger_1),getString(R.string.scary_die_5),false));
        ghostDAO.insertAll(new Ghost(16, getString(R.string.scary_name_6),getString(R.string.scary_description_6),getString(R.string.danger_2),getString(R.string.scary_die_6),false));
        ghostDAO.insertAll(new Ghost(17, getString(R.string.scary_name_7),getString(R.string.scary_description_7),getString(R.string.danger_0),getString(R.string.scary_die_7),false));
        ghostDAO.insertAll(new Ghost(18, getString(R.string.scary_name_8),getString(R.string.scary_description_8),getString(R.string.danger_1),getString(R.string.scary_die_8),false));
        ghostDAO.insertAll(new Ghost(19, getString(R.string.scary_name_9),getString(R.string.scary_description_9),getString(R.string.danger_0),getString(R.string.scary_die_9),false));
        ghostDAO.insertAll(new Ghost(20, getString(R.string.scary_name_10),getString(R.string.scary_description_10),getString(R.string.danger_0),getString(R.string.scary_die_10),false));
    }
}
