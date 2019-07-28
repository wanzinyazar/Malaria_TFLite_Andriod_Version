package com.jpaw.projectm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    //private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "patient_data.DB";
   // private static final String COL1 = "Parasitized";
    // private static final String COL2 = "Uninfected";

    private static final String CREATE_QUERY = "CREATE TABLE " + DataObject.UserInfo.TABLE_NAME + " ( " +
            DataObject.UserInfo.name+ " TEXT," + DataObject.UserInfo.parasitized_percentage + " TEXT,"+ DataObject.UserInfo.uninfected_percentage+
            " TEXT);";


    public DatabaseHelper(Context context) {

        super(context, TABLE_NAME, null, 1);
        Log.e("DATABASE OPERATIONS", "Database Created...");

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);

        Log.e("DATABASE OPERATIONS", "TABLE CREATED...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData( String name, String parasitized, String uninfected, SQLiteDatabase db) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(DataObject.UserInfo.name, name);
        contentValues.put(DataObject.UserInfo.parasitized_percentage, parasitized);
        contentValues.put(DataObject.UserInfo.uninfected_percentage, uninfected);
        db.insert(DataObject.UserInfo.TABLE_NAME, null,contentValues);

        Log.e("DATABASE OPERATIONS", "ONE ROW INSERTED...");
    }



    /**
     * Returns all the data from database
     * @return **/

    public Cursor getData( SQLiteDatabase db){

       Cursor cursor;
       String[] projections = { DataObject.UserInfo.name, DataObject.UserInfo.uninfected_percentage, DataObject.UserInfo.parasitized_percentage};
       cursor = db.query(DataObject.UserInfo.TABLE_NAME, projections, null, null, null,null,null);
       return cursor;

    }

/*    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     *//*
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName

    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
*/
}
