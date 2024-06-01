package com.example.aplikasitravgo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;

public class TiketPesawatActivity extends AppCompatActivity {

    protected Cursor cursor;
    Database dbHelper;
    SQLiteDatabase db;
    Spinner spinAsal, spinTujuan, spinKelas;
    TextView tvJmlhAnak, tvJmlhDewasa;
    SessionManager session;
    String email;
    int id_book;
    public String sAsal, sTujuan, sKelas, sTanggal, sDewasa, sAnak;
    int countDewasa = 0;
    int countAnak = 0;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        dbHelper = new Database(TiketPesawatActivity.this);
        db = dbHelper.getReadableDatabase();

        final String[] asal = {"Jakarta", "Semarang", "Surabaya"};
        final String[] tujuan = {"Jakarta", "Semarang", "Surabaya"};
        final String[] kelas = {"Ekonomi","Business"};

        spinAsal = findViewById(R.id.spAsal);
        spinTujuan = findViewById(R.id.spTujuan);
        spinKelas = findViewById(R.id.spKelas);
        tvJmlhAnak = findViewById(R.id.tvJmlAnak);
        tvJmlhDewasa = findViewById(R.id.tvJmlDewasa);

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, asal);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAsal.setAdapter(adapterAsal);

        ArrayAdapter<CharSequence> adapterTujuan = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, tujuan);
        adapterTujuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTujuan.setAdapter(adapterTujuan);

        ArrayAdapter<CharSequence> adapterKelas = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, kelas);
        adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKelas.setAdapter(adapterKelas);

        spinAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAsal = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTujuan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sKelas = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnCheckOut = findViewById(R.id.btnCheckout);

        etTanggal = findViewById(R.id.inputTanggal);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        sDewasa = Integer.toString(countDewasa);
        sAnak = Integer.toString(countAnak);

        setDateTimeField();

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HitungHarga();

                if (sAsal != null && sTujuan != null && sKelas != null && sTanggal != null && sDewasa != null)
                {
                    if ((sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Jakarta"))
                            || (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Semarang"))
                            || (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Surabaya")))
                    {
                        Toast.makeText(TiketPesawatActivity.this, "Asal dan Tujuan tidak boleh sama !", Toast.LENGTH_LONG).show();
                    } else
                    {
                        AlertDialog dialog = new AlertDialog.Builder(TiketPesawatActivity.this)
                                .setTitle("Ingin Pesan Tiket Sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            db.execSQL("INSERT INTO TB_BOOK (nama, asal, tujuan, kelas, tanggal, dewasa, anak) VALUES ('" +
                                                    email + "','" +
                                                    sAsal + "','" +
                                                    sTujuan + "','" +
                                                    sKelas + "','" +
                                                    sTanggal + "','" +
                                                    countAnak + "','" +
                                                    countDewasa + "');");

                                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                            cursor.moveToLast();

                                            if (cursor.getCount() > 0) {
                                                cursor.moveToPosition(0);
                                                id_book = cursor.getInt(0);
                                            }

                                            db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                    email + "','" +
                                                    id_book + "','" +
                                                    hargaTotalDewasa + "','" +
                                                    hargaTotalAnak + "','" +
                                                    hargaTotal + "');");

                                            Toast.makeText(TiketPesawatActivity.this, "Pemesanan berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(TiketPesawatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(TiketPesawatActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addAnak(View v){
        countAnak++;
        tvJmlhAnak.setText("" + countAnak);
    }

    public void minAnak(View v){
        if(countAnak <= 0) countAnak = 0;
        else countAnak--;

        tvJmlhAnak.setText("" + countAnak);
    }

    public void addDewasa(View v){
        countDewasa++;
        tvJmlhDewasa.setText("" + countDewasa);
    }

    public void minDewasa(View v){
        if(countDewasa <= 0) countDewasa = 0;
        else countDewasa--;

        tvJmlhDewasa.setText("" + countDewasa);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void HitungHarga() {
        if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Semarang") && sKelas.equalsIgnoreCase("Ekonomi")) {
            hargaDewasa = 1300000;
            hargaAnak = 420000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Surabaya") && sKelas.equalsIgnoreCase("Ekonomi")) {
            hargaDewasa = 1500000;
            hargaAnak = 400000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Jakarta") && sKelas.equalsIgnoreCase("Ekonomi")) {
            hargaDewasa = 1300000;
            hargaAnak = 420000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Surabaya") && sKelas.equalsIgnoreCase("Ekonomi")) {
            hargaDewasa = 800000;
            hargaAnak = 350000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Jakarta") && sKelas.equalsIgnoreCase("Ekonomi")) {
            hargaDewasa = 1500000;
            hargaAnak = 400000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Semarang") && sKelas.equalsIgnoreCase("Ekonomi")) {
            hargaDewasa = 800000;
            hargaAnak = 350000;
        }else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Semarang") && sKelas.equalsIgnoreCase("Business")) {
            hargaDewasa = 2600000;
            hargaAnak = 840000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Surabaya") && sKelas.equalsIgnoreCase("Business")) {
            hargaDewasa = 3000000;
            hargaAnak = 800000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Jakarta") && sKelas.equalsIgnoreCase("Business")) {
            hargaDewasa = 2600000;
            hargaAnak = 840000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Surabaya") && sKelas.equalsIgnoreCase("Business")) {
            hargaDewasa = 1600000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Jakarta") && sKelas.equalsIgnoreCase("Business")) {
            hargaDewasa = 3000000;
            hargaAnak = 800000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Semarang") && sKelas.equalsIgnoreCase("Business")) {
            hargaDewasa = 1600000;
            hargaAnak = 700000;
        }

        hargaTotalDewasa = countDewasa * hargaDewasa;
        hargaTotalAnak = countAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void onBackPressed(View v){
        super.onBackPressed();
        Intent intent = new Intent(TiketPesawatActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
