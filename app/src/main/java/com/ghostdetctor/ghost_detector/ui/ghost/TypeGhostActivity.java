package com.ghostdetctor.ghost_detector.ui.ghost;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityTypeGhostBinding;

public class TypeGhostActivity extends BaseActivity<ActivityTypeGhostBinding> {

    private final int TYPE_HORROR_GHOSTS = 1;
    private final int TYPE_SCARY_SPIRITS = 2;
    private int ghostType = 0;
    @Override
    public ActivityTypeGhostBinding getBinding() {
        return ActivityTypeGhostBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"option_view");
        binding.optionHeader.tvTitle.setText(R.string.option);
        ghostType = SPUtils.getInt(this,SPUtils.GHOST_TYPE,0);
        Log.e("isCheck", "ghostType "+ghostType);
        if (ghostType==TYPE_HORROR_GHOSTS){
            binding.clHorror.setVisibility(View.INVISIBLE);
            binding.clScary.setVisibility(View.VISIBLE);
            binding.clStart.setVisibility(View.INVISIBLE);
            binding.clHorrorSelect.setVisibility(View.VISIBLE);
            binding.clScarySelect.setVisibility(View.INVISIBLE);
            binding.clStartSelect.setVisibility(View.VISIBLE);
        } else {
            if (ghostType == TYPE_SCARY_SPIRITS){
                binding.clHorror.setVisibility(View.VISIBLE);
                binding.clScary.setVisibility(View.INVISIBLE);
                binding.clStart.setVisibility(View.INVISIBLE);
                binding.clHorrorSelect.setVisibility(View.INVISIBLE);
                binding.clScarySelect.setVisibility(View.VISIBLE);
                binding.clStartSelect.setVisibility(View.VISIBLE);
            } else {
                binding.clHorror.setVisibility(View.VISIBLE);
                binding.clScary.setVisibility(View.VISIBLE);
                binding.clStart.setVisibility(View.VISIBLE);
                binding.clHorrorSelect.setVisibility(View.INVISIBLE);
                binding.clScarySelect.setVisibility(View.INVISIBLE);
                binding.clStartSelect.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void bindView() {
        binding.optionHeader.ivBack.setOnClickListener(view -> onBackPressed());
        binding.clStart.setOnClickListener(view -> {
            EventTracking.logEvent(this,"option_start_scanning_click");
            Toast.makeText(this, getString(R.string.please_select_ghost_type), Toast.LENGTH_SHORT).show();
        });
        binding.ivScary.setOnClickListener(view -> {
            EventTracking.logEvent(this,"option_scary_spirits_click");
            ghostType = TYPE_SCARY_SPIRITS;
            binding.clHorror.setVisibility(View.VISIBLE);
            binding.clScary.setVisibility(View.INVISIBLE);
            binding.clStart.setVisibility(View.INVISIBLE);
            binding.clHorrorSelect.setVisibility(View.INVISIBLE);
            binding.clScarySelect.setVisibility(View.VISIBLE);
            binding.clStartSelect.setVisibility(View.VISIBLE);
        });
        binding.ivHorror.setOnClickListener(view -> {
            EventTracking.logEvent(this,"option_horror_ghosts_click");
            ghostType = TYPE_HORROR_GHOSTS;
            binding.clHorror.setVisibility(View.INVISIBLE);
            binding.clScary.setVisibility(View.VISIBLE);
            binding.clStart.setVisibility(View.INVISIBLE);
            binding.clHorrorSelect.setVisibility(View.VISIBLE);
            binding.clScarySelect.setVisibility(View.INVISIBLE);
            binding.clStartSelect.setVisibility(View.VISIBLE);
        });
        binding.clStartSelect.setOnClickListener(view -> {
            EventTracking.logEvent(this,"option_start_scanning_click");
            SPUtils.setInt(this,SPUtils.GHOST_TYPE,ghostType);
            finish();
        });

    }
    @Override
    public void onBackPressed() {
        EventTracking.logEvent(this,"option_back_click");
        setResult(RESULT_OK);
        finish();
    }
}