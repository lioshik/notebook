package com.example.notebook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        saveData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void saveData(){
        try {
            FileOutputStream output = openFileOutput("saveData", MODE_PRIVATE);
            output.write((Integer.toString(MainActivity.AllData.size()) + "\n").getBytes());
            for (int i = 0; i < MainActivity.AllData.size(); i++) {
                output.write((MainActivity.AllData.get(i).subj + "\n").getBytes());
                output.write((Integer.toString(MainActivity.AllData.get(i).date.year) + " " + Integer.toString(MainActivity.AllData.get(i).date.month) + " " + Integer.toString(MainActivity.AllData.get(i).date.day) + "\n").getBytes());
                output.write("iiiii\n".getBytes());
                output.write((MainActivity.AllData.get(i).txt + "\n").getBytes());
                output.write("iiiii\n".getBytes());
                output.write((Integer.toString(MainActivity.AllData.get(i).photos.size()) + "\n").getBytes());
                for (int j = 0; j < MainActivity.AllData.get(i).photos.size(); j++) {
                    output.write((MainActivity.AllData.get(i).photos.get(j) + "\n").getBytes());
                }
            }
            output.write("endoffile\n".getBytes());
            output.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.stopSelf();
    }
}