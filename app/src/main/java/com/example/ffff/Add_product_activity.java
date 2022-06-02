package com.example.ffff;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Add_product_activity extends AppCompatActivity {
    DataBase db;
    Ingredient_in_product_list_adp ingredients_in_product_adp;
    ArrayAdapter<String>ingredients_adp;
    EditText enter_name;
    EditText enter_weight;
    Spinner choose_method_of_cook;
    ListView ingredients_in_product;
    ListView ingredients;
    Button complete;
    //Ingredient_list_adp ingredient_adp;
    Button dialog_cancel_button;
    Button dialog_ok_button;
    ArrayList<Ingredient>composition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);
        getSupportActionBar().hide();
        db = new DataBase(this);
        ingredients = findViewById(R.id.add_product_activity_ingredients_list);
        ingredients_in_product = findViewById(R.id.add_product_activity_ingredients_in_product);
        ArrayList<Ingredient> ingredient_in_product_list = new ArrayList<>();
        ingredients_in_product_adp = new Ingredient_in_product_list_adp(Add_product_activity.this,ingredient_in_product_list);
        ingredients_in_product.setAdapter(ingredients_in_product_adp);
        ArrayList<Ingredient> ingredient_list = new ArrayList<>();
        db.get_ingredients(ingredient_list);
        String[]ingredient_array = new String[ingredient_list.size()];
        for(int i =0;i<=ingredient_array.length-1;i++){ingredient_array[i]= ingredient_list.get(i).name;}
        ingredients_adp = new ArrayAdapter<String>(Add_product_activity.this, android.R.layout.simple_list_item_1, ingredient_array);
        ingredients.setAdapter(ingredients_adp);
        composition = new ArrayList<>();
        ingredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Dialog add_in_composition_dialog = new Dialog(Add_product_activity.this);
                add_in_composition_dialog.setContentView(R.layout.add_product_activity_dialog);
                enter_name = add_in_composition_dialog.findViewById(R.id.add_product_activity_dialog_enter_name);
                choose_method_of_cook = add_in_composition_dialog.findViewById(R.id.add_product_activity_dialog_method_of_cook_spinner);
                dialog_cancel_button = add_in_composition_dialog.findViewById(R.id.add_product_activity_dialog_cancel_button);
                enter_weight = add_in_composition_dialog.findViewById(R.id.add_product_activity_dialog_enter_weight);
                dialog_ok_button = add_in_composition_dialog.findViewById(R.id.add_product_activity_dialog_ok_button);
                String[] methods_of_cook = getResources().getStringArray(R.array.methods_of_cooking);
                ArrayAdapter<String>spinner_array_adp = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,methods_of_cook){
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                choose_method_of_cook.setAdapter(spinner_array_adp);
                dialog_cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_in_composition_dialog.dismiss();
                    }
                });
                dialog_ok_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Ingredient added_ingredient = new Ingredient();
                        try{
                            added_ingredient.name = ingredients_adp.getItem(pos);
                            added_ingredient.relWeight = Integer.valueOf(enter_weight.getText().toString());
                            added_ingredient.type_of_ingredient = ingredient_list.get(pos).type_of_ingredient;
                            added_ingredient.method_of_cook = choose_method_of_cook.getSelectedItem().toString();
                            for(int i= 0; i< methods_of_cook.length;i++){
                                if (methods_of_cook[i].equals(added_ingredient.method_of_cook)){
                                    added_ingredient.method_of_cook_id = i+1;
                                }
                            }
                            ingredient_in_product_list.add(added_ingredient);
                            ingredients_in_product_adp.notifyDataSetChanged();
                            composition.add(added_ingredient);
                        }catch (Exception e){
                            Toast.makeText(Add_product_activity.this,R.string.wrong_data_entered,Toast.LENGTH_SHORT).show();
                            add_in_composition_dialog.dismiss();
                            e.printStackTrace();
                        }

                        add_in_composition_dialog.dismiss();
                    }
                });
            add_in_composition_dialog.show();
            }
        });
        ingredients_in_product.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                AlertDialog.Builder confirming_dialog_builder = new AlertDialog.Builder(new ContextThemeWrapper(Add_product_activity.this, R.style.alert_dialog_style))
                        .setMessage(R.string.are_you_sure).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ingredient_in_product_list.remove(pos);
                                ingredients_in_product_adp.notifyDataSetChanged();
                                composition.remove(pos);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog confirming_dialog = confirming_dialog_builder.create();
                confirming_dialog.show();
                return true;
            }
        });
    enter_name = findViewById(R.id.add_product_activity_enter_name);
    complete = findViewById(R.id.add_product_activity_complete);
    complete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (composition.isEmpty() || enter_name.getText().equals(null))
                Toast.makeText(Add_product_activity.this,R.string.wrong_product_fill,Toast.LENGTH_SHORT).show();
            else{
                Product prod = db.add_product(enter_name.getText().toString(), composition);

                Intent intent = new Intent(Add_product_activity.this,List_activity.class);
                intent.putExtra(List_activity.type_arg,2);
                startActivity(intent);
            }
        }
    });
    }
}
