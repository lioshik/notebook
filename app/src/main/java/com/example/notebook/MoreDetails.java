package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoreDetails extends AppCompatActivity implements View.OnTouchListener {

    private int pos;
    String[] photos;
    ImageButton btnshowphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        TextView txt = (TextView)findViewById(R.id.textView);
        btnshowphoto = (ImageButton)findViewById(R.id.buttonshowphto);
        pos = getIntent().getIntExtra("pos", 0);
        txt.setText(getIntent().getStringExtra("txt"));
        photos = getIntent().getStringArrayExtra("photos");
        ImageView triger = (ImageView)findViewById(R.id.triger2);
        ScrollView sc = (ScrollView)findViewById(R.id.scrollView2);
        sc.setOnTouchListener(this);
        triger.setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                btnshowphoto.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                btnshowphoto.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_CANCEL:
                btnshowphoto.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

}
