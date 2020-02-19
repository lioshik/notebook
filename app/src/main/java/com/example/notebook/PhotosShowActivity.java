package com.example.notebook;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotosShowActivity extends AppCompatActivity {
    public String[] photos;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_show);
        photos = getIntent().getStringArrayExtra("photos");
        ListView lw = (ListView)findViewById(R.id.photosshowlist);
        lw.setAdapter(new photoshowAdapter(this, photos));
    }

    public void onClickback(View v){
        finish();
    }

}
