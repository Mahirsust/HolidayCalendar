package com.example.mahirhasan.registrationandlogin;
// This class handles all the database activities
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.widget.TableLayout;

import java.util.Vector;

public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "holidaysDB.db";
    public static String Table_Name = "";
    public static final String COLUMN_HolidayName = "HolidayName";
    public static final String COLUMN_date = "Date";
    public static final String COLUMN_category = "Category";


    //We need to pass database information along to superclass
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // String query = "CREATE TABLE " + Table_Name + "(" + COLUMN_HolidayName + " TEXT, " + COLUMN_date + " TEXT, " +  COLUMN_category + " TEXT " +");";
        //db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    //Add a new row to the database
    public void addData(String Table_Name, Vector<Holiday> data ){
        System.out.println("DB = " + Table_Name);

        SQLiteDatabase db = getWritableDatabase();
        // onUpgrade(db, 1, 1);
        String query = "CREATE TABLE IF NOT EXISTS " + Table_Name + "(" + COLUMN_HolidayName + " TEXT, " + COLUMN_date + " TEXT, "
                +  COLUMN_category + " TEXT " +");";
        db.execSQL(query);
        //System.out.println("SQL = date - " + h.getDate() + " event - " + h.getName() +" category - " + h.getCategory());
        ContentValues values = new ContentValues();


        for(int i=0; i<data.size(); i++) {
            String name = data.get(i).getName();
            String category = data.get(i).getCategory();
            category = get_category(category);
            String date = data.get(i).getDate();
            values.put(COLUMN_HolidayName, name);
            values.put(COLUMN_date, date);
            values.put(COLUMN_category, category);
            db.insert(Table_Name, null, values);
            //System.out.println("date - " + date + " event - " + name +" category - " + category);
        }

        db.close();
    }

    //Delete a product from the database
    public void deleteData(String Table_Name, String category){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Table_Name + " WHERE " + COLUMN_category + "=\"" + category + "\";");
    }

    // this is goint in record_TextView in the Main activity.
    public Vector<Holiday> databaseToString(String Table_Name){

        // String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        Vector <Holiday> data = new Vector<Holiday>();
        // Holiday h = new Holiday();
        String query = "CREATE TABLE IF NOT EXISTS " + Table_Name + "(" + COLUMN_HolidayName + " TEXT, " + COLUMN_date + " TEXT, "
                +  COLUMN_category + " TEXT " +");";
        String query1 = "SELECT * FROM " + Table_Name;

        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        recordSet = db.rawQuery(query1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {

            String name = recordSet.getString(recordSet.getColumnIndex(COLUMN_HolidayName));
            String category = recordSet.getString(recordSet.getColumnIndex(COLUMN_category));
            String date = recordSet.getString(recordSet.getColumnIndex(COLUMN_date));
            Holiday h = new Holiday(name, date, category);
            //System.out.println("SQL = date - " + h.getDate() + " event - " + h.getName() +" category - " + h.getCategory());
            data.add(h);
            recordSet.moveToNext();
        }
        db.close();
        System.out.println("DB = Data Size = " + data.size());
        return data;
    }

    public String get_category(String ii) {

        String ret = ii;
        if(ii.equals( "1")) ret = "SUST_Student";
        else if(ii.equals( "2")) ret = "SUST_Teacher";
        else if(ii.equals( "3")) ret = "DU_Student";
        else if(ii.equals( "4")) ret = "DU_Teacher";
        else if(ii.equals( "5")) ret = "Buet_Student";
        else if(ii.equals( "6")) ret = "Buet_Teacher";
        else if(ii.equals( "7")) ret = "Bank_Holidays";
        //System.out.println("ret = " + ret);
        return ret;

    }

    public void setName(String Table_Name) {
        this.Table_Name = Table_Name;
    }
}