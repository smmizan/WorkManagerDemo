package com.smmizan.workmanagerdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker  extends Worker {


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data retriveData = getInputData();
        String desc = retriveData.getString("input_data");

        showNotification("My Notify",desc);

        Data outputData = new Data.Builder().putString("output_date","this is my output data from worker class").build();

        return Result.success(outputData);
    }


    @Override
    public void onStopped() {
        super.onStopped();
    }

    private void  showNotification(String title, String description){

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("miz","miz",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"miz")
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1,builder.build());

    }
}
