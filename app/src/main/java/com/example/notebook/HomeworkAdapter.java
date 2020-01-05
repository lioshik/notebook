package com.example.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HomeworkAdapter extends BaseAdapter {

    private List<Homework> list;
    private LayoutInflater layoutInflater;

    public HomeworkAdapter(Context context, List<Homework> list) {
        this.list = list;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.show_homework, parent, false);
        }
        TextView large = (TextView)view.findViewById(R.id.name);
        TextView small = (TextView)view.findViewById(R.id.moreinfo);
        TextView date = (TextView)view.findViewById(R.id.placefordate);
        large.setText(list.get(position).subj);
        date.setText(list.get(position).date.getString());
        StringBuilder format = new StringBuilder(list.get(position).txt);
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '\n') {
                format.setCharAt(i, ' ');
            }
        }
        if (format.length() > 20) {
            small.setText(format.toString().substring(0, 19) + "...");
        } else {
            small.setText(format.toString());
        }

        return view;
    }
}
