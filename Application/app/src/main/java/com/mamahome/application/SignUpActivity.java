package com.mamahome.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    EditText et_Name;
    EditText et_Email;
    EditText et_Phone;
    Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_Name = (EditText)findViewById(R.id.et_name);
        et_Email = (EditText) findViewById(R.id.et_email);
        et_Phone = (EditText) findViewById(R.id.et_phoneNumber);

        btn_signUp = (Button) findViewById(R.id.btn_signup);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = et_Name.getText().toString();
                String Email = et_Email.getText().toString();
                String Phone = et_Phone.getText().toString();


                if (TextUtils.isEmpty(Name)) {
                    et_Name.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Phone)) {
                    et_Phone.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Email)) {
                    et_Email.setError(getString(R.string.cannot_empty));
                    return;
                }

                new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("Signed Up Successful")
                        .setMessage("Welcome to Mama Home! \n You will shortly receive a call from our Executive.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setCancelable(false)
                        .show();
            }
        });

    }
}
