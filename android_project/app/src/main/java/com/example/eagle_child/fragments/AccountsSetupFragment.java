package com.example.eagle_child.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eagle_child.R;
import com.example.eagle_child.adapters.AppsAdapter;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.dialog.AppDialogs;
import com.example.eagle_child.models.AppModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class AccountsSetupFragment extends BaseFragment {


    @BindView(R.id.rvApps)
    RecyclerView rvApps;

    @BindView(R.id.txtNoResults)
    TextView txtNoResults;

    AppsAdapter appsAdapter;
    ArrayList<AppModel> listApp;

    void setListApp(ArrayList<AppModel> listApp){
        this.listApp = listApp;
    }

    @Override
    protected void setTitleBar(TitleBar titleBar) {
        titleBar.showHeaderView();
        titleBar.showHeaderTitle("Accounts Setup");
        titleBar.showLeftIconAndListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityReference.onBackPressed();
            }
        });


    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_accounts_setup;
    }


    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        if (listApp != null) {
            if (listApp.size() > 0) {
                appsAdapter = new AppsAdapter(activityReference, listApp);
                rvApps.setAdapter(appsAdapter);
                rvApps.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.GONE);
            } else {
                rvApps.setVisibility(View.GONE);
                txtNoResults.setVisibility(View.VISIBLE);
            }

        }else {
            rvApps.setVisibility(View.GONE);
            txtNoResults.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCustomBackPressed() {

        activityReference.onPageBack();

    }


    @OnClick({R.id.btnAddApp})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.btnAddApp:
                AddAppFragment addAppFragment = new AddAppFragment();
                activityReference.addSupportFragment(addAppFragment, AppConstant.TRANSITION_TYPES.SLIDE);

                break;


            default:
                break;

        }
    }

}

