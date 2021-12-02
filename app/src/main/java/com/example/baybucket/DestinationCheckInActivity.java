package com.example.baybucket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baybucket.db.DestinationRepository;
import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.db.UserRepository;
import com.example.baybucket.models.Memory;
import com.example.baybucket.models.User;
import com.github.jinatonic.confetti.CommonConfetti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DestinationCheckInActivity extends AppCompatActivity {

    private static final String TAG = "DestinationCheckInActivity";

    Button btnTakePhoto, btnPickPhoto;
    Button btnCancel, btnSubmit;
    ImageView mainImage;
    EditText etDescription;

    TextView tv_timestamp, tv_name;
    String destinationName, destinationCoordinates, destinationBucket, destinationDistance;

    private FirebaseUser user;
    private String userID;
    Date timestamp;

    String currentPhotoPath;
    Uri photoURI;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_check_in);

        destinationName = getIntent().getExtras().getString("name");
        destinationDistance = getIntent().getExtras().getString("distance");
        destinationCoordinates = getIntent().getExtras().getString("coordinates");
        destinationBucket = getIntent().getExtras().getString("bucket");

        mainImage = findViewById(R.id.check_in_image_view);
        btnTakePhoto = findViewById(R.id.take_image_button);
//        btnPickPhoto = findViewById(R.id.pick_image_button);
        btnCancel = findViewById(R.id.button_cancel);
        btnSubmit = findViewById(R.id.button_submit);
        etDescription = findViewById(R.id.description_edit_text);

        // get bucketListItemName
        // get coordinates

        btnCancel.setOnClickListener(v -> {
            backToDestination();
            finish();
        });

        btnSubmit.setOnClickListener(view12 -> {
//            CommonConfetti.rainingConfetti(findViewById(R.id.container), new int[] { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN })
//                    .infinite();

            persistDestinationMemory();

            backToList();
            finish();
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

    private void backToDestination() {
        // Intent back to DestinationActivity
        Intent intentDestination = new Intent(this, DestinationActivity.class);
        intentDestination.putExtra("name", destinationName);
        intentDestination.putExtra("distance", destinationDistance);
        intentDestination.putExtra("coordinates", destinationCoordinates);
        intentDestination.putExtra("bucket", destinationBucket);
        startActivity(intentDestination);
    }

    private void backToList() {
        // Intent back to AdapterList
        String destinationBucketName = destinationBucket.replaceAll("\\s", "%20");
        Intent intentList = new Intent(this, BucketList.class);
        intentList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentList.putExtra("name", destinationBucketName);
        startActivity(intentList);
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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        timestamp = calendar.getTime();

        // Persist memory
        MemoryRepository memoryRepository = new MemoryRepository(this);
        Memory memory;
        if (photoURI != null) {
            memory = new Memory(user.getEmail(), destinationName, destinationCoordinates, timestamp, etDescription.getText().toString(), photoURI.toString());
            Log.i(TAG, user.getEmail() + " " + destinationName + " " + destinationCoordinates + " " + timestamp + " " + etDescription.getText().toString() + " " + photoURI.toString());
        }else {
            memory = new Memory(user.getEmail(), destinationName, destinationCoordinates, timestamp);
            Log.i(TAG, user.getEmail() + " " + destinationName + " " + destinationCoordinates + " " + timestamp);
        }
        memoryRepository.insertMemory(memory);

        // Persist destination

        DestinationRepository destinationRepository = new DestinationRepository(this);
        destinationRepository.insertDestination(destinationName, destinationBucket);

        // update user points on roomDB and on firebase
        addPoints();

        Toast.makeText(DestinationCheckInActivity.this, "Memory saved", Toast.LENGTH_SHORT).show();
    }

    private void addPoints(){
        int currentUserPoints;
        int updatedUserPoints;

        //get user points locally
        UserRepository userRepository = new UserRepository(this);
        List<User> currentUserList = userRepository.getUserByEmail(user.getEmail());
        currentUserPoints = currentUserList.get(0).getPoints();
        Log.i("debug", String.valueOf(currentUserPoints));

        updatedUserPoints = currentUserPoints + 25;
        Log.i("debug", Integer.toString(updatedUserPoints));

        //updates user points on firebase with local user points + 25
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userID).child("points").setValue(Integer.toString(updatedUserPoints));

        //update local user with point total
        currentUserList.get(0).setPoints(updatedUserPoints);
        userRepository.updateUser(currentUserList.get(0));
    }
}