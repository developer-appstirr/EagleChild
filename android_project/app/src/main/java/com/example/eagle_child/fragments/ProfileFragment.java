package com.example.eagle_child.fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.eagle_child.R;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.activities.LoginFlow.LoginActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.dialog.AppDialogs;
import com.example.eagle_child.listners.ChooseImageInterface;
import com.example.eagle_child.listners.MediaTypePicker;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends BaseFragment implements MediaTypePicker {

    @BindView(R.id.imvProfile)
    CircleImageView imvProfile;
    private File imgGallery = null;

    @BindView(R.id.etFullName)
    EditText etFullName;


    @BindView(R.id.etLastName)
    EditText etLastName;


    @BindView(R.id.etEmail)
    EditText etEmail;


    @Override
    protected void setTitleBar(TitleBar titleBar) {
        titleBar.showHeaderView();
        titleBar.showHeaderTitle("My Profile");
        titleBar.showLeftIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityReference.onBackPressed();
            }
        });

    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_profile;
    }


    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {


    }

    @Override
    public void onCustomBackPressed() {

        activityReference.onPageBack();

    }

    @OnClick({R.id.ivEditProfileImg,R.id.btnUpdate})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.btnUpdate:
                if(validate()) {
                    HomeFragment homeFragment = new HomeFragment();
                    activityReference.addSupportFragment(homeFragment, AppConstant.TRANSITION_TYPES.SLIDE);
                    Utils.showToast(activityReference, "Profile Updated.", AppConstant.TOAST_TYPES.SUCCESS);
                }
                break;

            case R.id.ivEditProfileImg:
                AppDialogs dialogs = new AppDialogs();
                dialogs.showChooseImageDialog(activityReference, new ChooseImageInterface() {
                    @Override
                    public void chooseGalleryCallBack() {

                        activityReference.openImagePicker(ProfileFragment.this);
                    }


                    @Override
                    public void chooseCameraCallBack() {

                        activityReference.openCameraPicker(ProfileFragment.this);

                    }


                });
                break;

            default:
                break;

        }
    }

    public Boolean validate(){


        if (etFullName.getText().toString().isEmpty()){

            Utils.showToast(activityReference,getString(R.string.fullName_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }
        if (etLastName.getText().toString().isEmpty()){

            Utils.showToast(activityReference,getString(R.string.lastName_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }

        if (etEmail.getText().toString().isEmpty()){

            Utils.showToast(activityReference,getString(R.string.email_is_empty), AppConstant.TOAST_TYPES.INFO);

            return false;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {

            Utils.showToast(activityReference,getString(R.string.email_is_not_valid), AppConstant.TOAST_TYPES.INFO);

            return false;
        }


        return true;


    }

    @Override
    public void onPhotoClicked(ArrayList<File> file) {
        if(file.size() > 0) {
            imgGallery = file.get(0);
            imvProfile.setImageBitmap(BitmapFactory.decodeFile(imgGallery.getAbsolutePath()));
        }
    }

}

