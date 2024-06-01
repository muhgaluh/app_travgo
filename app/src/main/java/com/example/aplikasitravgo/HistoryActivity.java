package com.example.aplikasitravgo;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;

import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HistoryActivity extends AppCompatActivity {

    Database dbHelper;
    SQLiteDatabase db;
    ArrayList <String> id_book, nama, asal, tujuan, kelas, tanggal, harga;
    RecyclerView rvHistory;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new Database(this);
        db = dbHelper.getReadableDatabase();

        id_book = new ArrayList<>();
        nama = new ArrayList<>();
        asal = new ArrayList<>();
        tujuan = new ArrayList<>();
        kelas = new ArrayList<>();
        tanggal = new ArrayList<>();
        harga = new ArrayList<>();

        rvHistory = findViewById(R.id.rvHistory);

        historyAdapter = new HistoryAdapter(this, id_book, nama, asal, tujuan, kelas, tanggal, harga);
        rvHistory.setAdapter(historyAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        displayData();
    }

    private void displayData() {
        Cursor cursor = dbHelper.getData();

        if(cursor.getCount()==0){
            Toast.makeText(HistoryActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
            return;
        }else{
            while(cursor.moveToNext()){
                id_book.add(cursor.getString(0));
                nama.add(cursor.getString(1));
                asal.add(cursor.getString(2));
                tujuan.add(cursor.getString(3));
                kelas.add(cursor.getString(4));
                tanggal.add(cursor.getString(5));
                harga.add(cursor.getString(6));
            }
        }
    }

    public void onBackPressed(View v){
        super.onBackPressed();
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
