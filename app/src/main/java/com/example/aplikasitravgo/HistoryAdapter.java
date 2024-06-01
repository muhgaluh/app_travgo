package com.example.aplikasitravgo;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList id_book, nama, asal, tujuan, kelas, tanggal, harga;


    public HistoryAdapter(Context context, ArrayList id_book, ArrayList nama, ArrayList asal, ArrayList tujuan, ArrayList kelas, ArrayList tanggal, ArrayList harga) {
        this.context = context;
        this.id_book = id_book;
        this.nama = nama;
        this.asal = asal;
        this.tujuan = tujuan;
        this.kelas = kelas;
        this.tanggal = tanggal;
        this.harga = harga;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.id_book.setText("ID : " + id_book.get(position));
        holder.nama.setText(String.valueOf(nama.get(position)));
        holder.asal.setText(String.valueOf(asal.get(position)));
        holder.tujuan.setText(String.valueOf(tujuan.get(position)));
        holder.kelas.setText(String.valueOf(kelas.get(position)));
        holder.tanggal.setText(String.valueOf(tanggal.get(position)));
        holder.harga.setText("Total : Rp." + harga.get(position));
    }

    @Override
    public int getItemCount() {
        return id_book.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id_book, nama, asal, tujuan, kelas, tanggal, harga;
        public CardView cvHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_book = itemView.findViewById(R.id.tvIdBook);
            nama = itemView.findViewById(R.id.tvNama);
            asal = itemView.findViewById(R.id.tvAsal);
            tujuan = itemView.findViewById(R.id.tvTujuan);
            kelas = itemView.findViewById(R.id.tvKelas);
            tanggal = itemView.findViewById(R.id.tvTanggal);
            harga = itemView.findViewById(R.id.tvHargaTiket);
            cvHistory = itemView.findViewById(R.id.cvHistory);
        }
    }
}
