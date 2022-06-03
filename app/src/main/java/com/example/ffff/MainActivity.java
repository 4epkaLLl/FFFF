package com.example.ffff;

import androidx.annotation.NonNull;
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
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

            }
        });
        toIngredients = findViewById(R.id.mainActivity_toIngredients);
        toIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, List_activity.class);
                intent.putExtra(List_activity.type_arg,1);
                startActivity(intent);
            }
        });
        toProducts = findViewById(R.id.mainActivity_toProducts);
        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, List_activity.class);
                intent.putExtra(List_activity.type_arg,2);
                startActivity(intent);
            }
        });
    }
}