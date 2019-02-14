package com.namobiletech.imamiajantri.UI.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.MainHelpers.GMailSender;

import java.util.Random;

public class  forgetPasswordScreen extends AppCompatActivity {

    EditText etRecipient;
    TextView forg_pass;
    TextView forg_pass_instructions;
    Button btnSend;

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_screen);

        Typeface open_Sans_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular.ttf");

        etRecipient = (EditText)findViewById(R.id.etRecipient);
        forg_pass = (TextView) findViewById(R.id.forg_pas_tv);
        forg_pass_instructions = (TextView) findViewById(R.id.instructions_tv);

        btnSend = (Button) findViewById(R.id.btnSend);

        etRecipient.setTypeface(open_Sans_font);
        forg_pass.setTypeface(open_Sans_font);
        forg_pass_instructions.setTypeface(open_Sans_font);
        btnSend.setTypeface(open_Sans_font);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidEmail(etRecipient.getText().toString())) {

                    if(isOnline()) {
                        sendMessage();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter valid email Address", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendMessage() {

        Random r = new Random();

        code = String.format("%04d", r.nextInt(10000));
        final ProgressDialog dialog = new ProgressDialog(forgetPasswordScreen.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        GMailSender sender = new GMailSender("imamiajantriofficial@gmail.com", "imamia_jantri");
                    sender.sendMail("Recover Password",
                            "Your Confirmation code is " + code,
                            "imamiajantriofficial@gmail.com",
                            etRecipient.getText().toString());
                    dialog.dismiss();

                   Intent intent = new Intent(forgetPasswordScreen.this, verifyCode.class);
                   intent.putExtra("code", code);
                   intent.putExtra("email", etRecipient.getText().toString() );
                   startActivity(intent);
                   finish();

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                    Toast.makeText(forgetPasswordScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        sender.start();
    }

    public static boolean isValidEmail(String target)
    {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
