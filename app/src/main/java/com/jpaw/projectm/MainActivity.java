

package com.jpaw.projectm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // for permission requests
    public static final int REQUEST_PERMISSION = 300;

    // request code for permission requests to the os for image
    public static final int REQUEST_IMAGE_CAPTURE = 101;

    // will hold uri of image obtained from camera
    private Uri imageUri;
    private ImageView imageView;

    private Button button;

    // string to send to next activity that describes the chosen classifier
    //private String chosen;

    //boolean value dictating if chosen model is quantized version or not.
    //private boolean quant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // request permission to use the camera on the user's phone
//        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, REQUEST_PERMISSION);
//        }
//
//        // request permission to write data (aka images) to the user's external storage of their phone
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_PERMISSION);
//        }
//
//        // request permission to read data (aka images) from the user's external storage of their phone
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_PERMISSION);
//        }

//THIS LINK TO ANOTHER PAGE WHERE WE NEED TO STORE DATA

      button = (Button) findViewById(R.id.button2);
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              openactivity_data();
          }
      });

     }



    public void openactivity_data(){
         Intent intent = new Intent(this, DataStorage.class);
         startActivity(intent);

    }
//END LINKING TO STORE DATA

    // opens camera for user
    public void takePicture (View view) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        // tell camera where to store the resulting picture
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // check if the picture is taken ( i think )
        //        if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeImageIntent, REQUEST_IMAGE_CAPTURE);

    }

    /*

    THIS IS FOR SQLITE DATABASE
    public void displayData(View view) {
        Intent i = new Intent(MainActivity.this, DatabaseHelper.class);

    }*/

    // checks that the user has allowed all the required permission of read and write and camera. If not, notify the user and close the application
//    @Override
//    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION) {
//            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                Toast.makeText(getApplicationContext(),"This application needs read, write, and camera permissions to run. Application now closing.",Toast.LENGTH_LONG);
//                System.exit(0);
//            }
//        }
//    }

    // dictates what to do after the user takes an image, selects and image, or crops an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // if the camera activity is finished, obtained the uri, crop it to make it square, and send it to 'Classify' activity
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Uri source_uri = imageUri;
                Uri dest_uri = Uri.fromFile(new File(getCacheDir(), "cropped"));
                // need to crop it to square image as CNN's always required square input
                Crop.of(source_uri, dest_uri).asSquare().start(MainActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // if cropping acitivty is finished, get the resulting cropped image uri and send it to 'Classify' activity
        else if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK){
            imageUri = Crop.getOutput(data);
            Intent i = new Intent(MainActivity.this, Classify.class);
            // put image data in extras to send
            i.putExtra("resID_uri", imageUri);

            startActivity(i);
            //setContentView(R.layout.activity_classify);

        }

    }

   public void CheckPastPatient (View view){

        Intent intent = new Intent (this,DataStorage.class);
        startActivity(intent);

   }

}

//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//public class MainActivity extends AppCompatActivity {
//    private ImageView imageView;
//    private static final int REQUEST_IMAGE_CAPTURE = 101;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        imageView = (ImageView) findViewById(R.id.bloodCell);
//    }
//
//    public void takePicture(View view) {
//        Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takeImageIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//            LinearLayout layout = (LinearLayout) findViewById(R.id.resultLayout);
//            layout.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//}
