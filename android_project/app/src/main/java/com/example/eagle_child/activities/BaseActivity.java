package com.example.eagle_child.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eagle_child.R;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.customViews.TitleBar;
import com.example.eagle_child.dialog.AppDialogs;
import com.example.eagle_child.fragments.BaseFragment;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.example.eagle_child.listners.ExitAppListner;
import com.example.eagle_child.listners.MediaTypePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yovenny.videocompress.MediaController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getMainLayoutId();

    protected abstract void onViewReady();
    public abstract int getFragmentFrameLayoutId();

    public abstract TitleBar getTitleBar();


    protected Context context;
    public BaseFragment baseFragment;
    public static final String KEY_FRAG_FIRST = "firstFrag";

    public BasePreferenceHelper prefHelper;

    MediaTypePicker mediaPickerListener;
    private static final int CAMERA_PIC_REQUEST = 110;

    ArrayList<String> photoPaths;



    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed_Videos/";
    public static final String TEMP_DIR = "/Temp/";

    public static Dialog dialog ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getMainLayoutId());
        ButterKnife.bind(this);
        context = BaseActivity.this;
        prefHelper = new BasePreferenceHelper(context);

        onViewReady();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public <T> void changeActivity(Class<T> cls, boolean isActivityFinish) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        startActivity(intent);
        if (isActivityFinish) {
            finish();
        }

    }

    public void realAddSupportFragment(BaseFragment frag, int transition) {

        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (transition == AppConstant.TRANSITION_TYPES.FADE)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        else if (transition == AppConstant.TRANSITION_TYPES.SLIDE)
            transaction.setCustomAnimations(R.anim.anim_enter, 0);

        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(getFragmentFrameLayoutId(), frag, frag.getClass().getSimpleName());

        transaction.addToBackStack(
                getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                        : null).commitAllowingStateLoss();// AllowingStateLoss();

    }

    public void replaceSupportFragment(BaseFragment frag, int transition) {

        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (transition == AppConstant.TRANSITION_TYPES.FADE)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        else if (transition == AppConstant.TRANSITION_TYPES.SLIDE)
            transaction.setCustomAnimations(R.anim.anim_enter, 0);
        //transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );

        transaction.replace(getFragmentFrameLayoutId(), frag, frag.getClass().getName());
        transaction.commitAllowingStateLoss();// AllowingStateLoss();
    }

    public void addSupportFragment(BaseFragment frag, int transition) {

//        if(isTransactionSafe){
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (transition == AppConstant.TRANSITION_TYPES.FADE)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        else if (transition == AppConstant.TRANSITION_TYPES.SLIDE)
            transaction.setCustomAnimations(R.anim.anim_enter, 0);
        //transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );

        transaction.replace(getFragmentFrameLayoutId(), frag, frag.getClass().getName());
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ?
                KEY_FRAG_FIRST : null)
                .commitAllowingStateLoss();// AllowingStateLoss();
//        }else{
//             /*
//     If any transaction is not done because the activity is in background. We set the
//     isTransactionPending variable to true so that we can pick this up when we come back to
//foreground
//     */
//            isTransactionPending = true;
//        }
    }

    public void replaceSupportFragmentWithContainer(int framLayoutId, Fragment frag, int transition) {

        //   baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (transition == AppConstant.TRANSITION_TYPES.FADE)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        else if (transition == AppConstant.TRANSITION_TYPES.SLIDE)
            transaction.setCustomAnimations(R.anim.anim_enter, 0);
        //transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );

        transaction.replace(framLayoutId, frag);
        transaction.commit();

     /*   transaction.replace(framLayoutId, frag, frag.getClass().getName());
        transaction.commitAllowingStateLoss();// AllowingStateLoss();*/
    }

    public void changeToHomeFragment(BaseFragment frag){

        emptyBackStack();

        replaceSupportFragment(frag,AppConstant.TRANSITION_TYPES.SLIDE);

    }


    public void emptyBackStack() {
        try {
            popBackStackTillEntry(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStackImmediate(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void onPageBack() {
        //  if (!loading) {
        super.onBackPressed();
    /*    } else {
            Utils.showToast(context, context.getString(R.string.please_wait_data_is_loading), AppConstant.TOAST_TYPES.INFO);
        }*/
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            baseFragment.onCustomBackPressed();

        }else {

            exitAppDialog();

        }

    }


    public void openScannerCamera(final MediaTypePicker listener){

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        listener.onPhotoClicked(null);

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }

                }).check();

    }


    public void  progressDialog(final Activity activity){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.setCancelable(false);

        //Dismiss the Dialog



    }



    public void onLoadingStarted() {

       progressDialog((Activity)context);

        if (dialog == null) {
            return;
        }
        dialog.show();
    }


    public void onLoadingFinished() {
       // progressDialog((Activity)context);
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    public void openImagePicker(final MediaTypePicker listener) {
        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(AppConstant.SELECT_IMAGE_COUNT)
                                //.setSelectedFiles(photoPaths)
                                //.setActivityTheme(R.style.AppTheme)
                                .enableVideoPicker(false)
                                .enableCameraSupport(false)
                                .showGifs(false)
                                .enableSelectAll(false)
                                .showFolderView(false)
                                .enableImagePicker(true)
                                .pickPhoto(BaseActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }

                }).check();


    }


    public void openCameraPicker(final MediaTypePicker listener) {

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }

                }).check();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    new AsyncTaskRunner().execute(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }

                break;
//            case FilePickerConst.REQUEST_CODE_DOC:
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    mediaPickerListener.onDocClicked(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
//                }
//                break;
            case CAMERA_PIC_REQUEST:
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                    ArrayList<String> cameraPic = new ArrayList<>();
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(this, (Bitmap) data.getExtras().get("data"));
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    cameraPic.add(getRealPathFromURI(tempUri));

                    new AsyncTaskRunner().execute(cameraPic);
                }
                break;
            default:
                break;
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private class AsyncTaskRunner extends AsyncTask<ArrayList<String>, ArrayList<File>, ArrayList<File>> {

        ProgressDialog progressDialog;

        @Override
        protected ArrayList<File> doInBackground(ArrayList<String>... params) {

            ArrayList<File> compressedAndVideoImageFileList = new ArrayList<>();

            for (int index = 0; index < params[0].size(); index++) {

                File file = new File(params[0].get(index));

                if (file.toString().endsWith(".jpg") ||
                        file.toString().endsWith(".jpeg") ||
                        file.toString().endsWith(".png") ||
                        file.toString().endsWith(".gif")) {

                    try {
                        File compressedImageFile = new Compressor(BaseActivity.this).compressToFile(file, "compressed_" + file.getName());
                        compressedAndVideoImageFileList.add(compressedImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {


                    if (!file.toString().endsWith(".3gp")) {

                        String compressVideoPath = Environment.getExternalStorageDirectory()
                                + File.separator
                                + APP_DIR
                                + COMPRESSED_VIDEOS_DIR
                                + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";

                        boolean isCompressSuccess = MediaController.getInstance().convertVideo(file.getAbsolutePath(), compressVideoPath);

                        if (isCompressSuccess) {
                            compressedAndVideoImageFileList.add(new File(compressVideoPath));
                        } else {
                            compressedAndVideoImageFileList.add(file);
                        }

                    } else {

                        compressedAndVideoImageFileList.add(file);
                    }
                }
            }
            return compressedAndVideoImageFileList;
        }


        @Override
        protected void onPostExecute(ArrayList<File> result) {
            // execution of result of Long time consuming operation

            if (progressDialog != null)
                progressDialog.dismiss();
            mediaPickerListener.onPhotoClicked(result);
        }


        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(BaseActivity.this,
                        context.getString(R.string.app_name),
                        context.getString(R.string.compressing_please_wait));
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }



    public void exitAppDialog(){

        if(isTaskRoot()) {
            AppDialogs appDialogs = new AppDialogs();
            appDialogs.exitAppDialog((Activity) context, new ExitAppListner() {

                @Override
                public void exitApp() {
                    finish();
                }
            });
        }
        else{
            AppDialogs appDialogs = new AppDialogs();
            appDialogs.exitAppDialog((Activity) context, new ExitAppListner() {

                @Override
                public void exitApp() {
                    finishAffinity();
                }
            });

           // super.onBackPressed();
        }
    }


}
