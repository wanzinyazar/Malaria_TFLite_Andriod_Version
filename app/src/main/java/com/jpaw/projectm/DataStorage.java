package com.jpaw.projectm;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DataStorage extends AppCompatActivity {

    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    Cursor cursor;
    ListDataAdapter listDataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_storage);
        listView = (ListView) findViewById(R.id.listView);
        listDataAdapter = new ListDataAdapter(getApplicationContext(),R.layout.row_layout);
        listView.setAdapter(listDataAdapter);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        cursor = databaseHelper.getData(sqLiteDatabase);

        if (cursor.moveToFirst()){

            do{
                String name,para,unin;

                name = cursor.getString(0);
                para = cursor.getString(1);
                unin = cursor.getString(2);
                DataProvider dataProvider = new DataProvider(name, para,unin);
                listDataAdapter.add(dataProvider);


            }
            while (cursor.moveToNext());

        }


    }



}
