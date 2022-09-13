package com.example.eagle_child.activities;


import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.eagle_child.R;
import com.example.eagle_child.Utils.DeviceAdminDemo;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.fragments.HomeFragment;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.example.eagle_child.recievers.BroadcastRece;
import com.example.eagle_child.services.TService;
import com.example.eagle_child.vpn.VpnServicee;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import butterknife.BindView;

import static java.lang.Math.min;


public class HomeActivity extends BaseActivity{


    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.mTitleBar)
    TitleBar mTitleBar;

    BroadcastRece exampleBroadcastReceiver = new BroadcastRece();

    BasePreferenceHelper preferenceHelper;

    //record
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    //for vpn
    private static final int VPN_REQUEST_CODE = 0x0F;

    private boolean isStart;


    @Override
    public int getMainLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return R.id.fragmentContainer;
    }

    @Override
    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    protected void onViewReady() {

        setAndBindTitleBar();

 //       registerReceiver(vpnStateReceiver, new IntentFilter(VpnServicee.BROADCAST_VPN_STATE));

//        if(!isStart) {
//            startVPN();
//        }else{
//            sendBroadcast(new Intent(VpnServicee.BROADCAST_STOP_VPN));
//        }


        try {
            // Initiate DevicePolicyManager.
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, DeviceAdminDemo.class);

            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                // mDPM.lockNow();
                // Intent intent = new Intent(MainActivity.this,
                // TrackDeviceService.class);
                // startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        preferenceHelper = this.prefHelper;

        TedPermission.with(this)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_SMS,
                        Manifest.permission.RECORD_AUDIO,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        LocalBroadcastManager.getInstance(context).registerReceiver(exampleBroadcastReceiver,
                                new IntentFilter("com.eagle_child.GET_IMAGES"));

                        LocalBroadcastManager.getInstance(context).registerReceiver(exampleBroadcastReceiver,
                                new IntentFilter("com.eagle_child.GET_SMS"));

                        //false
                        if(!preferenceHelper.getAccessibility()) {
                            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                        }


                        AccessibilityManager accessibilityManager = (AccessibilityManager)context.getSystemService(Context.ACCESSIBILITY_SERVICE);
                        List<AccessibilityServiceInfo> runningservice = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

                        accessibilityManager.addAccessibilityStateChangeListener(new AccessibilityManager.AccessibilityStateChangeListener() {
                            @Override
                            public void onAccessibilityStateChanged(boolean b) {
                                preferenceHelper.setAccessibility(b);
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                        Toast.makeText(getApplicationContext(), "READ Permission denied", Toast.LENGTH_SHORT).show();

                    }
                }).check();





        replaceSupportFragment(new HomeFragment(), AppConstant.TRANSITION_TYPES.SLIDE);

    }

    private void setAndBindTitleBar() {
        mTitleBar.setVisibility(View.VISIBLE);
    }








//    public void postResultToServer(){
//
//        apiInterface = APIClient.getClient().create(APIInterface.class);
//
//        Map<String, Object> userInfoMap = new HashMap<>();
//
//        userInfoMap.put("list", resultList);
//
//
//        /**
//         GET List Resources
//         **/
//        Call<JsonElement> call = apiInterface.uploadResult(userInfoMap);
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                if (response != null){
//
//                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//
//
//
//            }
//        });
//
//
//
//    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            stopService(new Intent(HomeActivity.this, VpnServicee.class));
        }
    };

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (VpnServicee.BROADCAST_VPN_STATE.equals(intent.getAction()))
            {
                if (intent.getBooleanExtra("running", false))
                {
                    isStart = true;
                }
                else
                {
                    isStart =false;
                    handler.postDelayed(runnable,200);
                }
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK)
        {
            startService(new Intent(this, VpnServicee.class));
        }

        if (REQUEST_CODE == requestCode) {
            Intent intent = new Intent(this, TService.class);
            startService(intent);
        }

    }

    private void startVPN()
    {
        Intent vpnIntent = android.net.VpnService.prepare(this);
        if (vpnIntent != null)
        {
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        }
        else
        {
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(exampleBroadcastReceiver);
        LocalBroadcastManager.getInstance(this). unregisterReceiver(vpnStateReceiver);
        handler.removeCallbacks(runnable);
    }
}
