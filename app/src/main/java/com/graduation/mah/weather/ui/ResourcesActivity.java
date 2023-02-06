package com.graduation.mah.weather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.graduation.mah.weather.R;

public class ResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        CardView cd1 = findViewById(R.id.card1);
        CardView cd2 = findViewById(R.id.card2);
        CardView cd3 = findViewById(R.id.card3);

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResourcesActivity.this,EarthquakeEmergencyStepsActivity.class));
            }
        });

        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResourcesActivity.this,BeforeDuringActivity.class));
            }
        });

        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResourcesActivity.this,MoreActivity.class));
            }
        });


    }
}