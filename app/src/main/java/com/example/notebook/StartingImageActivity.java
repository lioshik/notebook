package com.example.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartingImageActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_starting);
        ImageView img = (findViewById(R.id.imageView2));
        img.setImageResource(R.mipmap.title_background);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        TimerTask task = new TimerTask() {
            public void run() {
                Intent intent = new Intent(StartingImageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 2000L;
        timer.schedule(task, delay);
    }
}
