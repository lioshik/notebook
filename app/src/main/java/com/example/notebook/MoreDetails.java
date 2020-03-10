package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoreDetails extends AppCompatActivity {

    private int pos;
    String[] photos;
    ImageButton btnshowphoto;
    public static ScrollView sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        TextView txt = (TextView)findViewById(R.id.textView);
        btnshowphoto = (ImageButton)findViewById(R.id.buttonshowphto);
        pos = getIntent().getIntExtra("pos", 0);
        txt.setText(getIntent().getStringExtra("txt"));
        photos = getIntent().getStringArrayExtra("photos");
        sc = (ScrollView)findViewById(R.id.scrollView2);
        sc.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(sc.getScrollY() == 0) {
                    if (btnshowphoto.getVisibility() == View.INVISIBLE) {
                        btnshowphoto.setVisibility(View.VISIBLE);
                        TranslateAnimation animate = new TranslateAnimation(
                                0,
                                0,
                                btnshowphoto.getHeight(),
                                0);
                        animate.setDuration(250);
                        animate.setFillAfter(true);
                        btnshowphoto.startAnimation(animate);
                    }
                } else {
                    if (btnshowphoto.getVisibility() == View.VISIBLE) {
                        btnshowphoto.setVisibility(View.INVISIBLE);
                        TranslateAnimation animate = new TranslateAnimation(
                                0,
                                0,
                                0,
                                btnshowphoto.getHeight() + 600);
                        animate.setDuration(250);
                        animate.setFillAfter(true);
                        btnshowphoto.startAnimation(animate);
                    }
                }
            }
        });
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
