package com.example.baybucket;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.baybucket.db.UserRepository;
import com.example.baybucket.models.User;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageTestActivity extends AppCompatActivity {
    private static final String TAG = "yes";
    Button btnTakePhoto;
    ImageView main_image;
    TextView photoUriText;
    Button btnCancel, btnSubmit;

    String currentPhotoPath;

    Uri photoURI;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_test);

        main_image = findViewById(R.id.profile_image_preview);
        btnTakePhoto = findViewById(R.id.take_profile_image_button);

        btnCancel = findViewById(R.id.button_cancel);
        btnSubmit = findViewById(R.id.button_submit);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(ImageTestActivity.this, "com.example.baybucket.fileprovider", photoFile);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserRepository userRepository = new UserRepository(ImageTestActivity.this);
                    if(userRepository.getUserByEmail(user.getEmail()).size() > 0){
                        userRepository.getUserByEmail(user.getEmail()).get(0).setImageUri(photoURI.toString());
                    }
                    takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    try {
                        startActivityForResult(takePhoto, 1);

                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                }

            }
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });

        btnSubmit.setOnClickListener(view12 -> {
            finish();
        });

    }
    private void checkCameraPermission() {
        //Add runtime permission for accessing camera
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        } else {
            Log.i(TAG, "Camera permission granted");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        main_image.setImageURI(photoURI);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
