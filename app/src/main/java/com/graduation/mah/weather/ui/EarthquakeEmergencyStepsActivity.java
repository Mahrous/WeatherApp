package com.graduation.mah.weather.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.graduation.mah.weather.R;

public class EarthquakeEmergencyStepsActivity extends AppCompatActivity {

    Button issuealert,emergencycontacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_emergency_steps);

        issuealert = (Button)findViewById(R.id.issuealertbutton);
        emergencycontacts = (Button) findViewById(R.id.emergencycontactsbutton);
//        issuealert.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(EarthquakeEmergencyStepsActivity.this,
//                        IssueAlertsMenuActivity.class);
//                startActivity(myIntent);
//            }
//        });
//        emergencycontacts.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(EarthquakeEmergencyStepsActivity.this,
//                        EmergencyContactsActivity.class);
//                startActivity(myIntent);
//            }
//        });
    }
}
