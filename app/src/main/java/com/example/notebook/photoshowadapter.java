package com.example.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class photoshowAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    String[] list;

    public photoshowAdapter(Context context, String[] list) {
        this.list = list;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.photoshowadapter, parent, false);
        }
        ImageView img = (ImageView)view.findViewById(R.id.imgshowphoto);
        File f = new File(getItem(position));
        Picasso.get().load(f).fit().centerInside().into(img);
        return view;
    }
}
