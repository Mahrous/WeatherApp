package com.graduation.mah.weather.ui.egypthistory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.graduation.mah.weather.R;
import com.graduation.mah.weather.adapters.EGAdapter;
import com.graduation.mah.weather.databinding.ActivityEgdhistoryBinding;
import com.graduation.mah.weather.model.EGModel;

import java.util.ArrayList;

public class EGDHistory extends AppCompatActivity {
    ActivityEgdhistoryBinding binding;
    ArrayList<EGModel> egModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_egdhistory);
        binding.setModel(this);
        egModels.add(new EGModel("Egypt: Floods Due To Canal Collapse - Dec 1991" , "", "31 Dec 1991" ,"Flood" , R.drawable.m1));
        egModels.add(new EGModel("Egypt - Earthquake Oct 1992" , "", "12 Oct 1992" ,"Earthquake" , R.drawable.earth2));
        egModels.add(new EGModel("Sudan/Egypt - Earthquakes Aug 1993" , "", "3 Aug 1993" ,"Earthquake" , R.drawable.earh1));
        egModels.add(new EGModel("Egypt - Floods Nov 1994" , "", "4 Nov 1994" ,"Flood" , R.drawable.flood0));
        egModels.add(new EGModel("Influenza A (H1N1) Pandemic - Apr 2009" , "", "1 Apr 2009" ,"Diseases" , R.drawable.flue));
        egModels.add(new EGModel("Floods - Jan 2010" , "", "21 Jan 2010" ,"Flood" , R.drawable.flood));
//        egModels.add(new EGModel("Syria/Iraq: Polio Outbreak - Oct 2013" , "", "" ,"" , 0));
        egModels.add(new EGModel("Egypt: Floods - Oct 2016" , "", "26 October 2016" ,"Flood" , R.drawable.flood3));
        egModels.add(new EGModel("Floods - Mar 2020" , "", "11 March 2020" ,"Flood" , R.drawable.flood2));
        egModels.add(new EGModel("Egypt: Floods - Nov 2021" , "", "12 November 2021" ,"Flood36" , R.drawable.flood4));

        binding.recEg.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        EGAdapter adapter = new EGAdapter(getApplicationContext() , egModels);
        binding.recEg.setAdapter(adapter);
    }
}