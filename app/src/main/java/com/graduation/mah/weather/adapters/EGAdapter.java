package com.graduation.mah.weather.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduation.mah.weather.R;
import com.graduation.mah.weather.model.EGModel;
import com.graduation.mah.weather.ui.egypthistory.ShowOneDisaster;

import java.util.List;


public class EGAdapter extends RecyclerView.Adapter<EGAdapter.Holder> {

    List<EGModel> posts;
    Context context;
    int row;

    public EGAdapter(Context mContext, List<EGModel> posts) {
        this.context = mContext;
        this.posts = posts;
    }

    public EGAdapter(Context mContext) {
        this.context = mContext;

    }

    public void setList(List<EGModel> posts) {
        this.posts = posts;
        Log.i("TAG", "setList: size = " + posts.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_eg, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        EGModel model = posts.get(position);
        holder.text_date.setText(model.getDate());
        holder.title.setText(model.getTitle());

holder.linearLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context , ShowOneDisaster.class);
        intent.putExtra("p" , holder.getAdapterPosition());
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
});
        holder.imageView2.setImageResource(model.getImage());
//            Glide.with(context)
//                    .load(model.getImage())
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .into(holder.imageView2);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView description, text_date, title;
        ImageView imageView2;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView2 = itemView.findViewById(R.id.imageView2);
            linearLayout = itemView.findViewById(R.id.layout);
            text_date = itemView.findViewById(R.id.text_date);
            title = itemView.findViewById(R.id.title);


        }


    }


}
