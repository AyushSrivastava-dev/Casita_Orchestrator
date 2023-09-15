package com.example.casita_v1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VenueAdapter extends ArrayAdapter<ModelClass> {
    Context context;
    List<ModelClass> modelClasses;
    int resource;

    public VenueAdapter(@NonNull Context context, int resource, @NonNull List<ModelClass> objects) {
        super(context, resource, objects);
        this.context = context;
        this.modelClasses = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ModelClass modelClass = modelClasses.get(position);
        View v = LayoutInflater.from(context).inflate(resource, null);
        TextView mainHeading = v.findViewById(R.id.venueFull_name);
        TextView city = v.findViewById(R.id.address_content);
        TextView contact = v.findViewById(R.id.capacity_num);

        mainHeading.setText(modelClass.getTitle());
        city.setText(modelClass.getCity());
        contact.setText(modelClass.getPhone());

        return v;
    }
}
