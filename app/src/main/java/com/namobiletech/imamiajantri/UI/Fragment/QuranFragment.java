package com.namobiletech.imamiajantri.UI.Fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.namobiletech.imamiajantri.Adapter.SurahRecyclerAdapter;
import com.namobiletech.imamiajantri.Model.SurahList;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class QuranFragment extends Fragment {


    View v;

    ArrayList<SurahList> arrayList = new ArrayList<>();
    private RecyclerView mSurahRecyclerview;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    DbHelper dbHelper;
    Cursor cursor;
    Cursor ayahcursor;

    RequestQueue requestQueue_a;
    StringRequest request_a;

    RequestQueue requestQueue_b;
    StringRequest request_b;

    ProgressDialog progressDialog;

    public QuranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_quran, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("QURAN");
        progressDialog.setMessage("Loading Surrah");
//        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        dbHelper = new DbHelper(getContext());

        cursor = dbHelper.getQuranInformation(dbHelper);

        if(cursor.getCount() != 0)
        {
            showQuranSurrahs();
        }
        /*else {
            loadQURAN();
        }
*/
        return v;
    }

    private void showQuranSurrahs() {

        mSurahRecyclerview = v.findViewById(R.id.surahsindex);
        layoutManager = new LinearLayoutManager(getContext());
        mSurahRecyclerview.setLayoutManager(layoutManager);
        mSurahRecyclerview.setHasFixedSize(true);

        Log.e("test", "test");

        progressDialog.dismiss();

        cursor.moveToNext();

        do{

            SurahList surahList = new SurahList(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            arrayList.add(surahList);

        }
        while(cursor.moveToNext());

        dbHelper.close();

        adapter = new SurahRecyclerAdapter(arrayList, getContext());
        mSurahRecyclerview.setAdapter(adapter);

    }

/*
    private void loadQURAN() {

        requestQueue_b = Volley.newRequestQueue(getContext());
        String quran_url_uthmani = "http://api.alquran.cloud/quran/en.asad";

        request_b = new StringRequest(Request.Method.GET, quran_url_uthmani, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (cursor.getCount() == 0) {
                        dbHelper.PutQuranInformation(dbHelper, jsonObject);
                        dbHelper.PutAyyahInformationEnglish(dbHelper, jsonObject);

                        showQuranSurrahs();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();

            }
        });

        request_b.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_b.add(request_b);

    }
*/

    /*private void loadArbiAyah() {

        ayahcursor = dbHelper.getAyyahsInformation(dbHelper);
        requestQueue_a = Volley.newRequestQueue(getContext());
        String quran_url_uthmani = "http://api.alquran.cloud/quran";

        request_a = new StringRequest(Request.Method.GET, quran_url_uthmani, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (ayahcursor.getCount() == 0) {
                        dbHelper.PutAyyahInformation(dbHelper, jsonObject);

                        Toast.makeText(getContext(), "ayyah", Toast.LENGTH_LONG).show();

                        Log.e("abc", "done");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage() + " a ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage() + "b", Toast.LENGTH_LONG).show();
            }
        });
        request_a.setRetryPolicy(new DefaultRetryPolicy(3600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue_a.add(request_a);

    }*/
}
