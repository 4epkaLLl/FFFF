package com.example.ffff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Main_activity_list_adp extends ArrayAdapter {
    public Main_activity_list_adp(Context ctx, ArrayList<Product> arr){
        super(ctx,R.layout.main_activity_list_item,arr);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Product prod = (Product) getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_activity_list_item, null);
        }
        ((TextView)convertView.findViewById(R.id.main_activity_list_item_name)).setText(prod.name);
        ((TextView)convertView.findViewById(R.id.main_activity_list_item_vrg_calories)).setText(prod.weight+"г / "+prod.calories_per_gram*prod.weight+"Ккал");
        return convertView;
    }
}
