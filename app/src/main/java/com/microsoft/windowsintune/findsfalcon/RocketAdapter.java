package com.microsoft.windowsintune.findsfalcon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.windowsintune.findsfalcon.DataModels.Rocket;

import java.util.ArrayList;

public class RocketAdapter extends ArrayAdapter<Rocket> {

    public RocketAdapter(@NonNull Context context, int resource, ArrayList<Rocket> rockets) {
        super(context, resource, rockets);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Rocket rocket = getItem(position);
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rocket_cards, parent, false);
            convertView = view;
        }
        TextView name = convertView.findViewById(R.id.name);
        TextView size = convertView.findViewById(R.id.count);
        TextView total = convertView.findViewById(R.id.total);
        RadioButton radioButton = convertView.findViewById(R.id.radio);
        name.setText(rocket.getName());
        size.setText("avail : " + String.valueOf(rocket.getAvailableUnits()));
        total.setText("total : " + String.valueOf(rocket.getTotalNo()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton.setChecked(true);
            }
        });

        return convertView;
    }
}
