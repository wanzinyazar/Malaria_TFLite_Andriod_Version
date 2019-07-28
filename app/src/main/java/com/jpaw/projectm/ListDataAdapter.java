package com.jpaw.projectm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListDataAdapter extends ArrayAdapter {

 List list = new ArrayList();
    public ListDataAdapter (Context context, int resource){
        super (context, resource);

    }

    static class LayoutHandler{

        TextView NAME,PARA,UNIN;
    }

    public void add(Object object){

        super.add(object);
        list.add(object);
    }

    public int getCount(){

        return list.size();
    }

    public Object getItem(int position){

        return list.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View row = convertView;
        LayoutHandler layoutHandler;

        if (row == null){

            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.NAME = (TextView) row.findViewById(R.id.text_user_name);
            layoutHandler.PARA = (TextView) row.findViewById(R.id.text_paratisized);
            layoutHandler.UNIN = (TextView) row.findViewById(R.id.text_uninfected);
            row.setTag(layoutHandler);

        }

        else{

            layoutHandler = (LayoutHandler) row.getTag();


        }

        DataProvider dataProvider = (DataProvider) this.getItem(position);
        layoutHandler.NAME.setText(dataProvider.getName());
       layoutHandler.PARA.setText(dataProvider.getParatisized());
       layoutHandler.UNIN.setText(dataProvider.getUninfected());

        return row;
    }
}
