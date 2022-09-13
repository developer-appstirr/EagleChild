package com.example.eagle_child.webservices;


import com.example.eagle_child.constant.AppConstant;
import com.google.gson.JsonElement;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebService {

    // For image upload to server
    @Multipart
    @POST(AppConstant.ServerAPICalls.FILE_UPLOAD)
    Call<JsonElement> uploadImageToServer(
            @Part MultipartBody.Part image
    );

    @POST(AppConstant.ServerAPICalls.GALLERY)
    Call<JsonElement> uploadGuruProfileGallery(
            @Body Map<String,Object> body
    );



//    //For Document Form
//    @GET(AppConstant.ServerAPICalls.CUSTOM_DOCUMENT_FIELD)
//    Call<JsonElement> customDocument(
//            @Path("slug") String slug
//    );
//
//
//
//    //Get Form Quotes
//    @POST(AppConstant.ServerAPICalls.POST_QUOTES)
//    Call<JsonElement> getFormQuotes(
//            @Body Map<String, String> body
//
//    );
//
//
//    //Get dashboard category
//    @GET(AppConstant.ServerAPICalls.GET_CATEGORY)
//    Call<JsonElement> getAllImages(
//    );





}
