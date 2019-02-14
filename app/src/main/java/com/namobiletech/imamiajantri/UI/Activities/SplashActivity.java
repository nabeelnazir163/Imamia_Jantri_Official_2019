package com.namobiletech.imamiajantri.UI.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.AnalyticsApplication;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbContract;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;
import com.namobiletech.imamiajantri.Utils.others.HttpJsonParser;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        InternetConnectivityListener {


    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private LocationRequest mlocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLAstLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    double latitude;
    double longitude;

    private SharedPreferences locationSharedPref;
    private SharedPreferences.Editor editor;


    String prayer_url;
    String prayer_url_2;
    DbHelper dbHelper;
    Cursor cursor;

    ProgressDialog progressDialog;

    boolean isGPSEnabled = false;

    private LinearLayout mainLayout;

    Snackbar snackbar;

    InternetAvailabilityChecker internetAvailabilityChecker;

    private boolean checksignin = false;

    Tracker mTracker;

    int couter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        mainLayout = (LinearLayout) findViewById(R.id.splas_mainlayout);

        InternetAvailabilityChecker.init(this);

        locationSharedPref = getSharedPreferences("location", MODE_PRIVATE);

        dbHelper = new DbHelper(getApplicationContext());
        cursor = dbHelper.getNamazTimeInformation(dbHelper);

        if(cursor.getCount() != 0)
        {
            cursor.moveToNext();

            final Calendar calendar = Calendar.getInstance();

            int crntMonth = (calendar.get(Calendar.MONTH)) + 1;
            int calYear = calendar.get(Calendar.YEAR);
            String monthfromDb = (cursor.getString(0).substring(3,5));
            String yearfromDb = (cursor.getString(0).substring(6,10));

            if(crntMonth > Integer.valueOf(monthfromDb) || calYear > Integer.valueOf(yearfromDb) )
            {
                setNamazTimes();
            }
        }

        String lat = locationSharedPref.getString("latitude", "");

        if (lat.equals("")) {

            if(!isNetworkAvailable()) {
                snackbar = Snackbar.make(mainLayout, "Connect Internet to Continue", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
            setUpLocation();

            internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
            internetAvailabilityChecker.addInternetConnectivityListener(this);

        } else if (dbHelper.getNamazTimeInformation(dbHelper).getCount() == 0) {

            if(isNetworkAvailable()) {
                setNamazTimes();
            }
            else {
                snackbar = Snackbar.make(mainLayout, "Connect Internet to Continue", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }


        } else if(!lat.equals("") && dbHelper.getNamazTimeInformation(dbHelper).getCount() != 0 ){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 1500);
        }

    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if(isConnected)
        {
            if (snackbar != null) {
                snackbar.dismiss();
            }

            startActivity(new Intent(SplashActivity.this, SplashActivity.class));
            finish();
        }
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request run time permission...
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, MY_PERMISSION_REQUEST_CODE);

        } else {
            if (checkplayservices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLAstLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);

        if (mLAstLocation != null) {

            latitude = mLAstLocation.getLatitude();
            longitude = mLAstLocation.getLongitude();


            if(!checksignin) {
                init();

                Log.v("abc", String.valueOf(latitude));
            }

        } else {
            String lat = locationSharedPref.getString("latitude", "");

            if (lat.equals("")) {

                couter += 1;

                Log.v("abc", "checkPlayservice hvj");

                if (!isGPSEnabled) {
                    showSettingsAlert();
                    if(progressDialog.isShowing())
                    progressDialog.dismiss();
                    Log.e("abc", ":(");
                }
                else {
//                    if(couter == 1)
//                    showSettingsAlert();

//                    LocationRequest locationRequest = new LocationRequest();
//                    locationRequest.setInterval(3000);
//                    locationRequest.setFastestInterval(3000);
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                }

            } else {
                Log.v("abc", "checkPlayservice");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }, 1500);
            }

        }
    }

    public void showSettingsAlert() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void createLocationRequest() {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkplayservices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }


        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean checkplayservices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            else {

                Toast.makeText(this, "This device is not supported ", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Splash Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void startLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.v("abc", "connected");
        displayLocation();
        startLocationUpdate();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLAstLocation = location;
        displayLocation();
    }

    public void init() {

        checksignin = true;
        double m_latitude = latitude;
        double m_longitude = longitude;

        editor = locationSharedPref.edit();

        editor.putString("longitude", String.valueOf(longitude));
        editor.putString("latitude", String.valueOf(latitude));

        Log.v("latitude", String.valueOf(latitude));

        editor.commit();


        if (dbHelper.getNamazTimeInformation(dbHelper).getCount() == 0) {
            Log.v("check", "there");
            setNamazTimes();
        } else {

            Log.v("check", "OR there");

            if(progressDialog.isShowing())
            progressDialog.dismiss();

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }

    }

    private void setNamazTimes() {
        final Calendar calendar = Calendar.getInstance();

        final int date = calendar.get(Calendar.DATE);
        final int calMonth = (calendar.get(Calendar.MONTH)) + 1;
        final int calYear = (calendar.get(Calendar.YEAR));

        String lat = locationSharedPref.getString("latitude", "0.0");
        String lng = locationSharedPref.getString("longitude", "0.0");

        Log.e("year", calYear+"");

        //Url of API
        prayer_url = "http://api.aladhan.com/v1/calendar?latitude=" + (lat) + "&longitude=" + (lng) + "&method=1&month=" + calMonth + "&year=" + calYear + "&school=1";


        new FetchStats().execute();

    }

    private class FetchStats extends AsyncTask<String, String, String> {
        JSONObject response;

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser jsonParser = new HttpJsonParser();
            response = jsonParser.makeHttpRequest(prayer_url, "GET", null);
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {


                    Log.v("Response", "abc");
                    if (response != null) {

                        Log.v("Response", "def");

                        dbHelper.deleteDataFromTable(dbHelper, DbContract.MenuEntry.TABLE_NAME);
                        dbHelper.PutNamazTimeInformation(dbHelper, response,SplashActivity.this);

                        setNextMonthNamazTimes();

                    } else {
                        Log.v("Response", "ghi");

                        if (progressDialog.isShowing())
                        progressDialog.dismiss();
//                    finish();
                        Toast.makeText(getApplicationContext(), "Unable to Load Data", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void setNextMonthNamazTimes() {
        final Calendar calendar = Calendar.getInstance();

        int calMonth = (calendar.get(Calendar.MONTH)) + 2;
        int calYear = (calendar.get(Calendar.YEAR));

        if (calMonth > 12)
        {
            calMonth = calMonth - 12;
            calYear = calYear + 1;
        }


        String lat = locationSharedPref.getString("latitude", "0.0");
        String lng = locationSharedPref.getString("longitude", "0.0");

        //Url of API
        prayer_url_2 = "http://api.aladhan.com/v1/calendar?latitude=" + (lat) + "&longitude=" + (lng) + "&method=1&month=" + calMonth + "&year=" + calYear + "&school=1";

        new FetchStats2().execute();
    }

    private class FetchStats2 extends AsyncTask<String, String, String> {
        JSONObject response;

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser jsonParser = new HttpJsonParser();
            response = jsonParser.makeHttpRequest(prayer_url_2, "GET", null);
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {

                    Log.v("Response", "jkl");


                    if (response != null) {

                        dbHelper.PutNamazTimeInformation(dbHelper, response, SplashActivity.this);

                        // Start your app main activity
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    } else {


                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
//                    finish();
                        Toast.makeText(getApplicationContext(), "Unable to Load Data", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

}
