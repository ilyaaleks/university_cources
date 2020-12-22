package com.example.fakegrammob.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.fakegrammob.R;

import java.io.File;
import java.io.IOException;

import static com.example.fakegrammob.converter.PathConverter.getRealPathFromURI;
import static com.example.fakegrammob.facade.AlertDialogFacade.showErrorDialog;
import static com.example.fakegrammob.facade.ClaimRetrieve.retrieveUserIdFromToken;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.savePost;
import static com.example.fakegrammob.validator.StringFieldsValidators.isContainEmptyString;

public class PostAddingActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 249;
    private ImageView imageView;
    private EditText hashTags;
    private EditText postText;
    private Uri currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_adding);
        imageView = findViewById(R.id.imageView);
        hashTags = findViewById(R.id.editTextHashTags);
        postText = findViewById(R.id.editTextPostText);
    }




    public void peekImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imageView.setImageBitmap(bitmap);
                        currentImagePath = selectedImage;
                    } catch (IOException e) {
                        Log.i("TAG", "Exception: " + e);
                    }
                    break;
            }
    }

    public void addPost(View view) {
        final String pathFromURI = getRealPathFromURI(currentImagePath, this);
        final File currentPhoto = new File(pathFromURI);
        final long authorId = retrieveUserIdFromToken(getApplicationContext());
        final String hashTags = this.hashTags.getText().toString();
        final String postText = this.postText.getText().toString();
        if (isContainEmptyString(hashTags, postText)) {
            showErrorDialog();
            return;
        }
        savePost(currentPhoto, authorId, hashTags,postText);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FakeGram")
                        .setContentText("Post has been added successfully");

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
        finish();
    }

}