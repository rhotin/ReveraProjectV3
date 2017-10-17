package com.ideaone.tabletapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MenuAdapter extends BaseAdapter {

    ArrayList<MenuObject> list;
    Context context;

    MenuAdapter(Context c, ArrayList<MenuObject> menuObject){
        this.context = c;
        this.list = menuObject;
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
        View row = inflater.inflate(R.layout.single_album, parent, false);

        TextView userName = (TextView) row.findViewById(R.id.textView);

        MenuObject theObject = list.get(position);
        userName.setText(theObject.lunchSoup);

        return row;
    }
}