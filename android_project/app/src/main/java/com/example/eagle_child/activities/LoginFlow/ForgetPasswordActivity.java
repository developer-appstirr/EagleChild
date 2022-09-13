package com.example.eagle_child.activities.LoginFlow;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.eagle_child.R;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;


public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;


    @Override
    public int getMainLayoutId() {
        return R.layout.activity_forget_pasword;
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

    @OnClick({R.id.btnNext,R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btnNext:


                if (validate()){

                    Intent intentForget = new Intent(ForgetPasswordActivity.this, ResetVerifyActivity.class);
                    startActivity(intentForget);


                }

                break;

            case R.id.ivBack:
                onPageBack();
                break;

            default:
                break;


        }
    }

    public Boolean validate(){



        if (etEmail.getText().toString().isEmpty()){

            Utils.showToast(this,getString(R.string.email_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }


        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {

            Utils.showToast(this,getString(R.string.email_is_not_valid), AppConstant.TOAST_TYPES.INFO);

            return false;
        }


        return true;


    }

}