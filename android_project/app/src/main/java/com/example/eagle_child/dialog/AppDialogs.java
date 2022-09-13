package com.example.eagle_child.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.eagle_child.R;
import com.example.eagle_child.activities.LoginFlow.LoginActivity;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.example.eagle_child.listners.ChooseImageInterface;
import com.example.eagle_child.listners.DialogBoxListner;
import com.example.eagle_child.listners.ExitAppListner;

import org.w3c.dom.Text;


public class AppDialogs implements DialogBoxListner {

    Dialog dialog;

    public void showChooseImageDialog(final Activity activity, ChooseImageInterface chooseImageInterface){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_choose_image);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView btnGallery = dialog.findViewById(R.id.btnGallery);
        final TextView btnCamera = dialog.findViewById(R.id.btnCamera);
        final ImageView btnCross = dialog.findViewById(R.id.btnCross);


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                chooseImageInterface.chooseGalleryCallBack();
                // logoutInterface.logoutCallBack();
            }
        });

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // logoutInterface.logoutCallBack();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                chooseImageInterface.chooseCameraCallBack();
                // logoutInterface.logoutCallBack();
            }
        });


        dialog.setCancelable(false);

        //Dismiss the Dialog
        dialog.dismiss();
        //Show Dialog
        dialog.show();


    }

    public void exitAppDialog(final Activity activity, ExitAppListner exitAppListner){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_app);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView btnYes = dialog.findViewById(R.id.btnYes);
        final TextView btnNo = dialog.findViewById(R.id.btnNo);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAppListner.exitApp();
                dialog.dismiss();
                // logoutInterface.logoutCallBack();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // logoutInterface.logoutCallBack();
            }
        });

        dialog.setCancelable(false);

        //Show Dialog
        dialog.show();


    }


    public void signOutDialog(final Activity activity, BasePreferenceHelper preferenceHelper){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sign_out);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView btnYes = dialog.findViewById(R.id.btnYes);
        final TextView btnNo = dialog.findViewById(R.id.btnNo);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(activity, LoginActivity.class);
                preferenceHelper.removeLoginPreference();
                activity.startActivity(intentLogin);
                activity.finish();
                dialog.dismiss();
                // logoutInterface.logoutCallBack();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // logoutInterface.logoutCallBack();
            }
        });

        dialog.setCancelable(false);

        //Show Dialog
        dialog.show();


    }


    @Override
    public void dismiss() {
        dialog.dismiss();
    }


}
