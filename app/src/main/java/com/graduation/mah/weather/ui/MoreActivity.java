package com.graduation.mah.weather.ui;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.graduation.mah.weather.R;
import com.graduation.mah.weather.adapters.MoreAdapter;
import com.graduation.mah.weather.model.ModelMore;

import java.util.ArrayList;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        ListView simpleList;
        ArrayList<ModelMore> ModelList=new ArrayList<>();



        simpleList = (ListView) findViewById(R.id.more_list);
        ModelList.add(new ModelMore("EARTHQUAKE TIPS(IIT-K)","https://tnsdma.tn.gov.in/app/webroot/img/document/library/06-Earth-Quake-Tips.pdf"));
        ModelList.add(new ModelMore("EARTHQUAKE PREPAREDNESS GUIDE","https://nidm.gov.in/PDF/IEC/eq%20guide.pdf"));
        ModelList.add(new ModelMore("SCHOOL SAFETY","https://nidm.gov.in/PDF/safety/school/link1.pdf"));
        ModelList.add(new ModelMore("DO'S & DONT'S","https://nidm.gov.in/PDF/IEC/Dosnewnidm.pdf"));
        ModelList.add(new ModelMore("HEAT WAVE","https://nidm.gov.in/PDF/IEC/heat%20wave-14%20eng.pdf"));
        ModelList.add(new ModelMore("COLD WAVE","https://nidm.gov.in/PDF/IEC/cold%20eng.pdf"));
        ModelList.add(new ModelMore("FLOOD SAFETY TIPS","https://nidm.gov.in/PDF/IEC/flood-15.pdf"));





      //  ModelList.add(new ModelMore("POLICE","100"));
//        ModelList.add(new ModelMore("FIRE","101"));
//        ModelList.add(new ModelMore("AMBULANCE","102"));
//        ModelList.add(new ModelMore("Disaster Management Services","108"));
//        ModelList.add(new ModelMore("Women Helpline","1091"));
//        ModelList.add(new ModelMore("Air Ambulance","9540161344"));
//        ModelList.add(new ModelMore("Disaster Management ( N.D.M.A )","1078"));
//        ModelList.add(new ModelMore("EARTHQUAKE / FLOOD / DISASTER  ( N.D.R.F Headquaters )","9711077372"));
//        ModelList.add(new ModelMore("Deputy Commissioner Of Police – Missing Child And Women","1094"));
//        ModelList.add(new ModelMore("Tourist Helpline","1363"));

//

        MoreAdapter myAdapter=new MoreAdapter(this,R.layout.more_layout,ModelList);
        simpleList.setAdapter(myAdapter);


    }
}