package com.example.eagle_child.customViews;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import com.example.eagle_child.R;


public class TitleBar extends FrameLayout {



    LinearLayout llBackLayout;
    public ImageView ivLeftBackIcon,ivSettingIcon;
    public TextView tvTitle;
    TextView tvLeftText;
    FrameLayout headerLayout;
    public FrameLayout flBackGround;
    LinearLayout llAppLogoSection;

    private Context context;

    public TitleBar(Context context) {
        super(context);
        this.context = context;
        initLayout(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.titlebar, this);


        bindViews();
        resetViews();

    }


    public void resetViews() {

        headerLayout.setVisibility(GONE);
        llBackLayout.setVisibility(GONE);
        ivSettingIcon.setVisibility(GONE);
        ivLeftBackIcon.setVisibility(GONE);
        llBackLayout.setOnClickListener(null);



    }

    private void bindViews() {

        headerLayout = (FrameLayout) this.findViewById(R.id.headerLayout);
        flBackGround = (FrameLayout) this.findViewById(R.id.flBackGround);
        llBackLayout = (LinearLayout) this.findViewById(R.id.llBackLayout);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        llAppLogoSection = (LinearLayout) this.findViewById(R.id.llAppLogoSection);
        tvLeftText = (TextView) this.findViewById(R.id.tvLeftText);
        ivLeftBackIcon = (ImageView) this.findViewById(R.id.ivLeftBackIcon);
        ivSettingIcon = (ImageView) this.findViewById(R.id.ivSettingIcon);
    }




    public void showHeaderView(){
        resetViews();
        headerLayout.setVisibility(View.VISIBLE);

    }


    public void hideHeaderView(){
        headerLayout.setVisibility(View.GONE);
    }

    public void showBackMenuView(){
        llBackLayout.setVisibility(View.VISIBLE);
    }


    public void showLeftIconAndListener(OnClickListener onClickListener) {

        showBackMenuView();

        ivLeftBackIcon.setVisibility(View.VISIBLE);
        ivLeftBackIcon.setOnClickListener(onClickListener);
//        AnimationHelpers.animate(Techniques.BounceInLeft, 500, ivLeftBackIcon);
    }

    public void showRightSettingIconAndListener(OnClickListener onClickListener) {

        showBackMenuView();

        ivSettingIcon.setVisibility(View.VISIBLE);
        ivSettingIcon.setOnClickListener(onClickListener);
    }


    public void showLeftIconAndListener(int iconResId, OnClickListener onClickListener) {
        ivLeftBackIcon.setVisibility(View.VISIBLE);
        ivLeftBackIcon.setImageResource(iconResId);
        ivLeftBackIcon.setOnClickListener(onClickListener);
//        AnimationHelpers.animate(Techniques.BounceInLeft, 500, ivLeftBackIcon);
    }




    public void setLeftTitleText(String text) {

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        @ColorInt int color;
        if(text.equals(getContext().getString(R.string.app_name))){
          //  viewLeftSeperator.setVisibility(GONE);
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        }
        else{
         //   viewLeftSeperator.setVisibility(View.VISIBLE);
            theme.resolveAttribute(R.color.black, typedValue, true);
        }

        color = typedValue.data;

        tvLeftText.setVisibility(View.VISIBLE);
        tvLeftText.setText(text);
        tvLeftText.setTextColor(color);
    }


    public void showHeaderTitle() {
        llAppLogoSection.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void showHeaderTitle(int gravity) {
        llAppLogoSection.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        llAppLogoSection.setGravity(gravity);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        if(gravity != Gravity.CENTER)
            params.setMarginStart(35);
        else
            params.setMarginStart(0);

        params.gravity = gravity;
        llAppLogoSection.setLayoutParams(params);
    }


    public void showHeaderTitle(String text) {
        llAppLogoSection.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(text);
    }



}

