package com.example.ffff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Ingredient_list_adp extends ArrayAdapter {
    public Ingredient_list_adp(Context ctx, ArrayList<Ingredient> arr){
        super(ctx,R.layout.ingredient_list_item,arr);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Ingredient ing = (Ingredient)getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_list_item, null);
        }
        ((TextView)convertView.findViewById(R.id.ingredient_list_item_ingredient_name)).setText(ing.name);
        ((TextView)convertView.findViewById(R.id.ingredient_list_item_ingredient_type)).setText(ing.type_of_ingredient);
        ((TextView)convertView.findViewById(R.id.ingredient_list_item_vrg_calories)).setText(String.valueOf(ing.calories_per_gram));
        return convertView;

    }
}
