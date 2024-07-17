package com.ghostdetctor.ghost_detector.ui.language;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.ui.language.adapter.LanguageAdapter;
import com.ghostdetctor.ghost_detector.ui.language.model.LanguageModel;
import com.ghostdetctor.ghost_detector.util.SystemUtil;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityLanguageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;

    @Override
    public ActivityLanguageBinding getBinding() {
        return ActivityLanguageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        codeLang = Locale.getDefault().getLanguage();

        binding.viewTop.tvToolBar.setText(getString(R.string.language));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageAdapter languageAdapter = new LanguageAdapter(listLanguage, code -> codeLang = code, this);


        languageAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));

        binding.rcvLang.setLayoutManager(linearLayoutManager);
        binding.rcvLang.setAdapter(languageAdapter);
    }

    @Override
    public void bindView() {
        binding.viewTop.ivCheck.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });

        binding.viewTop.ivBack.setOnClickListener(v -> onBackPressed());
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        listLanguage.add(new LanguageModel("English", "en", false));
        listLanguage.add(new LanguageModel("China", "zh", false));
        listLanguage.add(new LanguageModel("French", "fr", false));
        listLanguage.add(new LanguageModel("German", "de", false));
        listLanguage.add(new LanguageModel("Hindi", "hi", false));
        listLanguage.add(new LanguageModel("Indonesia", "in", false));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false));
        listLanguage.add(new LanguageModel("Spanish", "es", false));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishThisActivity();
    }
}
