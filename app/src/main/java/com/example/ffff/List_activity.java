package com.example.ffff;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class List_activity extends AppCompatActivity {

    SearchView searchView;
    ListView list;
    Button add;
    DataBase db;
    FrameLayout ingredients_activity;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        getSupportActionBar().hide();
        db = new DataBase(this);
        add = findViewById(R.id.list_activity_add_button);
        if(Ingredient.ingredients_activity_is_opened) {
            ArrayList<Ingredient> ingredients =new ArrayList<>();
            ingredients = db.get_ingredients(ingredients);
            if (ingredients.isEmpty()) {
                textView = findViewById(R.id.list_is_empty);
                textView.setText(this.getResources().getString(R.string.list_of_ingredients_is_empty));
                textView.setVisibility(View.VISIBLE);
            } else {
                searchView = findViewById(R.id.list_activity_search_view);
                list = findViewById(R.id.list_activity_list);
                Ingredient_list_adp list_adp = new Ingredient_list_adp(this, ingredients);
                list.setAdapter(list_adp);
            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog add_ingredient = new Dialog(List_activity.this);
                    add_ingredient.setContentView(R.layout.add_ingredient_dialog_window);
                    Spinner ingredient_type_spinner = findViewById(R.id.ingredient_type_spinner);
                    add_ingredient.setOwnerActivity(List_activity.this);
                    /* здесь надо получить контекст
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(,
                            android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.ingredient_types));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ingredient_type_spinner.setAdapter(adapter);
                    add_ingredient.show();*/
                }
            });
        }
        else {
            ArrayList<Product> products = new ArrayList<>();
            products = db.get_products(products);
            if(products.isEmpty()){
                textView = findViewById(R.id.list_is_empty);
                textView.setText(this.getResources().getString(R.string.list_of_products_is_empty));
                textView.setVisibility(View.VISIBLE);
            }else{
                searchView = findViewById(R.id.list_activity_search_view);
                list = findViewById(R.id.list_activity_list);
                Product_list_adp prod_adp = new Product_list_adp(this,products);
                list.setAdapter(prod_adp);

            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(List_activity.this,Add_product_activity.class);
                    startActivity(intent);
                }
            });
        }
    }
}