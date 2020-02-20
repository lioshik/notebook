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

public class photosdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    photosdapter adapter = this;

    public photosdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return photos_activity.photos.size();
    }

    @Override
    public String getItem(int position) {
        return photos_activity.photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.photoadapter, parent, false);
        }
        ImageView img = (ImageView)view.findViewById(R.id.imageView);
        Button btn = (Button)view.findViewById(R.id.del);
        btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        View parent_row = (View) v.getParent();
                        ListView lv = (ListView) parent_row.getParent();
                        int position = lv.getPositionForView(parent_row);
                        photos_activity.photos.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }
        );
        File f = new File(getItem(position));
        Picasso.get().load(f).fit().centerInside().into(img);
        return view;
    }


}
