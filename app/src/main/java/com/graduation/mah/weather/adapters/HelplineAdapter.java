package com.graduation.mah.weather.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.graduation.mah.weather.R;
import com.graduation.mah.weather.model.ModelHelpline;

import java.util.ArrayList;

public class HelplineAdapter extends ArrayAdapter<ModelHelpline> {

    ArrayList<ModelHelpline> HelplineList = new ArrayList<>();
    public HelplineAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ModelHelpline> objects) {
        super(context, resource, objects);
        HelplineList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.helpline_layout, null);
        TextView textView1 = (TextView) v.findViewById(R.id.dept);
        final TextView textView2 = (TextView) v.findViewById(R.id.help);
        textView1.setText(HelplineList.get(position).getDepartment());
        textView2.setText(HelplineList.get(position).getContact());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse("tel:" + textView2.getText().toString());

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                getContext().startActivity(i);
            }
        });
        return v;


    }
}
