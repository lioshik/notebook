package com.example.notebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_HW = 1;
    public static final int REQUEST_CHANGE_HW = 2;
    public static final int RESULT_NEED_CHANGE = 3;
    public static final int RESULT_NEED_DELETE = 4;
    public static final int REQUEST_SEE_MORE_INFO = 5;

    private ListView lw;
    private List<String> subjects;
    private HashMap<String, Boolean> map;
    private List<Homework> list;
    public static List<Homework> AllData;
    private DateData dedline;
    private Button btndedline;
    private Button subjchose;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lw = (ListView) findViewById(R.id.listview);
        list = new ArrayList<Homework>();
        AllData = new ArrayList<Homework>();
        map = new HashMap<String, Boolean>();
        subjects = new ArrayList<String>();
        dedline = new DateData(9999, 10, 10);
        btndedline = (Button)findViewById(R.id.button2);
        subjchose = (Button)findViewById(R.id.filter);
        btndedline.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dateChooseDialog();
                    }
                }
        );
        calendar = Calendar.getInstance();

        lw.setAdapter(new HomeworkAdapter(this, list));
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, MoreDetails.class);
                i.putExtra("txt", list.get(position).txt);
                i.putExtra("pos", position);
                startActivityForResult(i, REQUEST_SEE_MORE_INFO);
            }
        });
        readData();

        lw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                choseActionDialog(pos);
                return true;
            }
        });

        subjects.add("Алгебра");
        subjects.add("Биология");
        subjects.add("Английский");
        subjects.add("Немецкий");
        subjects.add("Французский");
        subjects.add("География");
        subjects.add("Геометрия");
        subjects.add("История");
        subjects.add("Информатика");
        subjects.add("Литература");
        subjects.add("ОБЖ");
        subjects.add("Обществознание");
        subjects.add("Физика");
        subjects.add("Русский");
        subjects.add("Химия");

        for (int i = 0; i < subjects.size(); i++) {
            map.put(subjects.get(i), true);
        }
        updateList();
    }

    @Override
    public void onDestroy(){
        Intent intent = new Intent(this, saveService.class);
        startService(intent);
        super.onDestroy();
    }

    public void readData(){
        try {
            FileInputStream fileinput = openFileInput("saveData");
            InputStreamReader input = new InputStreamReader(fileinput);
            BufferedReader reader = new BufferedReader(input);
            String firstline = reader.readLine();
            if (firstline == null) {
                Toast.makeText(this, "Ошибка при загрузке данных", Toast.LENGTH_LONG);
            } else {
                int sz = Integer.parseInt(firstline);
                for (int i = 0; i < sz; i++) {
                    String sbj = reader.readLine();
                    String[] datestr = reader.readLine().split(" ");
                    DateData d = new DateData(Integer.parseInt(datestr[0]), Integer.parseInt(datestr[1]), Integer.parseInt(datestr[2]));
                    reader.readLine();

                    String txt = "";
                    String line;
                    while (!((line = reader.readLine()).equals("iiiii"))) {
                        if (!(line.equals(""))) {
                            txt += line + "\n";
                        }
                    }
                    Log.v("sastxt", txt);
                    Homework hw = new Homework(sbj, txt, d);
                    AllData.add(hw);
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dateChooseDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dedline = new DateData(year, month, day);
                        btndedline.setText(dedline.getString());
                        updateList();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_NEW_HW:
                if (resultCode != 0){
                    DateData retDate = new DateData(data.getIntExtra("year", 0), data.getIntExtra("month", 0), data.getIntExtra("day", 0));
                    Homework add = new Homework(data.getStringExtra("subj"), data.getStringExtra("txt"), retDate);
                    AllData.add(add);
                    updateList();
                }
                break;
            case REQUEST_CHANGE_HW:
                if (resultCode != 0) {
                    AllData.remove(list.get(pos));
                    Homework add = new Homework(data.getStringExtra("subj"), data.getStringExtra("txt"), new DateData(data.getIntExtra("year", 0), data.getIntExtra("month", 0), data.getIntExtra("day", 0)));
                    AllData.add(add);
                    updateList();
                }
                break;
            case REQUEST_SEE_MORE_INFO:
                switch(resultCode) {
                    case RESULT_NEED_CHANGE:
                        pos = data.getIntExtra("pos", 0);
                        changeItem();
                        break;
                    case RESULT_NEED_DELETE:
                        pos = data.getIntExtra("pos", 0);
                        AllData.remove(list.get(pos));
                        updateList();
                        break;
                }
        }
    }

    public void onClickFilter(View v) {
        changeFilter();
    }

    public void onClickAdd(View v) {
        Intent i = new Intent(this, new_homework.class);
        String[] kostyl = new String[subjects.size()];
        for (int j = 0; j < subjects.size(); j++) {
            kostyl[j] = subjects.get(j);
        }
        i.putExtra("Subjects", kostyl);
        i.putExtra("RequestCode", REQUEST_NEW_HW);

        startActivityForResult(i, REQUEST_NEW_HW);
    }

    public static int pos; //костыль
    public void choseActionDialog(int p){
        pos = p;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Выберите действие");
        String[] actions = {"Удалить", "Изменить", "Отмена"};
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        AllData.remove(list.get(MainActivity.pos));
                        updateList();
                        break;
                    case 1:
                        changeItem();
                }
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeItem() {
        Intent i = new Intent(MainActivity.this, new_homework.class);
        String[] kostyl = new String[subjects.size()];
        for (int j = 0; j < subjects.size(); j++) {
            kostyl[j] = subjects.get(j);
        }
        i.putExtra("Subjects", kostyl);
        i.putExtra("RequestCode", REQUEST_CHANGE_HW);
        i.putExtra("subj", list.get(MainActivity.pos).subj);
        i.putExtra("txt", list.get(MainActivity.pos).txt);
        i.putExtra("day", list.get(MainActivity.pos).date.day);
        i.putExtra("month", list.get(MainActivity.pos).date.month);
        i.putExtra("year", list.get(MainActivity.pos).date.year);

        startActivityForResult(i, REQUEST_CHANGE_HW);
    }

    private void changeFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Фильтр");
        boolean[] checkedItems = new boolean[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            if (map.get(subjects.get(i))) {
                checkedItems[i] = true;
            }
        }
        String[] kostyl = new String[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            kostyl[i] = subjects.get(i);
        }
        builder.setMultiChoiceItems(kostyl, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                map.put(subjects.get(which), isChecked);
                updateList();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateList() {
        list.clear();
        for (int i = 0; i < AllData.size(); i++) {
            if (map.get(AllData.get(i).subj) && dedline.notGreater(AllData.get(i).date)) {
                list.add(AllData.get(i));
            }
        }

        Collections.sort(list, new Comparator<Homework>() {
            public int compare(Homework o1, Homework o2) {
                if (o1.date == o2.date) {
                    return 0;
                }
                if(o1.date.notGreater(o2.date)){
                    return 1;
                }
                return -1;
            }
        });

        lw.setAdapter(new HomeworkAdapter(this, list));
    }

}
