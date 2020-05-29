package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoreDetailsActivity extends AppCompatActivity {

    public int pos;
    public String[] photos;
    public ImageButton btnshowphoto;
    public Homework curHw;
    public static ScrollView sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Подробности");

        setContentView(R.layout.activity_more_details);

        TextView txt = (TextView)findViewById(R.id.textView);
        btnshowphoto = (ImageButton)findViewById(R.id.buttonshowphto);
        pos = getIntent().getIntExtra("pos", 0);
        txt.setText(getIntent().getStringExtra("txt"));
        photos = getIntent().getStringArrayExtra("photos");
        sc = (ScrollView)findViewById(R.id.scrollView2);

        curHw = MainActivity.list.get(pos);

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

    public void onClickChange() {
        Intent i = new Intent();
        i.putExtra("pos", pos);
        setResult(MainActivity.RESULT_NEED_CHANGE, i);
        finish();
    }

    public void onClickDelete(){
        Intent i = new Intent();
        i.putExtra("pos", pos);
        setResult(MainActivity.RESULT_NEED_DELETE, i);
        finish();
    }

    public void onClickSend(){
        String serverUrl = "http://10.0.2.2:4567/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[curHw.photos.size()];
        for (int i = 0; i < curHw.photos.size(); i++) {
            File file = new File(curHw.photos.get(i));
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            surveyImagesParts[i] = MultipartBody.Part.createFormData("upload" + Integer.toString(i), file.getName(), fileReqBody);
        }
        if (surveyImagesParts.length == 0) {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            surveyImagesParts = new MultipartBody.Part[1];
            surveyImagesParts[0] = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        APIservice apiService = retrofit.create(APIservice.class);
        Gson gson1 = new Gson();
        String jsonString = gson1.toJson(curHw);
        Log.d("sas", jsonString);

        Call<UploadResponse> postHomework = apiService.uploadHomework(surveyImagesParts, jsonString);
        postHomework.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful()) {
                    ClipData clipData = ClipData.newPlainText("text", response.body().result);
                    ClipboardManager manager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);;
                    manager.setPrimaryClip(clipData);
                    Toast toast = Toast.makeText(MoreDetailsActivity.this, "код скопирован в буфер обмена", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Log.d("sas", call.request().url().toString());
                    Toast toast = Toast.makeText(MoreDetailsActivity.this, "ошибка", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("sas", "fail2");
                t.printStackTrace();
            }
        });
    }

    public void onClickShowphotos(View v){
        Intent i = new Intent(this, PhotosShowActivity.class);
        i.putExtra("photos", photos);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.more_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_button:
                onClickChange();
                break;
            case R.id.menu_delete_button:
                onClickDelete();
                break;
        }
        return true;
    }

}
