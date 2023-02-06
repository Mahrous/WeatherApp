package com.graduation.mah.weather.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.graduation.mah.weather.R;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        Intent intent = getIntent();
        String book = intent.getStringExtra("pdf");
        if (book == null || book.isEmpty()){
        PDFView pdf = findViewById(R.id.pdfview1);
        pdf.fromAsset("erthtips.pdf").fitEachPage(true).pageSnap(true).load(); }
        else {
            PDFView pdf = findViewById(R.id.pdfview1);
            pdf.fromAsset(book).fitEachPage(true).pageSnap(true).load();
        }
    }
}