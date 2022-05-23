package com.example.ffff;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Add_product_activity extends AppCompatActivity {
    ArrayAdapter<String>ingredients_in_product_adp;
    ArrayAdapter<String>ingredients_adp;
    EditText enter_name;
    ListView ingredients_in_product;
    ListView ingredients;
    Button complete;
    //Ingredient_list_adp ingredient_adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);
        getSupportActionBar().hide();
        ingredients = findViewById(R.id.add_product_activity_ingredients_list);

        ingredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog dialog = new AlertDialog.Builder(Add_product_activity.this)
                .setView(R.layout.add_product_activity_dialog)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });

    }
}
