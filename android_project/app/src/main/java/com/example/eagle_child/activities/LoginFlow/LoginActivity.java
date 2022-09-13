package com.example.eagle_child.activities.LoginFlow;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.R;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.activities.HomeActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.example.eagle_child.listners.MediaTypePicker;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements MediaTypePicker {


    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;


    public BasePreferenceHelper preferenceHelper;

    @Override
    public int getMainLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady() {
        preferenceHelper = this.prefHelper;
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return 0;
    }

    @Override
    public TitleBar getTitleBar() {
        return null;
    }

    @OnClick({R.id.btnSignIn,R.id.txtForgotPassword,R.id.ivScan})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btnSignIn:

                    if (validate()) {
                        Intent intentLogin = new Intent(LoginActivity.this, HomeActivity.class);
                        preferenceHelper.setLoginStatus(true);
                        startActivity(intentLogin);
                        finish();

                }
                break;


            case R.id.txtForgotPassword:

                Intent intentForgetPassword = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intentForgetPassword);

                break;

            case R.id.ivScan:
                openScannerCamera(LoginActivity.this);
                break;

            default:


        }
    }

    public Boolean validate(){

        if (etEmail.getText().toString().isEmpty()){

            Utils.showToast(this,getString(R.string.email_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }

        if (etPassword.getText().toString().isEmpty()){

            Utils.showToast(this,getString(R.string.password_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {

            Utils.showToast(this,getString(R.string.email_is_not_valid), AppConstant.TOAST_TYPES.INFO);

            return false;
        }


        return true;


    }

    @Override
    public void onPhotoClicked(ArrayList<File> file) {
     //   public void addSupportFragment(BaseFragment frag, int transition) {
        Intent scannerIntent = new Intent(LoginActivity.this, LoginBarcodeActivity.class);
        startActivity(scannerIntent);

    }


}