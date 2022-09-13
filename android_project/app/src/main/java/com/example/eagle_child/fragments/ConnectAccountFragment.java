package com.example.eagle_child.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.eagle_child.R;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.dialog.AppDialogs;
import com.example.eagle_child.models.AppModel;

import butterknife.BindView;
import butterknife.OnClick;


public class ConnectAccountFragment extends BaseFragment {

    @BindView(R.id.ivAppImage)
    ImageView ivAppImage;

    @BindView(R.id.txtAppName)
    TextView txtAppName;

    @BindView(R.id.btnRadioDevice)
    RadioButton btnRadioDevice;

    @BindView(R.id.btnRadioPassword)
    RadioButton btnRadioPassword;

    @BindView(R.id.btnRadio)
    RadioButton btnRadio;


    AppModel appModel;


    public void setAppModel(AppModel appModel){
        this.appModel = appModel;
    }

    @Override
    protected void setTitleBar(TitleBar titleBar) {
        titleBar.showHeaderView();
        titleBar.showHeaderTitle("Accounts");
        titleBar.showLeftIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityReference.onBackPressed();
            }
        });


    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_connect_account;
    }


    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {


        if(appModel!=null){
            txtAppName.setText("Connect John's " + appModel.getName());
            ivAppImage.setImageResource(appModel.getIcon());
        }

    }

    @Override
    public void onCustomBackPressed() {

        activityReference.onPageBack();

    }


    @OnClick({R.id.btnNext, R.id.btnRadioDevice, R.id.btnRadioPassword, R.id.btnRadio})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.btnNext:
                HomeFragment homeFragment = new HomeFragment();
                activityReference.addSupportFragment(homeFragment, AppConstant.TRANSITION_TYPES.SLIDE);
                break;

            case R.id.btnRadioDevice:
                btnRadioDevice.setChecked(true);
                btnRadio.setChecked(false);
                btnRadioPassword.setChecked(false);
                break;


            case R.id.btnRadioPassword:
                btnRadioDevice.setChecked(false);
                btnRadio.setChecked(false);
                btnRadioPassword.setChecked(true);
                break;

            case R.id.btnRadio:
                btnRadioDevice.setChecked(false);
                btnRadio.setChecked(true);
                btnRadioPassword.setChecked(false);
                break;

            default:
                break;

        }
    }

}

