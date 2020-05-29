package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class New_homework extends AppCompatActivity{

    Button btnok, btncancel, datechose, subjchose;
    ImageButton btnshowphoto, btntakephoto;
    TextView txt;
    String[] subjects;
    DateData chosendata;
    int chosensubj;
    Calendar calendar;
    List<String> photos;
    ImageButton btnaddphoto;
    Uri outputFileUri;
    public static final int REQUEST_PHOTO = 1337;
    public static final int REQUEST_SHOW_PHOTO = 1432;
    public static final int REQUEST_CHANGE_PHOTO = 1435;
    public static final int REQUEST_PICK_PHOTO = 15372;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Добавить");
        setContentView(R.layout.activity_new_homework);
        btnaddphoto = (ImageButton)findViewById(R.id.takephotobutton);
        btnshowphoto = (ImageButton)findViewById(R.id.showimagesbutton);
        btnok = (Button)findViewById(R.id.ok);
        txt = (TextView)findViewById(R.id.text);
        btncancel = (Button)findViewById(R.id.cancel);
        subjects = getIntent().getStringArrayExtra("Subjects");
        subjchose = (Button)findViewById(R.id.subjectchose);
        datechose = (Button)findViewById(R.id.date);
        chosensubj = 0;
        calendar = Calendar.getInstance();
        photos = new ArrayList<String>();
        btnaddphoto = (ImageButton)findViewById(R.id.takephotobutton);

        int Date = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);
        chosendata = new DateData(Year, Month, Date);

        if (getIntent().getIntExtra("RequestCode", 0) == MainActivity.REQUEST_CHANGE_HW) {
            setTitle("Изменить");
            txt.setText(getIntent().getStringExtra("txt"));
            String[] photosar = getIntent().getStringArrayExtra("photos");
            photos = new ArrayList<String>();
            for (int i = 0; i < photosar.length; i++) {
                photos.add(photosar[i]);
            }
            for (int i = 0; i < subjects.length; i++) {
                if (getIntent().getStringExtra("subj") == subjects[i]){
                    chosensubj = i;
                    subjchose.setText(subjects[i]);
                }
            }
            chosendata = new DateData(getIntent().getIntExtra("year", Year), getIntent().getIntExtra("month", Month), getIntent().getIntExtra("day", Date));
            datechose.setText(chosendata.getString());
            subjchose.setText(getIntent().getStringExtra("subj"));
            for (int j = 0; j < subjects.length; j++) {
                if (subjects[j].equals(getIntent().getStringExtra("subj"))){
                    chosensubj = j;
                    break;
                }
            }
        }
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (btnshowphoto.getVisibility() == View.VISIBLE) {
                            btnshowphoto.setVisibility(View.INVISIBLE);
                            btnaddphoto.setVisibility(View.INVISIBLE);
                            TranslateAnimation animate = new TranslateAnimation(
                                    0,
                                    0,
                                    0,
                                    btnshowphoto.getHeight() + 600);
                            animate.setDuration(250);
                            animate.setFillAfter(true);
                            btnshowphoto.startAnimation(animate);
                            btnaddphoto.startAnimation(animate);
                        } else {
                            btnshowphoto.setVisibility(View.VISIBLE);
                            btnaddphoto.setVisibility(View.VISIBLE);
                            TranslateAnimation animate = new TranslateAnimation(
                                    0,
                                    0,
                                    btnshowphoto.getHeight(),
                                    0);
                            animate.setDuration(250);
                            animate.setFillAfter(true);
                            btnshowphoto.startAnimation(animate);
                            btnaddphoto.startAnimation(animate);
                        }
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            photos.add(currentPhotoPath);
        } else if (requestCode == REQUEST_SHOW_PHOTO && resultCode != 0) {
            photos = new ArrayList<String>();
            for (int i = 0; i < data.getStringArrayExtra("photos").length; i++){
                photos.add(data.getStringArrayExtra("photos")[i]);
            }
        } else if (requestCode == REQUEST_PICK_PHOTO) {
            try {
                photos.add(saveBitmapIntoSDCardImage(this, getContactBitmapFromURI(this, data.getData())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String saveBitmapIntoSDCardImage(Context context, Bitmap finalBitmap) throws IOException {

        File destination = createImageFile();

        try {
            FileOutputStream out = new FileOutputStream(destination);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  destination.getAbsolutePath();
    }

    public Bitmap getContactBitmapFromURI(Context context, Uri uri) {
        try {

            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input == null) {
                return null;
            }
            return BitmapFactory.decodeStream(input);
        }
        catch (FileNotFoundException e)
        {

        }
        return null;

    }

    @Override
    protected void onDestroy() {
        setResult(0);
        super.onDestroy();
    }

    public void onClickOk(View v){
        Intent i = new Intent();
        i.putExtra("subj", subjects[chosensubj]);
        i.putExtra("txt", txt.getText().toString());
        i.putExtra("year", chosendata.year);
        i.putExtra("month", chosendata.month);
        i.putExtra("day", chosendata.day);
        String[] photosar = new String[photos.size()];
        for (int j = 0; j < photos.size(); j++) {
            photosar[j] = photos.get(j);
        }
        Log.d("sasa", photosar.toString());
        i.putExtra("photos", photosar);
        setResult(228322, i);
        finish();
    }

    public void onClickCancel(View v){
        setResult(0);
        finish();
    }

    public void onClickchosesubj(View view){
        subjchoseDialog();
    }

    public void onClickchoosedate(View v){
        dateChooseDialog();
    }

    public void dateChooseDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.AlertDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        chosendata = new DateData(year, month, day);
                        datechose.setText(chosendata.getString());
                    }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                );
        datePickerDialog.show();
    }

    public void subjchoseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Выберите предмет");
        builder.setItems(subjects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosensubj = which;
                subjchose.setText(subjects[which]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onclickAddphoto(View v){
        requestPermissions();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        CharSequence[] items = {"Сделать фото", "Загрузить из галлереи"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        File saveFile = createImageFile();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        outputFileUri = FileProvider.getUriForFile(New_homework.this, "com.example.android.fileprovider", saveFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent, REQUEST_PHOTO);
                    } catch (ActivityNotFoundException e) {
                        String errorMessage = "Ваше устройство не поддерживает работу с камерой!";
                        Toast toast = Toast.makeText(New_homework.this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        String errorMessage = "Ошибка при сохранении файла";
                        Toast toast = Toast.makeText(New_homework.this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                    startActivityForResult(intent, REQUEST_PICK_PHOTO);
                }
            }
        });
        dialog.show();
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onClickShowPhotos(View v){
        String[] ret = new String[photos.size()];
        for(int i = 0; i < photos.size(); i++) {
            ret[i] = photos.get(i);
        }
        Intent i = new Intent(New_homework.this, Photos_activity.class);
        i.putExtra("photos", ret);
        startActivityForResult(i, REQUEST_SHOW_PHOTO);
    }

    private void requestPermissions(){
        int permissionStatusWrite = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permissionStatusRead = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        if (permissionStatusWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 3123);
        }
        if (permissionStatusRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 13123);
        }
    }


}
