package com.example.casita_v1;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<DataStoreAdmin> mList;
    private Context context;
    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


    public MyAdapter(Context context , ArrayList<DataStoreAdmin> mList){

        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item , parent ,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("afterShow","click  1");

                Gson gson = new Gson();
                DataStoreAdmin dsa = mList.get(holder.getAdapterPosition());
                String myJson = gson.toJson(dsa);
                Intent intent = new Intent(context, SelectedVenueAfterShow.class);
                intent.putExtra("myDetailsOfVenue",myJson);

                context.startActivity(intent);


            }
        });


        //        Glide.with(context).asBitmap().load(modelClassArrayList.get(position).getUrlToImage()).transition(withCrossFade(factory)).into(holder.imageView);
        Glide.with(context).asBitmap().load(mList.get(position).getImageUrl())
                .fitCenter().transition(withCrossFade(factory)).into(holder.imageView);
        holder.venueFullName.setText(mList.get(position).getVenueName());
        holder.address.setText(mList.get(position).getCompleteAdd());
        holder.capacityDetailsNumber.setText(mList.get(position).getCapacity());
        holder.rateOfVenue.setText(mList.get(position).getPrice());
        holder.latestRating.setText(mList.get(position).getRatings());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView venueFullName;
        TextView address;
        TextView capacityDetailsNumber;
        TextView rateOfVenue;
        CardView cardView;
        TextView latestRating;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.venue_image);
            venueFullName = itemView.findViewById(R.id.venueFull_name);
            address = itemView.findViewById(R.id.address_content);
            capacityDetailsNumber = itemView.findViewById(R.id.capacity_num);
            rateOfVenue = itemView.findViewById(R.id.rate_of_venue);
            cardView = itemView.findViewById(R.id.cardview);
            latestRating = itemView.findViewById(R.id.ratings_field);

        }
    }
}
