package com.example.aplikasitravgo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView tvUser;
    Button btnPesanPesawat, btnPesanKapal, btnPesanKereta;
    ImageView img_hstry, img_logout;
    SessionManager session;
    String namaUser;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        tvUser = findViewById(R.id.tvUser);
        btnPesanPesawat = findViewById(R.id.btnPesanPesawat);
        btnPesanKapal = findViewById(R.id.btnPesanKapal);
        btnPesanKereta = findViewById(R.id.btnPesanKereta);
        img_hstry = findViewById(R.id.img_hstry);
        img_logout = findViewById(R.id.img_logout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        namaUser = user.getEmail();

        Snackbar.make(tvUser, namaUser, Snackbar.LENGTH_LONG).show();

        img_hstry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(i);
            }
        });

        btnPesanPesawat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TiketPesawatActivity.class);
                startActivity(i);
            }
        });

        btnPesanKapal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TiketKapalActivity.class);
                startActivity(i);
            }
        });

        btnPesanKereta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TiketKeretaActivity.class);
                startActivity(i);
            }
        });

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Ingin Logout?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                session.logout();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .create();
                dialog.show();
            }
        });

    }
}