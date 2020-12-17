package com.example.fakegrammob.facade;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;
import com.example.fakegrammob.R;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClaimRetrieve {
    public static long retrieveUserIdFromToken(Context context)
    {
        final String authKey = context.getString(R.string.auth_token_bearer);
        SharedPreferences sharedPref = context.getSharedPreferences(
                authKey, Context.MODE_PRIVATE);
        final String authToken = sharedPref.getString(authKey, "");
        JWT jwt = new JWT(authToken);
        return jwt.getClaim("id").asLong();
    }
}
