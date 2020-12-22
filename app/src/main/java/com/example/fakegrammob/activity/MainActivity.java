package com.example.fakegrammob.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.example.fakegrammob.R;
import com.example.fakegrammob.auth.AuthInterceptor;
import com.example.fakegrammob.dto.AuthToken;
import com.example.fakegrammob.facade.ServerEndpointsFacade;
import com.example.fakegrammob.service.FingerPrintService;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.text.TextUtils.isEmpty;
import static com.example.fakegrammob.facade.AlertDialogFacade.showErrorDialog;
import static com.example.fakegrammob.validator.StringFieldsValidators.isContainEmptyString;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_EXTERNAL_STORAGE = 1;
    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        final String token = getApplicationContext().getSharedPreferences(
                getApplicationContext().getString(R.string.auth_token_bearer), Context.MODE_PRIVATE).getString(getApplicationContext().getString(R.string.auth_token_bearer), "");
        if (!isEmpty(token)) {
            getApplicationContext().getSharedPreferences(
                    getApplicationContext().getString(R.string.auth_token_bearer), Context.MODE_PRIVATE).edit().clear();
        }
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new AuthInterceptor(getApplicationContext()))
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_EXTERNAL_STORAGE);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void loginUser(View view) {
        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();
        final String authKey = getString(R.string.auth_token_bearer);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                authKey, Context.MODE_PRIVATE);
        if (isContainEmptyString(username, password)) {
            showErrorDialog();
            return;
        }
        final AuthToken authToken = ServerEndpointsFacade.doLogin(username, password);
        if (authToken == null) {
            showErrorDialog();
            return;
        }
        sharedPref.edit().putString(authKey, authToken.getToken()).commit();
        FingerPrintService fingerPrintService = new FingerPrintService(getApplicationContext(), this);
        fingerPrintService.auth();

    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}