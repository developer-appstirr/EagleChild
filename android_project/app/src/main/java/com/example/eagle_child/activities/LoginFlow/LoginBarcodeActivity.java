package com.example.eagle_child.activities.LoginFlow;

import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;


import com.example.eagle_child.R;
import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LoginBarcodeActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    public BasePreferenceHelper preferenceHelper;


    @Override
    public void handleResult(Result rawResult) {

     /*   Utils.showToast(activityReference, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), AppConstant.TOAST_TYPES.INFO);
*/
        String[] separated = rawResult.getText().split(",");
        Log.i("string1",separated[0]);
        Log.i("string2",separated[1]);

       // loginStudentToServer(separated[0],separated[1]);
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mScannerView.resumeCameraPreview(LoginBarcodeActivity.this);
            }
        }, 2000);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }


    @Override
    public int getMainLayoutId() {
        return R.layout.fragment_barcode_scanner;
    }

    @Override
    protected void onViewReady() {
        preferenceHelper = prefHelper;

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(context);
        contentFrame.addView(mScannerView);

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return 0;
    }

    @Override
    public TitleBar getTitleBar() {
        return null;
    }
}
