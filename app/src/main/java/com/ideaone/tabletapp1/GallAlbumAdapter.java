package com.ideaone.tabletapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class GallAlbumAdapter extends BaseAdapter {

    ArrayList<GallAlbumObject> list;
    Context context;

    GallAlbumAdapter(Context c, ArrayList<GallAlbumObject> albumObject){
        this.context = c;
        this.list = albumObject;
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

        GallAlbumObject theObject = list.get(position);
        userName.setText(theObject.albumName);

        return row;
    }
}