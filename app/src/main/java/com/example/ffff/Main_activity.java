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

import java.util.ArrayList;

public class Main_activity extends AppCompatActivity {
    CalendarView calendar;
    Data_base db;
    ListView products_in_day;
    TextView list_is_empty;
    Product_in_day_list_adp products_in_day_adp;
    Button toProducts;
    Button toIngredients;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        db = new Data_base(Main_activity.this);
        list_is_empty = findViewById(R.id.main_activity_list_is_empty);
        list_is_empty.setText(R.string.pick_day);
        calendar =findViewById(R.id.mainActivity_calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String month_str;
                String day_str;
                if (month<10) month_str = "0"+month;
                else month_str = ""+month;
                if (day<10) day_str = "0"+day;
                else day_str = ""+day;
                String picked_date = year +"."+month_str +"."+day_str;
                ArrayList<Product> productsList = db.get_product_list_by_day(picked_date);
                products_in_day = findViewById(R.id.mainActivity_listOfProductsInDay);
                products_in_day_adp = new Product_in_day_list_adp(Main_activity.this, productsList);
                products_in_day.setAdapter(products_in_day_adp);
                add = findViewById(R.id.main_activity_rounded_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Main_activity.this, Add_product_in_day_activity.class);
                        intent.putExtra("TYPE",picked_date);
                        startActivity(intent);
                    }
                });
                if(productsList.isEmpty()){
                    list_is_empty.setText(R.string.list_products_isnt_added);
                }
                else {

                }

            }
        });
        toIngredients = findViewById(R.id.mainActivity_toIngredients);
        toIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_activity.this, List_activity.class);
                intent.putExtra(List_activity.type_arg,1);
                startActivity(intent);
            }
        });
        toProducts = findViewById(R.id.mainActivity_toProducts);
        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_activity.this, List_activity.class);
                intent.putExtra(List_activity.type_arg,2);
                startActivity(intent);
            }
        });
    }
}