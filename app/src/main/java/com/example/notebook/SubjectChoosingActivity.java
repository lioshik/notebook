package com.example.notebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubjectChoosingActivity extends AppCompatActivity {

    public Button buttonBack, buttonAdd;
    public ListView lw;
    public List<String> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Список предметов");
        setContentView(R.layout.activity_subject_choosing);
        buttonBack = (Button)findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                String[] k = new String[subjects.size()];
                for (int j = 0; j < subjects.size(); j++) {
                    k[j] = subjects.get(j);
                }
                i.putExtra("Subjects", k);
                setResult(122, i);
                finish();
            }
        });

        buttonAdd = (Button)findViewById(R.id.buttonAddSubject);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder codeDialogBuilder;
                codeDialogBuilder = new AlertDialog.Builder(SubjectChoosingActivity.this);
                codeDialogBuilder.setTitle("Введите  название");
                final EditText input = new EditText(SubjectChoosingActivity.this);
                input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                codeDialogBuilder.setView(input);
                codeDialogBuilder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                codeDialogBuilder.setPositiveButton("добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SubjectChoosingActivity.this.subjects.add(input.getText().toString());
                        SubjectChoosingActivity.this.updateListView();
                    }
                });
                codeDialogBuilder.show();
            }
        });

        lw = (ListView)findViewById(R.id.listviewwithSubj);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SubjectChoosingActivity.this);
                dialog.setTitle("Удалить предмет из списка?");
                dialog.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (subjects.size() != 1) {
                            subjects.remove(position);
                            updateListView();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        ((Button)findViewById(R.id.buttonSubjGoBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subjects = new ArrayList<String>(Arrays.asList(getIntent().getStringArrayExtra("Subjects")));
        updateListView();
    }

    public void updateListView() {
        lw.setAdapter(new SubjectsAdapter(this, subjects));
    }

    @Override
    public void onDestroy() {
        Intent i = new Intent();
        i.putExtra("Subjects", subjects.toArray());
        setResult(122, i);
        super.onDestroy();
    }

}
