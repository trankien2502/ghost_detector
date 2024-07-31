package com.ghostdetctor.ghost_detector.ui.ghost;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ghostdetctor.ghost_detector.base.BaseActivity;
import com.ghostdetctor.ghost_detector.dialog.camera_access.CameraAccessDialog;
import com.ghostdetctor.ghost_detector.dialog.camera_access.IClickDialogCameraAccess;
import com.ghostdetctor.ghost_detector.ui.home.HomeActivity;
import com.ghostdetctor.ghost_detector.util.EventTracking;
import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;
import com.ghostdetector.ghost_detector.databinding.ActivityOptionBinding;

public class OptionActivity extends BaseActivity<ActivityOptionBinding> {

    private final int TYPE_HORROR_GHOSTS = 1;
    private final int TYPE_SCARY_SPIRITS = 2;
    private final int REQUEST_CODE_CAMERA_PERMISSION = 130;
    private int countCamera = 0;
    private int ghostType = 0;
    @Override
    public ActivityOptionBinding getBinding() {
        return ActivityOptionBinding.inflate(getLayoutInflater());
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
            if (checkCameraPermission()){
                resultLauncher.launch(new Intent(OptionActivity.this, GhostActivity.class));
                SPUtils.setInt(this,SPUtils.GHOST_TYPE,ghostType);
                finish();
            }else {
                accessCamera();
            }
        });

    }
    private void accessCamera(){
        CameraAccessDialog cameraAccessDialog = new CameraAccessDialog(this,false);
        cameraAccessDialog.init(new IClickDialogCameraAccess() {
            @Override
            public void deny() {
                cameraAccessDialog.dismiss();
            }

            @Override
            public void allow() {
                ActivityCompat.requestPermissions(OptionActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                cameraAccessDialog.dismiss();
            }
        });
        try {
            cameraAccessDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                resultLauncher.launch(new Intent(OptionActivity.this, GhostActivity.class));
                SPUtils.setInt(this,SPUtils.GHOST_TYPE,ghostType);
                finish();
            }

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        SPUtils.getInt(this,SPUtils.CAMERA,0);
                        countCamera++;
                        SPUtils.setInt(this,SPUtils.CAMERA, countCamera);
                        if (countCamera > 1) {
                            showDialogGotoSetting();
                        }
                    }
                }
            }
        }
    }

    private void showDialogGotoSetting() {
        Toast.makeText(this, getString(R.string.permission_denied_please_go_to_setting_to_enable), Toast.LENGTH_SHORT).show();
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
       if (result.getResultCode()==RESULT_OK){
           Log.d("activity_check","ghost_scan");
       }
    });

    @Override
    public void onBackPressed() {
        EventTracking.logEvent(this,"option_back_click");
        setResult(RESULT_OK);
        finish();
    }
}