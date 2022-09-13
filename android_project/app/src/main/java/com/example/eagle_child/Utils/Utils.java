package com.example.eagle_child.Utils;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;

import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.R;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.listners.DateListner;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.IOException;
import java.time.LocalDateTime;

import es.dmoral.toasty.Toasty;


public class Utils {

    public int selectedHour = 0;
    public int selectedMinute = 0;

    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }


    public static boolean isEmptyOrNull(String string) {
        if (string == null)
            return true;

        return (string.trim().length() <= 0);
    }


    public static class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "No Internet Connection";
            // You can send any message whatever you want from here.
        }
    }


    public static void datePicker(FragmentManager fragmentManager, DateListner dateListner){
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.show(fragmentManager, "MATERIAL_DATE_PICKER");


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateListner.setDate(materialDatePicker.getHeaderText());
                        //  tvPolicyDate.setText(materialDatePicker.getHeaderText());
                    }
                });

    }

    public static void showToast(Context ctx, String msg, int type) {

        switch (type){
            case AppConstant.TOAST_TYPES.INFO:
                Toasty.custom(ctx, msg, R.drawable.ic_error, R.color.golden,  Toast.LENGTH_SHORT,true,true).show();
                break;

            case AppConstant.TOAST_TYPES.ERROR:
                Toasty.custom(ctx, msg, R.drawable.ic_error, R.color.red,  Toast.LENGTH_SHORT,true,true).show();
                break;

            case AppConstant.TOAST_TYPES.SUCCESS:
                Toasty.custom(ctx, msg, R.drawable.ic_check, R.color.primaryBlue,  Toast.LENGTH_SHORT,true,true).show();
               // Toasty.success(ctx, msg,).show();
                break;

        }
    }


}
