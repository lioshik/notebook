package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MoreDetails extends AppCompatActivity {

    private int pos;
    String[] photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        TextView txt = (TextView)findViewById(R.id.textView);
        pos = getIntent().getIntExtra("pos", 0);
        txt.setText(getIntent().getStringExtra("txt"));
        photos = getIntent().getStringArrayExtra("photos");
    }

    public void onClickBack(View v){
        finish();
    }

    public void onClickChange(View v) {
        Intent i = new Intent();
        i.putExtra("pos", pos);
        setResult(MainActivity.RESULT_NEED_CHANGE, i);
        finish();
    }

    public void onClickDelete(View v){
        Intent i = new Intent();
        i.putExtra("pos", pos);
        setResult(MainActivity.RESULT_NEED_DELETE, i);
        finish();
    }

    public void onClickShowphotos(View v){
        Intent i = new Intent(this, PhotosShowActivity.class);
        i.putExtra("photos", photos);
        startActivity(i);
    }


}
