package com.namobiletech.imamiajantri.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.PrayerSharePreview;
import com.namobiletech.imamiajantri.UI.Activities.PrayerTimesCalendarView;
import com.namobiletech.imamiajantri.UI.Activities.ReadJantri;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class homeFragment extends Fragment {

    private View v;

    Cursor cursor;
    DbHelper dbHelper;

    String formattedCurrDate;
    String tomorrowDate;

    //Namaz Strings
    private String FajrTime;
    private String DhuhrTime;
    private String AsrTime;
    private String MaghribTime;
    private String IshaTime;
    private String SunriseTime;
    private String imsakTime;
    private String tomorrorFajrTime;

    String longitude;
    String latitude;

    //TextViews for Prayer time
    private TextView todayDate;
    private TextView Fajr;
    private TextView Dhuhr;
    private TextView Asr;
    private TextView Maghrib;
    private TextView Isha;
    private TextView sunRise;
    private TextView imsak;
    private TextView nextNamazProgressBar;
    private TextView timeDifferencProgressBar;
    private TextView timeOfNextPray_tv;

    private TextView fajr_Tv;
    private TextView sunrise_Tv;
    private TextView dhuhr_Tv;
    private TextView asr_Tv;
    private TextView maghrib_Tv;
    private TextView isha_Tv;
    private TextView imsak_Tv;

    //Imageviews of prayer time
    private ImageView shareImageView;
    private ImageView showCalendar_IV;

    //Linear LAyout
    private RelativeLayout rootLayout;

    private ProgressBar progressBar;

    SharedPreferences sh;

    Date current_time;
    private Date now_time;
    private Date next_Time;

    int totalminutes;
    int minutesLeft;

    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        initializeWidgets();

        dbHelper = new DbHelper(getContext());
        cursor = dbHelper.getNamazTimeInformation(dbHelper);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedCurrDate = df.format(c);

        //Finding date for Tomorrow
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 1); // <--
        Date tomorrow = cal.getTime();
        tomorrowDate = df.format(tomorrow);

        sh = getActivity().getSharedPreferences("location", getActivity().MODE_PRIVATE);

        longitude = sh.getString("longitude", "");
        latitude = sh.getString("latitude", "");

        DataFromDatabase();
        animate();

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(getContext(), PrayerSharePreview.class);

                shareIntent.putExtra("Fajr", FajrTime);
                shareIntent.putExtra("Dhuhr", DhuhrTime);
                shareIntent.putExtra("Asr", AsrTime);
                shareIntent.putExtra("Maghrib", MaghribTime);
                shareIntent.putExtra("Isha", IshaTime);
                shareIntent.putExtra("Sunrise", SunriseTime);
                shareIntent.putExtra("Imsak", imsakTime);

                startActivity(shareIntent);
            }
        });

        return v;
    }

    private void DataFromDatabase() {
        cursor.moveToNext();
        for (int j = 0; j < cursor.getCount(); j++) {
            if (formattedCurrDate.equals(cursor.getString(0))) {

                //Getting times from server
                FajrTime = cursor.getString(1);
                DhuhrTime = cursor.getString(2);
                AsrTime = cursor.getString(3);
                MaghribTime = cursor.getString(4);
                IshaTime = cursor.getString(5);
                SunriseTime = cursor.getString(6);
                imsakTime = cursor.getString(7);

                //setting date
                todayDate.setText(cursor.getString(0));

                //setting time
                Fajr.setText(FajrTime);
                Dhuhr.setText(DhuhrTime);
                Asr.setText(AsrTime);
                Maghrib.setText(MaghribTime);
                Isha.setText(IshaTime);
                sunRise.setText(SunriseTime);
                imsak.setText(imsakTime);
            } else if (tomorrowDate.equals(cursor.getString(0))) {
                tomorrorFajrTime = cursor.getString(1);
            }

            cursor.moveToNext();
        }

        //Sharing Prayer Times
       /* shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(getContext(), PrayerSharePreview.class);

                shareIntent.putExtra("Fajr", FajrTime);
                shareIntent.putExtra("Dhuhr", DhuhrTime);
                shareIntent.putExtra("Asr", AsrTime);
                shareIntent.putExtra("Maghrib", MaghribTime);
                shareIntent.putExtra("Isha", IshaTime);
                shareIntent.putExtra("Sunrise", SunriseTime);
                shareIntent.putExtra("Imsak", imsakTime);

                startActivity(shareIntent);
            }
        });*/

        showCalendar_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), PrayerTimesCalendarView.class));

            }
        });
    }

    private void initializeWidgets() {

        //Prayer time widgets
        Fajr = (TextView) v.findViewById(R.id.FajrTime);
        Dhuhr = (TextView) v.findViewById(R.id.dhuhrTime);
        Asr = (TextView) v.findViewById(R.id.asrTime);
        Maghrib = (TextView) v.findViewById(R.id.maghribTime);
        Isha = (TextView) v.findViewById(R.id.ishaTime);
        sunRise = (TextView) v.findViewById(R.id.sunriseTime);
        imsak = (TextView) v.findViewById(R.id.imsakTime);
        todayDate = (TextView) v.findViewById(R.id.todayDate);

        fajr_Tv = (TextView) v.findViewById(R.id.fajr_tv_home);
        sunrise_Tv = (TextView) v.findViewById(R.id.sunrise_tv_home);
        dhuhr_Tv = (TextView) v.findViewById(R.id.dhuhr_tv_home);
        asr_Tv = (TextView) v.findViewById(R.id.asr_tv_home);
        maghrib_Tv = (TextView) v.findViewById(R.id.maghrib_tv_home);
        isha_Tv = (TextView) v.findViewById(R.id.isha_tv_home);
        imsak_Tv = (TextView) v.findViewById(R.id.imsak_tv_home);

        shareImageView = (ImageView) v.findViewById(R.id.sharePrayerTime_Iv);
        showCalendar_IV = (ImageView) v.findViewById(R.id.prayerTimeCalendar_IV);

        //Progress Bar widgets
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        nextNamazProgressBar = (TextView) v.findViewById(R.id.nextNamaz_progressbar_tv);
        timeDifferencProgressBar = (TextView) v.findViewById(R.id.timeDifferecneProgressBAr_tv);

        timeOfNextPray_tv = (TextView) v.findViewById(R.id.nextPrayerTime);

        rootLayout = (RelativeLayout) v.findViewById(R.id.rootLayout_Home);

        Typeface open_Sans_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans_Regular.ttf");

        Fajr.setTypeface(open_Sans_font);
        Dhuhr.setTypeface(open_Sans_font);
        Asr.setTypeface(open_Sans_font);
        Maghrib.setTypeface(open_Sans_font);
        Isha.setTypeface(open_Sans_font);
        sunRise.setTypeface(open_Sans_font);
        imsak_Tv.setTypeface(open_Sans_font);
        todayDate.setTypeface(open_Sans_font);

        fajr_Tv.setTypeface(open_Sans_font);
        sunrise_Tv.setTypeface(open_Sans_font);
        dhuhr_Tv.setTypeface(open_Sans_font);
        asr_Tv.setTypeface(open_Sans_font);
        maghrib_Tv.setTypeface(open_Sans_font);
        isha_Tv.setTypeface(open_Sans_font);
        imsak_Tv.setTypeface(open_Sans_font);
    }

    public void animate() {
        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doWork() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                    Date systemDate = Calendar.getInstance().getTime();
                    String myDate = sdf.format(systemDate);
                    current_time = sdf.parse(myDate);

                    Date fajrtime = sdf.parse(FajrTime + ":00");
                    Date dhuhrtime = sdf.parse(DhuhrTime + ":00");
                    Date asrtime = sdf.parse(AsrTime + ":00");
                    Date maghribtime = sdf.parse(MaghribTime + ":00");
                    Date ishatime = sdf.parse(IshaTime + ":00");


                    if (current_time.after(fajrtime)) {
                        now_time = sdf.parse(FajrTime + ":00");
                        next_Time = sdf.parse(DhuhrTime + ":00");
                        nextNamazProgressBar.setText(R.string.dhuhr);
                        rootLayout.setBackgroundResource(R.drawable.dhuhr);
                        timeOfNextPray_tv.setText(DhuhrTime);

                        if (current_time.after(dhuhrtime)) {
                            now_time = sdf.parse(DhuhrTime + ":00");
                            next_Time = sdf.parse(AsrTime + ":00");
                            nextNamazProgressBar.setText(R.string.asr);
                            rootLayout.setBackgroundResource(0);
                            rootLayout.setBackgroundResource(R.drawable.asr);
                            timeOfNextPray_tv.setText(AsrTime);

                            if (current_time.after(asrtime)) {
                                now_time = sdf.parse(AsrTime + ":00");
                                next_Time = sdf.parse(MaghribTime + ":00");
                                nextNamazProgressBar.setText(R.string.maghrib);
                                rootLayout.setBackgroundResource(0);
                                rootLayout.setBackgroundResource(R.drawable.maghrib);
                                timeOfNextPray_tv.setText(MaghribTime);

                                if (current_time.after(maghribtime)) {
                                    now_time = sdf.parse(MaghribTime + ":00");
                                    next_Time = sdf.parse(IshaTime + ":00");
                                    nextNamazProgressBar.setText(R.string.isha);
                                    rootLayout.setBackgroundResource(0);
                                    rootLayout.setBackgroundResource(R.drawable.isha);
                                    timeOfNextPray_tv.setText(IshaTime);

                                    if (current_time.after(ishatime)) {
                                        now_time = sdf.parse(IshaTime + ":00");
                                        next_Time = sdf.parse(tomorrorFajrTime + ":00");
                                        nextNamazProgressBar.setText(R.string.fajr);
                                        rootLayout.setBackgroundResource(0);
                                        rootLayout.setBackgroundResource(R.drawable.fajr);
                                        timeOfNextPray_tv.setText(FajrTime);

                                    }
                                }
                            }
                        }
                    }


                    if (current_time.before(fajrtime))
                    {
                        now_time = sdf.parse(IshaTime + ":00");
                        next_Time = sdf.parse(tomorrorFajrTime + ":00");
                        nextNamazProgressBar.setText(R.string.fajr);
                        rootLayout.setBackgroundResource(0);
                        rootLayout.setBackgroundResource(R.drawable.fajr);
                        timeOfNextPray_tv.setText(FajrTime);
                    }

                    long millse = next_Time.getTime() - now_time.getTime();

                    if (millse < 0) {

                        Date dateMax = null;
                        Date dateMin = null;
                        try {

                            dateMax = sdf.parse("24:00:00");
                            dateMin = sdf.parse("00:00:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        millse = (dateMax.getTime() - now_time.getTime()) + (next_Time.getTime() - dateMin.getTime());
                    }

                    int Hours = (int) (millse / (1000 * 60 * 60));
                    int Mins = (int) (millse / (1000 * 60)) % 60;
                    long Secs = (int) (millse / 1000) % 60;

                    totalminutes = Hours * 60 + Mins;

                    Log.d("checkit", "efwegwrgwrg");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("abc",e.toString());
                }


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                    Date nextTime = (next_Time);

                    long millse = nextTime.getTime() - current_time.getTime();

                    if (millse < 0) {

                        Date dateMax = null;
                        Date dateMin = null;
                        try {

                            dateMax = sdf.parse("24:00:00");
                            dateMin = sdf.parse("00:00:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        millse = (dateMax.getTime() - current_time.getTime()) + (nextTime.getTime() - dateMin.getTime());
                    }

                    int Hours = (int) (millse / (1000 * 60 * 60));
                    int Mins = (int) (millse / (1000 * 60)) % 60;
                    int Secs = (int) (millse / 1000) % 60;

                    @SuppressLint("DefaultLocale") String secs = String.format("%02d", Secs);
                    @SuppressLint("DefaultLocale") String mins = String.format("%02d", Mins);
                    @SuppressLint("DefaultLocale") String hours = String.format("%02d", Hours);

                    minutesLeft = (Hours * 60) + Mins;

                    int progress = (minutesLeft * 100) / totalminutes;

                    String diff = hours + ":" + mins + ":" + secs; // updated value every1 second

                    progressBar.setProgress(100 - progress);

                    timeDifferencProgressBar.setText(diff);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
