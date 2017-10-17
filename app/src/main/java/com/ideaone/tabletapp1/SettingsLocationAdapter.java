package com.ideaone.tabletapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsLocationAdapter extends BaseAdapter {

    ArrayList<SettingsLocationObject> list;
    Context context;

    SettingsLocationAdapter(Context c, ArrayList<SettingsLocationObject> locObjects){
        this.context = c;
        this.list = locObjects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.a_single_location_row, parent, false);

        TextView locName = (TextView) row.findViewById(R.id.locTextView);

        SettingsLocationObject theLocObject = list.get(position);
        locName.setText(theLocObject.handle);

        return row;
    }
}
