package com.namobiletech.imamiajantri.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.namobiletech.imamiajantri.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecreatePassword extends AppCompatActivity {

    EditText password;
    EditText confirm_password;
    
    Button changePassBtn;

    //Volley
    RequestQueue requestQueue;
    StringRequest request;

    String FEED_URL = "https://www.imamiajantri.com/imamia_jantri/Imamiajantri/Reset_password.php";

    String email;

    ProgressDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreate_password);

        requestQueue = Volley.newRequestQueue(this);

        Typeface open_Sans_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular.ttf");

        Intent intent = getIntent();
        email= intent.getStringExtra("email");
        
        password = (EditText) findViewById(R.id.password_et_rec_pass);
        confirm_password = (EditText) findViewById(R.id.c_Pass_et_rec_pass);
        
        changePassBtn = (Button) findViewById(R.id.changepass_rec_pass);
        
        password.setTypeface(open_Sans_font);
        confirm_password.setTypeface(open_Sans_font);
        changePassBtn.setTypeface(open_Sans_font);

        loginDialog = new ProgressDialog(RecreatePassword.this);
        loginDialog.setCancelable(false);
        loginDialog.setTitle("Changing Password");
        loginDialog.setMessage("Please wait");
        loginDialog.setCanceledOnTouchOutside(false);
        
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getText().length() > 5) {

                    if ((password.getText().toString()).equals(confirm_password.getText().toString())) {
                        changePassword();
                    } else {
                        Toast.makeText(RecreatePassword.this, "Password Mismatches", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RecreatePassword.this, "Password Length Must be more than 6 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword() {

        loginDialog.show();
        request = new StringRequest(Request.Method.POST, FEED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.names().get(0).equals("success"))
                    {
                        loginDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(jsonObject.names().get(0).equals("error"))
                    {
                        loginDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    loginDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                loginDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", email);
                hashMap.put("password", password.getText().toString());
                return hashMap;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy( 60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.getCache().clear();
        requestQueue.add(request);
    }
}
