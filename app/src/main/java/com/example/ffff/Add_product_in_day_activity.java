package com.example.ffff;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Add_product_in_day_activity extends AppCompatActivity {
    ListView product_list;
    Product_list_adp list_adp;
    Data_base db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_in_day);
        getSupportActionBar().hide();
        product_list = findViewById(R.id.add_product_in_day_list_view);
        ArrayList<Product> products = new ArrayList<>();
        db = new Data_base(Add_product_in_day_activity.this);
        db.get_products(products);
        list_adp = new Product_list_adp(Add_product_in_day_activity.this,products);
        product_list.setAdapter(list_adp);
        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Dialog enter_weight_dialog = new Dialog(Add_product_in_day_activity.this,R.style.alert_dialog_style);
                        enter_weight_dialog.setContentView(R.layout.enter_weight_dialog);
                EditText enter_weight = enter_weight_dialog.findViewById(R.id.add_product_in_day_dialog_enter_weight);
                Button ok_btn = enter_weight_dialog.findViewById(R.id.add_product_in_day_dialog_ok_button);
                Button cancel_button =enter_weight_dialog.findViewById(R.id.add_product_in_day_dialog_cancel_button);
                enter_weight_dialog.setTitle(R.string.enter_weight_of_product);
                Product p = (Product)list_adp.getItem(pos);
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enter_weight_dialog.dismiss();
                    }
                });
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            String weight = enter_weight.getText().toString();
                            weight.replace(",",".");

                            db.add_product_in_day(Float.parseFloat(weight),p.id,
                                getIntent().getLongExtra("TYPE",0));
                            Intent intent = new Intent(Add_product_in_day_activity.this,Main_activity.class);
                            startActivity(intent);
                        }catch (ClassCastException e){
                            e.printStackTrace();
                            Toast.makeText(Add_product_in_day_activity.this,R.string.wrong_data_entered,Toast.LENGTH_SHORT).show();
                            enter_weight_dialog.dismiss();
                        }
                        enter_weight_dialog.dismiss();
                    }
                });
                enter_weight_dialog.show();
            }
        });
    }
}
