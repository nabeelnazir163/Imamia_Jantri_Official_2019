package com.namobiletech.imamiajantri.UI.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.namobiletech.imamiajantri.AlarmNotification.AppSettings;
import com.namobiletech.imamiajantri.AlarmNotification.Constants;
import com.namobiletech.imamiajantri.AlarmNotification.MyReceiver;
import com.namobiletech.imamiajantri.AlarmNotification.SalaatSchedulingService;
import com.namobiletech.imamiajantri.Noti_Receiver.NotificationReceiver;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Fragment.Aboutus_Frag;
import com.namobiletech.imamiajantri.UI.Fragment.QuranFragment;
import com.namobiletech.imamiajantri.UI.Fragment.UserProfileFrag;
import com.namobiletech.imamiajantri.UI.Fragment.duaFragment;
import com.namobiletech.imamiajantri.UI.Fragment.favouriteList;
import com.namobiletech.imamiajantri.UI.Fragment.homeFragment;
import com.namobiletech.imamiajantri.UI.Fragment.jantriFragment;
import com.namobiletech.imamiajantri.UI.Fragment.qiblaDirection;
import com.namobiletech.imamiajantri.Utils.AnalyticsApplication;
import com.namobiletech.imamiajantri.Utils.UserSessionManager;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbContract;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //WIDGETS
    //Linear Layout
    private LinearLayout profile_dispay_LL;
    private LinearLayout home_LL;
    private LinearLayout Profile_LL;
    private LinearLayout Jantri_LL;
    private LinearLayout qiblaDirection_LL;
    private LinearLayout Quran_LL;
    private LinearLayout Dua_LL;
    private LinearLayout rateUs_LL;
    private LinearLayout aboutus_LL;
    private LinearLayout favourite_LL;
    private LinearLayout logout_LL;

    //TEXTVIEW
    private TextView username_nav;
    private TextView home_tv;
    private TextView profile_tv;
    private TextView Jantri_tv;
    private TextView qiblaDirection_tv;
    private TextView Dua_tv;
    private TextView rateUS_tv;
    private TextView aboutUs_tv;
    private TextView favourite_tv;
    private TextView logout_tv;

    private CircleImageView userProfileImage;

    DrawerLayout drawer;

    Cursor ayahcursor;
    Cursor surahCursor;
    Cursor duaCursor;
    DbHelper dbHelper;
    DbHelper duadbHelper;
    Cursor duacursor;
    Cursor cursornamaz;

    RequestQueue requestQueue_a;
    StringRequest request_a;

    RequestQueue requestQueue_b;
    StringRequest request_b;

    UserSessionManager sessionManager;

    public static final String KEY_NAME = "name";

    Tracker mTracker;

    GoogleApiClient mGoogleApiClient;

    String formattedCurrDate;

    String FajrTime;
    String DhuhrTime;
    String AsrTime;
    String MaghribTime;
    String IshaTime;

    String userImageString;

    //shared_pref file name
    private static final String LOGIN_PREFERNCE_NAME = "LoginPref";

    //Shared Preference mode
    int PRIVATE_MODE = 0;

    SharedPreferences user_login_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);

        FirebaseMessaging.getInstance().subscribeToTopic("ImamiaJantri")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Faied";
                        }
                        Log.d("msg", msg);
                    }
                });

        Intent service = new Intent(getApplicationContext(), SalaatSchedulingService.class);
        this.startService(service);

        dbHelper = new DbHelper(MainActivity.this);
        dbHelper.getWritableDatabase();
        cursornamaz = dbHelper.getNamazTimeInformation(dbHelper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //referencing UserSessionMangment
        sessionManager = new UserSessionManager(MainActivity.this);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedCurrDate = df.format(c);

        FacebookSdk.sdkInitialize(getApplicationContext());

        profile_dispay_LL = (LinearLayout) findViewById(R.id.name_img_display_Layout);
        home_LL = (LinearLayout) findViewById(R.id.nav_homeLay);
        Profile_LL = (LinearLayout) findViewById(R.id.nav_ProfileLay);
        Jantri_LL = (LinearLayout) findViewById(R.id.nav_jantriLay);
        qiblaDirection_LL = (LinearLayout) findViewById(R.id.nav_qiblaLayout);
        Quran_LL = (LinearLayout) findViewById(R.id.nav_quranLayout);
        Dua_LL = (LinearLayout) findViewById(R.id.nav_duaLayout);
        rateUs_LL = (LinearLayout) findViewById(R.id.nav_rateLayout);
        aboutus_LL = (LinearLayout) findViewById(R.id.nav_aboutLayout);
        favourite_LL = (LinearLayout) findViewById(R.id.fvrtList_Layout);
        logout_LL = (LinearLayout) findViewById(R.id.nav_logoutLay);

        if (!sessionManager.CheckLogin()) {
            logout_LL.setVisibility(View.GONE);
            profile_dispay_LL.setVisibility(View.GONE);
            Profile_LL.setVisibility(View.GONE);
        } else {
            logout_LL.setVisibility(View.VISIBLE);
            profile_dispay_LL.setVisibility(View.VISIBLE);
            Profile_LL.setVisibility(View.VISIBLE);
        }

        username_nav = (TextView) findViewById(R.id.username_tv_nav);
        home_tv = (TextView) findViewById(R.id.nav_home_tv);
        profile_tv = (TextView) findViewById(R.id.nav_pro_tv);
        Jantri_tv = (TextView) findViewById(R.id.nav_jantri_tv);
        qiblaDirection_tv = (TextView) findViewById(R.id.nav_qibla_tv);
        Dua_tv = (TextView) findViewById(R.id.nav_dua_tv);
        rateUS_tv = (TextView) findViewById(R.id.rateus_dua_tv);
        aboutUs_tv = (TextView) findViewById(R.id.aboutus_tv);
        favourite_tv = (TextView) findViewById(R.id.fvrtList_tv);
        logout_tv = (TextView) findViewById(R.id.navlogout_tv);

        userProfileImage = (CircleImageView) findViewById(R.id.userImageView);

        Typeface open_Sans_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular.ttf");

        username_nav.setTypeface(open_Sans_font);
        home_tv.setTypeface(open_Sans_font);
        profile_tv.setTypeface(open_Sans_font);
        Jantri_tv.setTypeface(open_Sans_font);
        qiblaDirection_tv.setTypeface(open_Sans_font);
        Dua_tv.setTypeface(open_Sans_font);
        rateUS_tv.setTypeface(open_Sans_font);
        aboutUs_tv.setTypeface(open_Sans_font);
        favourite_tv.setTypeface(open_Sans_font);
        logout_tv.setTypeface(open_Sans_font);


        profile_dispay_LL.setOnClickListener(this);
        home_LL.setOnClickListener(this);
        Profile_LL.setOnClickListener(this);
        Jantri_LL.setOnClickListener(this);
        qiblaDirection_LL.setOnClickListener(this);
        Quran_LL.setOnClickListener(this);
        Dua_LL.setOnClickListener(this);
        rateUs_LL.setOnClickListener(this);
        aboutus_LL.setOnClickListener(this);
        favourite_LL.setOnClickListener(this);
        logout_LL.setOnClickListener(this);

        /*HashMap<String, String> user = new HashMap<>();

        user = sessionManager.getUserDetails();

        String username = user.get(KEY_NAME);

        username_nav.setText(username);*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new homeFragment());
        fragmentTransaction.commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("234249254590-0vr0oofi19kmse1u6jenvriraufnfhqd.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loadArbiAyah();
//        loadEnglishAyah();
        loadQURAN();
        loadDua();

        Calendar then = Calendar.getInstance(TimeZone.getDefault());
        Calendar now = Calendar.getInstance(TimeZone.getDefault());

        then = getCalendarFromPrayerTime(then, "09:30");

        if (then.before(now)) {
            then.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private Calendar getCalendarFromPrayerTime(Calendar cal, String prayerTime) {
        String[] time = prayerTime.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
        cal.set(Calendar.MINUTE, Integer.valueOf(time[1]));
        cal.set(Calendar.SECOND, 0);

        return cal;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment fragment = getVisibleFragment();

            if (fragment instanceof homeFragment) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new homeFragment());
                fragmentTransaction.commit();
            }

        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (view.getId()) {

            case R.id.name_img_display_Layout:

                fragmentTransaction.replace(R.id.mainContainer, new UserProfileFrag());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);

                break;

            case R.id.nav_homeLay:

                fragmentTransaction.replace(R.id.mainContainer, new homeFragment());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);

                break;

            case R.id.nav_ProfileLay:

                fragmentTransaction.replace(R.id.mainContainer, new UserProfileFrag());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);

                break;

            case R.id.nav_jantriLay:

                fragmentTransaction.replace(R.id.mainContainer, new jantriFragment());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);

                break;

            case R.id.nav_qiblaLayout:

                fragmentTransaction.replace(R.id.mainContainer, new qiblaDirection());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);

                break;

            case R.id.nav_duaLayout:

                duadbHelper = new DbHelper(this);
                duacursor = duadbHelper.getDuasInformation(duadbHelper);

                if (duaCursor.getCount() != 0) {

                    /*fragmentTransaction.replace(R.id.mainContainer, new duaFragment());
                    fragmentTransaction.commit();
                    drawer.closeDrawer(Gravity.START);*/
                    Intent intent = new Intent(MainActivity.this, subCat_activity.class);
                    intent.putExtra("cat_id", "507");
                    startActivity(intent);

                    drawer.closeDrawer(Gravity.START);
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Dua, Please Wait ...", Toast.LENGTH_LONG).show();
                    drawer.closeDrawer(Gravity.START);
                }

                break;

            case R.id.nav_quranLayout:

                ayahcursor = dbHelper.getAyyahsInformation(dbHelper);
                Cursor ayahcursor = dbHelper.getAyyahsInformationEnglish(dbHelper);
//                surahCursor = dbHelper.getQuranInformation(dbHelper);

                if (surahCursor.getCount() != 0 && ayahcursor.getCount() != 0 /*&& ayahcursor.getCount() != 0*/) {
                    fragmentTransaction.replace(R.id.mainContainer, new QuranFragment());
                    fragmentTransaction.commit();
                    drawer.closeDrawer(Gravity.START);
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Quran, Please Wait ...", Toast.LENGTH_LONG).show();
                    drawer.closeDrawer(Gravity.START);
                }

                break;

            case R.id.nav_rateLayout:

                try{
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
                break;

            case R.id.nav_aboutLayout:

                fragmentTransaction.replace(R.id.mainContainer, new Aboutus_Frag());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.fvrtList_Layout:

                fragmentTransaction.replace(R.id.mainContainer, new favouriteList());
                fragmentTransaction.commit();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.nav_logoutLay:

                Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                LoginManager.getInstance().logOut();
                sessionManager.Logoutuser();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
//                finish();

                break;
        }

    }

    private void loadArbiAyah() {

        ayahcursor = dbHelper.getAyyahsInformation(dbHelper);
        requestQueue_a = Volley.newRequestQueue(this);
        String quran_url_uthmani = "http://api.alquran.cloud/quran";

        request_a = new StringRequest(Request.Method.GET, quran_url_uthmani, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (ayahcursor.getCount() == 0) {
                        dbHelper.PutAyyahInformation(dbHelper, jsonObject, MainActivity.this);
                        Log.e("teste", "arbi");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString() + " abc", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        request_a.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_a.add(request_a);

    }

    private void loadEnglishAyah() {

        ayahcursor = dbHelper.getAyyahsInformation(dbHelper);
        requestQueue_a = Volley.newRequestQueue(this);
        String quran_url_uthmani = "http://api.alquran.cloud/quran";

        request_a = new StringRequest(Request.Method.GET, quran_url_uthmani, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (ayahcursor.getCount() == 0) {
                        dbHelper.PutAyyahInformationEnglish(dbHelper, jsonObject, MainActivity.this);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        request_a.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_a.add(request_a);

    }

    private void loadDua() {

        duaCursor = dbHelper.getDuasInformation(dbHelper);
        requestQueue_b = Volley.newRequestQueue(this);

        String DUA_URL = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/categories.php";

        request_b = new StringRequest(Request.Method.GET, DUA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    dbHelper.deleteDuaFromTable(dbHelper, DbContract.MenuEntry.DUA);
                    dbHelper.PutDuaInformation(dbHelper, jsonArray, MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("error", error.getMessage());
            }
        });

        request_b.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_b.add(request_b);

    }

    private void loadQURAN() {

        surahCursor = dbHelper.getQuranInformation(dbHelper);
        requestQueue_b = Volley.newRequestQueue(this);
        String quran_url_uthmani = "http://api.alquran.cloud/quran/en.asad";

        request_b = new StringRequest(Request.Method.GET, quran_url_uthmani, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (surahCursor.getCount() == 0) {
                        dbHelper.PutQuranInformation(dbHelper, jsonObject, MainActivity.this);
                        dbHelper.PutAyyahInformationEnglish(dbHelper, jsonObject, MainActivity.this);

                        Log.e("teste", "quran");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();

            }
        });

        request_b.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_b.add(request_b);

    }
/*
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        *//**
     * Before starting background thread
     * *//*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            Log.d("pdf", "start");
            Toast.makeText(getApplicationContext(), "downloading", Toast.LENGTH_LONG).show();
        }

        *//**
     * Downloading file in background thread
     * *//*
        @Override
        protected String doInBackground(String... f_url) {

            InputStream input = null;
            FileOutputStream output = null;

            try {
                URL url = new URL(f_url[0]);

                String outputName = "imamiajanri.pdf";

                input = url.openConnection().getInputStream();
                output = getApplicationContext().openFileOutput(outputName, Context.MODE_PRIVATE);

                int read;
                byte[] data = new byte[1024];
                while ((read = input.read(data)) != -1) {
                    output.write(data, 0, read);

                    Log.d("abc", output.toString());
                }

                return outputName;

            }
            catch (IOException e)
            {
                Log.e("exception", e.toString());
//                Toast.makeText(getApplicationContext(), "downloading interupted", Toast.LENGTH_LONG).show();
            }
            finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        Log.e("exception", e.toString());
//                        Toast.makeText(getApplicationContext(), "downloading interupted", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        Log.e("exception", e.toString());
//                        Toast.makeText(getApplicationContext(), "downloading interupted", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }



        */

    /**
     * After completing background task
     **//*
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");

            Log.d("def", getFileStreamPath("imamiajanri.pdf").toString());

            SharedPreferences sharedPreferences =  getSharedPreferences("pdf", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            jantriFragment jantriFragment = new jantriFragment();

            Toast.makeText(getApplicationContext(), "downloaded", Toast.LENGTH_LONG).show();


            File file = getFileStreamPath("imamiajanri.pdf");
            if (file.exists()) {
                editor.putBoolean("pdf", true).apply();
            }else {
                editor.putBoolean("pdf", false).apply();
            }
        }

    }*/
    @Override
    protected void onResume() {
        super.onResume();

        if (!sessionManager.CheckLogin()) {
            logout_LL.setVisibility(View.GONE);
            profile_dispay_LL.setVisibility(View.GONE);
            Profile_LL.setVisibility(View.GONE);
        } else {
            logout_LL.setVisibility(View.VISIBLE);
            profile_dispay_LL.setVisibility(View.VISIBLE);
            Profile_LL.setVisibility(View.VISIBLE);

            HashMap<String, String> user = new HashMap<>();

            user = sessionManager.getUserDetails();

            String username = user.get(KEY_NAME);

            username_nav.setText(username);

            LoadImage();

        }

        mTracker.setScreenName("Image~" + "Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (AppSettings.getInstance(this).getInt(Constants.EXTRA_PRAYER_NAME) == -1 || AppSettings.getInstance(this).getInt(Constants.EXTRA_PRAYER_NAME) >= 5)
            AppSettings.getInstance(this).set(Constants.EXTRA_PRAYER_NAME, 0);

        DataFromDatabase();
    }


    public void LoadImage() {

        RequestQueue requestQueue;
        CacheRequest cacheRequest;
        StringRequest request;

        String FEED_URL = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/User_data.php";

        user_login_pref = getSharedPreferences(LOGIN_PREFERNCE_NAME, PRIVATE_MODE);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        request = new StringRequest(Request.Method.POST, FEED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("user_file") != null) {
                        userImageString = jsonObject.getString("user_file");
                    }

                    if (!userImageString.equals("")) {
                        Picasso.with(MainActivity.this).load(userImageString).placeholder(R.drawable.imageplaceholdr).into(userProfileImage);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", user_login_pref.getString("user_id", ""));
                return hashMap;
            }
        };

        requestQueue.add(request);

        cacheRequest = new CacheRequest(Request.Method.POST, FEED_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {

                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONObject jsonObject = new JSONObject(jsonString);

                    if (jsonObject.getString("user_file") != null) {
                        userImageString = jsonObject.getString("user_file");
                    }

                    if (!userImageString.equals("")) {
                        Picasso.with(MainActivity.this).load(userImageString).placeholder(R.drawable.imageplaceholdr).into(userProfileImage);
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", user_login_pref.getString("user_id", ""));
                return hashMap;
            }
        };

        // Add the request to the RequestQueue.
        requestQueue.add(cacheRequest);

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
            final long cacheExpired = 7 * 24 * 60 * 60 * 1000; // in 7 days this cache entry expires completely
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
