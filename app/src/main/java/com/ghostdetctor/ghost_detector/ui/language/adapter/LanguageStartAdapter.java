package com.ghostdetctor.ghost_detector.ui.language.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ghostdetctor.ghost_detector.ui.language.interfaces.IClickLanguage;
import com.ghostdetctor.ghost_detector.ui.language.model.LanguageModel;
import com.ghostdetector.ghost_detector.R;

import java.util.List;

public class LanguageStartAdapter extends RecyclerView.Adapter<LanguageStartAdapter.LanguageViewHolder> {
    private final List<LanguageModel> languageModelList;
    private final IClickLanguage iClickItemLanguage;
    private final Context context;

    public LanguageStartAdapter(List<LanguageModel> languageModelList, IClickLanguage listener, Context context) {
        this.languageModelList = languageModelList;
        this.iClickItemLanguage = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        LanguageModel languageModel = languageModelList.get(position);
        if (languageModel == null) {
            return;
        }
        holder.tvLang.setText(languageModel.getName());
        holder.tvLangSelect.setText(languageModel.getName());
        if (languageModel.getActive()) {
            holder.ivBackgroundLang.setImageResource(R.drawable.img_language_select);
            holder.tvLang.setVisibility(View.GONE);
            holder.tvLangSelect.setVisibility(View.VISIBLE);
            holder.icLang.setVisibility(View.GONE);
            holder.icLangSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ivBackgroundLang.setImageResource(R.drawable.img_language_unselect);
            holder.tvLang.setVisibility(View.VISIBLE);
            holder.tvLangSelect.setVisibility(View.GONE);
            holder.icLang.setVisibility(View.VISIBLE);
            holder.icLangSelect.setVisibility(View.GONE);
        }

        switch (languageModel.getCode()) {
            case "fr":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_fr).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_fr).into(holder.icLangSelect);
                break;
            case "es":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_es).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_es).into(holder.icLangSelect);
                break;
            case "zh":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_zh).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_zh).into(holder.icLangSelect);
                break;
            case "in":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_in).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_in).into(holder.icLangSelect);
                break;
            case "hi":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_hi).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_hi).into(holder.icLangSelect);
                break;
            case "de":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_ge).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_ge).into(holder.icLangSelect);
                break;
            case "pt":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_pt).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_pt).into(holder.icLangSelect);
                break;
            case "en":
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_en).into(holder.icLang);
                Glide.with(context).asBitmap().load(R.drawable.ic_lang_en).into(holder.icLangSelect);
                break;
        }

        holder.layoutItem.setOnClickListener(v -> {
            setCheck(languageModel.getCode());
            iClickItemLanguage.onClickItemLanguage(languageModel);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        if (languageModelList != null) {
            return languageModelList.size();
        } else {
            return 0;
        }
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivBackgroundLang;
        private final TextView tvLang;
        private final ImageView icLang;
        private final LinearLayout layoutItem;
        private final TextView tvLangSelect;
        private final ImageView icLangSelect;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackgroundLang = itemView.findViewById(R.id.ivBackgroundLang);
            icLang = itemView.findViewById(R.id.icLang);
            tvLang = itemView.findViewById(R.id.tvLang);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            icLangSelect = itemView.findViewById(R.id.icLangSelect);
            tvLangSelect = itemView.findViewById(R.id.tvLangSelect);
        }
    }

    public void setCheck(String code) {
        for (LanguageModel item : languageModelList) {
            item.setActive(item.getCode().equals(code));
        }
        notifyDataSetChanged();
    }
}
