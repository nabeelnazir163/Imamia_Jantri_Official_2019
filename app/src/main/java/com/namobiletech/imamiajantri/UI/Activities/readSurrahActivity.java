package com.namobiletech.imamiajantri.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.namobiletech.imamiajantri.Adapter.AyyahRecyclerAdapter;
import com.namobiletech.imamiajantri.Adapter.SurahRecyclerAdapter;
import com.namobiletech.imamiajantri.Model.AyyatList;
import com.namobiletech.imamiajantri.Model.SurahList;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbContract;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class readSurrahActivity extends AppCompatActivity {

    String SURRAHNAME;
    String SURRAHNUMBER;
    String REVELATIONTTYPE;

    //Textviews
    private TextView surrahType;
    private TextView SurrahName;
    private TextView totalayyahcount;

    //Cursor
    Cursor cursor;
    Cursor cursorenglish;
    DbHelper dbHelper;

    ArrayList<AyyatList> arrayList = new ArrayList<>();
    ArrayList<AyyatList> arrayListEnglish = new ArrayList<>();
    private RecyclerView mSurahRecyclerview;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Cursor ayahcursor;

    RequestQueue requestQueue_a;
    StringRequest request_a;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_surrah);

        Intent intent = getIntent();

        SURRAHNUMBER = intent.getStringExtra("surrahNumber");
        SURRAHNAME = intent.getStringExtra("surrahName");
        REVELATIONTTYPE = intent.getStringExtra("type");

        surrahType = (TextView) findViewById(R.id.revelationType_tv);
        SurrahName = (TextView) findViewById(R.id.surrahNameTv);
        totalayyahcount = (TextView) findViewById(R.id.ayyah_tv);

        surrahType.setText(REVELATIONTTYPE);
        SurrahName.setText(SURRAHNAME);

        dbHelper = new DbHelper(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("QURAN");
        progressDialog.setMessage("Loading Ayyahs");
//        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mSurahRecyclerview = findViewById(R.id.readSurrah_Rv);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        mSurahRecyclerview.setLayoutManager(layoutManager);
        mSurahRecyclerview.setHasFixedSize(true);

        cursor = dbHelper.returnnumRows(dbHelper, SURRAHNUMBER);
        cursorenglish = dbHelper.returnnumRowsEnglish(dbHelper, SURRAHNUMBER);

        if(cursorenglish.getCount() != 0 && cursor.getCount() != 0) {

            DataFromDB();
        }
        else {
            loadArbiAyah();
        }
    }

    private void loadArbiAyah() {

        ayahcursor = dbHelper.getAyyahsInformation(dbHelper);
        requestQueue_a = Volley.newRequestQueue(this);
        String quran_url_uthmani = "http://api.alquran.cloud/quran";

//        Toast.makeText(getContext(), "" + ayahcursor.getCount(), Toast.LENGTH_LONG).show();

        request_a = new StringRequest(Request.Method.GET, quran_url_uthmani, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (ayahcursor.getCount() < 6236) {
                        {
                            Log.e("def", "da");

                            dbHelper.deleteDuaFromTable(dbHelper, DbContract.MenuEntry.AYAH_DESCRIPTION_TABLE_ARABIC);

                            dbHelper.PutAyyahInformation(dbHelper, jsonObject, readSurrahActivity.this);
                            DataFromDB();

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage() , Toast.LENGTH_LONG).show();
            }
        });
        request_a.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_a.add(request_a);

    }

    private void DataFromDB() {

        progressDialog.dismiss();

        cursor.moveToNext();
        cursorenglish.moveToNext();

        do{

            AyyatList surahList = new AyyatList(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5), cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10), cursor.getString(11));
            AyyatList surahListEnglish = new AyyatList(cursorenglish.getString(1),cursorenglish.getString(2),cursorenglish.getString(3),cursorenglish.getString(4),cursorenglish.getString(5), cursorenglish.getString(6),cursorenglish.getString(7),cursorenglish.getString(8),cursorenglish.getString(9),cursorenglish.getString(10), cursorenglish.getString(11));
            arrayList.add(surahList);
            arrayListEnglish.add(surahListEnglish);

        }
        while(cursor.moveToNext() && cursorenglish.moveToNext());

        dbHelper.close();

        totalayyahcount.setText("Ayyat " + arrayListEnglish.size());

        adapter = new AyyahRecyclerAdapter(arrayList, arrayListEnglish, getApplicationContext());
        mSurahRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}