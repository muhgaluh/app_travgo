package com.example.aplikasitravgo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText reg_username, reg_email, reg_password;
    Button btnDaftar, btnKeLogin;
    Database dbHelper;
    SQLiteDatabase db;
    String username, email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        dbHelper = new Database(this);
        db = dbHelper.getWritableDatabase();

        reg_username = findViewById(R.id.reg_username);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);

        btnDaftar = findViewById(R.id.daftar);
        btnKeLogin = findViewById(R.id.ke_login);

        mAuth = FirebaseAuth.getInstance();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = reg_username.getText().toString();
                email = reg_email.getText().toString();
                password = reg_password.getText().toString();

                try {
                    if (username.trim().length() > 0 && email.trim().length() > 0 && password.trim().length() > 0) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful())
                                                {
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    dbHelper.open();
                                                    dbHelper.Register(username, email, password);
                                                    Toast.makeText(getApplicationContext(), "Daftar berhasil", Toast.LENGTH_LONG).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Daftar gagal, tolong mengisi ulang form", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Daftar gagal, tolong mengisi ulang form", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnKeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
