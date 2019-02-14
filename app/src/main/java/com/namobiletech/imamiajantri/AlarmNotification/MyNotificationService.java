package com.namobiletech.imamiajantri.AlarmNotification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyNotificationService extends Service {

    private NotificationManager notificationManager;
    private int prayerNotificationID = 420; //blazeIt

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent1, int flag, int startId) {

      /*  MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();*/

        return START_STICKY;
    }


    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
