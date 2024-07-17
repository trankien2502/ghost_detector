package com.ghostdetctor.ghost_detector.ui.language;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.ui.intro.IntroActivity;
import com.ghostdetctor.ghost_detector.ui.language.adapter.LanguageStartAdapter;
import com.ghostdetctor.ghost_detector.ui.language.model.LanguageModel;
import com.ghostdetctor.ghost_detector.util.SystemUtil;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityLanguageStartBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageStartActivity extends BaseActivity<ActivityLanguageStartBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;

    @Override
    public ActivityLanguageStartBinding getBinding() {
        return ActivityLanguageStartBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        codeLang = Locale.getDefault().getLanguage();

        binding.viewTop.ivBack.setVisibility(View.INVISIBLE);
        binding.viewTop.tvToolBar.setText(getString(R.string.language));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageStartAdapter = new LanguageStartAdapter(listLanguage, code -> codeLang = code, this);


        languageStartAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));

        binding.rcvLangStart.setLayoutManager(linearLayoutManager);
        binding.rcvLangStart.setAdapter(languageStartAdapter);
    }

    @Override
    public void bindView() {
        binding.viewTop.ivCheck.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            startNextActivity(IntroActivity.class, null);
            finishAffinity();
        });
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en", false));
        listLanguage.add(new LanguageModel("China", "zh", false));
        listLanguage.add(new LanguageModel("French", "fr", false));
        listLanguage.add(new LanguageModel("German", "de", false));
        listLanguage.add(new LanguageModel("Hindi", "hi", false));
        listLanguage.add(new LanguageModel("Indonesia", "in", false));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false));
        listLanguage.add(new LanguageModel("Spanish", "es", false));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
