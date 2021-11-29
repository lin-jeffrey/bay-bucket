package com.example.baybucket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

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

    Button btn_cancel, btn_submit;
    EditText et_description;
    RatingBar rb_review;
    ImageView iv_photo;

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DestinationFragment.
     */
    public static DestinationFragment newInstance(String param1, String param2) {
        DestinationFragment fragment = new DestinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        rb_review = view.findViewById(R.id.simple_rating_bar);
        iv_photo = view.findViewById(R.id.destination_photo);

        // handle photo capture/upload
        iv_photo.setOnClickListener(view13 -> startCamera());

        // handle cancel clicked
        btn_cancel.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());

        // handle submit clicked
        btn_submit.setOnClickListener(view12 -> {
            // TODO replace with save to database
            Log.i(TAG, "OnSubmit " + et_description.getText().toString() + " " + rb_review.getRating());

            persistDestinationMemory();
        });
    }

    /*
    * Function to store the destination attributes to memory
    */
    private void persistDestinationMemory() {
        // DestinationDatabase addVisit
    }

    /*
    * Function to start activity to capture a photo
    * */
    // SOURCE: https://developer.android.com/training/camera/photobasics
    private void startCamera() {
        Log.i(TAG, "starting TakePicture intent");
        File photoFile = null;
        try {
            photoFile = createImageFile();
            Log.i(TAG, "File name: " + photoFile.getName());
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast.makeText(getContext(), "Unable to create image file", Toast.LENGTH_SHORT).show();
            Log.e(TAG, ex.getMessage());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.i(TAG,"intent created");
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                Log.i(TAG, "intent activity resolved");
                Log.i(TAG, getContext().getPackageName() + ".provider");
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.baybucket.fileprovider",
                        photoFile);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                registerForActivityResult(new ActivityResultContracts.TakePicture(),
                        result -> {
                            Log.i(TAG, "Activity result: " + result);
                            // use photoURI to save image to destination memory
                            // use photoURI to update ImageView src to thumbnail
                        });

            } else {
                Log.i(TAG, "No image capture functionality available");
                Toast.makeText(getContext(), "No image capture functionality available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No photo file created", Toast.LENGTH_SHORT).show();
        }

    }

    /*
    *  Function to get the thumbnail of the photo
    * */
    private void handlePhoto(Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_photo.setImageBitmap(imageBitmap);
//        }
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
}