package com.namobiletech.imamiajantri.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.namobiletech.imamiajantri.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrayerSharePreview extends AppCompatActivity {


    //TextViews for Prayer time
    private TextView todayDate;
    private TextView Fajr;
    private TextView Dhuhr;
    private TextView Asr;
    private TextView Maghrib;
    private TextView Isha;
    private TextView sunRise;
    private TextView imsak;

    //Imageviews of prayer time
    private ImageView shareImageView;
    private ImageView backimageview;

    //Namaz Strings
    private String FajrTime;
    private String DhuhrTime;
    private String AsrTime;
    private String MaghribTime;
    private String IshaTime;
    private String SunriseTime;
    private String imsakTime;

    Intent intent;

    File new_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_share_preview);

        initWidgets();

        intent = getIntent();

        gettingStringIntent();
    }

    private void initWidgets()
    {
        Fajr = (TextView) findViewById(R.id.FajrTime_sharePreview);
        Dhuhr = (TextView) findViewById(R.id.dhuhrTime_sharePreview);
        Asr = (TextView) findViewById(R.id.asrTime_sharePreview);
        Maghrib = (TextView) findViewById(R.id.maghribTime_sharePreview);
        Isha = (TextView) findViewById(R.id.ishaTime_sharePreview);
        sunRise = (TextView) findViewById(R.id.sunriseTime_sharePreview);
        imsak = (TextView) findViewById(R.id.imsakTime_sharePreview);
        todayDate = (TextView) findViewById(R.id.todayDate_sharePreview);

        shareImageView = (ImageView) findViewById(R.id.shareImage_preview_iv);
        backimageview = (ImageView) findViewById(R.id.backarraow_iv);

        backimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Bitmap bitmap = takeScreenshot();
                StartSave(bitmap);
            }
        });
    }

    public void gettingStringIntent()
    {
        FajrTime = intent.getStringExtra("Fajr");
        DhuhrTime = intent.getStringExtra("Dhuhr");
        AsrTime = intent.getStringExtra("Asr");
        MaghribTime = intent.getStringExtra("Maghrib");
        IshaTime = intent.getStringExtra("Isha");
        SunriseTime = intent.getStringExtra("Sunrise");
        imsakTime = intent.getStringExtra("Imsak");

        settingValues();
    }

    private void settingValues()
    {
        Fajr.setText(FajrTime);
        Dhuhr.setText(DhuhrTime);
        Asr.setText(AsrTime);
        Maghrib.setText(MaghribTime);
        Isha.setText(IshaTime);
        sunRise.setText(SunriseTime);
        imsak.setText(imsakTime);
    }

    public Bitmap takeScreenshot()
    {
        LinearLayout ll = findViewById(R.id.abcdee);
        ll.setDrawingCacheEnabled(true);
        ll.buildDrawingCache(true);
        Bitmap cs = Bitmap.createBitmap(ll.getDrawingCache());
        ll.setDrawingCacheEnabled(false);

        return cs;
    }

    private void StartSave(Bitmap bitmap)
    {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if(!file.exists() && !file.mkdirs())
        {
            Toast.makeText(PrayerSharePreview.this, "Can't create directory to save image", Toast.LENGTH_SHORT).show();
        } else {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");

            String date = simpleDateFormat.format(new Date());

            String name = "Img" + date + ".jpg";

            String filename = file.getAbsolutePath() + "/" + name;

            new_file = new File(filename);

            try {
                fileOutputStream = new FileOutputStream(new_file);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            refreshGallery(new_file);
            shareIt();
        }
    }

    private void refreshGallery(File new_file)
    {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new_file));
        sendBroadcast(intent);
    }

    private File getDisc()
    {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        return new File(file, "Imamia Jantri");
    }

    private void shareIt()
    {
        Uri uri = Uri.fromFile(new_file);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "Download Imamia Jantri from play store ... !";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Tweecher score");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
