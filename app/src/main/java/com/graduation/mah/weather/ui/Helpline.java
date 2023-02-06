package com.graduation.mah.weather.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.graduation.mah.weather.R;
import com.graduation.mah.weather.adapters.HelplineAdapter;
import com.graduation.mah.weather.model.ModelHelpline;

import java.util.ArrayList;

public class Helpline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);

        CardView cd = findViewById(R.id.cardView);

        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://indianhelpline.com/")));
            }
        });

        ListView simpleList;
        ArrayList<ModelHelpline> HelplineList=new ArrayList<>();



            simpleList = (ListView) findViewById(R.id.help_list);
            HelplineList.add(new ModelHelpline("NATIONAL EMERGENCY NUMBER","112"));
            HelplineList.add(new ModelHelpline("POLICE","100"));
            HelplineList.add(new ModelHelpline("FIRE","101"));
            HelplineList.add(new ModelHelpline("AMBULANCE","102"));
            HelplineList.add(new ModelHelpline("Disaster Management Services","108"));
            HelplineList.add(new ModelHelpline("Women Helpline","1091"));
            HelplineList.add(new ModelHelpline("Air Ambulance","9540161344"));
            HelplineList.add(new ModelHelpline("Disaster Management ( N.D.M.A )","1078"));
            HelplineList.add(new ModelHelpline("EARTHQUAKE / FLOOD / DISASTER  ( N.D.R.F Headquarters )","9711077372"));
            HelplineList.add(new ModelHelpline("Deputy Commissioner Of Police – Missing Child And Women","1094"));
            HelplineList.add(new ModelHelpline("Tourist Helpline","1363"));

//

            HelplineAdapter myAdapter=new HelplineAdapter(this,R.layout.helpline_layout,HelplineList);
            simpleList.setAdapter(myAdapter);
    }
}