package com.example.ffff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Product_list_adp extends ArrayAdapter {
    public Product_list_adp(Context ctx, ArrayList<Product> arr){
        super(ctx,R.layout.product_list_item,arr);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Product pr = (Product) getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item, null);
        }
        ((TextView)convertView.findViewById(R.id.product_list_adp_product_name)).setTextColor(getContext().getResources().getColor(R.color.white));
        ((TextView)convertView.findViewById(R.id.product_list_adp_product_name)).setText(pr.name);
        ((TextView)convertView.findViewById(R.id.product_list_adp_product_vrg_calories)).setText(String.valueOf(pr.calories_per_gram*10000)+" Ккал/100г");
        return convertView;
    }
}
