package com.example.aplikasitravgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Muh Galuh";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
