package com.example.ffff;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    boolean dialog_completed;
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

                    //Toast.makeText(List_activity.this,R.string.wrong_ingredient_fill,Toast.LENGTH_SHORT).show();
                    Dialog addIngredient = new Dialog(List_activity.this);
                    addIngredient.setContentView(R.layout.add_ingredient_dialog_window);
                    addIngredient.show();
                    Spinner ingredient_type_spinner = addIngredient.findViewById(R.id.ingredient_type_spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(List_activity.this,
                            android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.ingredient_types));
                    ingredient_type_spinner.setAdapter(adapter);
                    EditText enter_name_et = addIngredient.findViewById(R.id.add_ingredient_dialog_window_enter_name);
                    EditText enter_vrg_calories_et = addIngredient.findViewById(R.id.add_ingredient_dialog_window_enter_vrg_calories);
                    Button complete = addIngredient.findViewById(R.id.add_ingredient_dialog_window_complete);
                    complete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Ingredient created_ingredient = new Ingredient();
                            created_ingredient.name = String.valueOf(enter_name_et.getText());
                            dialog_completed = true;
                            created_ingredient.type_of_ingredient = ingredient_type_spinner.getSelectedItem().toString();
                            Toast.makeText(addIngredient.getContext(),R.string.wrong_ingredient_fill,Toast.LENGTH_SHORT);
                            try{
                                created_ingredient.calories_per_gram = Integer.valueOf(String.valueOf(enter_vrg_calories_et.getText()));
                                addIngredient.dismiss();
                                db.add_ingredient(created_ingredient);
                            }catch (NumberFormatException e){
                                dialog_completed = false;
                                addIngredient.dismiss();
                            }
                            if(!dialog_completed){
                                Toast.makeText(List_activity.this,R.string.wrong_ingredient_fill,Toast.LENGTH_SHORT);
                                dialog_completed = true;
                            }
                        }
                    });

                    Button cancel = addIngredient.findViewById(R.id.add_ingredient_dialog_window_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addIngredient.dismiss();
                        }
                    });
                    //Spinner ingredient_type_spinner = add_ingredient.findViewById(R.id.ingredient_type_spinner);
                    //add_ingredient.setOwnerActivity(List_activity.this);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(List_activity.this,
                    //        android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.ingredient_types));
                    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //add_ingredient.show();
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