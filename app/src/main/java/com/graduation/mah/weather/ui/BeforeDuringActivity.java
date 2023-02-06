package com.graduation.mah.weather.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.graduation.mah.weather.R;

public class BeforeDuringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_during);

        PDFView pdf = findViewById(R.id.pdfview2);
        pdf.fromAsset("beforeduringafter.pdf").fitEachPage(true).pageSnap(true).load();
    }
}