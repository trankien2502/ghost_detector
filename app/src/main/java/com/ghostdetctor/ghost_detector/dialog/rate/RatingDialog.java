package com.ghostdetctor.ghost_detector.dialog.rate;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.ghostdetctor.ghost_detector.base.BaseDialog;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.DialogRatingAppBinding;


public class RatingDialog extends BaseDialog<DialogRatingAppBinding> {

    private IClickDialogRate iClickDialogRate;
    private final Context context;

    public RatingDialog(@NonNull Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }

    @Override
    protected DialogRatingAppBinding setBinding() {
        return DialogRatingAppBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.rtb.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            String getRating = String.valueOf(binding.rtb.getRating());
            switch (getRating) {
                case "1.0":
                    SPUtils.setInt(getContext(),SPUtils.RATE_STAR,1);
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_1);
                    break;
                case "2.0":
                    SPUtils.setInt(getContext(),SPUtils.RATE_STAR,2);
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_2);
                    break;
                case "3.0":
                    SPUtils.setInt(getContext(),SPUtils.RATE_STAR,3);
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_3);
                    break;
                case "4.0":
                    SPUtils.setInt(getContext(),SPUtils.RATE_STAR,4);
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_4);
                    break;
                case "5.0":
                    SPUtils.setInt(getContext(),SPUtils.RATE_STAR,5);
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_5);
                    break;
                default:
                    SPUtils.setInt(getContext(),SPUtils.RATE_STAR,0);
                    binding.btnRateUs.setText(context.getResources().getString(R.string.rate_us));
                    binding.imgIcon.setImageResource(R.drawable.rating_0);
                    break;
            }
        });
    }

    @Override
    protected void bindView() {
        binding.btnRateUs.setOnClickListener(view -> {
            if (binding.rtb.getRating() == 0) {
                return;
            }
            if (binding.rtb.getRating() <= 3.0) {
                binding.imgIcon.setVisibility(View.GONE);
                iClickDialogRate.send();
            } else {
                binding.imgIcon.setVisibility(View.VISIBLE);
                iClickDialogRate.rate();
            }
        });

        binding.btnNotNow.setOnClickListener(view -> iClickDialogRate.later());
    }

    public void init(IClickDialogRate iClickDialogRate) {
        this.iClickDialogRate = iClickDialogRate;
    }

    public String getRating() {
        return String.valueOf(this.binding.rtb.getRating());
    }

}
