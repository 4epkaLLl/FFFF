package com.example.ffff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Ingredient_in_product_list_adp extends ArrayAdapter {
    public Ingredient_in_product_list_adp(Context ctx, ArrayList<Ingredient> arr){
        super(ctx,R.layout.ingredient_list_item,arr);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Ingredient ing = (Ingredient)getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_product_activity_ingredients_in_product_item, null);
        }
        ((TextView)convertView.findViewById(R.id.add_product_activity_ingredients_in_product_item_name)).setText(ing.name);
        ((TextView)convertView.findViewById(R.id.add_product_activity_ingredients_in_product_list_item_weight)).setText(""+ing.relWeight+" г");
        ((TextView)convertView.findViewById(R.id.add_product_activity_ingredients_in_product_item_method_of_cook)).setText(String.valueOf(ing.method_of_cook));
        return convertView;
    }
}
