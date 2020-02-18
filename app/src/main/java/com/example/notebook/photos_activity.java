package com.example.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class photos_activity extends AppCompatActivity {
    public ListView lw;
    public static ArrayList<String> photos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_activity);
        lw = (ListView)findViewById(R.id.photoslist);
        photos = new ArrayList<String>();
        for (int i = 0; i < getIntent().getStringArrayExtra("photos").length; i++) {
            photos.add(getIntent().getStringArrayExtra("photos")[i]);
        }
        lw.setAdapter(new photoshowadapter(this));
    }

    public void onClickcancel(View v){
        finish();
    }

    public void onClickOk(View v){
        String[] ret = new String[photos.size()];
        for (int i = 0; i < photos.size(); i++) {
            ret[i] = photos.get(i);
        }
        Intent i = new Intent();
        i.putExtra("photos", ret);
        setResult(123, i);
        finish();
    }
}
