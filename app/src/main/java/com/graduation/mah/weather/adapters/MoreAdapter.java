package com.graduation.mah.weather.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.graduation.mah.weather.R;
import com.graduation.mah.weather.model.ModelMore;
import com.graduation.mah.weather.ui.EmergencyActivity;

import java.util.ArrayList;

public class MoreAdapter extends ArrayAdapter<ModelMore> {
    ArrayList<ModelMore> MoreList = new ArrayList<>();

    public MoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ModelMore> objects) {
        super(context, resource, objects);
        MoreList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.more_layout, null);
        TextView textView1 = (TextView) v.findViewById(R.id.morecard);

        textView1.setText(MoreList.get(position).getDesc());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), EmergencyActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra("pdf", "erthtips.pdf");
                        break;
                    case 1:
                        intent.putExtra("pdf", "eqguide.pdf");
                        break;
                    case 2:
                        intent.putExtra("pdf", "saftyschool.pdf");
                        break;
                    case 3:
                        intent.putExtra("pdf", "dosnewnidm.pdf");
                        break;
                    case 4:
                        intent.putExtra("pdf", "heatwave.pdf");
                        break;
                    case 5:
                        intent.putExtra("pdf", "coldeng.pdf");
                        break;
                    case 6:
                        intent.putExtra("pdf", "flood.pdf");
                        break;

                }

                getContext().startActivity(intent);

//                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                browserIntent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + Uri.parse(MoreList.get(position).getUrl())), "text/html");
//                getContext().startActivity(browserIntent);
//
////                Uri u = Uri.parse("tel:" + textView2.getText().toString());
////
////                // Create the intent and set the data for the
////                // intent as the phone number.
////                Intent i = new Intent(Intent.ACTION_DIAL, u);
////                getContext().startActivity(i);
            }
        });
        return v;
    }
}
