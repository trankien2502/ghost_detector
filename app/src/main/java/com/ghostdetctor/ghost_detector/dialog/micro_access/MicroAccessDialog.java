package com.ghostdetctor.ghost_detector.dialog.micro_access;

import android.content.Context;

import com.ghostdetctor.ghost_detector.base.BaseDialog;
import com.ghostdetector.ghost_detector.databinding.DialogMicroAccessBinding;

public class MicroAccessDialog extends BaseDialog<DialogMicroAccessBinding> {
    IClickDialogMicroAccess iBaseListener;
    Context context;

    public MicroAccessDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogMicroAccessBinding setBinding() {
        return DialogMicroAccessBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.btnDeny.setOnClickListener(view -> iBaseListener.deny());

        binding.btnAllow.setOnClickListener(view -> iBaseListener.allow());

    }

    public void init(IClickDialogMicroAccess iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
