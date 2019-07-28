package com.jpaw.projectm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import androidx.appcompat.app.AppCompatActivity;

public class Classify extends AppCompatActivity {

    // presets for rgb conversion
    private static final int RESULTS_TO_SHOW = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    // options for model interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    // tflite graph
    private Interpreter tflite;
    // holds all the possible labels for model
    private List<String> labelList;
    // holds the selected image data as bytes
    private ByteBuffer imgData = null;
    // holds the probabilities of each label for non-quantized graphs
    private float[][] labelProbArray = null;
    private final int NUM_LABELS = 2;
    // array that holds the highest probabilities
    //private String[] labelsResultArray = null;
    static String[] confidenceResultArray = null;


    // selected classifier information received from extras
    private String chosen;

    // input image dimensions for the Inception Model
    private int DIM_IMG_SIZE_X = 64;
    private int DIM_IMG_SIZE_Y = 64;
    private int DIM_PIXEL_SIZE = 3;

    // int array to hold image data
    private int[] intValues;


    // activity elements
    private ImageView selected_image;
    private Button classify_button;
    private Button back_button;
    EditText etname ;
    private TextView parasitizedWithPercentage;
    private TextView uninfectedWithPercentage;
    Context context= this;
    DatabaseHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get all selected classifier data from classifiers
        chosen = "converted_model.tflite";
        // initialize array that holds image data
        intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

        super.onCreate(savedInstanceState);

        //initilize graph and labels
        try{
            tflite = new Interpreter(loadModelFile(), tfliteOptions);

        } catch (Exception ex){
            ex.printStackTrace();
        }

        // initialize byte array
        imgData = ByteBuffer.allocateDirect(4 * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());

        // initialize probabilities array
        labelProbArray = new float[1][NUM_LABELS];

        setContentView(R.layout.activity_classify);


        // This is to put the user name
        etname =(EditText)findViewById(R.id.name);
        // labels that hold top three results of CNN
        parasitizedWithPercentage = (TextView) findViewById(R.id.parasitized);
        uninfectedWithPercentage = (TextView) findViewById(R.id.uninfected);
        // initialize imageView that displays selected image to the user
        selected_image = (ImageView) findViewById(R.id.bloodCell);

        // initialize array to hold top labels
        labelList = new ArrayList<>(2);
        labelList.add("Parasitized");
        labelList.add("Uninfected");
        // initialize array to hold top probabilities
        confidenceResultArray = new String[NUM_LABELS];

        Log.i("LINE", "143======================================================");
        try {
            Log.i("LINE", "14333======================================================");
            Bitmap bitmap_orig = ((BitmapDrawable) selected_image.getDrawable()).getBitmap();
            Log.i("LINE", "144======================================================");
            // System.out.println("LINE 143");
//          // resize the bitmap to the required input size to the CNN
            Bitmap bitmap = getResizedBitmap(bitmap_orig, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
            //Log.i("LINE", "148======================================================");
            // convert bitmap to byte array
            //convertBitmapToByteBuffer(bitmap);
            // pass byte data to the graph

            //Log.i("LINE", "153======================================================");
        } catch (Exception e) {
            //Log.i("LINE", "158======================================================");
            System.out.print("NullPointerException" + e);
        }


        // get image from previous activity to show in the imageView
        Uri uri = (Uri)getIntent().getParcelableExtra("resID_uri");
        try {
            Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            selected_image.setImageBitmap(bitmap1);
            tflite.run(imgData, labelProbArray);
            printLabels();
            // not sure why this happens, but without this the image appears on its side
            //selected_image.setRotation(selected_image.getRotation() + 90);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // loads tflite graph from file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd(chosen);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // converts bitmap to byte array which is passed in the tflite graph
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.
                // if quantized, convert each rgb value to a byte, otherwise to a float

                imgData.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);


            }
        }
    }

    // loads the labels from the label txt file in assets into a string array
    // print the top labels and respective confidences
    private void printLabels() {
        // add all results to priority queue

        for (int i = 0; i < confidenceResultArray.length; i++) {

            confidenceResultArray[i] = String.format("%.0f%%", labelProbArray[0][i] * 100);
        }

        // set the corresponding textviews with the results
        parasitizedWithPercentage.setText("Parasitized  -  " + confidenceResultArray[0]);
        uninfectedWithPercentage.setText("Uninfected  -  " + confidenceResultArray[1]);
        //SharedPreferences
        /*SharedPreferences sharedPreferences = this.getSharedPreferences("package com.jpaw.projectm;", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("Past Client Information", ObjectSerializer.serialize(Classify.confidenceResultArray));

        }

        catch(IOException e){
            e.printStackTrace();
        }*/

    }


    // resizes bitmap to given dimensions
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    //This adds the data on the Classify
    public void addData (View view){


      String name = etname.getText().toString();
      String para = parasitizedWithPercentage.getText().toString();
      String unin = uninfectedWithPercentage.getText().toString();
      dbhelper = new DatabaseHelper(context);
      sqLiteDatabase = dbhelper.getWritableDatabase();
      dbhelper.addData(name, para,unin,sqLiteDatabase);
        Toast.makeText(getBaseContext(),"Data Saved",Toast.LENGTH_LONG).show();

      dbhelper.close();

    }
}