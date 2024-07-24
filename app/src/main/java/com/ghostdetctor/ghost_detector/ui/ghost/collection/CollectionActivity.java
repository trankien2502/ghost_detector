package com.ghostdetctor.ghost_detector.ui.ghost.collection;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityCollectionBinding;

public class CollectionActivity extends BaseActivity<ActivityCollectionBinding> {


    @Override
    public ActivityCollectionBinding getBinding() {
        return ActivityCollectionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        CollectionAdapter collectionAdapter = new CollectionAdapter(this);
        binding.frameContentPager.setAdapter(collectionAdapter);
        binding.frameContentPager.setCurrentItem(0);
    }

    @Override
    public void bindView() {
        binding.header.tvTitle.setText(R.string.collection);
        binding.header.ivBack.setOnClickListener(view -> onBackPressed());
        binding.tvHorrorGhosts.setOnClickListener(view -> {
            binding.frameContentPager.setCurrentItem(0);
        });
        binding.tvScarySpirits.setOnClickListener(view -> {
            binding.frameContentPager.setCurrentItem(1);
        });
        binding.frameContentPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position==0){
                    binding.tvHorrorGhosts.setBackgroundResource(R.drawable.img_border_select);
                    binding.tvHorrorGhosts.setTextColor(getResources().getColor(R.color.text_select));
                    binding.tvScarySpirits.setBackgroundResource(R.drawable.img_border_unselect);
                    binding.tvScarySpirits.setTextColor(getResources().getColor(R.color.text_unselect));
                    binding.tvScarySpirits.setTypeface(null, Typeface.NORMAL);
                    binding.tvHorrorGhosts.setTypeface(null,Typeface.BOLD);
                }
                else {
                    binding.tvHorrorGhosts.setBackgroundResource(R.drawable.img_border_unselect);
                    binding.tvHorrorGhosts.setTextColor(getResources().getColor(R.color.text_unselect));
                    binding.tvScarySpirits.setBackgroundResource(R.drawable.img_border_select);
                    binding.tvScarySpirits.setTextColor(getResources().getColor(R.color.text_select));
                    binding.tvScarySpirits.setTypeface(null,Typeface.BOLD);
                    binding.tvHorrorGhosts.setTypeface(null,Typeface.NORMAL);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK){
            Log.d("isCheckGhost", "switch to detail ghost: ");
        }
    });

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}