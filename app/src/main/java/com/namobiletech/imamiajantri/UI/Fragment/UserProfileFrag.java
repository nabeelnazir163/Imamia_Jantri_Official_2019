package com.namobiletech.imamiajantri.UI.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.namobiletech.imamiajantri.UI.Activities.SigninScreen;
import com.namobiletech.imamiajantri.UI.Activities.SignupScreen;
import com.namobiletech.imamiajantri.UI.Activities.forgetPasswordScreen;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFrag extends Fragment {

    View v;

    private TextView change_pwd;
    private TextView firstname_tv;
    private TextView lastName_tv;
    private TextView email_tv;
    private TextView number_tv;
    private TextView gender_tv;
    private TextView dob_tv;

    private TextView update_tv;

    private TextView forgotPass_tv;

    private Button update_btn;

    private EditText firstname_et;
    private EditText lastname_et;
    private EditText number_et;
    private EditText old_password_et;
    private EditText new_password_et;
    private EditText confirm_password_et;

    private Spinner Gender_Spinner;

    private LinearLayout changePwd_Layout;

    CircleImageView profile_iv;

    SharedPreferences user_login_pref;

    //shared_pref file name
    private static final String LOGIN_PREFERNCE_NAME = "LoginPref";

    //Shared Preference mode
    int PRIVATE_MODE = 0;

    public static final int PICK_IMAGE = 1;

    Bitmap bitmap;

    //Volley
    RequestQueue requestQueue;
    CacheRequest cacheRequest;
    StringRequest request;

    String email;
    String firstname;
    String lastname;
    String number;
    String gender;
    String password;
    String dob;
    String userImageString;

    String FEED_URL = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/User_data.php";
    String FEED_URL_UPDATE = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/User_update.php";

    DatePicker picker;
    Typeface open_Sans_font;

    private TextView note_tv;

    ProgressDialog progressDialog;

    public UserProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //For shared Preferences
        user_login_pref = getActivity().getSharedPreferences(LOGIN_PREFERNCE_NAME, PRIVATE_MODE);

        if (user_login_pref.getString("user_id", "").equals("")) {
            startActivity(new Intent(getContext(), SigninScreen.class));
            getActivity().finish();
        }

        open_Sans_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans_Regular.ttf");

        profile_iv = (CircleImageView) v.findViewById(R.id.profile_iv_frag);
        profile_iv.setClickable(false);
        profile_iv.setEnabled(false);

        change_pwd = (TextView) v.findViewById(R.id.changePassword_Prof_frag);
        firstname_tv = (TextView) v.findViewById(R.id.firstname_userProf_frag);
        lastName_tv = (TextView) v.findViewById(R.id.lastname_userProf_frag);
        email_tv = (TextView) v.findViewById(R.id.useremail_userProf_Frag);
        number_tv = (TextView) v.findViewById(R.id.con_number_userpro_Frag);
        gender_tv = (TextView) v.findViewById(R.id.gender_userpro_Frag_tv);
        dob_tv = (TextView) v.findViewById(R.id.dob_userpro_Frag_tv);

        firstname_et = (EditText) v.findViewById(R.id.firstname_userProf_frag_et);
        lastname_et = (EditText) v.findViewById(R.id.lastname_userProf_frag_et);
        number_et = (EditText) v.findViewById(R.id.con_number_userpro_Frag_et);
        old_password_et = (EditText) v.findViewById(R.id.old_password_et);
        new_password_et = (EditText) v.findViewById(R.id.new_password_et);
        confirm_password_et = (EditText) v.findViewById(R.id.confirm_new_pass_et);

        Gender_Spinner = (Spinner) v.findViewById(R.id.gender_userpro_Frag_spinner);

        changePwd_Layout = (LinearLayout) v.findViewById(R.id.changepwd_layout);

        update_tv = (TextView) v.findViewById(R.id.update_tv);

        forgotPass_tv = (TextView) v.findViewById(R.id.forget_password_tv_userFrag);

        update_btn = (Button) v.findViewById(R.id.update_btn_pro_frag);

        forgotPass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), forgetPasswordScreen.class));

            }
        });

        change_pwd.setTypeface(open_Sans_font);
        firstname_tv.setTypeface(open_Sans_font);
        lastName_tv.setTypeface(open_Sans_font);
        email_tv.setTypeface(open_Sans_font);
        number_tv.setTypeface(open_Sans_font);
        gender_tv.setTypeface(open_Sans_font);
        dob_tv.setTypeface(open_Sans_font);
        forgotPass_tv.setTypeface(open_Sans_font);
        old_password_et.setTypeface(open_Sans_font);
        new_password_et.setTypeface(open_Sans_font);
        confirm_password_et.setTypeface(open_Sans_font);

        firstname_et.setTypeface(open_Sans_font);
        lastname_et.setTypeface(open_Sans_font);
        number_et.setTypeface(open_Sans_font);

        update_tv.setTypeface(open_Sans_font);
        update_btn.setTypeface(open_Sans_font);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Gender_Spinner.setAdapter(genderAdapter);

        update_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update_tv.setVisibility(View.GONE);

                profile_iv.setClickable(true);
                profile_iv.setEnabled(true);

                firstname_tv.setVisibility(View.GONE);
                lastName_tv.setVisibility(View.GONE);
                number_tv.setVisibility(View.GONE);
                gender_tv.setVisibility(View.GONE);
                dob_tv.setVisibility(View.GONE);

                firstname_et.setVisibility(View.VISIBLE);
                lastname_et.setVisibility(View.VISIBLE);
                number_et.setVisibility(View.VISIBLE);
                Gender_Spinner.setVisibility(View.VISIBLE);
                dob_tv.setVisibility(View.VISIBLE);
                change_pwd.setVisibility(View.VISIBLE);

                update_btn.setVisibility(View.VISIBLE);

                dob_tv.setClickable(true);
                dob_tv.setEnabled(true);

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!firstname_et.getText().toString().equals("")) {

                    if (!lastname_et.getText().toString().equals("")) {

                        if (!number_et.getText().toString().equals("")) {

                            if (!new_password_et.getText().toString().equals("")) {

                                if (old_password_et.getText().toString().equals(password)) {

                                    if (new_password_et.getText().length() > 5) {

                                        if (confirm_password_et.getText().toString().equals(new_password_et.getText().toString())) {

                                            password = new_password_et.getText().toString();

                                            updateProfile();

                                        } else {

                                            Toast.makeText(getContext(), "Password and confirm Password Fields doesnot match", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Password Length Must be more than 6 characters", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                updateProfile();
                            }

                        } else {
                            Toast.makeText(getContext(), "Enter Number", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Enter Last Name", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Enter First Name", Toast.LENGTH_LONG).show();
                }
            }
        });

        loadProfile();

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change_pwd.setVisibility(View.GONE);

                changePwd_Layout.setVisibility(View.VISIBLE);

            }
        });

        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        dob_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext());
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

                        dob_tv.setText(picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());

                        dob = (picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
                        dialog.dismiss();

                    }
                });

                Calendar calendar = Calendar.getInstance();
                int Year = calendar.get(Calendar.YEAR);

                note_tv.setText("Click " + String.valueOf(Year) + " to select year");

            }
        });
        return v;
    }

    private void updateProfile() {

        if(bitmap != null)
        {
            userImageString = imagetoString(bitmap);
        }
        else
        {
            userImageString = "";
        }


        gender = Gender_Spinner.getSelectedItem().toString();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Saving...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        request = new StringRequest(Request.Method.POST, FEED_URL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("message") != null) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        update_tv.setVisibility(View.VISIBLE);

                        firstname_tv.setVisibility(View.VISIBLE);
                        lastName_tv.setVisibility(View.VISIBLE);
                        number_tv.setVisibility(View.VISIBLE);
                        gender_tv.setVisibility(View.VISIBLE);
                        dob_tv.setVisibility(View.VISIBLE);

                        firstname_et.setVisibility(View.GONE);
                        lastname_et.setVisibility(View.GONE);
                        number_et.setVisibility(View.GONE);
                        change_pwd.setVisibility(View.GONE);

                        Gender_Spinner.setVisibility(View.GONE);

                        changePwd_Layout.setVisibility(View.GONE);

                        update_btn.setVisibility(View.GONE);

                        firstname_tv.setText(firstname_et.getText().toString());
                        lastName_tv.setText(lastname_et.getText().toString());
                        number_tv.setText(number_et.getText().toString());
                        gender_tv.setText(gender);
                        dob_tv.setText(dob);

                        firstname_et.setText("");
                        lastname_et.setText("");
                        number_et.setText("");
                        old_password_et.setText("");
                        new_password_et.setText("");
                        confirm_password_et.setText("");

                        dob_tv.setClickable(false);
                        dob_tv.setEnabled(false);


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ("error"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", email_tv.getText().toString());
                hashMap.put("first_name", firstname_et.getText().toString());
                hashMap.put("last_name", lastname_et.getText().toString());
                hashMap.put("gender", gender);
                hashMap.put("dob", dob);
                hashMap.put("user_file", userImageString);
                hashMap.put("contact_number", number_et.getText().toString());
                hashMap.put("user_id", user_login_pref.getString("user_id", ""));
                hashMap.put("password", password);
                return hashMap;
            }
        };

        requestQueue.add(request);

    }

    private void loadProfile() {

        request = new StringRequest(Request.Method.POST, FEED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("email") != null) {
                        email = jsonObject.getString("email");
                    }

                    if (jsonObject.getString("first_name") != null) {
                        firstname = jsonObject.getString("first_name");
                    }

                    if (jsonObject.getString("last_name") != null) {
                        lastname = jsonObject.getString("last_name");
                    }

                    if (jsonObject.getString("contact_number") != null) {
                        number = jsonObject.getString("contact_number");
                    }

                    if (jsonObject.getString("gender") != null) {
                        gender = jsonObject.getString("gender");
                    }
                    if (jsonObject.getString("dob") != null) {
                        dob = jsonObject.getString("dob");
                    }

                    if (jsonObject.getString("password") != null) {
                        password = jsonObject.getString("password");
                    }

                    if (jsonObject.getString("user_file") != null) {
                        userImageString = jsonObject.getString("user_file");
                    }

                    if(!email.equals("")) {
                        email_tv.setText(email);
                    }

                    if(!firstname.equals("")) {
                        firstname_tv.setText(firstname);
                    }

                    if(!lastname.equals("")){
                    lastName_tv.setText(lastname);
                    }

                    if(!number.equals("")) {
                        number_tv.setText(number);
                    }
                    if(!gender.equals("")) {
                        gender_tv.setText(gender);
                    }

                    if(!dob.equals("")) {
                        dob_tv.setText(dob);
                    }

                    if(!firstname.equals("")) {
                        firstname_et.setText(firstname);
                    }

                    if(!lastname.equals("")) {
                        lastname_et.setText(lastname);
                    }

                    if(!number.equals("")) {
                        number_et.setText(number);
                    }

                    if(!userImageString.equals("")) {
                        Picasso.with(getContext()).load(userImageString).placeholder(R.drawable.imageplaceholdr).into(profile_iv);
                    }

                    update_tv.setVisibility(View.VISIBLE);

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

                    email = "";
                    firstname = "";
                    lastname = "";
                    number = "";
                    password = "";
                    gender = "";
                    dob = "";

                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONObject jsonObject = new JSONObject(jsonString);

                    if (jsonObject.getString("email") != null) {
                        email = jsonObject.getString("email");

                    }

                    if (jsonObject.getString("first_name") != null) {
                        firstname = jsonObject.getString("first_name");
                    }

                    if (jsonObject.getString("last_name") != null) {
                        lastname = jsonObject.getString("last_name");
                    }

                    if (jsonObject.getString("contact_number") != null) {
                        number = jsonObject.getString("contact_number");
                    }

                    if (jsonObject.getString("gender") != null) {
                        gender = jsonObject.getString("gender");
                    }
                    if (jsonObject.getString("dob") != null) {
                        dob = jsonObject.getString("dob");
                    }

                    if (jsonObject.getString("password") != null) {
                        password = jsonObject.getString("password");
                    }

                    if (jsonObject.getString("user_file") != null) {
                        userImageString = jsonObject.getString("user_file");
                    }
                    /*if (jsonObject.getString("user_file") != null) {
                        String imageuri = jsonObject.getString("user_file");
                        Picasso.with(getContext()).load(imageuri).into(profile_iv);

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(imageuri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/


                    if(!email.equals("")) {
                        email_tv.setText(email);
                    }

                    if(!firstname.equals("")) {
                        firstname_tv.setText(firstname);
                    }

                    if(!lastname.equals("")){
                        lastName_tv.setText(lastname);
                    }

                    if(!number.equals("")) {
                        number_tv.setText(number);
                    }
                    if(!gender.equals("")) {
                        gender_tv.setText(gender);
                    }

                    if(!dob.equals("")) {
                        dob_tv.setText(dob);
                    }

                    if(!firstname.equals("")) {
                        firstname_et.setText(firstname);
                    }

                    if(!lastname.equals("")) {
                        lastname_et.setText(lastname);
                    }

                    if(!number.equals("")) {
                        number_et.setText(number);
                    }

                    if(!userImageString.equals("")) {
                        Picasso.with(getContext()).load(userImageString).placeholder(R.drawable.imageplaceholdr).into(profile_iv);
                    }


                    update_tv.setVisibility(View.VISIBLE);


                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageuri);
                profile_iv.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
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
            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
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
