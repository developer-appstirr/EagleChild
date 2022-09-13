package com.example.eagle_child.webhelpers;

import android.app.Activity;

import com.example.eagle_child.R;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.webservices.WebService;
import com.example.eagle_child.webservices.WebServiceFactory;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebApiRequest {

    private static WebService apiService;
    private static Activity currentActivity;
    private static WebApiRequest ourInstance = new WebApiRequest();


    public static WebApiRequest getInstance(Activity activity, boolean isShow) {

        apiService = WebServiceFactory.getInstance(activity);
        currentActivity = activity;
        if (isShow) {
            if (currentActivity instanceof BaseActivity) {


                //   ((BaseActivity) currentActivity).onLoadingStarted();
                //   KeyboardHelper.hideSoftKeyboard((currentActivity));


            }
        }
        return ourInstance;
    }

    private void showErrorMessage(String parseErrorMessage) {
       /* Utils.showSnackBar(currentActivity, currentActivity.getCurrentFocus(),
                parseErrorMessage, ContextCompat.getColor(currentActivity, R.color.red));*/

        Utils.showToast(currentActivity, parseErrorMessage, AppConstant.TOAST_TYPES.ERROR);


    }


    private String parseErrorMessage(JSONObject jsonObject) {

        for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
            String key = iter.next();
            JSONArray parentArray = null;
            try {
                parentArray = jsonObject.getJSONArray(key);

                return parentArray.get(0).toString();

                //  Utils.showToast(currentActivity,parentArray.get(0).toString(),AppConstant.TOAST_TYPES.ERROR);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return currentActivity.getString(Integer.parseInt("Something wrong"));
    }


    public interface APIRequestDataCallBack {
        void onSuccess(JsonElement response);

        void onError(JsonElement response);

        void onNoNetwork();
    }


    public void uploadImage(String userImgURI, final APIRequestDataCallBack apiRequestDataCallBack) {

        try {

            MultipartBody.Part body;
            if (!Utils.isEmptyOrNull(userImgURI)) {
                File file = new File(userImgURI);

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            } else {
                body = null;
            }


            Call<JsonElement> call = apiService.uploadImageToServer(body);
            call.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(Call<JsonElement> pCall, Response<JsonElement> response) {

                    if (currentActivity instanceof BaseActivity) {
                       // ((BaseActivity) currentActivity).onLoadingFinished();
                    }


                    if (response != null && response.body() != null) {
                        apiRequestDataCallBack.onSuccess(response.body());
                    } else {
                        try {

                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            showErrorMessage(parseErrorMessage(jsonObject));


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(final Call<JsonElement> pCall, Throwable throwable) {
                    if (currentActivity instanceof BaseActivity)
                       // ((BaseActivity) currentActivity).onLoadingFinished();
                    throwable.printStackTrace();
                    apiRequestDataCallBack.onError(null);

                    if (throwable instanceof Utils.NoConnectivityException) {
                        if (currentActivity instanceof BaseActivity)
                         //   ((BaseActivity) currentActivity).onLoadingFinished();
                        Utils.showToast(currentActivity, throwable.getMessage(), AppConstant.TOAST_TYPES.ERROR);

                    }
                }
            });


        } catch (Exception e) {

        }
    }


    public void uploadGallery(Map<String, Object> galleryInfo, final APIRequestDataCallBack apiRequestDataCallBack) {

        Call<JsonElement> call = apiService.uploadGuruProfileGallery(galleryInfo);
        call.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> pCall, Response<JsonElement> response) {

                if (currentActivity instanceof BaseActivity) {
                    //((BaseActivity) currentActivity).onLoadingFinished();
                }

                if (response != null && response.body() != null) {

                    apiRequestDataCallBack.onSuccess(response.body());

                } else {
                    try {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        showErrorMessage(parseErrorMessage(jsonObject));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(final Call<JsonElement> pCall, Throwable throwable) {
                if (currentActivity instanceof BaseActivity)
                   // ((BaseActivity) currentActivity).onLoadingFinished();
                throwable.printStackTrace();
                apiRequestDataCallBack.onError(null);

                if(throwable instanceof Utils.NoConnectivityException) {
                    if (currentActivity instanceof BaseActivity)
                     //   ((BaseActivity) currentActivity).onLoadingFinished();
                    Utils.showToast(currentActivity, throwable.getMessage(), AppConstant.TOAST_TYPES.ERROR);

                }
            }
        });

    }



}