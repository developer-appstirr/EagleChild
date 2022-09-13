package com.example.eagle_child.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.example.eagle_child.R;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.dialog.AppDialogs;

import butterknife.OnClick;


public class HomeFragment extends BaseFragment {

    @Override
    protected void setTitleBar(TitleBar titleBar) {
        titleBar.showHeaderView();
        titleBar.showHeaderTitle("Eagle");
        titleBar.showLeftIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityReference.onBackPressed();
            }
        });

        titleBar.showRightSettingIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingFragment settingFragment = new SettingFragment();
                activityReference.addSupportFragment(settingFragment, AppConstant.TRANSITION_TYPES.SLIDE);
            }
        });
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_home;
    }


    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {


    }

    @Override
    public void onCustomBackPressed() {

        activityReference.onPageBack();

    }



    @OnClick({R.id.btnAccountSetup})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btnAccountSetup:
                AccountsSetupFragment accountsSetupFragment = new AccountsSetupFragment();
                activityReference.addSupportFragment(accountsSetupFragment, AppConstant.TRANSITION_TYPES.SLIDE);
            break;


            default:
                break;

        }
    }

}

