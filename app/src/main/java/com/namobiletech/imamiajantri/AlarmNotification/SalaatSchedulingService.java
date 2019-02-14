package com.namobiletech.imamiajantri.AlarmNotification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.namobiletech.imamiajantri.Noti_Receiver.NotificationReceiver;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.MainActivity;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SalaatSchedulingService extends Service implements Constants {

  Cursor cursornamaz;

  String formattedCurrDate;

  String FajrTime;
  String DhuhrTime;
  String AsrTime;
  String MaghribTime;
  String IshaTime;
  DbHelper dbHelper;


  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    /*Calendar then = Calendar.getInstance(TimeZone.getDefault());
    Calendar now = Calendar.getInstance(TimeZone.getDefault());

    then = getCalendarFromPrayerTime(then,"07:23");

    if(then.before(now))
    {
      then.add(Calendar.DAY_OF_YEAR, 1);
    }

    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
*/
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    formattedCurrDate = df.format(c);

    dbHelper = new DbHelper(getBaseContext());
    cursornamaz = dbHelper.getNamazTimeInformation(dbHelper);


    if (AppSettings.getInstance(this).getInt(Constants.EXTRA_PRAYER_NAME) == -1 || AppSettings.getInstance(this).getInt(Constants.EXTRA_PRAYER_NAME) >= 5)
      AppSettings.getInstance(this).set(Constants.EXTRA_PRAYER_NAME, 0);

    DataFromDatabase();

  }

  private Calendar getCalendarFromPrayerTime(Calendar cal, String prayerTime) {
    String[] time = prayerTime.split(":");
    cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
    cal.set(Calendar.MINUTE, Integer.valueOf(time[1]));
    cal.set(Calendar.SECOND, 0);

    return cal;
  }

  @Override
  public void onDestroy() {
    /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
    //handler.removeCallbacks(runnable);
  }

  @Override
  public void onStart(Intent intent, int startid) { }

  private void DataFromDatabase() {
    cursornamaz.moveToFirst();

    for (int j = 0; j < cursornamaz.getCount(); j++) {

      if (formattedCurrDate.equals(cursornamaz.getString(0))) {

        //Getting times from server
        FajrTime = cursornamaz.getString(1);
        DhuhrTime = cursornamaz.getString(2);
        AsrTime = cursornamaz.getString(3);
        MaghribTime = cursornamaz.getString(4);
        IshaTime = cursornamaz.getString(5);

        MyReceiver salaatAlarmReceiver = new MyReceiver();
        salaatAlarmReceiver.setAlarm(this, FajrTime, DhuhrTime, AsrTime, MaghribTime, IshaTime);

      }

      cursornamaz.moveToNext();
    }
  }

}
