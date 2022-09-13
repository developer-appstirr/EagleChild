package com.example.eagle_child.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.eagle_child.R;
import com.example.eagle_child.activities.HomeActivity;
import com.example.eagle_child.activities.LoginFlow.LoginActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.dialog.AppDialogs;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;

import butterknife.OnClick;


public class SettingFragment extends BaseFragment {


    public BasePreferenceHelper preferenceHelper;

    @Override
    protected void setTitleBar(TitleBar titleBar) {
        titleBar.showHeaderView();
        titleBar.showHeaderTitle("Settings");
        titleBar.showLeftIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityReference.onBackPressed();
            }
        });


    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_settings;
    }


    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        preferenceHelper = activityReference.prefHelper;
    }

    @Override
    public void onCustomBackPressed() {

        activityReference.onPageBack();

    }


    @OnClick({R.id.llEditProfile,R.id.llSignOut})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.llSignOut:
                AppDialogs appDialogs = new AppDialogs();
                appDialogs.signOutDialog(activityReference,preferenceHelper);
                break;

            case R.id.llEditProfile:
                ProfileFragment profileFragment = new ProfileFragment();
                activityReference.addSupportFragment(profileFragment, AppConstant.TRANSITION_TYPES.SLIDE);
                break;

            default:
                break;

        }
    }

}

