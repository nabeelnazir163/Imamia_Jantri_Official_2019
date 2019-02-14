package com.namobiletech.imamiajantri.AlarmNotification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.namobiletech.imamiajantri.Noti_Receiver.NotificationReceiver;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.MainActivity;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MyReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private int prayerNotificationID = 420; //blazeIt
    private AlarmManager am;
    private PendingIntent pendingIntent;

    DbHelper dbHelper;

    Cursor cursornamaz;

    String formattedCurrDate;

    String FajrTime;
    String DhuhrTime;
    String AsrTime;
    String MaghribTime;
    String IshaTime;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public void onReceive(Context context, Intent intent1) {

        /*//  SalaatSchedulingService salaatSchedulingService = new SalaatSchedulingService(context);
        Intent service = new Intent(context, SalaatSchedulingService.class);
//        context.startService(service);
        startWakefulService(context, service);*/

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri sound = Uri.parse("android.resource://" + context.getPackageName()+ "/" + R.raw.azan);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Prayer Time")
                .setContentText("It's Time to Pray Salaah")
                .setSound(sound)
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(100, builder.build());

        int count = AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME);
        count++;
        if (count == 5) {
            AppSettings.getInstance(context).set(Constants.EXTRA_PRAYER_NAME, 0);
        } else {
            AppSettings.getInstance(context).set(Constants.EXTRA_PRAYER_NAME, count);
        }

        dbHelper = new DbHelper(context);
        cursornamaz = dbHelper.getNamazTimeInformation(dbHelper);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedCurrDate = df.format(c);
//        setAlarm(context);
        DataFromDatabase(context);
    }

    private void DataFromDatabase(Context context) {
        cursornamaz.moveToFirst();

        for (int j = 0; j < cursornamaz.getCount() ; j++) {

            if (formattedCurrDate.equals(cursornamaz.getString(0))) {

                //Getting times from server
                FajrTime = cursornamaz.getString(1);
                DhuhrTime = cursornamaz.getString(2);
                AsrTime = cursornamaz.getString(3);
                MaghribTime = cursornamaz.getString(4);
                IshaTime = cursornamaz.getString(5);

                MyReceiver salaatAlarmReceiver = new MyReceiver();
                salaatAlarmReceiver.setAlarm(context, FajrTime, DhuhrTime, AsrTime, MaghribTime, IshaTime);

            }

            cursornamaz.moveToNext();
        }
    }

    public void setAlarm(Context context, String fajrTime, String dhuhrTime, String AsrTime, String MaghribTime, String IshaTime) {

        Calendar then = Calendar.getInstance(TimeZone.getDefault());
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTimeInMillis(System.currentTimeMillis());

        //then.setTimeInMillis(System.currentTimeMillis());
        if (AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 0) {

            then = getCalendarFromPrayerTime(then, fajrTime, context);
            if (then.before(now)) {
                then.add(Calendar.DAY_OF_YEAR, 1);
            }
            Intent intent = new Intent(context, MyReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }
        if (AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 1) {
            then = getCalendarFromPrayerTime(then, dhuhrTime, context);
            if (then.before(now)) {
                then.add(Calendar.DAY_OF_YEAR, 1);
            }
            Intent intent = new Intent(context, MyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            am.setExact(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), pendingIntent);
        }
        if (AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 2) {
            then = getCalendarFromPrayerTime(then, AsrTime, context);
            if (then.before(now)) {
                then.add(Calendar.DAY_OF_YEAR, 1);
            }
            Intent intent = new Intent(context, MyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            am.setExact(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), pendingIntent);
        }
        if (AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 3) {
            then = getCalendarFromPrayerTime(then, MaghribTime, context);
            if (then.before(now)) {
                then.add(Calendar.DAY_OF_YEAR, 1);
            }
            Intent intent = new Intent(context, MyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            am.setExact(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), pendingIntent);
        }
        if (AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 4) {
            then = getCalendarFromPrayerTime(then, IshaTime, context);
            if (then.before(now)) {
                then.add(Calendar.DAY_OF_YEAR, 1);
            }
            Intent intent = new Intent(context, MyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            am.setExact(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), pendingIntent);
        }
    }

    private Calendar getCalendarFromPrayerTime(Calendar cal, String prayerTime, Context context) {

        String[] time = prayerTime.split(":");
        int hour = Integer.valueOf(time[0]);
        if( AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 2 ||
                AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 3 ||
                    AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 4 ||
                        AppSettings.getInstance(context).getInt(Constants.EXTRA_PRAYER_NAME) == 5
        )
        {
            if(hour < 10)
            {
                hour = hour + 12;
            }
        }

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, Integer.valueOf(time[1]));
        cal.set(Calendar.SECOND, 0);

        return cal;
    }


}
