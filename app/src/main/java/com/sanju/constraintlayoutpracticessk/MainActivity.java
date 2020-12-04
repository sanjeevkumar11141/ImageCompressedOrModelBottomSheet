package com.sanju.constraintlayoutpracticessk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 8901;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton edit_fab;
    Group group2;
    boolean groupFlag = false;
    ImageView imageView;
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.constraint_test);
        setContentView(R.layout.expendable_list_design);
        imageView = findViewById(R.id.imageView);
        imageView3 = findViewById(R.id.imageView3);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* MyBottomSheet myBottomSheet = new MyBottomSheet();
                myBottomSheet.show(getSupportFragmentManager(),"MyTag");*/
                showCameraPreview();
            }
        });
    }

    private void showCameraPreview() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //showCameraDescriptionDialog();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                dispatchTakePictureIntent();
            }
        }
    }

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }*/


    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File f = new File(path);

            Log.d("SIZE Of Image", "getBitmap: "+(f.length() / 1024) / 1024);


            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            imageView.setImageBitmap(bitmap);


            Log.d("MySize", "before Compresed: " +(bitmap.getByteCount()/(1024*1024)) + "MB");
            Log.d("MySize", "before Compresed width:" + bitmap.getWidth()+" "
                    +"before Compresed height:"+bitmap.getHeight());

            Log.d("MySize", "getBitmap in KB SIZE: "+((float) Math.round((bitmap.getByteCount()/ (1024)) * 10) / 10));
            Log.d("MySize", "getBitmap in MB SIZE: "+((float) Math.round((bitmap.getByteCount() / (1024 * 1024)) * 10) / 10));

            Bitmap updatedBitMap = ImageUtils.getInstant().getCompressedBitmap(path);
            imageView3.setImageBitmap(updatedBitMap);


            Log.d("MySize", "After Compresed: " +(updatedBitMap.getByteCount()/1024)/1024  + "MB");
            Log.d("MySize", "After Compresed width:" + updatedBitMap.getWidth()+" "
                    +"After Compresed height:"+updatedBitMap.getHeight());


            Log.d("MySize", "getBitmap in KB SIZE: "+((float) Math.round((updatedBitMap.getByteCount() / (1024)) * 10) / 10));
            Log.d("MySize", "getBitmap in MB SIZE: "+((float) Math.round((updatedBitMap.getByteCount() / (1024 * 1024)) * 10) / 10));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);*/
            // imageView.setImageURI(Uri.parse(currentPhotoPath));

            getBitmap(currentPhotoPath);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */".jpg",/* suffix */storageDir/* directory */);
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}