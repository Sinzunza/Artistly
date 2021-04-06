package com.inzucorp.artistly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lyft.android.scissors.CropView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import Classes.userDB;

public class CropPhotoActivity extends AppCompatActivity {

// declare layout ids
    CropView cvCropPhotoCropView;
    TextView tvCropPhotoDone;

// local variables
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_photo);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        cvCropPhotoCropView = findViewById(R.id.cvCropPhoto_Photo);
        tvCropPhotoDone = findViewById(R.id.tvCropPhoto_Done);

    // immediately open up gallery to choose photo to crop
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(openGallery,1); // opens up onActivityResult

    // once user is done cropping get the cropped image into a bitmap and then turn it back into a uri
        tvCropPhotoDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedBitmap = cvCropPhotoCropView.crop();
                imageUri = getImageUri(CropPhotoActivity.this, croppedBitmap);
                uploadPhoto(imageUri);
            }
        });
    }

// get image data after user selects photo and set it in the cropview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                cvCropPhotoCropView.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                Toast.makeText(CropPhotoActivity.this, "Image to Bitmap failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

// upload picture to firebase storage
    private void uploadPhoto(Uri photo) {
        String randomUrl = "profilePhotos/" + UUID.randomUUID().toString();
        final StorageReference photosStorage = FirebaseStorage.getInstance().getReference().child(randomUrl);

        UploadTask uploadTask = photosStorage.putFile(photo);
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        photosStorage.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final userDB theUserDB = new userDB();
                                        theUserDB.addVal("profilePhoto", uri.toString());
                                        startActivity(new Intent(CropPhotoActivity.this, EditProfileActivity.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CropPhotoActivity.this, "Error setting profile photo.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CropPhotoActivity.this, "Image failed to upload.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

// get a uri from a Bitmap image
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }
}