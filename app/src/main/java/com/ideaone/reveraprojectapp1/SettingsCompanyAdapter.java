package com.ideaone.reveraprojectapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by roman on 11/5/2015.
 */
public class SettingsCompanyAdapter extends BaseAdapter {

    ArrayList<SettingsCompanyObject> list;
    Context context;

    SettingsCompanyAdapter(Context c, ArrayList<SettingsCompanyObject> companyObjects){
        this.context = c;
        this.list = companyObjects;
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
        View row = inflater.inflate(R.layout.a_single_company_row, parent, false);

        TextView compName = (TextView) row.findViewById(R.id.compTextView);

        SettingsCompanyObject theCompObject = list.get(position);
        compName.setText(theCompObject.text);

        return row;
    }
}
