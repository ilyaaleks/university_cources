package com.example.fakegrammob.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.widget.ANImageView;
import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.UserDto;

import java.io.File;
import java.io.IOException;

import static com.example.fakegrammob.converter.PathConverter.getRealPathFromURI;
import static com.example.fakegrammob.facade.AlertDialogFacade.showErrorDialog;
import static com.example.fakegrammob.facade.ServerEndpoints.BASE_URL;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.doRegistration;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.updateUser;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.updateUserPhoto;
import static com.example.fakegrammob.validator.StringFieldsValidators.isContainEmptyString;

public class RegistrationActivity extends AppCompatActivity {


    private EditText lastNameField;
    private EditText nameField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;
    private EditText userDescriptionField;
    private EditText statusField;
    private UserDto currentUser;
    private ANImageView imageView;
    private static final int GALLERY_REQUEST = 249;
    private Uri currentImagePath;
    final String serverUrl = BASE_URL + "img/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        lastNameField = findViewById(R.id.editTextPersonLastname);
        nameField = findViewById(R.id.editTextName);
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextTextPassword);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        userDescriptionField = findViewById(R.id.editTextUserDescription);
        statusField = findViewById(R.id.editTextStatus);
        imageView = findViewById(R.id.userAvatarView);
        final Bundle extras = getIntent().getExtras();
        if (extras!=null && extras.getBoolean("updateFlag")) {
            final Button button = findViewById(R.id.sendDataButton);
            button.setText("Update");
            button.setOnClickListener(v -> {
                sendUpdatedData(v);
            });
            currentUser = (UserDto) extras.getSerializable("user");
            updateImageViewField();
            setFields();
            passwordField.setVisibility(View.INVISIBLE);
        }
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

    public void sendRegisterData(View view) {
        final String lastName = lastNameField.getText().toString();
        final String name = nameField.getText().toString();
        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();
        final String email = emailField.getText().toString();
        final String userDescription = userDescriptionField.getText().toString();
        final String status = statusField.getText().toString();
        if (isContainEmptyString(lastName, name, username, password, email, userDescription, status)) {
            showErrorDialog();
            return;
        }
        UserDto user = new UserDto(lastName, name, username, email, userDescription, false, status);
        user.setPassword(password);
        doRegistration(user);
        this.finish();
    }

    public void sendUpdatedData(View view) {
        final String lastName = lastNameField.getText().toString();
        final String name = nameField.getText().toString();
        final String username = usernameField.getText().toString();
        final String email = emailField.getText().toString();
        final String userDescription = userDescriptionField.getText().toString();
        if (isContainEmptyString(lastName, name, username, email, userDescription)) {
            showErrorDialog();
            return;
        }
        final String pathFromURI = getRealPathFromURI(currentImagePath, this);
        final File currentPhoto = new File(pathFromURI);
        UserDto user = new UserDto();
        user.setLastName(lastName);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setUserDescription(userDescription);
        final UserDto updatedUser = updateUser(user);
        updateUserPhoto(updatedUser.getUsername(), currentPhoto);
    }

    public void peekImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    private void setFields() {
        lastNameField.setText(currentUser.getLastName());
        nameField.setText(currentUser.getName());
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
        userDescriptionField.setText(currentUser.getUserDescription());
    }

    private void updateImageViewField() {
        imageView.setVisibility(View.VISIBLE);
        imageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        imageView.setErrorImageResId(R.drawable.ic_baseline_error_24);
        imageView.setImageUrl(serverUrl + currentUser.getPhotoUrl());
    }
}