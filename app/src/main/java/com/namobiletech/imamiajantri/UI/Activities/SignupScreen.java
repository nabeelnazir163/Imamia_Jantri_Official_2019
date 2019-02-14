package com.namobiletech.imamiajantri.UI.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.AnalyticsApplication;
import com.namobiletech.imamiajantri.Utils.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupScreen extends AppCompatActivity {

    //WIDGETS
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNo;
    private EditText email;
    private EditText Password;
    private EditText c_password;

    private static TextView dateOfBirth;
    private TextView note_tv;

    private Spinner Gender_spinner;

    private Button signupBtn;

    private CheckBox agree_cb;

    private ImageView backImage;

    private boolean cb_status;

    //Volley
    RequestQueue requestQueue;
    StringRequest request;

    String FEED_URL = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/User_registration.php";

    //user session referecne
    UserSessionManager sessionManager;

    //shared_pref file name
    private static final String LOGIN_PREFERNCE_NAME = "LoginPref";

    //Shared Preference mode
    int PRIVATE_MODE = 0;

    //Editor reference for Shared preference
    SharedPreferences.Editor userlogin_editor;

    //Shared Preference
    SharedPreferences user_login_pref;

    ProgressDialog loginDialog;

    DatePicker picker;
    Typeface open_Sans_font;

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        firstName = (EditText) findViewById(R.id.firstname_et_SU);
        lastName = (EditText) findViewById(R.id.lastname_et_SU);
        phoneNo = (EditText) findViewById(R.id.phoneNumber_et_SU);
        email = (EditText) findViewById(R.id.email_et_SU);
        Password = (EditText) findViewById(R.id.password_et_SU);
        c_password = (EditText) findViewById(R.id.c_Pass_et_SU);

        dateOfBirth = (TextView) findViewById(R.id.DOB_tv_SU);

        Gender_spinner = (Spinner) findViewById(R.id.gender_spinner_SU);

        signupBtn = (Button) findViewById(R.id.signpBtn_SU);

        agree_cb = (CheckBox) findViewById(R.id.checkbox_SU);

        backImage = (ImageView) findViewById(R.id.backarraow_iv_SU);

        open_Sans_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular.ttf");
        firstName.setTypeface(open_Sans_font);
        lastName.setTypeface(open_Sans_font);
        phoneNo.setTypeface(open_Sans_font);
        email.setTypeface(open_Sans_font);
        Password.setTypeface(open_Sans_font);
        c_password.setTypeface(open_Sans_font);
        dateOfBirth.setTypeface(open_Sans_font);
        signupBtn.setTypeface(open_Sans_font);
        agree_cb.setTypeface(open_Sans_font);

        requestQueue = Volley.newRequestQueue(this);

        loginDialog = new ProgressDialog(SignupScreen.this);
        loginDialog.setCancelable(false);
        loginDialog.setMessage("Registering");
        loginDialog.setCanceledOnTouchOutside(false);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(SignupScreen.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Gender_spinner.setAdapter(genderAdapter);

        //referencing UserSessionMangment
        sessionManager = new UserSessionManager(SignupScreen.this);

        //Shared Pref for creating session
        user_login_pref = getSharedPreferences(LOGIN_PREFERNCE_NAME, PRIVATE_MODE);

        userlogin_editor = user_login_pref.edit();

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                DialogFragment newFragment = new SelectDateFragmentTrans();
//                newFragment.show(getFragmentManager(), "DatePicker");
                final Dialog dialog = new Dialog(SignupScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.datepicker_dialog);

                Button cancelBtn = (Button) dialog.findViewById(R.id.datePicker_cancel);
                Button okBtn = (Button) dialog.findViewById(R.id.datePicker_ok);
                note_tv = (TextView) dialog.findViewById(R.id.note_tv);
                note_tv.setTypeface(open_Sans_font);

                picker = (DatePicker) dialog.findViewById(R.id.datePicker1);

                dialog.show();

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dateOfBirth.setText(picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
                        dialog.dismiss();

                    }
                });

                Calendar calendar = Calendar.getInstance();
                int Year = calendar.get(Calendar.YEAR);

                note_tv.setText("Click " + String.valueOf(Year) + " to select year");

            }
        });

        agree_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    cb_status = true;
                } else {
                    cb_status = false;
                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignupScreen.this, SigninScreen.class));
                finish();

            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!firstName.getText().toString().equals("")) {
                    if (!lastName.getText().toString().equals("")) {

                        if (!phoneNo.getText().toString().equals("")) {
                            if (!email.getText().toString().equals("")) {
                                if (isValidEmail(email.getText().toString())) {
                                    if (!Password.getText().toString().equals("")) {
                                        if (Password.getText().length() > 5) {
                                            if (!c_password.getText().toString().equals("")) {
                                                if (Password.getText().toString().equals(c_password.getText().toString())) {
                                                    if (cb_status) {

                                                        Register();

                                                    } else {
                                                        Toast.makeText(SignupScreen.this, "Agree to our policies before proceeding", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(SignupScreen.this, "Password Mismatches", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SignupScreen.this, "Please Confirm your password", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(SignupScreen.this, "Password Length Must be more than 6 characters", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(SignupScreen.this, "Enter Password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SignupScreen.this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignupScreen.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupScreen.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SignupScreen.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupScreen.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void Register() {

        loginDialog.show();

        request = new StringRequest(Request.Method.POST, FEED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("message")) {
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        AppEventsLogger logger = AppEventsLogger.newLogger(SignupScreen.this);
                        logger.logEvent(AppEventsConstants.EVENT_PARAM_SOURCE_APPLICATION);

                        loginDialog.dismiss();
                        Toast.makeText(SignupScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                       /* Intent i = new Intent(SignupScreen.this, MainActivity.class);

                        //Closing all the activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        //Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

                        /* creating user login session*/

                        sessionManager.CreateUSerLoginSession(email.getText().toString(), Password.getText().toString(), firstName.getText().toString() + " " + lastName.getText().toString());

                        String user_name = firstName.getText().toString() + " " + lastName.getText().toString();
                        String user_email = email.getText().toString();
                        String user_id = jsonObject.getString("user_id");

                        userlogin_editor.putString("username", user_name).commit();
                        userlogin_editor.putString("email", user_email).commit();
                        userlogin_editor.putString("user_id", user_id).commit();

                        //start Login Activity
//                        startActivity(i);
                        finish();

                    } else if (jsonObject.names().get(0).equals("exists")) {
                        loginDialog.dismiss();
                        Toast.makeText(SignupScreen.this, jsonObject.getString("exists"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.names().get(0).equals("error")) {
                        loginDialog.dismiss();
                        Toast.makeText(SignupScreen.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loginDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage() + "a", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                loginDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Can not establish a connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", email.getText().toString());
                hashMap.put("password", Password.getText().toString());
                hashMap.put("first_name", firstName.getText().toString());
                hashMap.put("last_name", lastName.getText().toString());
                hashMap.put("gender", Gender_spinner.getSelectedItem().toString());
                hashMap.put("contact_number", phoneNo.getText().toString());
                hashMap.put("dob", dateOfBirth.getText().toString());
                hashMap.put("confirm_password", c_password.getText().toString());

                return hashMap;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
        requestQueue.getCache().clear();

    }

    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupScreen.this, SigninScreen.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Signup Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
