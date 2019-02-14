package com.namobiletech.imamiajantri.UI.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.AnalyticsApplication;
import com.namobiletech.imamiajantri.Utils.UserSessionManager;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class duaActivity extends AppCompatActivity {

   /* List<String> sleeping_list = new ArrayList<String>();
    List<String> toiletList = new ArrayList<String>();
    List<String> mosqueList = new ArrayList<String>();

    List<String> sleeping_list_eng = new ArrayList<String>();
    List<String> toiletList_eng = new ArrayList<String>();
    List<String> mosqueList_eng = new ArrayList<String>();*/

    private TextView duaEnglish;
    private TextView readMore;
    private TextView readless;

    private ImageView duaBackBtn;
    private ImageView shareImage;
    private ImageView saveImage;
    private ImageView UnsaveImage;

    private WebView webView;

    Tracker mTracker;

    private DbHelper dbHelper;
    private Cursor cursor;

    String postid;
    int child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        duaBackBtn = (ImageView) findViewById(R.id.backarraow_iv_dua);
        shareImage = (ImageView) findViewById(R.id.share_iv_dua);
        saveImage = (ImageView) findViewById(R.id.save_iv_dua);
        UnsaveImage = (ImageView) findViewById(R.id.unsave_iv_dua);

        Typeface MUHAMMAI_QURANIC_FONT = Typeface.createFromAsset(getAssets(), "fonts/MUHAMMADI_QURANIC.ttf");

        duaEnglish = (TextView) findViewById(R.id.dua_english);
        readMore = (TextView) findViewById(R.id.readmoretv_duaActivity);
        readless = (TextView) findViewById(R.id.readlesstv_duaActivity);

        webView = (WebView) findViewById(R.id.webview);
        duaEnglish.setTypeface(MUHAMMAI_QURANIC_FONT);

//        duaEnglish.setMovementMethod(ScrollingMovementMethod.getInstance());

//        String sleepingDua[] = getResources().getStringArray(R.array.sleeping_dua);
//        String mosqueDua[] = getResources().getStringArray(R.array.dua_mosque);
//        String toilet[] = getResources().getStringArray(R.array.toilet_dua);
//
//        String sleepingDua_eng[] = getResources().getStringArray(R.array.sleeping_dua_english);
//        String mosqueDua_eng[] = getResources().getStringArray(R.array.dua_mosque_english);
//        String toilet_eng[] = getResources().getStringArray(R.array.toilet_dua_english);

        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        if(intent.getStringExtra("id") != null) {
            postid = intent.getStringExtra("id");
        }

        if(dbHelper.getduastatus(postid, dbHelper))
        {
            saveImage.setVisibility(View.GONE);
            UnsaveImage.setVisibility(View.VISIBLE);
        }


        cursor = dbHelper.getpostInformation(dbHelper, (postid));

        cursor.moveToNext();

        String post = cursor.getString(5);
        webView.loadDataWithBaseURL(null, post, "text/html", "utf-8", null);

        duaEnglish.setText(Html.fromHtml(cursor.getString(5)));

        if(duaEnglish.getLineCount() > 8)
        {
            readMore.setVisibility(View.VISIBLE);
            readless.setVisibility(View.GONE);
        }
        else {
            readMore.setVisibility(View.VISIBLE);
            readless.setVisibility(View.GONE);
        }

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                duaEnglish.setMaxLines(Integer.MAX_VALUE);
                readMore.setVisibility(View.GONE);
                readless.setVisibility(View.VISIBLE);

            }
        });

        readless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                duaEnglish.setMaxLines(8);
                readMore.setVisibility(View.VISIBLE);
                readless.setVisibility(View.GONE);

            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fvrttext = cursor.getString(5);
                String fvrttitle = cursor.getString(6);

                dbHelper.savedua(postid, fvrttitle, fvrttext, dbHelper);
                Toast.makeText(getApplicationContext(), "Dua Saved", Toast.LENGTH_LONG).show();
                UnsaveImage.setVisibility(View.VISIBLE);
                saveImage.setVisibility(View.GONE);

            }
        });

        UnsaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.Unsavedua(postid, dbHelper);
                Toast.makeText(getApplicationContext(), "Dua Un-Saved", Toast.LENGTH_LONG).show();
                UnsaveImage.setVisibility(View.GONE);
                saveImage.setVisibility(View.VISIBLE);

            }
        });


       /* for (String ch : sleepingDua)
        {
            sleeping_list.add(ch);
        }

        for (String ch1 : mosqueDua)
        {
            mosqueList.add(ch1);
        }

        for (String ch2 : toilet)
        {
            toiletList.add(ch2);
        }

        for (String ch : sleepingDua_eng)
        {
            sleeping_list_eng.add(ch);
        }

        for (String ch1 : mosqueDua_eng)
        {
            mosqueList_eng.add(ch1);
        }

        for (String ch2 : toilet_eng)
        {
            toiletList_eng.add(ch2);
        }

        if(heading == 0)
        {
            duaArbi.setText(sleeping_list.get(child));
            duaEnglish.setText(sleeping_list_eng.get(child));
        }
        else if(heading == 1)
        {
            duaArbi.setText(toiletList.get(child));
            duaEnglish.setText(toiletList_eng.get(child));
        }
        else if(heading == 2)
        {
            duaArbi.setText(mosqueList.get(child));
            duaEnglish.setText(mosqueList_eng.get(child));
        }*/

        duaBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareIntent();

            }
        });
    }

    private void shareIntent() {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Dua");
        share.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(cursor.getString(5))+ "\n \n Download Imamia jantri 2019 (https://play.google.com/store/apps/details?id=com.namobiletech.imamiajantri&hl=en)");

        startActivity(Intent.createChooser(share, "Share Dua!"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Dua Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
