package com.example.baybucket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.models.Memory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DestinationCheckInActivity extends AppCompatActivity {

    private static final String TAG = "DestinationCheckInActivity";

    Button btnTakePhoto, btnPickPhoto;
    Button btnCancel, btnSubmit;
    ImageView mainImage;
    EditText etDescription;

    TextView tv_timestamp, tv_name;
    String destinationName;
    String destinationCoordinates;
    private FirebaseUser user;
    Date timestamp;

    String currentPhotoPath;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_check_in);

        destinationName = getIntent().getExtras().getString("name");
        destinationCoordinates = getIntent().getExtras().getString("coordinates");

        mainImage = findViewById(R.id.check_in_image_view);
        btnTakePhoto = findViewById(R.id.take_image_button);
//        btnPickPhoto = findViewById(R.id.pick_image_button);
        btnCancel = findViewById(R.id.button_cancel);
        btnSubmit = findViewById(R.id.button_submit);
        etDescription = findViewById(R.id.description_edit_text);

        // get bucketListItemName
        // get coordinates

        btnCancel.setOnClickListener(v -> {
            //Intent intent = new Intent(this, RegisterActivity.class);
            //startActivity(intent);
        });

        btnSubmit.setOnClickListener(view12 -> {
            persistDestinationMemory();
            // Intent back to AdapterList
        });

        btnTakePhoto.setOnClickListener(v1 -> {
            checkCameraPermission();
            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(DestinationCheckInActivity.this, "com.example.baybucket.fileprovider", photoFile);
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try {
                    startActivityForResult(takePhoto, 1);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(DestinationCheckInActivity.this, "Cannot take image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DestinationCheckInActivity.this, "Cannot save image", Toast.LENGTH_SHORT).show();
            }
        });

//        btnPickPhoto.setOnClickListener(v2 -> {
//            checkStoragePermission();
//            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            try {
//                startActivityForResult(pickPhoto, 2);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(DestinationCheckInActivity.this, "Cannot pick image", Toast.LENGTH_SHORT).show();
//            }
//        });
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

    private void checkStoragePermission() {
        //Add runtime permission for accessing storage
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        } else {
            Log.i(TAG, "Storage permission granted");
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            mainImage.setImageURI(photoURI);
        } else if (requestCode == 2) {
//            mainImage.setImageURI();
        }
    }

    private void persistDestinationMemory() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        MemoryRepository memoryRepository = new MemoryRepository(DestinationCheckInActivity.this);
        Memory memory = new Memory(user.getEmail(), destinationName, destinationCoordinates, timestamp, etDescription.getText().toString(), photoURI.toString());
        memoryRepository.insertMemory(memory);

        // TODO: update user points on firebase
        // TODO: save checkbox state
        // TODO: route back to bucket list
    }
}