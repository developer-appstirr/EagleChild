package com.example.eagle_child.services;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.eagle_child.R;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.activities.HomeActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.models.AppModel;
import com.example.eagle_child.models.SmsModel;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppDetectService extends AccessibilityService {

    ArrayList<String> imageList;
    ArrayList<String> existingList;
    ArrayList<AppModel> appList;
    ArrayList<SmsModel> smsList;
    ArrayList<SmsModel> existingListSms;

    ArrayList<String> audioList;
    ArrayList<String> existingAudioList;

    ArrayList<String> videoList;
    ArrayList<String> existingVideoList;
    String[] projection = {MediaStore.MediaColumns.DATA};
    SharedPreferences prefImages,prefSms,prefVideo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            Log.i("Accessibility",""+event.getPackageName());
           Log.i("TAG",""+event.getText());
           // Toast.makeText(getApplicationContext(), event.getPackageName(), Toast.LENGTH_SHORT).show();

        }
        Log.i("Accessibility",""+event.getText());


        //getting new image from photos or gallery
        if (event.getText().toString().matches("(.*)Photos(.*)") || event.getText().toString().matches("(.*)Gallery(.*)")){
          // findNewImages();
            findNewVideos();
        }

        //getting new messages from photos or gallery
        if (event.getText().toString().matches("(.*)Messages(.*)") || event.getText().toString().matches("(.*)Inbox(.*)")){

           findNewMessages();

        }

        getAllAudio();


    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.i("Interrupt", "Interrupt");
        Toast.makeText(getApplicationContext(), "onInterrupt", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

     //   Log.i("Service", "Connected");
        Toast.makeText(getApplicationContext(), "ServiceConnected", Toast.LENGTH_SHORT).show();
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.eagle_child");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }

        prefImages = getSharedPreferences("my_pref_images" , MODE_PRIVATE);
        prefSms = getSharedPreferences("my_pref_sms" , MODE_PRIVATE);

        //getting images from mobile
        imageList = new ArrayList<>();
        existingList = new ArrayList<>();
       // findNewImages();

        //getting sms messages from mobile
        smsList = new ArrayList<>();
        existingListSms = new ArrayList<>();
       //findNewMessages();

        //getting all videos from mobile
        videoList = new ArrayList<>();
        existingVideoList = new ArrayList<>();

        //getting audio from mobile
        audioList = new ArrayList<>();
        existingAudioList = new ArrayList<>();





        //getting all the mobile apps
//        appList = new ArrayList<>();
//        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
//        for (int i=0; i < packList.size(); i++)
//        {
//            PackageInfo packInfo = packList.get(i);
//            if ( ApplicationInfo.FLAG_SYSTEM != 0) {
//                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//                String packageName = packInfo.packageName;
//                String pkg = packageName;//your package name
//                try {
//                    Drawable icon = this.getPackageManager().getApplicationIcon(pkg);
//                    Log.d("App name " + Integer.toString(i), appName);
//                    Log.d("Package name " + Integer.toString(i), packageName);
//                    Log.d("App Icon " + Integer.toString(i), String.valueOf(icon));
//
//                    AppModel appModel2 = new AppModel();
//                    appModel2.setName(appName);
//                    //add icon here
//                    appModel2.setIcon(0);
//                    appModel2.setPackageName(packageName);
//                    appModel2.setSelected(false);
//
//                    appList.add(appModel2);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


    }

    public void getAllAudio(){
        audioList.clear();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfAudio = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            audioList.add(absolutePathOfAudio);
            Log.d("TAG",absolutePathOfAudio);
        }
        cursor.close();
    }

    public void getAllVideos(){
        videoList.clear();
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfVideo = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            videoList.add(absolutePathOfVideo);
            Log.d("TAG",absolutePathOfVideo);
        }
        cursor.close();
    }

    public void getVideosFromSharedPref(){

        String s = prefVideo.getString("savingAllVideos", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        existingVideoList = gson.fromJson(s, type);

    }


    public void findNewVideos(){
        getAllVideos();
        getVideosFromSharedPref();
        Collection<String> listNew = videoList; // all images from gallery
        Collection<String> listOld = existingVideoList; // all images from shared pref

        Collection<String> similar = new HashSet<>(listNew);
        Collection<String> different = new HashSet<>(listNew);

        if(listOld!=null)
            different.addAll( listOld );

        if(listOld!=null){
            similar.retainAll( listOld );
            different.removeAll( similar );
        }

        Log.d("TAG", Arrays.toString(different.toArray()));
        Log.d("TAG", String.valueOf(different.toArray().length));

        if(different.size()==0){
            //if no new new image do nothing
            //   Log.d("TAG", "New image is not found");
            Utils.showToast(getApplicationContext(),"No new video found",AppConstant.TOAST_TYPES.SUCCESS);


        }else {
            //send new image to server
            ArrayList<String> newList = new ArrayList<>(different);

            Intent intent1 = new Intent("com.eagle_child.GET_VIDEOS");
            intent1.putStringArrayListExtra("com.eagle_child.VIDEO_LIST", newList);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            Utils.showToast(getApplicationContext(),different.size()+" New video found",AppConstant.TOAST_TYPES.SUCCESS);
        }


    }


    private void getAllMsgs() {
            smsList.clear();
        Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int totalSMS = c.getCount();
        if(totalSMS!=0) {
            if (c.moveToFirst()) { // must check the result to prevent exception
                for (int i = 0; i < totalSMS; i++) {
                    SmsModel objSms = new SmsModel();
                    //  objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    smsList.add(objSms);
                  //  Log.d("TAG", c.getString(c.getColumnIndexOrThrow("address")));

                   // Log.d("TAG", c.getString(c.getColumnIndexOrThrow("body")));
                    //  objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    //    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
//                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
//                    objSms.setFolderName("inbox");
//                } else {
//                    objSms.setFolderName("sent");
//                }
                c.moveToNext();
                }
            }
            c.close();


        }else{
            Log.d("TAG","no new messages");
        }

    }

    public void getTextFromSharedPref(){
        String s = prefSms.getString("savingAllSms", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SmsModel>>(){}.getType();
        existingListSms = gson.fromJson(s, type);
    }


    public void findNewMessages(){

        getAllMsgs();
        getTextFromSharedPref();

        Collection<SmsModel> listNewSms = smsList; // all new sms from gallery
        Collection<SmsModel> listOldSms = existingListSms; // all old sms from shared pref

        Collection<SmsModel> similar = new HashSet<>(listNewSms);
        Collection<SmsModel> different = new HashSet<>(listNewSms);



        if(listOldSms!=null){
            different.addAll( listOldSms );
            similar.retainAll( listOldSms );
            different.removeAll( similar );
        }

        Log.d("TAG", Arrays.toString(different.toArray()));
        Log.d("TAG", String.valueOf(different.toArray().length));

        if(different.size()==0){
            Utils.showToast(getApplicationContext()," No new sms found",AppConstant.TOAST_TYPES.SUCCESS);

        }else {
            //save new sms to shared pref
            ArrayList<SmsModel> newList = new ArrayList<>(different);


            Intent intent = new Intent("com.eagle_child.GET_SMS");
            intent.putExtra("com.eagle_child.SMS_LIST", newList);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Utils.showToast(getApplicationContext(),different.size()+" New sms found",AppConstant.TOAST_TYPES.SUCCESS);
       }

    }

    public void getAllImages(){
            imageList.clear();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            imageList.add(absolutePathOfImage);
        }
        cursor.close();
    }

    public void getImageFromSharedPref(){

        String s = prefImages.getString("savingAllImages", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        existingList = gson.fromJson(s, type);


    }

    public void findNewImages(){

        getAllImages();

        getImageFromSharedPref();
        Collection<String> listNew = imageList; // all images from gallery
        Collection<String> listOld = existingList; // all images from shared pref

        Collection<String> similar = new HashSet<>(listNew);
        Collection<String> different = new HashSet<>(listNew);

        if(listOld!=null)
            different.addAll( listOld );

        if(listOld!=null){
            similar.retainAll( listOld );
            different.removeAll( similar );
        }

        Log.d("TAG", Arrays.toString(different.toArray()));
        Log.d("TAG", String.valueOf(different.toArray().length));

        if(different.size()==0){
            //if no new new image do nothing
         //   Log.d("TAG", "New image is not found");
            Utils.showToast(getApplicationContext(),"No new image found",AppConstant.TOAST_TYPES.SUCCESS);


        }else {
            //send new image to server
            ArrayList<String> newList = new ArrayList<>(different);

            Intent intent1 = new Intent("com.eagle_child.GET_IMAGES");
            intent1.putStringArrayListExtra("com.eagle_child.GALLERY_LIST", newList);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            Utils.showToast(getApplicationContext(),different.size()+" New image founded",AppConstant.TOAST_TYPES.SUCCESS);
        }


    }




}
