package com.smmizan.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button bNotify,bStop;
    TextView tStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bNotify = findViewById(R.id.buttton);
        bStop = findViewById(R.id.butttonStop);
        tStatus = findViewById(R.id.textView);


        // one time workrequest
        //OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class).build();


        Constraints constraints = new Constraints.Builder()
                //METERED = Data Connection
                //UNMETERED = Wifi
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class,15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();


        bNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(MainActivity.this).enqueue(periodicWorkRequest);
            }
        });



        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(MainActivity.this).cancelWorkById(periodicWorkRequest.getId());
            }
        });


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String status = workInfo.getState().name();
                        tStatus.append(status + "\n");
                        Log.d("mizan",status);
                    }
                });

    }
}