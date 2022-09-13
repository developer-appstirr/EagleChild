package com.example.eagle_child.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eagle_child.R;
import com.example.eagle_child.adapters.AddAppAdapter;
import com.example.eagle_child.adapters.AppsAdapter;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.listners.CheckBoxListner;
import com.example.eagle_child.models.AppModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class AddAppFragment extends BaseFragment implements CheckBoxListner {


    @BindView(R.id.rvAddApp)
    RecyclerView rvAddApp;

    AddAppAdapter addAppAdapter;

    ArrayList<AppModel> listApp;


    @Override
    protected void setTitleBar(TitleBar titleBar) {
        titleBar.showHeaderView();
        titleBar.showHeaderTitle("Add App");
        titleBar.showLeftIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityReference.onBackPressed();
            }
        });


    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_add_app;
    }


    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        listApp = new ArrayList<>();

        AppModel appModel = new AppModel();
        appModel.setName("Facebook");
        appModel.setIcon(R.drawable.facebook);
        appModel.setPackageName("com.facebook");
        appModel.setSelected(false);

        AppModel appModel1 = new AppModel();
        appModel1.setName("Twitter");
        appModel1.setIcon(R.drawable.twitter);
        appModel1.setPackageName("com.twitter");
        appModel1.setSelected(false);

        AppModel appModel2 = new AppModel();
        appModel2.setName("Instagram");
        appModel2.setIcon(R.drawable.insta);
        appModel2.setPackageName("com.instagram");
        appModel2.setSelected(false);

        listApp.add(appModel);
        listApp.add(appModel1);
        listApp.add(appModel2);


        addAppAdapter = new AddAppAdapter(activityReference,listApp,AddAppFragment.this);
        rvAddApp.setAdapter(addAppAdapter);

    }

    @Override
    public void onCustomBackPressed() {

        activityReference.onPageBack();

    }


    @OnClick({R.id.btnSave})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.btnSave:
                AccountsSetupFragment accountsSetupFragment = new AccountsSetupFragment();
                accountsSetupFragment.setListApp(listApp);
                activityReference.addSupportFragment(accountsSetupFragment, AppConstant.TRANSITION_TYPES.SLIDE);

                break;


            default:
                break;

        }
    }

    @Override
    public void setListApp(ArrayList<AppModel> listApp) {
        this.listApp = listApp;
    }
}

