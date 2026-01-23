package com.example.maps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maps.R;
import com.example.maps.model.Country;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country> {

    public CountryAdapter(Context context, ArrayList<Country> lista) {
        super(context, R.layout.item_country, lista);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_country, parent, false);
        }

        Country country = getItem(position);

        TextView txtCountry = convertView.findViewById(R.id.txtCountry);
        ImageView imgFlag = convertView.findViewById(R.id.imgFlag);

        txtCountry.setText(country.getNombre());

        Glide.with(getContext())
                .load(country.getBandera())
                .into(imgFlag);

        return convertView;
    }
}

