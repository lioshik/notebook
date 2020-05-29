package com.example.notebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final int REQUEST_CHANGE_SUBJ_LIST = 6;

    public static ListView lw;
    public static List<String> subjects;
    private static HashMap<String, Boolean> map;
    public static List<Homework> list;
    public static List<Homework> AllData;
    private static DateData dedline;
    private static final DateData dedlinedefault = new DateData(9999, 10, 10);
    private Button btndedline;
    private Button subjchose;
    private Button btnremovesubjfilter;
    private Button btnremovedatefilter;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Мои задания");
        lw = (ListView) findViewById(R.id.listviewwithSubj);
        list = new ArrayList<Homework>();
        AllData = new ArrayList<Homework>();
        map = new HashMap<String, Boolean>();
        subjects = new ArrayList<String>();
        dedline = dedlinedefault;
        btndedline = (Button)findViewById(R.id.button2);
        btnremovedatefilter = (Button)findViewById(R.id.removedatefilter);
        btnremovesubjfilter = (Button)findViewById(R.id.removesubjfilter);
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

        lw.setAdapter(new HomeworkAdapter(MainActivity.this, list));
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, MoreDetailsActivity.class);
                i.putExtra("txt", list.get(position).txt);
                i.putExtra("pos", position);
                String[] photos = new String[list.get(position).photos.size()];
                for (int j = 0; j < list.get(position).photos.size(); j++) {
                    photos[j] = list.get(position).photos.get(j);
                }
                i.putExtra("photos", photos);
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

        for (int i = 0; i < subjects.size(); i++) {
            map.put(subjects.get(i), true);
        }
        updateList();
    }

    public void updateSubjList() {
        for (int i = 0; i < AllData.size(); i++) {
            if (!subjects.contains(AllData.get(i).subj)) {
                AllData.remove(i);
            }
        }
        for (int i = 0; i < subjects.size(); i++) {
            if (map.get(subjects.get(i)) == null) {
                map.put(subjects.get(i), true);
            }
        }
        updateList();
    }

    public void onClickChangeSubj() {
        Intent i = new Intent(MainActivity.this, SubjectChoosingActivity.class);
        String[] kostyl = new String[subjects.size()];
        for (int j = 0; j < subjects.size(); j++) {
            kostyl[j] = subjects.get(j);
        }
        i.putExtra("Subjects", kostyl);
        startActivityForResult(i, REQUEST_CHANGE_SUBJ_LIST);
    }

    @Override
    public void onPause(){
        Intent intent = new Intent(this, SaveService.class);
        startService(intent);
        super.onPause();
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
                    subjects.add(reader.readLine());
                }
                sz = Integer.parseInt(reader.readLine());
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
                    int photocnt = Integer.parseInt(reader.readLine());
                    String photos[] = new String[photocnt];
                    for (int j = 0; j < photocnt; j++) {
                        photos[j] = reader.readLine();
                    }
                    Homework hw = new Homework(sbj, txt, d, photos);
                    AllData.add(hw);
                }
            }
        } catch (Exception e) {
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
            e.printStackTrace();
        }
        for (int i = 0; i < subjects.size(); i++) {
            map.put(subjects.get(i), true);
        }
    }

    public void dateChooseDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.AlertDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dedline = new DateData(year, month, day);
                        btndedline.setText(dedline.getString());
                        updateList();
                        btnremovedatefilter.setVisibility(View.VISIBLE);
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
                    String[] photos = data.getStringArrayExtra("photos");
                    Log.d("sasa", Integer.toString(photos.length));
                    Homework add = new Homework(data.getStringExtra("subj"), data.getStringExtra("txt"), retDate, photos);
                    AllData.add(add);
                    updateList();
                }
                break;
            case REQUEST_CHANGE_HW:
                if (resultCode != 0) {
                    AllData.remove(list.get(pos));
                    String[] photos = data.getStringArrayExtra("photos");
                    Log.d("sasacurnumofphotos", Integer.toString(photos.length));
                    Homework add = new Homework(data.getStringExtra("subj"), data.getStringExtra("txt"), new DateData(data.getIntExtra("year", 0), data.getIntExtra("month", 0), data.getIntExtra("day", 0)), photos);
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
                break;
            case REQUEST_CHANGE_SUBJ_LIST:
                try {
                    subjects = Arrays.asList(data.getStringArrayExtra("Subjects"));
                    updateSubjList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public void onClickFilter(View v) {
        changeFilter();
    }

    public void onClickAdd(View v) {
        Intent i = new Intent(MainActivity.this, New_homework.class);
        String[] kostyl = new String[subjects.size()];
        for (int j = 0; j < subjects.size(); j++) {
            kostyl[j] = subjects.get(j);
        }
        i.putExtra("Subjects", kostyl);
        i.putExtra("RequestCode", REQUEST_NEW_HW);

        startActivityForResult(i, REQUEST_NEW_HW);
        /*
        startActivityForResult(i, REQUEST_NEW_HW);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Новая запись");
        String[] actions = {"Создать новую", "Импортировать существующую", "Отмена"};
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        Intent i = new Intent(MainActivity.this, New_homework.class);
                        String[] kostyl = new String[subjects.size()];
                        for (int j = 0; j < subjects.size(); j++) {
                            kostyl[j] = subjects.get(j);
                        }
                        i.putExtra("Subjects", kostyl);
                        i.putExtra("RequestCode", REQUEST_NEW_HW);

                        startActivityForResult(i, REQUEST_NEW_HW);
                        break;
                    case 1:
                        AlertDialog.Builder codeDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        codeDialogBuilder.setTitle("Введите код");
                        final EditText input = new EditText(MainActivity.this);
                        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        codeDialogBuilder.setView(input);
                        codeDialogBuilder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        codeDialogBuilder.setPositiveButton("ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String serverUrl = "http://10.0.2.2:4567/";
                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .build();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(serverUrl)
                                        .client(okHttpClient)
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();
                                APIservice apiService = retrofit.create(APIservice.class);
                                Call<Homework> getHwFromServer = apiService.loadHomework(input.getText().toString());
                                getHwFromServer.enqueue(new Callback<Homework>() {
                                    @Override
                                    public void onResponse(Call<Homework> call, final Response<Homework> response) {
                                        if (response.isSuccessful()) {
                                            AllData.add(response.body());
                                            updateList();
                                            for (int i = 0; i < response.body().photos.size(); i++) {
                                                final int finalI = i;
                                                Thread thread = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try  {
                                                            final File outputDir = new File(response.body().photos.get(finalI));
                                                            if (!outputDir.exists()) {
                                                                try {
                                                                    outputDir.createNewFile();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.d("sas", "http://10.0.2.2:4567/getimage/" + outputDir.getName());
                                                                try {
                                                                    URL url = new URL("http://10.0.2.2:4567/getimage/" + outputDir.getName());
                                                                    InputStream inputStream = url.openStream();
                                                                    byte[] buffer = new byte[20000000];
                                                                    int bytesRead = 0;
                                                                    OutputStream output = new FileOutputStream(outputDir.getAbsolutePath());
                                                                    while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                                                                        output.write(buffer, 0, bytesRead);
                                                                    }
                                                                    output.close();
                                                                    inputStream.close();
                                                                } catch (MalformedURLException e) {
                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                thread.start();
                                            }
                                            Toast toast = Toast.makeText(MainActivity.this, "добавлено", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            Toast toast = Toast.makeText(MainActivity.this, "ошибка", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Homework> call, Throwable t) {
                                        Toast toast = Toast.makeText(MainActivity.this, "ошибка", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                        });
                        codeDialogBuilder.show();
                }
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();*/
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
        Intent i = new Intent(MainActivity.this, New_homework.class);
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
        String[] kostyl2 = new String[list.get(MainActivity.pos).photos.size()];
        for (int j = 0; j < list.get(MainActivity.pos).photos.size(); j++) {
            kostyl2[j] = list.get(MainActivity.pos).photos.get(j);
        }
        i.putExtra("photos", kostyl2);
        startActivityForResult(i, REQUEST_CHANGE_HW);
    }

    private void changeFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
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
                btnremovesubjfilter.setVisibility(View.VISIBLE);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("выделить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView list = ((AlertDialog) dialog).getListView();
                for (int i = 0; i < list.getCount(); i++) {
                    list.setItemChecked(i, true);
                    map.put(subjects.get(i), true);
                }
                updateList();
                btnremovesubjfilter.setVisibility(View.INVISIBLE);
                changeFilter();
            }
        });

        builder.setNegativeButton("убрать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView list = ((AlertDialog) dialog).getListView();
                for (int i = 0; i < list.getCount(); i++) {
                    list.setItemChecked(i, false);
                    map.put(subjects.get(i), false);
                }
                updateList();
                btnremovesubjfilter.setVisibility(View.VISIBLE);
                changeFilter();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClickremovesubjfilter(View v){
        btnremovesubjfilter.setVisibility(View.INVISIBLE);
        for (int i = 0; i < subjects.size(); i++) {
            map.put(subjects.get(i), true);
        }
        updateList();
    }

    public void onClickremovedatefilter(View v) {
        btnremovedatefilter.setVisibility(View.INVISIBLE);
        dedline = dedlinedefault;
        btndedline.setText("дата");
        updateList();
    }

    public void updateList() {
        list.clear();
        for (int i = 0; i < AllData.size(); i++) {
            Log.d("sas", AllData.get(i).date.getString() + "   " + dedline.getString());
            if (map.get(AllData.get(i).subj) && (dedline.equals(dedlinedefault) || dedline.equals(AllData.get(i).date))) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_exit:
                finish();
                break;
            case R.id.menu_push_button:
                break;
            case R.id.menu_subj_list:
                onClickChangeSubj();
                break;
        }
        return true;
    }

}
