package com.namobiletech.imamiajantri.UI.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.namobiletech.imamiajantri.R;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

public class verifyCode extends AppCompatActivity {

    CodeInputView codeInputView;
    String codeprevious;
    String emailprevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        Intent intent = getIntent();
        codeprevious = intent.getStringExtra("code");
        emailprevious = intent.getStringExtra("email");

        Typeface open_Sans_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular.ttf");
        TextView confirmPass_instructions = (TextView) findViewById(R.id.confirm_Code_instructions);

        confirmPass_instructions.setTypeface(open_Sans_font);

        codeInputView = (CodeInputView) findViewById(R.id.confirmation_code);

        codeInputView.addOnCompleteListener(new OnCodeCompleteListener() {
            @Override
            public void onCompleted(String code) {
                if (code.equals(codeprevious))
                {
                    Intent intent = new Intent(verifyCode.this, RecreatePassword.class);
                    intent.putExtra("email", emailprevious);
                    startActivity(intent);
                    finish();
                }
                else {
                    codeInputView.setCode("");
                    codeInputView.setEditable(true);
                    codeInputView.setError("Code Didn't Match");
                }
            }
        });

 }
}
