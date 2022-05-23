package com.example.ffff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;
    TextView date_tv;
    ListView products_in_day;
    Button toProducts;
    Button toIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        toIngredients = findViewById(R.id.mainActivity_toIngredients);

        toIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient.ingredients_activity_is_opened = true;
                Intent intent = new Intent(MainActivity.this, List_activity.class);
                startActivity(intent);
            }
        });
        toProducts = findViewById(R.id.mainActivity_toProducts);
        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product.products_activity_is_opened = true;
                Intent intent = new Intent(MainActivity.this, List_activity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        Ingredient.ingredients_activity_is_opened = false;
        Product.products_activity_is_opened = false;
    }
}