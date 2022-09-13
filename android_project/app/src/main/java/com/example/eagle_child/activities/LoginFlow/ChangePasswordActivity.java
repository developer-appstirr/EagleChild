package com.example.eagle_child.activities.LoginFlow;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.example.eagle_child.R;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;


import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @Override
    public int getMainLayoutId() {

        return R.layout.activity_change_password;
    }

    @Override
    protected void onViewReady() {

    }

    @Override
    public int getFragmentFrameLayoutId() {
        return 0;
    }

    @Override
    public TitleBar getTitleBar() {
        return null;
    }

    public Boolean validate(){

        if (etPassword.getText().toString().isEmpty()){

            Utils.showToast(ChangePasswordActivity.this,getString(R.string.password_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }

        if (etConfirmPassword.getText().toString().isEmpty()){

            Utils.showToast(ChangePasswordActivity.this,getString(R.string.is_empty_confirm_password), AppConstant.TOAST_TYPES.INFO);

            return false;

        }


        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){

            Utils.showToast(ChangePasswordActivity.this,getString(R.string.is_confrim_password_match), AppConstant.TOAST_TYPES.INFO);

            return false;

        }


        return true;


    }

    @OnClick({R.id.btnSubmit,R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (validate()) {

                    Utils.showToast(ChangePasswordActivity.this,"Password has been changed successfully", AppConstant.TOAST_TYPES.SUCCESS);
                    Intent loginIntent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
                break;

            case R.id.ivBack:
                onPageBack();
                break;

            default:
                break;
        }
    }

}


