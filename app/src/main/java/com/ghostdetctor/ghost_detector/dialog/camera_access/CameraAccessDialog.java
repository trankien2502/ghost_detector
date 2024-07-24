package com.ghostdetctor.ghost_detector.dialog.camera_access;

import android.content.Context;

import com.ghostdetctor.ghost_detector.base.BaseDialog;
import com.ghostdetector.ghost_detector.databinding.DialogCameraAccessBinding;

public class CameraAccessDialog extends BaseDialog<DialogCameraAccessBinding> {
    IClickDialogCameraAccess iBaseListener;
    Context context;

    public CameraAccessDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogCameraAccessBinding setBinding() {
        return DialogCameraAccessBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.btnDeny.setOnClickListener(view -> iBaseListener.deny());

        binding.btnAllow.setOnClickListener(view -> iBaseListener.allow());

    }

    public void init(IClickDialogCameraAccess iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
