package com.example.eagle_child.webservices;

import android.app.Activity;

import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.helpers.preference.BasePreferenceHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceFactory {


    private static WebService webservice;

    public static WebService getInstance(final Activity activity) {

        final BasePreferenceHelper prefHelper = new BasePreferenceHelper(activity.getApplicationContext());

        if (webservice == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.callTimeout(1, TimeUnit.MINUTES);
            httpClient.connectTimeout(20, TimeUnit.SECONDS);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.writeTimeout(30, TimeUnit.SECONDS);




//             add your other interceptors …

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    // Request customization: add request headers
                    Request request = null;

                    Request.Builder requestBuilder = original.newBuilder();

                    //   if(!original.url().url().getHost().contains("insight.sout")){
                    String tokenValue = "";
                    if (!Utils.isEmptyOrNull(prefHelper.getUserToken())) {
                        tokenValue = prefHelper.getUserToken();
                        requestBuilder.addHeader("Accept", "application/json");
                        requestBuilder.addHeader("Authorization", String.format("Bearer %s", tokenValue));

                    }
                    //  }

                    request = requestBuilder.build();

                    return chain.proceed(request);

                }
            });
            httpClient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstant.ServerAPICalls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();

            webservice = retrofit.create(WebService.class);
        }


        return webservice;
    }

}

