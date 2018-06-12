package com.mamahome.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button btn_signup;
    Button btn_login;
    EditText et_username;
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        et_username = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validations
                String emailPhone = et_username.getText().toString();
                String password = et_password.getText().toString();

                if (TextUtils.isEmpty(emailPhone)) {
                    et_username.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    et_password.setError(getString(R.string.cannot_empty));
                    return;
                }
                else if (emailPhone.equals("admin") && password.equals("admin")){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("USER_LOGGED_IN", true);
                    editor.apply();
                    Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(home);
                    finish();
                }
                else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Wrong Credentials")
                            .setMessage("Please check you credentials and try again!")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setCancelable(false)
                            .show();
                }


            }
        });

    }
}
