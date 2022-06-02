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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;

public class List_activity extends AppCompatActivity {

    SearchView searchView;
    ListView list;
    Button add;
    DataBase db;
    FrameLayout ingredients_activity;
    TextView textView;
    Ingredient_list_adp list_adp;
    int deleted_ingredient_index;
    public static final String type_arg = "TYPE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        getSupportActionBar().hide();
        db = new DataBase(this);
        add = findViewById(R.id.list_activity_add_button);
        int type = getIntent().getIntExtra(type_arg,0);
        switch (type){
            case(1): {
                ArrayList<Ingredient> ingredients =new ArrayList<>();
                db.get_ingredients(ingredients);
                list = findViewById(R.id.list_activity_list);
                list_adp = new Ingredient_list_adp(this, ingredients);
                list.setAdapter(list_adp);
                textView = findViewById(R.id.list_is_empty);
                if (ingredients.isEmpty()) {
                    textView.setText(this.getResources().getString(R.string.list_of_ingredients_is_empty));
                    textView.setVisibility(View.VISIBLE);
                } else {
                    searchView = findViewById(R.id.list_activity_search_view);
                }
                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        deleted_ingredient_index = i;
                        AlertDialog.Builder confirming_dialog_builder = new AlertDialog.Builder(new ContextThemeWrapper(List_activity.this, R.style.alert_dialog_style))
                                .setMessage(R.string.are_you_sure).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Ingredient deleted_ingredient =(Ingredient) list_adp.getItem(deleted_ingredient_index);
                                        db.remove_ingredient(deleted_ingredient);
                                        ingredients.clear();
                                        db.get_ingredients(ingredients);
                                        list_adp.notifyDataSetChanged();
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
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog addIngredient = new Dialog(List_activity.this);
                        addIngredient.setContentView(R.layout.add_ingredient_dialog_window);
                        addIngredient.show();
                        Spinner ingredient_type_spinner = addIngredient.findViewById(R.id.ingredient_type_spinner);
                        String[] types = getResources().getStringArray(R.array.ingredient_types);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                              android.R.layout.simple_spinner_item,types){
                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                tv.setTextColor(Color.BLACK);
                                return view;
                            }
                        };
                        ingredient_type_spinner.setAdapter(adapter);
                        EditText enter_name_et = addIngredient.findViewById(R.id.add_ingredient_dialog_window_enter_name);
                        EditText enter_vrg_calories_et = addIngredient.findViewById(R.id.add_ingredient_dialog_window_enter_vrg_calories);
                        Button complete = addIngredient.findViewById(R.id.add_ingredient_dialog_window_complete);
                        complete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Ingredient created_ingredient = new Ingredient();
                                created_ingredient.name = String.valueOf(enter_name_et.getText());
                                created_ingredient.type_of_ingredient = ingredient_type_spinner.getSelectedItem().toString();
                                for(int i = 0;i<=types.length-1;i++){
                                    if(types[i].equals(created_ingredient.type_of_ingredient)){
                                        created_ingredient.type_of_ingredient_id = i+1;
                                        break;
                                    }else continue;
                                }
                                try{
                                    created_ingredient.calories_per_gram = Double.valueOf(String.valueOf(enter_vrg_calories_et.getText()))/100.0;
                                    addIngredient.dismiss();
                                    db.add_ingredient(created_ingredient);
                                }catch (NumberFormatException e){
                                    addIngredient.dismiss();
                                    Toast.makeText(List_activity.this,R.string.wrong_ingredient_fill,Toast.LENGTH_SHORT).show();
                                }
                                ingredients.add(created_ingredient);
                                list_adp.notifyDataSetInvalidated();
                                textView.setText(null);
                            }
                        });
                        Button cancel = addIngredient.findViewById(R.id.add_ingredient_dialog_window_cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addIngredient.dismiss();
                            }
                        });
                    }
                });
            break;
            }
            case (2):{
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
                break;
            }
        }
    }
}