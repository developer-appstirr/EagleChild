package com.example.eagle_child.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.activities.HomeActivity;
import com.example.eagle_child.customViews.TitleBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

    protected BaseActivity activityReference;
    Unbinder unbinder;
    View rootView;


    //Abstract Methods
    protected abstract void setTitleBar(TitleBar titleBar);
    protected abstract int getMainLayout();
    protected abstract void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView);

    public abstract void onCustomBackPressed();
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReference = getBaseActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityReference = getBaseActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(getMainLayout(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        onFragmentViewReady(inflater, container, savedInstanceState, rootView);
        // Inflate the layout for this fragment
        return rootView;

    }

    protected BaseActivity getBaseActivity() {

        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        return null;
    }

    protected HomeActivity getHomeActivity() {

        if (getActivity() instanceof HomeActivity) {

            return (HomeActivity) getActivity();
        }
        return null;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getBaseActivity() != null) {
            setTitleBar(getBaseActivity().getTitleBar());
        }


    }


}