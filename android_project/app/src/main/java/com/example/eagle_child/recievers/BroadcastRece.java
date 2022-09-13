package com.example.eagle_child.recievers;

import static android.content.Context.MODE_PRIVATE;

import static java.lang.Math.min;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.eagle_child.Utils.Utils;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.imagerecognition.Classifier;
import com.example.eagle_child.models.SmsModel;
import com.example.eagle_child.webhelpers.WebApiRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class BroadcastRece extends BroadcastReceiver {


    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "final_result";
    public static final String MODEL_FILE = "file:///android_asset/saved_model.pb";
    public static final String LABEL_FILE = "file:///android_asset/conv_actions_labels.txt";

    private static final float PROBABILITY_MEAN = 0.0f;

    //  private static final float PROBABILITY_STD = 1.0f;
    private static final float PROBABILITY_STD = 255.0f;

    /** Number of results to show in the UI. */
    private static final int MAX_RESULTS = 2;
    private int rotation = 90;

    /** Image size along the x axis. */
    private  int imageSizeX;

    /** Image size along the y axis. */
    private  int imageSizeY;

    protected Interpreter tflite;
    private Integer sensorOrientation;

    /** Options for configuring the Interpreter. */
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    /** Labels corresponding to the output of the vision model. */
    private List<String> labels;

    /** Input image TensorBuffer. */
    private TensorImage inputImageBuffer;

    /** Output probability TensorBuffer. */
    private TensorBuffer outputProbabilityBuffer;

    /** Processer to apply post processing of the output probability. */
    private TensorProcessor probabilityProcessor;
    protected ImageClassifier imageClassifier;
    //private APIInterface apiInterface;

    private ArrayList<String> resultList = new ArrayList<>();

    // do in back
    SharedPreferences prefImages,prefSms,prefVideo;
    ArrayList<String> diffListImage;
    ArrayList<SmsModel> diffListSms;
    ArrayList<String> diffListVideo;



    Context context;
    ArrayList<String> galleryResult;
    ArrayList<String> videoResult;
    ArrayList<String> txtResult;

    private class MyTaskGallery extends AsyncTask<Void, Void, List<String>>{
            @Override
            protected List<String> doInBackground(Void... params) {

                for(int i=0;i<diffListImage.size();i++){
                     String output = setImageToTensorFlowWithprobality(diffListImage.get(i),context);
                     galleryResult.add(diffListImage.get(i));

                    //calling the api

                    //save new photos to shared pref
                    SharedPreferences.Editor editor = prefImages.edit();
                    prefImages.getString("savingAllImages", "");
                    Gson gson = new Gson();
                    editor.putString("savingAllImages", gson.toJson(galleryResult));
                    editor.apply();
                }
                return galleryResult;
            }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
                for(String s : strings){
                    Utils.showToast(context,s,AppConstant.TOAST_TYPES.SUCCESS);
                }

            }


        }


    private class MyTaskVideo extends AsyncTask<Void, Void, List<String>>{
        @Override
        protected List<String> doInBackground(Void... params) {

            for(int i=0;i<diffListVideo.size();i++){
                videoResult.add(diffListVideo.get(i));

                //calling the api

                //save new photos to shared pref
                SharedPreferences.Editor editor = prefVideo.edit();
                prefVideo.getString("savingAllVideos", "");
                Gson gson = new Gson();
                editor.putString("savingAllVideos", gson.toJson(videoResult));
                editor.apply();
            }
            return videoResult;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            for(String s : strings){
                Utils.showToast(context,s,AppConstant.TOAST_TYPES.SUCCESS);
            }

        }


    }



    private class MyTaskSMS extends AsyncTask<Void, Void, List<String>>{

        @Override
        protected List<String> doInBackground(Void... params) {


            for (SmsModel smsModel : diffListSms){
                String output = getFromPython(smsModel.getMsg());
                if(output.equals("1")){
                  //  txtResult.add("Output: "+output+ " " +smsModel.getMsg());
                    txtResult.add(smsModel.getMsg());
                    Log.d("TAG",output);

                    //calling the api

                    //save new photos to shared pref
                    SharedPreferences.Editor editor = prefSms.edit();
                    prefImages.getString("savingAllSms", "");
                    Gson gson = new Gson();
                    editor.putString("savingAllSms", gson.toJson(txtResult));
                    editor.apply();



                }

            }
            return txtResult;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            for(String s : strings){
                Utils.showToast(context,s,AppConstant.TOAST_TYPES.SUCCESS);
            }

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       // sensorOrientation = rotation - getScreenOrientation(context);
        diffListImage = new ArrayList<>();
        diffListSms = new ArrayList<>();
        diffListVideo = new ArrayList<>();
        prefImages = context.getSharedPreferences("my_pref_images" , MODE_PRIVATE);


        galleryResult = new ArrayList<>();
        txtResult = new ArrayList<>();
        this.context = context;

        if ("com.eagle_child.GET_IMAGES".equals(intent.getAction())) {
            diffListImage = intent.getStringArrayListExtra("com.eagle_child.GALLERY_LIST");
            new MyTaskGallery().execute();
        }

        if ("com.eagle_child.GET_SMS".equals(intent.getAction())) {
            diffListSms = intent.getParcelableArrayListExtra("com.eagle_child.SMS_LIST");
            new MyTaskSMS().execute();
        }

        if ("com.eagle_child.GET_VIDEOS".equals(intent.getAction())) {
            diffListVideo = intent.getStringArrayListExtra("com.eagle_child.VIDEO_LIST");
            new MyTaskVideo().execute();
        }


    }

    private String getFromPython(String msg) {
        Python python = Python.getInstance();
        PyObject pythonFile = python.getModule("inference");
        return pythonFile.callAttr("getProb",msg).toString();
    }


    public String setImageToTensorFlowWithprobality(String bitmap, Context context){

        MappedByteBuffer tfliteModel = null;
        Classifier.Recognition recognition =null;
        try {

            tfliteModel = FileUtil.loadMappedFile(context, getModelPath());
            tfliteOptions.setUseXNNPACK(true);
            tfliteOptions.setNumThreads(2);
            tflite = new Interpreter(tfliteModel, tfliteOptions);

            // Loads labels out from the label file.
            labels = FileUtil.loadLabels(context, "mylabels.txt");

            // Reads type and shape of input and output tensors, respectively.
            int imageTensorIndex = 0;
            int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
            imageSizeY = imageShape[1];
            imageSizeX = imageShape[2];
            DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
            int probabilityTensorIndex = 0;
            int[] probabilityShape =
                    tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
            DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

            // Creates the input tensor.
            inputImageBuffer = new TensorImage(imageDataType);

            // Creates the output tensor and its processor.
            outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);

            // Creates the post processor for the output probability.
            probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

         //   Log.d("TAG", "Created a Tensorflow Lite Image Classifier.");

            //    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.porn9);



            Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(bitmap), 224, 224, true);
                                                                                   // sensorOrientation
            final List<Classifier.Recognition> results = recognizeImage(scaledBitmap );

        //    Log.i("listSize",""+results.size());




            recognition = results.get(0);
            Log.d("Name",recognition.getTitle());
            Log.d("Conf",""+recognition.getConfidence());

          /*  float result = (recognition.getConfidence() + 128);

            Log.i("Conf",""+result/255);*/
            Classifier.Recognition recognition1 = results.get(1);
            Log.d("Name",recognition1.getTitle());
            Log.d("Conf",""+recognition1.getConfidence());


            if (recognition.getTitle().equals("porn")){

                resultList.add(recognition.getConfidence().toString());
              //  Utils.showToast(context,recognition.getConfidence().toString()+" Adult content founded calling Gallery Api "+bitmap, AppConstant.TOAST_TYPES.SUCCESS);
               // Log.d("TAG",recognition.getConfidence().toString()+" Adult content founded calling Gallery Api "+bitmap);
            }





         /*   float result1 = (recognition1.getConfidence() + 128);

            Log.i("Conf",""+result1/255);*/

            close();



        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(context,"Crashed",Toast.LENGTH_SHORT).show();

        }


        return recognition.getConfidence().toString()+" Adult content founded calling Gallery Api "+bitmap;
    }


    public String getModelPath() {
        // you can download this file from
        // see build.gradle for where to obtain this file. It should be auto
        // downloaded into assets.
        return "model.tflite";
    }
    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
    }
    /** Runs inference and returns the classification results. */  //int sensorOrientation
    public List<Classifier.Recognition> recognizeImage(final Bitmap bitmap) {
        // Logs this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage");

        Trace.beginSection("loadImage");
        long startTimeForLoadImage = SystemClock.uptimeMillis();
        //sensorOrientation
        inputImageBuffer = loadImage(bitmap );
        long endTimeForLoadImage = SystemClock.uptimeMillis();
        Trace.endSection();
        //Log.v("TAG", "Timecost to load the image: " + (endTimeForLoadImage - startTimeForLoadImage));

        // Runs the inference call.
        Trace.beginSection("runInference");
        long startTimeForReference = SystemClock.uptimeMillis();


        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());


        long endTimeForReference = SystemClock.uptimeMillis();
        Trace.endSection();

      //  Log.v("TAG", "Timecost to run model inference: " + (endTimeForReference - startTimeForReference));

        // Gets the map of label and probability.
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        Trace.endSection();

        // Gets top-k results.
        return getTopKProbability(labeledProbability);


    }

    /** Loads input image, and applies preprocessing. */   // int sensorOrientation
    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = min(bitmap.getWidth(), bitmap.getHeight());
      //  int numRotation = sensorOrientation / 90;
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        // TODO(b/169379396): investigate the impact of the resize algorithm on accuracy.
                        // To get the same inference results as lib_task_api, which is built on top of the Task
                        // Library, use ResizeMethod.BILINEAR.
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                      //  .add(new Rot90Op(numRotation))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    protected TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    /** Gets the top-k results. */
    private static List<Classifier.Recognition> getTopKProbability(Map<String, Float> labelProb) {
        // Find the best classifications.
        PriorityQueue<Classifier.Recognition> pq =
                new PriorityQueue<>(
                        MAX_RESULTS,
                        new Comparator<Classifier.Recognition>() {
                            @Override
                            public int compare(Classifier.Recognition lhs, Classifier.Recognition rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });

        for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
            pq.add(new Classifier.Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
        }

        final ArrayList<Classifier.Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

    private TensorOperator getPostprocessNormalizeOp() {
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

//    protected int getScreenOrientation(Context context) {
//        switch (((Activity)context).getWindowManager().getDefaultDisplay().getRotation()) {
//            case Surface.ROTATION_270:
//                return 270;
//            case Surface.ROTATION_180:
//                return 180;
//            case Surface.ROTATION_90:
//                return 90;
//            default:
//                return 0;
//        }
//    }


    private void imageUploadToServer(String imagePath) {


//        if (imgProfile != null) {
//            profilePath = imgProfile.getAbsolutePath();
            WebApiRequest.getInstance((Activity) context, true).uploadImage( imagePath, new WebApiRequest.APIRequestDataCallBack() {
                @Override
                public void onSuccess(JsonElement response) {

                    if (response != null && response.isJsonObject()) {

                        JsonObject jsonObject = response.getAsJsonObject();
                        Gson gson = new Gson();
                        // FileUploadModel fileUploadModel  = gson.fromJson(jsonObject, new TypeToken<FileUploadModel>(){}.getType());
                      //  userProfileImageServerFileName = fileUploadModel.getData().getFile();



                    }
                }

                @Override
                public void onError(JsonElement response) {

                }

                @Override
                public void onNoNetwork() {

                }
            });



//        else {
//
//            userProfileImageServerFileName = fileUploadModel.getData().getFile();
//
//        }
    }

}
