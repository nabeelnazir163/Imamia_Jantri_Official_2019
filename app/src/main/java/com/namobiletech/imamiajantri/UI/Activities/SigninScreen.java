package com.namobiletech.imamiajantri.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.AnalyticsApplication;
import com.namobiletech.imamiajantri.Utils.UserSessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SigninScreen extends AppCompatActivity {

    //WIDGETS
    private EditText email;
    private EditText password;

    private Button register_tv;
    private TextView forgetPwd_tv;

    private Button loginBtn;

    //FaceBook Login
    LoginButton fbLoginBtn;
    CallbackManager callbackManager;

    //Volley
    RequestQueue requestQueue;
    StringRequest request;

    String FEED_URL = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/User_login.php";
    String FEED_URL_SIGNUP = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/User_registration.php";

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

    Tracker mTracker;

    SignInButton signInButton;
    private final static int RC_SIGN_IN = 2;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        hideSoftKeyboard();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        FacebookSdk.sdkInitialize(SigninScreen.this);

        email = (EditText) findViewById(R.id.email_et_SI);
        password = (EditText) findViewById(R.id.password_et_SI);

        loginBtn = (Button) findViewById(R.id.continueBtn_SI);
        register_tv = (Button) findViewById(R.id.register_tv_SI);

        forgetPwd_tv = (TextView) findViewById(R.id.forgetPwd_signinScreen);

        fbLoginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String userId = loginResult.getAccessToken().getUserId();

                LoginManager.getInstance().logOut();

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        displayUserInfo(object);

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id, gender, birthday" );
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        Typeface open_Sans_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular.ttf");
        email.setTypeface(open_Sans_font);
        password.setTypeface(open_Sans_font);
        loginBtn.setTypeface(open_Sans_font);
        register_tv.setTypeface(open_Sans_font);
        forgetPwd_tv.setTypeface(open_Sans_font);

        forgetPwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SigninScreen.this, forgetPasswordScreen.class));

            }
        });
        requestQueue = Volley.newRequestQueue(this);

        loginDialog = new ProgressDialog(SigninScreen.this);
        loginDialog.setCancelable(false);
        loginDialog.setMessage("Please wait");
        loginDialog.setCanceledOnTouchOutside(false);

        //referencing UserSessionMangment
        sessionManager = new UserSessionManager(SigninScreen.this);

        //For shared Preferences
        user_login_pref = getSharedPreferences(LOGIN_PREFERNCE_NAME, PRIVATE_MODE);

        userlogin_editor = user_login_pref.edit();

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SigninScreen.this, SignupScreen.class));
                finish();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!email.getText().toString().equals(""))
                {
                    if(isValidEmail(email.getText().toString()))
                    {
                        if(!password.getText().toString().equals(""))
                        {
                            SignInStart( email.getText().toString(), password.getText().toString());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_LONG).show();
                }

            }
        });

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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Signin Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    private void displayUserInfo(JSONObject object) {

        final String fb_firstname;
        final String fb_lastname;
        final String fb_email;
        String id;

        try
        {
            fb_firstname = object.getString("first_name");
            fb_lastname = object.getString("last_name");
            fb_email = object.getString("email");
            id = object.getString("id");

            loginDialog.show();

            request = new StringRequest(Request.Method.POST, FEED_URL_SIGNUP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.names().get(0).equals("message"))
                        {

//                            Register(fb_email, fb_firstname, fb_lastname);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            loginDialog.dismiss();
//                        Toast.makeText(SigninScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            /*Intent i = new Intent(SigninScreen.this, MainActivity.class);

                            //Closing all the activities
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            //Add new Flag to start new Activity
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

                            /* creating user login session*/

                            sessionManager.CreateUSerLoginSession(fb_email, "", fb_firstname + " " + fb_lastname);

                            String user_name = fb_firstname + " " + fb_lastname;
                            String user_email = fb_email;
                            String user_id = jsonObject.getString("user_id");

                            userlogin_editor.putString("username", user_name).commit();
                            userlogin_editor.putString("email", user_email).commit();
                            userlogin_editor.putString("user_id", user_id).commit();

                            //start Login Activity
//                            startActivity(i);
                            finish();
                        }
                        else if (jsonObject.names().get(0).equals("exists"))
                        {
                            Log.e("abc", "sigg");
                            SignInStart(fb_email, "");
                        }
                        else if (jsonObject.names().get(0).equals("error"))
                        {
                            Log.e("abc", "error");
                            loginDialog.dismiss();
                            Toast.makeText(SigninScreen.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginDialog.dismiss();

                        Log.e("abc", e.toString());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    loginDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Can not establish a connection", Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", fb_email);
                    hashMap.put("password", "");
                    hashMap.put("first_name", fb_firstname);
                    hashMap.put("last_name", fb_lastname);
                    hashMap.put("gender", "");
                    hashMap.put("contact_number", "");
                    hashMap.put("dob", "");
                    hashMap.put("confirm_password", "");

                    return hashMap;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy( 60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(request);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(SigninScreen.this, SignupScreen.class));
        }

    }

    private void SignInStart(final String email, final String password) {

        Log.e("email", email);
        Log.e("pwd", password);
        loginDialog.show();

        request = new StringRequest(Request.Method.POST, FEED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("def", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(!jsonObject.names().get(0).equals("error"))
                    {
                        loginDialog.dismiss();
                        String useremail = jsonObject.getString("email");
                        String name = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");
                        String user_id = jsonObject.getString("user_id");

                        userlogin_editor.putString("username", name).commit();
                        userlogin_editor.putString("email", useremail).commit();
                        userlogin_editor.putString("user_id", user_id).commit();

                        //  creating user login session

                        sessionManager.CreateUSerLoginSession(email, password, name);

//                        startActivity(new Intent(SigninScreen.this, MainActivity.class));
                        finish();
                    }
                    else
                    {
                        loginDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loginDialog.dismiss();
                    Log.e("abc", e.toString());
                    Toast.makeText(SigninScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginDialog.dismiss();
//                Toast.makeText(SigninScreen.this,error.toString(), Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    // Is thrown if there's no network connection or server is down
                    Toast.makeText(SigninScreen.this, getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }

                } else {
                    // Is thrown if there's no network connection or server is down
                    Toast.makeText(SigninScreen.this, getString(R.string.error_network),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", email);
                hashMap.put("password", password);

                return hashMap;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
        requestQueue.getCache().clear();


    }

    public static boolean isValidEmail(String target)
    {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("checkback",true);
        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();

//                Toast.makeText(getApplicationContext(), account.getEmail(), Toast.LENGTH_LONG).show();
                updateUIGoogle(account.getEmail(), account.getDisplayName());
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Unable to Signin", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void updateUIGoogle(final String g_email, final String g_name) {

        request = new StringRequest(Request.Method.POST, FEED_URL_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.names().get(0).equals("message"))
                    {
//                        RegisterviaGoogle(g_email, g_name );
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        loginDialog.dismiss();
//                        Toast.makeText(SigninScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        /*Intent i = new Intent(SigninScreen.this, MainActivity.class);

                        //Closing all the activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        //Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

                        /* creating user login session*/

                        sessionManager.CreateUSerLoginSession(g_email, "", g_name);

                        String user_name = g_name;
                        String user_email = g_email;
                        String user_id = jsonObject.getString("user_id");

                        userlogin_editor.putString("username", user_name).commit();
                        userlogin_editor.putString("email", user_email).commit();
                        userlogin_editor.putString("user_id", user_id).commit();

                        //start Login Activity
//                        startActivity(i);
                        finish();
                    }
                    else if (jsonObject.names().get(0).equals("exists"))
                    {
                        SignInStart(g_email, "");
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Log.e("abc", "error");
                        Toast.makeText(SigninScreen.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loginDialog.dismiss();

                    Log.e("abc", e.toString());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                loginDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", g_email);
                hashMap.put("password", "");
                hashMap.put("first_name", g_name);
                hashMap.put("last_name", "");
                hashMap.put("gender", "");
                hashMap.put("contact_number", "");
                hashMap.put("dob", "");
                hashMap.put("confirm_password", "");

                return hashMap;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy( 60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
        requestQueue.getCache().clear();

    }

//    private void Register(final String fb_email, final String fb_firstname, final String fb_lastname) {
//
//        loginDialog.show();
//
//        request = new StringRequest(Request.Method.POST, FEED_URL_SIGNUP, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    if(jsonObject.names().get(0).equals("message"))
//                    {
//                        loginDialog.dismiss();
////                        Toast.makeText(SigninScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                        Intent i = new Intent(SigninScreen.this, MainActivity.class);
//
//                        //Closing all the activities
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                        //Add new Flag to start new Activity
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        /* creating user login session*/
//
//                        sessionManager.CreateUSerLoginSession(fb_email, "", fb_firstname + " " + fb_lastname);
//
//                        String user_name = fb_firstname + " " + fb_lastname;
//                        String user_email = fb_email;
//
//                        userlogin_editor.putString("username", user_name).commit();
//                        userlogin_editor.putString("email", user_email).commit();
//
//                        //start Login Activity
//                        startActivity(i);
//                        finish();
//
//                    }
//                    else if (jsonObject.names().get(0).equals("exists"))
//                    {
//                        loginDialog.dismiss();
//                        Toast.makeText(SigninScreen.this, jsonObject.getString("exists"), Toast.LENGTH_SHORT).show();
//                    }
//                    else if (jsonObject.names().get(0).equals("error"))
//                    {
//                        loginDialog.dismiss();
//                        Toast.makeText(SigninScreen.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    loginDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), e.getMessage() + "a", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError e) {
//                loginDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Can not establish a connection", Toast.LENGTH_SHORT).show();
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("email", fb_email);
//                hashMap.put("password", "");
//                hashMap.put("first_name", fb_firstname);
//                hashMap.put("last_name", fb_lastname);
//                hashMap.put("gender", "");
//                hashMap.put("contact_number", "");
//                hashMap.put("dob", "");
//                hashMap.put("confirm_password", "");
//
//                return hashMap;
//            }
//        };
//
//        request.setRetryPolicy(new DefaultRetryPolicy( 60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        requestQueue.add(request);
//        requestQueue.getCache().clear();
//
//    }
}
