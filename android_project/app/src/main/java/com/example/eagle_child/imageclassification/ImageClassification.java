package com.example.eagle_child.imageclassification;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eagle_child.R;
import com.example.eagle_child.imagerecognition.Classifier;

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
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.metadata.MetadataExtractor;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.Math.min;

public class ImageClassification extends AppCompatActivity {

    public ImageView imgvSelect;
    public Button btnSelect;
    //for CameraIntent
    private int PICK_IMAGE_REQUEST = 1;

    public Executor executor = Executors.newSingleThreadExecutor();
    public static final int INPUT_SIZE = 224;
  /*  public static final int IMAGE_MEAN = 128;
    public static final float IMAGE_STD = 128.0f;*/
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

    public ArrayList<String> list = new ArrayList<>();
   // String obj[] = new String[]{"normal","porn"};

    /** Input image TensorBuffer. */
    private TensorImage inputImageBuffer;

    /** Output probability TensorBuffer. */
    private TensorBuffer outputProbabilityBuffer;

    /** Processer to apply post processing of the output probability. */
    private TensorProcessor probabilityProcessor;
    protected ImageClassifier imageClassifier;
    private ArrayList<String> imageList;
    private  BroadcastReceiver broadcastReceiver;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_select_image);

        //    imageList = getIntent().getStringArrayListExtra("IMGAES");

      //  imgvSelect = findViewById(R.id.imvSelect);
     //   btnSelect = findViewById(R.id.btnSelectImage);

       // list.add("normal");


        sensorOrientation = rotation - getScreenOrientation();

      //  initTensorFlowAndLoadModel();


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            /*    for (int i = 0; i < imageList.size(); i++){

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    File image = new File(imageList.get(i), "imageName");
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                    setImageToTensorFlowWithprobality(bitmap);

                }
*/
                setImageToTensorFlowWithprobality();

              //  setImageToTensorFlowWithProbablity2();
              // selectPhoto();


            }
        });

    }

   /* private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
*/

    public void selectPhoto()
    {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);

    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try {

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);



           //    setImageToTensor(selectedImage);

           //     setImageToTensorFlow(selectedImage);
             //   setImageToTensorFlowWithprobality();
                //setImageToTensorFlowWithProbablity2();


             //   setPicture(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }

        }
    }

    //set Picture in the ImageView (iV1)
    public void setPicture(Bitmap bp)
    {
        Bitmap scaledBp =  Bitmap.createScaledBitmap(bp, imgvSelect.getWidth(), imgvSelect.getHeight(), false);
        imgvSelect.setImageBitmap(scaledBp);
    }

   /* public List<Classifier.Recognition> analyse(Bitmap bitmap)
    {

        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
        return results;

    }*/


    public String getModelPath() {
        // you can download this file from
        // see build.gradle for where to obtain this file. It should be auto
        // downloaded into assets.
        return "model.tflite";
    }

    public void setImageToTensor(Bitmap bitmap) {


        MappedByteBuffer tfliteModel
                = null;
        try {
            tfliteModel = FileUtil.loadMappedFile(getApplicationContext(),
                    "model.tflite");

        } catch (IOException e) {
            e.printStackTrace();
        }


        // tfliteOptions.setNumThreads(numThreads);
        try (Interpreter interpreter = new Interpreter(tfliteModel)) {
            //   interpreter.run(selectedImage, obj);


          ImageProcessor imageProcessor =
                    new ImageProcessor.Builder()
                            .add(new ResizeOp(400,400, ResizeOp.ResizeMethod.BILINEAR))
                            .build();


            // Create a TensorImage object. This creates the tensor of the corresponding
// tensor type (uint8 in this case) that the TensorFlow Lite interpreter needs.
            TensorImage tImage = new TensorImage(DataType.UINT8);

// Analysis code for every frame
// Preprocess the image
            tImage.load(bitmap);
            tImage = imageProcessor.process(tImage);

            TensorBuffer probabilityBuffer =
                    TensorBuffer.createFixedSize(new int[]{1,400,400,3}, DataType.UINT8);



            interpreter.run(tImage.getBuffer(), probabilityBuffer.getBuffer());



        }
    }
    public void setImageToTensorFlow(Bitmap bitmap){


        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.porn);

        MappedByteBuffer tfliteModel
                = null;
        try {
            tfliteModel = FileUtil.loadMappedFile(getApplicationContext(),
            "model.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Interpreter interpreter = new Interpreter(tfliteModel);

               DataType myImageDataType = interpreter.getInputTensor(0).dataType();
                   TensorImage myTensorImage = new TensorImage(myImageDataType);

                    // change bitmap size
                    Bitmap updatedImage = Bitmap.createScaledBitmap(bitmap1, 224, 224, true);

                    // load bitmap
                    myTensorImage.load(updatedImage);

                   ByteBuffer out = ByteBuffer.allocate(4 * 224 * 224 * 3);
                  //  ByteBuffer buf = ByteBuffer.allocate(2048);
                   interpreter.allocateTensors();

                   //interpreter.getInputTensor(0);

                   // run inference
                 //  interpreter.run(myTensorImage.getBuffer(), buf);

                  //Log.i("tensorindex",""+interpreter.getOutputTensor(0));

                    Log.i("output",""+interpreter.getOutputTensor(0));
                    Log.i("output1",""+ out.array());
                //    Log.i("output1",""+ interpreter.getOutputIndex());

                  //interpreter.close();

    }

    public void setImageToTensorFlowWithprobality(){

        MappedByteBuffer tfliteModel = null;

        try {

            tfliteModel = FileUtil.loadMappedFile(getApplicationContext(), getModelPath());
            tfliteOptions.setUseXNNPACK(true);
            tfliteOptions.setNumThreads(2);
            tflite = new Interpreter(tfliteModel, tfliteOptions);

            // Loads labels out from the label file.
            labels = FileUtil.loadLabels(getApplicationContext(), "mylabels.txt");

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

            Log.d("TAG", "Created a Tensorflow Lite Image Classifier.");

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.porn9);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

            final List<Classifier.Recognition> results = recognizeImage(scaledBitmap, sensorOrientation);

            Log.i("listSize",""+results.size());

            Classifier.Recognition recognition = results.get(0);
            Log.i("Name",recognition.getTitle());
            Log.i("Conf",""+recognition.getConfidence());

          /*  float result = (recognition.getConfidence() + 128);

            Log.i("Conf",""+result/255);*/
            Classifier.Recognition recognition1 = results.get(1);
            Log.i("Name",recognition1.getTitle());
            Log.i("Conf",""+recognition1.getConfidence());


         /*   float result1 = (recognition1.getConfidence() + 128);

            Log.i("Conf",""+result1/255);*/

            close();



        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(),"Crashed",Toast.LENGTH_SHORT).show();

        }


    }
    public void setImageToTensorFlowWithProbablity2(){

        // Create the ImageClassifier instance.
       ImageClassifier.ImageClassifierOptions options =
                ImageClassifier.ImageClassifierOptions.builder()
                        .setMaxResults(MAX_RESULTS)
                         .setNumThreads(2)
                        .build();
        try {

            imageClassifier = ImageClassifier.createFromFileAndOptions(this, getModelPath(), options);


        } catch (IOException e) {

            e.printStackTrace();
        }

        Log.d("TAG", "Created a Tensorflow Lite Image Classifier.");

        // Get the input image size information of the underlying tflite model.
        MappedByteBuffer tfliteModel = null;
        try {

            tfliteModel = FileUtil.loadMappedFile(getApplicationContext(), getModelPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        MetadataExtractor metadataExtractor = null;
        try {
            metadataExtractor = new MetadataExtractor(tfliteModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Image shape is in the format of {1, height, width, 3}.
        int[] imageShape = metadataExtractor.getInputTensorShape(/*inputIndex=*/ 0);
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.normal10);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        final List<Classifier.Recognition> results = recognizeImage1(scaledBitmap, sensorOrientation);



        Classifier.Recognition recognition = results.get(0);
        Log.i("Name",recognition.getTitle());
        Log.i("Conf",""+recognition.getConfidence());


        Classifier.Recognition recognition1 = results.get(1);
        Log.i("Name",recognition1.getTitle());
        Log.i("Conf",""+recognition1.getConfidence());

        close();


    }

    /** Runs inference and returns the classification results. */
    public List<Classifier.Recognition> recognizeImage1(final Bitmap bitmap, int sensorOrientation) {
        // Logs this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage");

        TensorImage inputImage = TensorImage.fromBitmap(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int cropSize = min(width, height);
        // TODO(b/169379396): investigate the impact of the resize algorithm on accuracy.
        // Task Library resize the images using bilinear interpolation, which is slightly different from
        // the nearest neighbor sampling algorithm used in lib_support. See
        // https://github.com/tensorflow/examples/blob/0ef3d93e2af95d325c70ef3bcbbd6844d0631e07/lite/examples/image_classification/android/lib_support/src/main/java/org/tensorflow/lite/examples/classification/tflite/Classifier.java#L310.
        ImageProcessingOptions imageOptions =
                ImageProcessingOptions.builder()
                        .setOrientation(getOrientation(sensorOrientation))
                        // Set the ROI to the center of the image.
                        .setRoi(
                                new Rect(
                                        /*left=*/ (width - cropSize) / 2,
                                        /*top=*/ (height - cropSize) / 2,
                                        /*right=*/ (width + cropSize) / 2,
                                        /*bottom=*/ (height + cropSize) / 2))
                        .build();

        // Runs the inference call.
        Trace.beginSection("runInference");
        long startTimeForReference = SystemClock.uptimeMillis();
        List<Classifications> results = imageClassifier.classify(inputImage, imageOptions);
        long endTimeForReference = SystemClock.uptimeMillis();
        Trace.endSection();
        Log.v("TAG", "Timecost to run model inference: " + (endTimeForReference - startTimeForReference));

        Trace.endSection();

        return getRecognitions(results);
    }


    private TensorOperator getPostprocessNormalizeOp() {
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }


  /*  public Bitmap convertBitmapToByteBuffer(){

        ByteBuffer buf = DecodeData.mReceivingBuffer;
        byte[] imageBytes= new byte[buf.remaining()];
        buf.get(imageBytes);
        final Bitmap bmp=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

        return  bmp;

    }
*/

    /** Runs inference and returns the classification results. */
    public List<Classifier.Recognition> recognizeImage(final Bitmap bitmap, int sensorOrientation) {
        // Logs this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage");

        Trace.beginSection("loadImage");
        long startTimeForLoadImage = SystemClock.uptimeMillis();
        inputImageBuffer = loadImage(bitmap, sensorOrientation);
        long endTimeForLoadImage = SystemClock.uptimeMillis();
        Trace.endSection();
        Log.v("TAG", "Timecost to load the image: " + (endTimeForLoadImage - startTimeForLoadImage));

        // Runs the inference call.
        Trace.beginSection("runInference");
        long startTimeForReference = SystemClock.uptimeMillis();
        //tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getFloatArray());
     /*   Object[] a = new Object[]{inputImageBuffer.getBuffer()};


        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(0, "normal");
        outputs.put(1, "porn");

        tflite.runForMultipleInputsOutputs(a,outputs);*/

     //   float[][] inputs = new float[1][2];
        // populate the inputs float array above

     /*   float[][] outputs = new float[1][2];

        tflite.run(inputImageBuffer.getBuffer(), outputs);

        float[] classProb = outputs[0];
        Log.i("classProb",""+outputs[0].clone());
*/


    /*    // for float model
       float[][] outputs = new float[1][2];

        tflite.run(inputImageBuffer.getBuffer(), outputs);


        Log.i("classProb1",""+outputs[0][0]);
        Log.i("classProb2",""+outputs[0][1]);
*/


//      Log.i("classProb3",""+outputs[0][2]);





        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());


        long endTimeForReference = SystemClock.uptimeMillis();
        Trace.endSection();

        Log.v("TAG", "Timecost to run model inference: " + (endTimeForReference - startTimeForReference));

        // Gets the map of label and probability.
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        Trace.endSection();

        // Gets top-k results.
        return getTopKProbability(labeledProbability);


    }

    /** Loads input image, and applies preprocessing. */
    private TensorImage loadImage(final Bitmap bitmap, int sensorOrientation) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = min(bitmap.getWidth(), bitmap.getHeight());
        int numRotation = sensorOrientation / 90;
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        // TODO(b/169379396): investigate the impact of the resize algorithm on accuracy.
                        // To get the same inference results as lib_task_api, which is built on top of the Task
                        // Library, use ResizeMethod.BILINEAR.
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(new Rot90Op(numRotation))
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

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
      /*  if (gpuDelegate != null) {
            gpuDelegate.close();
            gpuDelegate = null;
        }
        if (nnApiDelegate != null) {
            nnApiDelegate.close();
            nnApiDelegate = null;
        }*/
    }









    /**************other recognize imaeg**************/


    private static List<Classifier.Recognition> getRecognitions(List<Classifications> classifications) {

        final ArrayList<Classifier.Recognition> recognitions = new ArrayList<>();
        // All the demo models are single head models. Get the first Classifications in the results.
        for (Category category : classifications.get(0).getCategories()) {
            recognitions.add(
                    new Classifier.Recognition(
                            "" + category.getLabel(), category.getLabel(), category.getScore(), null));
        }
        return recognitions;
    }



    private static ImageProcessingOptions.Orientation getOrientation(int cameraOrientation) {
        switch (cameraOrientation / 90) {
            case 3:
                return ImageProcessingOptions.Orientation.BOTTOM_LEFT;
            case 2:
                return ImageProcessingOptions.Orientation.BOTTOM_RIGHT;
            case 1:
                return ImageProcessingOptions.Orientation.TOP_RIGHT;
            default:
                return ImageProcessingOptions.Orientation.TOP_LEFT;
        }
    }



}
