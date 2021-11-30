package com.example.baybucket;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.models.Memory;
import com.example.baybucket.models.User;
import com.github.jinatonic.confetti.CommonConfetti;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DestinationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DestinationFragment extends DialogFragment {

    private static final int RESULT_OK = 1;
    Button btn_cancel, btn_submit;
    EditText et_description;
//    RatingBar rb_review;
    TextView tv_timestamp, tv_name;
    ImageView iv_photo;
    String bucketListItemName;
    String coordinates;
    private FirebaseUser user;
    Date timestamp;

    ActivityResultLauncher<Intent> activityResultLauncher,
        activityResultLauncherUpload;

    private static final String TAG = "DestinationFragment";

    String currentPhotoPath;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public DestinationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param destinationName Parameter 1.
     * @param coordinates Parameter 2.
     * @param bucket Parameter 3.
     * @return A new instance of fragment DestinationFragment.
     */
    public static DestinationFragment newInstance(String destinationName, String coordinates, String bucket) {
        DestinationFragment fragment = new DestinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, "CANCEL");
        args.putString(ARG_PARAM2, "SUBMIT");
        fragment.setArguments(args);
        fragment.bucketListItemName = destinationName;
        fragment.coordinates = coordinates;
        // fragment.bucket = bucket;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1); // cancel
            mParam2 = getArguments().getString(ARG_PARAM2); // submit
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_cancel = view.findViewById(R.id.button_cancel);
        btn_submit = view.findViewById(R.id.button_submit);
        et_description = view.findViewById(R.id.destination_description);
//        rb_review = view.findViewById(R.id.simple_rating_bar);
        iv_photo = view.findViewById(R.id.destination_photo);
        tv_timestamp = view.findViewById(R.id.tv_timestamp);
        tv_name = view.findViewById(R.id.tv_name);

        tv_name.setText(bucketListItemName);

        timestamp = new java.util.Date();
        tv_timestamp.setText(timestamp.toString());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.i(TAG, "onActivityResult takePhoto " + result);
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            iv_photo.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(getContext(), "Image capture failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        activityResultLauncherUpload = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.i(TAG, "onActivityResult pickPhoto " + result);
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                            Bundle bundle = result.getData().getExtras();
//                            Bitmap bitmap = (Bitmap) bundle.get("data");
//                            iv_photo.setImageBitmap(bitmap);
                            Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // handle photo capture/upload
        iv_photo.setOnClickListener(view13 -> selectImageOption());

        // handle cancel clicked
        btn_cancel.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());

        // handle submit clicked
        btn_submit.setOnClickListener(view12 -> {
            Log.i(TAG, "OnSubmit");
            persistDestinationMemory();

        });
    }

    /*
    * Function to store the destination attributes to memory
    */
    private void persistDestinationMemory() {

        user = FirebaseAuth.getInstance().getCurrentUser(); // can i get this from our local db?

        // TODO: image URI
        MemoryRepository memoryRepository = new MemoryRepository(getActivity());
        // if imageView is not the default, save image URI
        // how to get user.getEmail() ?
        Memory memory = new Memory(user.getEmail(), bucketListItemName, coordinates, timestamp, et_description.getText().toString(), "");
        memoryRepository.insertMemory(memory);

        // TODO: update user points on firebase
            // https://firebase.google.com/docs/database/android/read-and-write
        //    read firebase for current user point total
        //    add x number of points to user point total and write to /users/uid/points

        // TODO: save checkbox state
        // TODO: route back to bucket list
    }

    /*
    * Function to create image file
    * */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*
    * Function to add photo to gallery
    * */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        //this.sendBroadcast(mediaScanIntent);
    }

    private void checkCameraPermission() {
        //Add runtime permission for accessing camera
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        } else {
            Log.i(TAG, "Camera permission granted");
        }
    }

    private void checkStoragePermission() {
        //Add runtime permission for accessing storage
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        } else {
            Log.i(TAG, "Storage permission granted");
        }
    }

    private void selectImageOption() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a photo");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                    Log.i(TAG, "File name: " + photoFile.getName());
//                } catch (IOException ex) {
//                    // Error occurred while creating the File
//                    Toast.makeText(getContext(), "Unable to create image file", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, ex.getMessage());
//                }
//                Continue only if the File was successfully created
//                if (photoFile != null) {

                if (options[item].equals("Take Photo")) {

                    checkCameraPermission();
                    Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePhoto.resolveActivity(getContext().getPackageManager()) != null) {
//                        Uri photoURI = FileProvider.getUriForFile(getContext(),
//                                "com.example.baybucket.fileprovider",
//                                photoFile);
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activityResultLauncher.launch(takePhoto);
                    } else {
                        Log.i(TAG, "No image capture functionality available");
                        Toast.makeText(getContext(), "No image capture functionality available", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Choose from Gallery")) {

                    checkStoragePermission();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if(pickPhoto.resolveActivity(getContext().getPackageManager()) != null) {
                        activityResultLauncherUpload.launch(pickPhoto);
                    } else {
                        Log.i(TAG, "No image upload functionality available");
                        Toast.makeText(getContext(), "No image upload functionality available", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




//    public  imageSelected(int requestCode, int resultCode ) { //Intent data
//        if(resultCode != RESULT_CANCELED) {
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage =  data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }
//
//                    }
//                    break;
//            }
//        }
//    }
}