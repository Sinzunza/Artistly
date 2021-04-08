package com.inzucorp.artistly;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lyft.android.scissors.CropView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import Classes.artistlyDB;
import Fragments.MessagesFragment;

public class NewPostActivity extends AppCompatActivity {

// declare layout ids
    TextView tvNewPostDone, tvNewPostMedia, tvNewPostService, tvNewPostMoneySign;
    FrameLayout flNewPostMediaBar, flNewPostServiceBar;
    ImageView ivNewPostCancel;
    CropView cvNewPostPhoto;
    EditText etNewPostTitle, etNewPostFee, etNewPostDescription;
    ConstraintLayout clNewPostDescription;
    ImageButton ibNewPostHome, ibNewPostMessages, ibNewPostExplore, ibNewPostNewPost, ibNewPostProfile;

// local variables
    Uri imageUri;
    String newPostTitle;
    String newPostFee;
    String newPostDescription;
    boolean isMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        tvNewPostDone = findViewById(R.id.tvNewPost_Done);
        tvNewPostMedia = findViewById(R.id.tvNewPost_Media);
        tvNewPostService = findViewById(R.id.tvNewPost_Service);
        tvNewPostMoneySign = findViewById(R.id.tvNewPost_MoneySign);
        flNewPostMediaBar = findViewById(R.id.flNewPost_MediaBar);
        flNewPostServiceBar = findViewById(R.id.flNewPost_ServicesBar);
        ivNewPostCancel = findViewById(R.id.ivNewPost_Cancel);
        cvNewPostPhoto = findViewById(R.id.cvNewPost_Photo);
        etNewPostTitle = findViewById(R.id.etNewPost_Title);
        etNewPostFee = findViewById(R.id.etNewPost_Fee);
        etNewPostDescription = findViewById(R.id.etNewPost_Description);
        clNewPostDescription = findViewById(R.id.clNewPost_Description);
        ibNewPostHome = findViewById(R.id.ibNewPost_Home);
        ibNewPostMessages = findViewById(R.id.ibNewPost_Messages);
        ibNewPostExplore = findViewById(R.id.ibNewPost_Explore);
        ibNewPostNewPost= findViewById(R.id.ibNewPost_NewPost);
        ibNewPostProfile = findViewById(R.id.ibNewPost_Profile);

    // initialize local variables
        isMedia = true;

    // immediately open up gallery to choose photo to crop
        openGallery();

    // initialize as media post, so hide fee views
        changeType();

    // set onClickListeners
        tvNewPostMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMedia) {
                    changeType();
                }
            }
        });

        tvNewPostService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMedia) {
                    changeType();
                }
            }
        });

        ivNewPostCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        tvNewPostDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPostTitle = etNewPostTitle.getText().toString();
                newPostFee = etNewPostFee.getText().toString();
                newPostDescription = etNewPostDescription.getText().toString();

                if (newPostTitle.isEmpty()) {
                    etNewPostTitle.setError("Please enter a title for your post.");
                    etNewPostTitle.requestFocus();
                }
                if(newPostFee.isEmpty() && !isMedia) {
                    etNewPostFee.setError("Please enter a service fee.");
                    etNewPostFee.requestFocus();
                }

                if (!newPostTitle.isEmpty() && (!newPostFee.isEmpty() || isMedia)) {
                    Bitmap croppedBitmap = cvNewPostPhoto.crop();
                    imageUri = getImageUri(NewPostActivity.this, croppedBitmap);
                    uploadPost();
                }
            }
        });

        ibNewPostHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPostActivity.this, HomeActivity.class));
            }
        });

        ibNewPostMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPostActivity.this, MessagingActivity.class));
            }
        });

        ibNewPostExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPostActivity.this, ExploreActivity.class));
            }
        });

        ibNewPostNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPostActivity.this, NewPostActivity.class));
            }
        });

        ibNewPostProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPostActivity.this, ProfileActivity.class));
            }
        });
    }

// get image data after user selects photo and set it in the imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                cvNewPostPhoto.setViewportRatio((float)cvNewPostPhoto.getWidth() / (float)cvNewPostPhoto.getHeight());
                cvNewPostPhoto.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                Toast.makeText(NewPostActivity.this, "Image to Bitmap failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

// opens up phones gallery
    private void openGallery(){
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(openGallery,1); // opens up onActivityResult
    }

// upload the file to storage and the post to firebase
    private void uploadPost() {
        String randomUrl;
    // upload file to storage path depending on type postType
        if (isMedia){
            randomUrl = "mediaPhotos/" + UUID.randomUUID().toString();
        }
        else {
            randomUrl = "servicesPhotos/" + UUID.randomUUID().toString();
        }
        final StorageReference photosStorage = FirebaseStorage.getInstance().getReference().child(randomUrl);
        UploadTask uploadTask = photosStorage.putFile(imageUri);
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        photosStorage.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Intent intentStartPost = new Intent(NewPostActivity.this, PostActivity.class);
                                        String postID;
                                    // upload post to database path depending on postType
                                        if (isMedia){
                                            postID = artistlyDB.createMedia(newPostTitle, uri, newPostDescription);
                                        }
                                        else {
                                            postID = artistlyDB.createService(newPostTitle, uri, newPostFee, newPostDescription);
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putString("postID", postID);
                                        bundle.putBoolean("postType", isMedia);
                                        intentStartPost.putExtras(bundle);
                                        startActivity(intentStartPost);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NewPostActivity.this, "Error setting profile photo.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewPostActivity.this, "Image failed to upload.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

// get a uri from a Bitmap image
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

// changes post type
    private void changeType(){
        if (isMedia){
            flNewPostMediaBar.setPadding(0,0,0,6);
            flNewPostServiceBar.setPadding(0,0,0,0);
            etNewPostTitle.setHint("Caption");
            clNewPostDescription.setVisibility(View.INVISIBLE);
            tvNewPostMoneySign.setVisibility(View.INVISIBLE);
            etNewPostFee.setVisibility(View.INVISIBLE);
            isMedia = true;
        }
        else {
            flNewPostServiceBar.setPadding(0,0,0,6);
            flNewPostMediaBar.setPadding(0,0,0,0);
            etNewPostTitle.setHint("Title");
            clNewPostDescription.setVisibility(View.VISIBLE);
            tvNewPostMoneySign.setVisibility(View.VISIBLE);
            etNewPostFee.setVisibility(View.VISIBLE);
            isMedia = false;
        }
    }
}