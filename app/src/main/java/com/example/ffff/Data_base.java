package com.example.ffff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Data_base {
    private static final String DATABASE_NAME = "simple.db";
    private static final int DATABASE_VERSION = 1;
    private static final String id_column = "id";
    private static final String name_column = "name";
    private SQLiteDatabase db;
    //Days table
    private static final String days_table_name = "Days";
    private static final String days_table_day = "day";

    //product table
    private static final String product_table_name = "Products";
    private static final String product_weight_column = "weight";
    private static final String product_vrgCalories_column = "vrg_calories";

    //Ingredients table
    private static final String ingredients_table_name = "Ingredients";
    private static final String ingredients_type_column = "type_of_ingredient";
    private static final String ingredients_caloriesPerGram_column = "calories_per_gramm";

    //Products_in_day table
    private static final String productsInDay_table_name = "Products_in_day";
    private static final String productsInDay_idDay_column = "id_day";
    private static final String productsInDay_idProducts_column = "id_products";
    private static final String productsInDay_productWeight_column = "product_weight";

    //Ingredients_in_product table
    private static final String ingredientsInProduct_table_name = "Ingredients_in_product";
    private static final String ingredientsInProduct_idProduct_column = "id_product";
    private static final String ingredientsInProduct_idIngredient_column = "id_ingredient";
    private static final String ingredientsInProduct_methodOfCookId_column = "method_of_cook_id";
    private static final String ingredientsInProduct_Weight_column = "weight";

    //Type_of_ingredient table
    private static final String typeOfIngredient_table_name = "Type_of_ingredient";
    private static final String typeOfIngredient_ingredientType_column = "ingredient_type";

    //Method_of_cook table
    private static final String methodOfCook_table_name = "Method_of_cook";
    private static final String methodOfCook_methodOfCook_column = "method_of_cook";

    public Data_base(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
    }
    public void remove_ingredient(Ingredient in) {
        db.delete(ingredients_table_name,name_column+"=?",new String[]{in.name});
    }
    public void remove_product(Product pr){
        db.delete(product_table_name,name_column+"=?",new String[]{pr.name});
    }
    public void get_ingredients(ArrayList<Ingredient> arr) {
        Cursor getIngredientsTable = db.query(ingredients_table_name,
                new String[]{id_column, name_column, ingredients_type_column, ingredients_caloriesPerGram_column}, null, null, null, null, null);
        Cursor typesOfIngredients;
        getIngredientsTable.moveToFirst();
        int i = 0;
        if (!getIngredientsTable.isAfterLast()) {
            do {
                arr.add(i,new Ingredient());
                arr.get(i).name = getIngredientsTable.getString(1);
                typesOfIngredients = db.query(typeOfIngredient_table_name,
                        new String[]{typeOfIngredient_ingredientType_column},
                        id_column + " = "+getIngredientsTable.getString(2),
                        null,null, null, null);
                typesOfIngredients.moveToFirst();
                if (!typesOfIngredients.isAfterLast()) {
                    arr.get(i).type_of_ingredient = typesOfIngredients.getString(0);
                }
                arr.get(i).calories_per_gram = getIngredientsTable.getFloat(3);
                i++;
            } while (getIngredientsTable.moveToNext());
            typesOfIngredients.close();
        }
        getIngredientsTable.close();
    }
    public ArrayList<Product> get_product_list_by_day(String date){
        Cursor day_crs = db.query(days_table_name,new String[]{id_column}
                ,days_table_day+"=?", new String[]{date},
                null,null,null);
        day_crs.moveToFirst();
        ArrayList<Product> final_arr= new ArrayList<>();
        if(day_crs.isAfterLast()){
            ContentValues cv = new ContentValues();
            cv.put(days_table_day,date);
            long id_d=db.insert(days_table_name,null,cv);
            cv = new ContentValues();
            cv.put(productsInDay_idDay_column,id_d);
            System.out.println("не было такого дня, а теперь есть");
            return final_arr;
        }
        else{
            Integer id_day = day_crs.getInt(0);
            System.out.println(id_day);
            Cursor products_in_day_crs = db.query(productsInDay_table_name,
                    new String[]{productsInDay_idDay_column},productsInDay_idDay_column+"=?"
                    ,new String[]{String.valueOf(id_day)},null,null,null);
            products_in_day_crs.moveToFirst();
            if(products_in_day_crs.isAfterLast()){
                System.out.println("не");
                products_in_day_crs.close();
                return final_arr;
            }
            else{
                Cursor products_crs;
                do{
                    products_crs = db.query(
                            product_table_name,new String[]{name_column,product_vrgCalories_column},
                            id_column+"=?",new String[]{products_in_day_crs.getString(0)},
                            null,null,null);
                    Product p = new Product();
                    p.name = products_crs.getString(0);
                    p.calories_per_gram = products_crs.getFloat(1);
                    final_arr.add(p);
                }while(products_in_day_crs.moveToNext());
                products_in_day_crs.close();
                products_crs.close();
            }
        }
        day_crs.close();
        return final_arr;
    }
    public ArrayList<Product>get_products(ArrayList<Product>arr){
        Cursor products_table = db.query(product_table_name,
                null,null,null,null,null,null);
        products_table.moveToFirst();
        arr.clear();
        if(!products_table.isAfterLast()){
            int i = 0;
            do{
                Product p = new Product();
                p.name = products_table.getString(1);
                p.calories_per_gram = products_table.getFloat(2);
                arr.add(p);
            }while(products_table.moveToNext());
            products_table.close();
        }
        return arr;
    }
    public void add_ingredient(Ingredient ingredient){
        ContentValues cv = new ContentValues();
        cv.put(name_column,ingredient.name);
        cv.put(ingredients_caloriesPerGram_column,ingredient.calories_per_gram);
        cv.put(ingredients_type_column, ingredient.type_of_ingredient_id);
        db.insert(ingredients_table_name,null,cv);
    }
    public void add_product(String name, ArrayList<Ingredient>composition){
        ContentValues ingredients_in_product_cv = new ContentValues();
        ContentValues product_cv = new ContentValues();
        Cursor ingredient_cursor;
        long product_id =0;
        float total_weight = 0;
        float total_calories =0;
        product_cv.put(name_column,name);
        product_id = db.insert(product_table_name,null,product_cv);
        for (int i = 0; i< composition.size();i++){
            ingredient_cursor = db.query(ingredients_table_name,new String[]{id_column,ingredients_caloriesPerGram_column},
                    name_column+"=?", new String[]{composition.get(i).name},
                    null,null,null);
            ingredient_cursor.moveToFirst();
            if(!ingredient_cursor.isAfterLast()){
                ingredients_in_product_cv.put(ingredientsInProduct_methodOfCookId_column, composition.get(i).method_of_cook_id);
                ingredients_in_product_cv.put(ingredientsInProduct_idIngredient_column,ingredient_cursor.getInt(0));
                ingredients_in_product_cv.put(ingredientsInProduct_idProduct_column,product_id);
                ingredients_in_product_cv.put(ingredientsInProduct_Weight_column,composition.get(i).weight);
                db.insert(ingredientsInProduct_table_name,null,ingredients_in_product_cv);
                total_calories+=composition.get(i).weight*0.01*ingredient_cursor.getFloat(1);
                total_weight+= composition.get(i).weight;
            }
        }

        product_cv = new ContentValues();
        product_cv.put(product_vrgCalories_column,(Math.round(total_calories/total_weight*10000))/10000.);
        System.out.println(String.valueOf(Math.round(total_calories/total_weight*10000)/10000.));
        db.update(product_table_name,product_cv,id_column+"=?",new String[]{Long.toString(product_id)});
    }
    public void add_product_in_day(float weight,int index,String date){
        Cursor cursor = db.query(days_table_name,new String[]{id_column},
                days_table_day+"=?",new String[]{date},null,null,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            ContentValues contentValues =new ContentValues();
            contentValues.put(productsInDay_idDay_column,cursor.getInt(0));
            contentValues.put(productsInDay_idProducts_column,index);
            contentValues.put(productsInDay_productWeight_column,weight);
        }
    }
    class OpenHelper extends SQLiteOpenHelper{
        private Context ctx;
        public OpenHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            ctx = context;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String  query = "CREATE TABLE " + typeOfIngredient_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    typeOfIngredient_ingredientType_column+ " TEXT UNIQUE NOT NULL "+
                    //"FOREIGN KEY("+id_column+") REFERENCES "+ name_column+"("+ingredients_type_column+")"+
                    ");";
            db.execSQL(query);
            query = "CREATE TABLE " + methodOfCook_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    methodOfCook_methodOfCook_column + " TEXT " +");";
            db.execSQL(query);
            query ="CREATE TABLE " + ingredientsInProduct_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ingredientsInProduct_idProduct_column + " INTEGER NOT NULL, " +
                    ingredientsInProduct_idIngredient_column + " INTEGER NOT NULL, " +
                    ingredientsInProduct_methodOfCookId_column + " INTEGER NOT NULL, " +
                    ingredientsInProduct_Weight_column + " FLOAT NOT NULL," +
                    "FOREIGN KEY("+ingredientsInProduct_methodOfCookId_column+") REFERENCES "+ methodOfCook_table_name+"("+id_column+"));";
            db.execSQL(query);
            query = "CREATE TABLE "+ productsInDay_table_name+" (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    productsInDay_idDay_column + " INTEGER NOT NULL, " +
                    productsInDay_idProducts_column + " INTEGER NOT NULL, "+
                    productsInDay_productWeight_column +" INTEGER NOT NULL, "+
                    "FOREIGN KEY("+productsInDay_idProducts_column+") REFERENCES "+ product_table_name+"("+id_column+"));";
            db.execSQL(query);query = "CREATE TABLE " + ingredients_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    name_column +" TEXT NOT NULL UNIQUE, " +
                    ingredients_type_column + " INTEGER NOT NULL," +
                    ingredients_caloriesPerGram_column + " REAL, "+
                    "FOREIGN KEY("+id_column+") REFERENCES "+ ingredientsInProduct_table_name+"("+ingredientsInProduct_idIngredient_column+"), "+
                    "FOREIGN KEY("+ingredients_type_column+") REFERENCES "+ typeOfIngredient_table_name+"("+id_column+")"+
                    ");";
            db.execSQL(query);
            query = "CREATE TABLE " +product_table_name+"(" +
                    id_column +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    name_column + " TEXT NOT NULL UNIQUE, "+
                    product_vrgCalories_column + " REAL, "+
                    "FOREIGN KEY("+id_column+") REFERENCES "+ingredientsInProduct_table_name +"("+ingredientsInProduct_idProduct_column+")"+
                    ");";
            db.execSQL(query);
            query = "CREATE TABLE "+ days_table_name+"("+
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    days_table_day + " TEXT NOT NULL UNIQUE, "+
                    "FOREIGN KEY("+id_column+") REFERENCES "+ productsInDay_table_name+"("+productsInDay_idProducts_column+"));";
            db.execSQL(query);
            String [] ingredients_name = ctx.getResources().getStringArray(R.array.ingredient_types);
            ContentValues cv;
            for(int i = 0;i<ingredients_name.length;i++) {
                cv = new ContentValues();
                cv.put(typeOfIngredient_ingredientType_column, ingredients_name[i]);
                db.insert(typeOfIngredient_table_name, null, cv);
            }
            String[]methods_of_cook = ctx.getResources().getStringArray(R.array.methods_of_cooking);
            for (int i = 0;i<methods_of_cook.length-1;i++){
                cv= new ContentValues();
                cv.put(methodOfCook_methodOfCook_column,methods_of_cook[i]);
                db.insert(methodOfCook_table_name, null, cv);
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + productsInDay_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + ingredientsInProduct_table_name );
            db.execSQL("DROP TABLE IF EXISTS " + typeOfIngredient_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + methodOfCook_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + ingredients_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + product_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + days_table_name);
            onCreate(db);
        }
    }
}