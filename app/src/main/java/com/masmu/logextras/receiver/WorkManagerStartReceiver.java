package com.masmu.logextras.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.masmu.logextras.SendDataWorker;

import java.util.concurrent.TimeUnit;

public class WorkManagerStartReceiver extends BroadcastReceiver {
    private static final String TAG_SEND_DATA = "LOG_EXTRAS_SENDING_TO_SERVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*PeriodicWorkRequest.Builder myWorkBuilder =
                new PeriodicWorkRequest.Builder(SendDataWorker.class,
                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                        TimeUnit.MILLISECONDS);

        PeriodicWorkRequest myWork = myWorkBuilder.build();
        mWorkManager = WorkManager.getInstance(context);
        mWorkManager.enqueue(myWork);*/

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .build();

        PeriodicWorkRequest periodicSendDataWork =
                new PeriodicWorkRequest.Builder(SendDataWorker.class, 15, TimeUnit.MINUTES)
                        .addTag(TAG_SEND_DATA)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        //.setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(periodicSendDataWork);
    }
}
