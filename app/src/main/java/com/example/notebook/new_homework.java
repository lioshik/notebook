package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.time.Year;
import java.util.Calendar;

public class new_homework extends AppCompatActivity {

    Button btnok, btncancel, datechose, subjchose;
    TextView txt;
    String[] subjects;
    DateData chosendata;
    int chosensubj;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_homework);
        btnok = (Button)findViewById(R.id.ok);
        txt = (TextView)findViewById(R.id.text);
        btncancel = (Button)findViewById(R.id.cancel);
        subjects = getIntent().getStringArrayExtra("Subjects");
        subjchose = (Button)findViewById(R.id.subjectchose);
        datechose = (Button)findViewById(R.id.date);
        chosensubj = 0;
        calendar = Calendar.getInstance();

        int Date = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);
        chosendata = new DateData(Year, Month, Date);

        if (getIntent().getIntExtra("RequestCode", 0) == MainActivity.REQUEST_CHANGE_HW) {
            txt.setText(getIntent().getStringExtra("txt"));
            for (int i = 0; i < subjects.length; i++) {
                if (getIntent().getStringExtra("subj") == subjects[i]){
                    chosensubj = i;
                    subjchose.setText(subjects[i]);
                }
            }
            chosendata = new DateData(getIntent().getIntExtra("year", Year), getIntent().getIntExtra("month", Month), getIntent().getIntExtra("day", Date));
            datechose.setText(chosendata.getString());
            subjchose.setText(getIntent().getStringExtra("subj"));
        }

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
        setResult(1, i);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

}
