package com.example.eagle_child.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.eagle_child.R;
import com.example.eagle_child.activities.LoginFlow.LoginActivity;
import com.example.eagle_child.adapters.OnboadingAdapter;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.example.eagle_child.models.AppModel;
import com.example.eagle_child.models.StoryboardingModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PagerActivity extends BaseActivity {


    @BindView(R.id.viewPager)
    ViewPager2 viewPager;

    @BindView(R.id.indicatorLinearLayout)
    LinearLayout indicatorLinearLayout;

    private OnboadingAdapter onboadingAdapter;
    TextView mNextTv;

    public BasePreferenceHelper preferenceHelper;

    static final String TAG = "PagerActivity";

    ArrayList<StoryboardingModel> listStoryBoarding;

    @Override
    public int getMainLayoutId() {
        return R.layout.activity_pager;
    }


    @Override
    protected void onViewReady() {

        listStoryBoarding = new ArrayList<>();

        StoryboardingModel storyboardingModel = new StoryboardingModel();
        storyboardingModel.setId("1");
        storyboardingModel.setHeadingText("Welcome");
        storyboardingModel.setParaText("Eagle Screen time parenting app has amazing features to protect your family! Swipe Right for more details");
        storyboardingModel.setBgImage(R.drawable.notes);

        StoryboardingModel storyboardingModel2 = new StoryboardingModel();
        storyboardingModel2.setId("1");
        storyboardingModel2.setHeadingText("Privacy Guidelines for Child");
        storyboardingModel2.setParaText("Eagle Screen time parenting app has amazing features to protect your family! Swipe Right for more details");
        storyboardingModel2.setBgImage(R.drawable.privacy);


        StoryboardingModel storyboardingModel3 = new StoryboardingModel();
        storyboardingModel3.setId("1");
        storyboardingModel3.setHeadingText("Data Security & Protection");
        storyboardingModel3.setParaText("Eagle Screen time parenting app has amazing features to protect your family! Swipe Right for more details");
        storyboardingModel3.setBgImage(R.drawable.security);



        listStoryBoarding.add(storyboardingModel);
        listStoryBoarding.add(storyboardingModel2);
        listStoryBoarding.add(storyboardingModel3);


        initStoryboardingAdapter(listStoryBoarding);



        preferenceHelper = this.prefHelper;

        mNextTv = (TextView) findViewById(R.id.tvNext);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentActiveIndicator(position);
                int cal = viewPager.getChildCount()-1;
                if(position == cal) {
                    mNextTv.setVisibility(View.VISIBLE);
                }
              //  else{
                   // mNextTv.setVisibility(View.GONE);
//                    Intent intentLogin = new Intent(PagerActivity.this, LoginActivity.class);
//                    startActivity(intentLogin);
//                    finish();
               // }


            }
        });




        mNextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(viewPager.getCurrentItem()+1< onboadingAdapter.getItemCount()){
                 viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
             }else{
                 Intent intentLogin = new Intent(PagerActivity.this, LoginActivity.class);
                 intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intentLogin);
                 finish();
             }
//             if(viewPager.getCurrentItem()+1 == 3){
//                 Intent intentLogin = new Intent(PagerActivity.this, LoginActivity.class);
//                 startActivity(intentLogin);
//                 finish();
//             }
            }
        });




    }

    @Override
    public int getFragmentFrameLayoutId() {
        return 0;
    }

    @Override
    public TitleBar getTitleBar() {
        return null;
    }


    public void initStoryboardingAdapter(List<StoryboardingModel> obj){


        onboadingAdapter = new OnboadingAdapter(getApplicationContext(),obj);
        viewPager.setAdapter(onboadingAdapter);
        setupIndicators();
        setCurrentActiveIndicator(0);


    }

    private void setupIndicators(){
        ImageView[] indicators = new ImageView[onboadingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10,0,10,0);

        for(int i=0;i<indicators.length;i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.non_active_dot));
            indicators[i].setLayoutParams(layoutParams);
            indicatorLinearLayout.addView(indicators[i]);
        }

    }

    private void setCurrentActiveIndicator(int index){
        int childCount = indicatorLinearLayout.getChildCount();
        for (int i=0;i<childCount;i++) {
            ImageView imageView = (ImageView) indicatorLinearLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.active_dot));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.non_active_dot));
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(preferenceHelper.getLoginStatus()){
            Intent intentLogin = new Intent(PagerActivity.this, HomeActivity.class);
            startActivity(intentLogin);
        }

//        if(preferenceHelper.getOnBoardingStatus()){
//
//            Intent intentLogin = new Intent(PagerActivity.this, LoginActivity.class);
//            startActivity(intentLogin);
//            finish();
//        }

    }


}