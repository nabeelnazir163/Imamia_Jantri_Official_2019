package com.namobiletech.imamiajantri.UI.Activities;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.AnalyticsApplication;
import com.namobiletech.imamiajantri.Utils.UserSessionManager;

public class ReadJantri extends AppCompatActivity {

    //WIDGETS
    //EditTexts
    private EditText search_edittext;

    //TextViews
    private TextView next_Tv;
    private TextView previous_tv;
    private TextView zoomInOut;

    //ImageViews
    private ImageView backimage;
    private ImageView searchImage;
    private TextView shareImage;

    //Seekbar
    private SeekBar seekBar;

    //PDFView
    private PDFView pdfView;

    private boolean isshow = false;

    private LinearLayout nextPrev;
    private LinearLayout shareZoom;

    /*AdView adView;
    InterstitialAd mInterstitialAd;*/

    //user session referecne
    UserSessionManager sessionManager;

    Tracker mTracker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_jantri);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        search_edittext = (EditText) findViewById(R.id.searchEditText_readJantri);

        next_Tv = (TextView) findViewById(R.id.next_tv_readJantri);
        previous_tv = (TextView) findViewById(R.id.previous_tv_readJantri);
        zoomInOut = (TextView) findViewById(R.id.zoominOut_tv);

        backimage = (ImageView) findViewById(R.id.backImage_readJantri);
        searchImage = (ImageView) findViewById(R.id.searchImage_readJantri);
        shareImage = (TextView) findViewById(R.id.shareIcon_readJantri);

        seekBar = (SeekBar) findViewById(R.id.seekbar);

        pdfView = (PDFView) findViewById(R.id.pdfView);

        nextPrev = (LinearLayout) findViewById(R.id.nextPrev_lL);
        shareZoom = (LinearLayout) findViewById(R.id.sharezoomLL);

        seekBar.setMax(10);
        pdfView.setMaxZoom(10.0f);
        pdfView.setMinZoom(1.0f);

        int mWidth= this.getResources().getDisplayMetrics().widthPixels;
        int mHeight= this.getResources().getDisplayMetrics().heightPixels;
        final PointF backgroundPosition = new PointF(mWidth/2, mHeight/2 - 200 );

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                float a = (float) i;
                if(a >= 1.0f) {
                    pdfView.zoomCenteredTo(a, backgroundPosition );
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        search_edittext.setCursorVisible(false);

        //Click Listeners
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(search_edittext.getText().toString() != null) {
                    int jumpTo = Integer.valueOf(search_edittext.getText().toString());

                    int total = pdfView.getPageCount();

                    if (jumpTo <= total) {
                        pdfView.jumpTo(jumpTo - 1, true);
                    } else {
                        Toast.makeText(ReadJantri.this, "Page # " + String.valueOf(jumpTo) + " not Exists", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter number to Search", Toast.LENGTH_LONG).show();
                }

            }
        });

        next_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfView.jumpTo(pdfView.getCurrentPage() + 1, true);
                }
        });


        previous_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfView.jumpTo(pdfView.getCurrentPage() - 1, true);
            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareIntent();

            }
        });

        zoomInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isshow) {
                    seekBar.setVisibility(View.GONE);
                    isshow = false;
                }
                else {
                    seekBar.setVisibility(View.VISIBLE);
                    isshow = true;
                }
            }
        });

        sessionManager = new UserSessionManager(this);

        if (!sessionManager.CheckLogin()) {

            startActivityForResult(new Intent(this, SigninScreen.class), 1);
//            finish();
        }

        pdfView.recycle();

        pdfView.fromAsset("imamiajantri2019.pdf")
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(0)
                .load();
    }

    /*private void displayInterstitialAd() {
        if(mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
    }*/

    private void shareIntent() {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Imamia Jantri Official");
        share.putExtra(Intent.EXTRA_TEXT, "Download Imamia jantri 2019 (https://play.google.com/store/apps/details?id=com.namobiletech.imamiajantri&hl=en)");

        startActivity(Intent.createChooser(share, "Share link!"));

    }

    @Override
    protected void onResume() {



        mTracker.setScreenName("Image~" + "Read Jantri Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                boolean check = data.getBooleanExtra("checkback", true);
                if(check)
                {
                    finish();
                }
            }
            else {
                ViewGroup group = (ViewGroup) pdfView.getParent();
                group.removeView(pdfView);
                pdfView = null;
                pdfView = new PDFView(this, null);
                pdfView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                group.addView(pdfView);

                pdfView.recycle();

                pdfView.fromAsset("imamiajantri2019.pdf")
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .enableAnnotationRendering(false)
                        .scrollHandle(null)
                        .enableAntialiasing(true)
                        .spacing(0)
                        .load();
            }
        }
    }
}
