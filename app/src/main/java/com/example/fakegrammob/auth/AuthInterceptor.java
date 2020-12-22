package com.example.fakegrammob.auth;

import android.content.Context;

import com.auth0.android.jwt.JWT;
import com.example.fakegrammob.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.fakegrammob.auth.HeaderParams.AUTH_HEADER;
import static com.example.fakegrammob.auth.HeaderParams.TOKEN_PREFIX;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        final String token = context.getSharedPreferences(
                context.getString(R.string.auth_token_bearer), Context.MODE_PRIVATE).getString(context.getString(R.string.auth_token_bearer), "");
        if (token.equals("")) {
            return chain.proceed(request);
        }
        JWT jwtToken = new JWT(token);
        if (jwtToken.isExpired(0)) {
            return chain.proceed(request);
        }
        newRequest = request.newBuilder()
                .addHeader(AUTH_HEADER, TOKEN_PREFIX + " " + token)
                .build();
        return chain.proceed(newRequest);
    }
}
