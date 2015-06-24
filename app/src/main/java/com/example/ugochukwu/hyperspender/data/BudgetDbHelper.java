package com.example.ugochukwu.hyperspender.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ugochukwu.hyperspender.data.BudgetContract.*;


/**
 * Created by ugochukwu on 6/7/2015.
 */
public class BudgetDbHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "budget.db";
    public BudgetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude

        final String SQL_CREATE_MONTH_TABLE = "CREATE TABLE " + MonthEntry.TABLE_NAME + " (" +
                MonthEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MonthEntry.COLUMN_BUDGET_AMOUNT + " REAL NOT NULL, " +
                MonthEntry.COLUMN_COMMENT + " TEXT NOT NULL, " +
                MonthEntry.COLUMN_MONTH_NAME + " TEXT NOT NULL); ";

        final String SQL_CREATE_AMOUNT_TABLE = "CREATE TABLE " + AmountEntry.TABLE_NAME + " (" +
                AmountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AmountEntry.COLUMN_MONTH_KEY + " INTEGER NOT NULL, " +
                AmountEntry.COLUMN_DATETEXT + " TEXT NOT NULL, " +
                AmountEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                AmountEntry.COLUMN_AMOUNT_TYPE + " INTEGER NOT NULL, " +
                AmountEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                AmountEntry.COLUMN_BRIEF_DESCRIPTION + " TEXT NOT NULL, "+
                // Set up the month key column as a foreign key to month table.
                " FOREIGN KEY (" + AmountEntry.COLUMN_MONTH_KEY + ") REFERENCES " +
                MonthEntry.TABLE_NAME + " (" + MonthEntry._ID + ")); " ;


        db.execSQL(SQL_CREATE_MONTH_TABLE);
        db.execSQL(SQL_CREATE_AMOUNT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        onCreate(db);

    }

    public  boolean getMonthWithName(String monthName){

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM month_budget_table WHERE month_name = ?", new String[]{monthName});

        if(cursor.moveToFirst()) {
            return true;
        }

      return false;

    }

    public double getTotalSpentForMonth(long monthId){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT amount FROM amount_table WHERE month_id = ? AND amount_type = 0",new String[]{String.valueOf(monthId)});
        double total =0;
        while(cursor.moveToNext()){
            total += cursor.getLong(cursor.getColumnIndex("amount")) ;
        }
        return total;

    }
    public double getTotalMadeForMonth(long monthId){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT amount FROM amount_table WHERE month_id = ? AND amount_type = 1", new String[]{String.valueOf(monthId)});
        double total =0;
        while(cursor.moveToNext()){
            total += cursor.getDouble(cursor.getColumnIndex("amount")) ;
        }
        return total;

    }
    public  double getBudgetamount(long monthId){

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT budget_amount FROM month_budget_table WHERE _id = ?", new String[]{String.valueOf(monthId)});
        int total = 0;
        if(cursor.moveToFirst()) {
            total+= cursor.getDouble(cursor.getColumnIndex("budget_amount"));
        }

        return total;

    }

    public int getCategoryCount(long monthId, String columnName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM amount_table WHERE category = ? AND month_id = ? ",new String[]{columnName,String.valueOf(monthId)});
        int total = cursor.getCount();
        return total;
    }

    public String getMonthNameWithId(long monthId){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM month_budget_table WHERE _id = ? ", new String[]{String.valueOf(monthId)});
            String name = "";
        if(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex("month_name"));
        }
        return name;
    }
}
