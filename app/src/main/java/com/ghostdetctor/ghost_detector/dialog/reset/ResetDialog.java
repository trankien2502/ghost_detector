package com.ghostdetctor.ghost_detector.dialog.reset;

import android.content.Context;

import com.ghostdetctor.ghost_detector.base.BaseDialog;
import com.ghostdetector.ghost_detector.databinding.DialogResetBinding;

public class ResetDialog extends BaseDialog<DialogResetBinding> {
    IClickDialogReset iBaseListener;
    Context context;

    public ResetDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogResetBinding setBinding() {
        return DialogResetBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.btnCancelQuitApp.setOnClickListener(view -> iBaseListener.cancel());

        binding.btnQuitApp.setOnClickListener(view -> iBaseListener.reset());

    }

    public void init(IClickDialogReset iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
