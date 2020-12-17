package com.example.fakegrammob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.UserDto;

import static com.example.fakegrammob.facade.AlertDialogFacade.showErrorDialog;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.doRegistration;
import static com.example.fakegrammob.validator.StringFieldsValidators.isContainEmptyString;

public class RegistrationActivity extends AppCompatActivity {


    private EditText lastNameField;
    private EditText nameField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;
    private EditText userDescriptionField;
    private EditText statusField;

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
        UserDto user = new UserDto(lastName, name, username,password, email, userDescription, false, status);
        doRegistration(user);
        this.finish();
    }
}