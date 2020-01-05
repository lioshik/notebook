package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MoreDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        TextView txt = (TextView)findViewById(R.id.textView);
        txt.setText(getIntent().getStringExtra("txt"));
    }

    public void onClickBack(View v){
        finish();
    }

}
