package com.namobiletech.imamiajantri.UI.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.namobiletech.imamiajantri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class PrayerTimesCalendarView extends AppCompatActivity {

    TextView scr1, scr2, scr3, scr4, scr5, scr6, scr7, scr8;

    TableLayout tableLayout;
    TableRow row;


    StringRequest request;
    RequestQueue requestQueue;
//    CacheRequest cacheRequest;

    String Fajr, Dhuhr, Asr, Maghrib, Isha, weekday, geo_day, hijri_day;

    //Shared Preferences
    private SharedPreferences sharedPreferences;

    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_times_calendar_view);

        sharedPreferences = getSharedPreferences("location",MODE_PRIVATE);

        latitude = sharedPreferences.getString("latitude","");
        longitude = sharedPreferences.getString("longitude","");

        requestQueue = Volley.newRequestQueue(this);

        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);



        String url = "http://api.aladhan.com/v1/calendar?latitude=" + latitude + "&longitude=" + longitude + "&method=1&month=" + month + "&year=" +year;


        tableLayout = findViewById(R.id.table1);

        tableLayout.setColumnStretchable(0, true);
        tableLayout.setColumnStretchable(1, true);
        tableLayout.setColumnStretchable(2, true);
        tableLayout.setColumnStretchable(3, true);
        tableLayout.setColumnStretchable(4, true);
        tableLayout.setColumnStretchable(5, true);
        tableLayout.setColumnStretchable(6, true);
        tableLayout.setColumnStretchable(7, true);

        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {

                        row = new TableRow(PrayerTimesCalendarView.this);

                        scr1 = new TextView(PrayerTimesCalendarView.this);
                        scr2 = new TextView(PrayerTimesCalendarView.this);
                        scr3 = new TextView(PrayerTimesCalendarView.this);
                        scr4 = new TextView(PrayerTimesCalendarView.this);
                        scr5 = new TextView(PrayerTimesCalendarView.this);
                        scr6 = new TextView(PrayerTimesCalendarView.this);
                        scr7 = new TextView(PrayerTimesCalendarView.this);
                        scr8 = new TextView(PrayerTimesCalendarView.this);

                        JSONObject timings = new JSONObject(data.getJSONObject(i).getString("timings"));

                        JSONObject date = new JSONObject(data.getJSONObject(i).getString("date"));
                        JSONObject gregorian = date.getJSONObject("gregorian");
                        JSONObject hijri = date.getJSONObject("hijri");

                        JSONObject weekday_obj = gregorian.getJSONObject("weekday");

                        Fajr = timings.getString("Fajr");
                        Dhuhr = timings.getString("Dhuhr");
                        Asr = timings.getString("Asr");
                        Maghrib = timings.getString("Maghrib");
                        Isha = timings.getString("Isha");

                        geo_day = gregorian.getString("day");
                        hijri_day = hijri.getString("day");

                        int hijriday = Integer.parseInt(hijri_day ) - 1;

                        weekday =  weekday_obj.getString("en");

//                        scr1.setText(geo_day.replace(" (PST)", ""));
//                        scr1.setGravity(Gravity.CENTER);
//                        scr1.setBackgroundColor(Color.parseColor("#589746"));
//
//                        scr2.setText(hijriday + "");
//                        scr2.setGravity(Gravity.CENTER);
//                        scr2.setBackgroundColor(Color.parseColor("#589746"));
//
//                        scr3.setText(weekday.replace(" (PST)", ""));
//                        scr3.setGravity(Gravity.CENTER);
//                        scr3.setBackgroundColor(Color.parseColor("#589746"));
//
//                        scr4.setText(Fajr.replace(" (PST)", ""));
//                        scr4.setGravity(Gravity.CENTER);
//
//                        scr5.setText(Dhuhr.replace(" (PST)", ""));
//                        scr5.setGravity(Gravity.CENTER);
//
//                        scr6.setText(Asr.replace(" (PST)", ""));
//                        scr6.setGravity(Gravity.CENTER);
//
//                        scr7.setText(Maghrib.replace(" (PST)", ""));
//                        scr7.setGravity(Gravity.CENTER);
//
//                        scr8.setText(Isha.replace(" (PST)", ""));
//                        scr8.setGravity(Gravity.CENTER);

                        scr1.setText(geo_day.substring(0,5));
                        scr1.setGravity(Gravity.CENTER);
                        scr1.setBackgroundColor(Color.parseColor("#589746"));

                        scr2.setText(hijriday + "");
                        scr2.setGravity(Gravity.CENTER);
                        scr2.setBackgroundColor(Color.parseColor("#589746"));

                        scr3.setText(weekday.substring(0,5));
                        scr3.setGravity(Gravity.CENTER);
                        scr3.setBackgroundColor(Color.parseColor("#589746"));

                        scr4.setText(Fajr.substring(0,5));
                        scr4.setGravity(Gravity.CENTER);

                        scr5.setText(Dhuhr.substring(0,5));
                        scr5.setGravity(Gravity.CENTER);

                        scr6.setText(Asr.substring(0,5));
                        scr6.setGravity(Gravity.CENTER);

                        scr7.setText(Maghrib.substring(0,5));
                        scr7.setGravity(Gravity.CENTER);

                        scr8.setText(Isha.substring(0,5));
                        scr8.setGravity(Gravity.CENTER);

                        row.addView(scr1);
                        row.addView(scr2);
                        row.addView(scr3);
                        row.addView(scr4);
                        row.addView(scr5);
                        row.addView(scr6);
                        row.addView(scr7);
                        row.addView(scr8);

                        tableLayout.addView(row);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);

        /*cacheRequest = new CacheRequest(Request.Method.GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {

                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONObject jsonObject = new JSONObject(jsonString);

                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {

                        row = new TableRow(PrayerTimesCalendarView.this);

                        scr1 = new TextView(PrayerTimesCalendarView.this);
                        scr2 = new TextView(PrayerTimesCalendarView.this);
                        scr3 = new TextView(PrayerTimesCalendarView.this);
                        scr4 = new TextView(PrayerTimesCalendarView.this);
                        scr5 = new TextView(PrayerTimesCalendarView.this);
                        scr6 = new TextView(PrayerTimesCalendarView.this);
                        scr7 = new TextView(PrayerTimesCalendarView.this);
                        scr8 = new TextView(PrayerTimesCalendarView.this);

                        JSONObject timings = new JSONObject(data.getJSONObject(i).getString("timings"));

                        JSONObject date = new JSONObject(data.getJSONObject(i).getString("date"));
                        JSONObject gregorian = date.getJSONObject("gregorian");
                        JSONObject hijri = date.getJSONObject("hijri");

                        JSONObject weekday_obj = gregorian.getJSONObject("weekday");

                        Fajr = timings.getString("Fajr");
                        Dhuhr = timings.getString("Dhuhr");
                        Asr = timings.getString("Asr");
                        Maghrib = timings.getString("Maghrib");
                        Isha = timings.getString("Isha");

                        geo_day = gregorian.getString("day");
                        hijri_day = hijri.getString("day");

                        int hijriday = Integer.parseInt(hijri_day ) - 1;

                        weekday =  weekday_obj.getString("en");

                        scr1.setText(geo_day.replace(" (PKT)", ""));
                        scr1.setGravity(Gravity.CENTER);
                        scr1.setBackgroundColor(Color.parseColor("#589746"));

                        scr2.setText(hijriday + "");
                        scr2.setGravity(Gravity.CENTER);
                        scr2.setBackgroundColor(Color.parseColor("#589746"));

                        scr3.setText(weekday.replace(" (PKT)", ""));
                        scr3.setGravity(Gravity.CENTER);
                        scr3.setBackgroundColor(Color.parseColor("#589746"));

                        scr4.setText(Fajr.replace(" (PKT)", ""));
                        scr4.setGravity(Gravity.CENTER);

                        scr5.setText(Dhuhr.replace(" (PKT)", ""));
                        scr5.setGravity(Gravity.CENTER);

                        scr6.setText(Asr.replace(" (PKT)", ""));
                        scr6.setGravity(Gravity.CENTER);

                        scr7.setText(Maghrib.replace(" (PKT)", ""));
                        scr7.setGravity(Gravity.CENTER);

                        scr8.setText(Isha.replace(" (PKT)", ""));
                        scr8.setGravity(Gravity.CENTER);

                        row.addView(scr1);
                        row.addView(scr2);
                        row.addView(scr3);
                        row.addView(scr4);
                        row.addView(scr5);
                        row.addView(scr6);
                        row.addView(scr7);
                        row.addView(scr8);

                        tableLayout.addView(row);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(cacheRequest);*/
    }
    private class CacheRequest extends Request<NetworkResponse> {
        private final Response.Listener<NetworkResponse> mListener;
        private final Response.ErrorListener mErrorListener;

        public CacheRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
        }


        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
            if (cacheEntry == null) {
                cacheEntry = new Cache.Entry();
            }
            final long cacheHitButRefreshed = 1000; // in evry second cache will be hit, but also refreshed on background
            final long cacheExpired = 7 * 24 * 60 * 60 * 1000; // in 7 * 24 hours this cache entry expires completely
            long now = System.currentTimeMillis();
            final long softExpire = now + cacheHitButRefreshed;
            final long ttl = now + cacheExpired;
            cacheEntry.data = response.data;
            cacheEntry.softTtl = softExpire;
            cacheEntry.ttl = ttl;
            String headerValue;
            headerValue = response.headers.get("Date");
            if (headerValue != null) {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            headerValue = response.headers.get("Last-Modified");
            if (headerValue != null) {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            cacheEntry.responseHeaders = response.headers;
            return Response.success(response, cacheEntry);
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {
            mListener.onResponse(response);
        }

        @Override
        protected VolleyError parseNetworkError(VolleyError volleyError) {
            return super.parseNetworkError(volleyError);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }
    }


}