// login page activity
package com.example.quicklogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    // Define the view buttons and Edittext
    private Button m_btnLogin;
    private Button m_btnSignup;
    private EditText mName;
    private EditText mpass;
    Context context;
    Database db;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Password = "passKey";

    @Override
    //launch the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//load the layout
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        db = new Database(this);
//initializing the view objects
        m_btnLogin = findViewById(R.id.btnLogin);
        m_btnSignup = findViewById(R.id.btnSignup);
        mName = findViewById(R.id.txtUsername);
        mpass = findViewById(R.id.txtPassword);
        // On clicking the login button
        m_btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String luname = "";
                String lpassword = "";
                //convert the form data into string and save it
                luname = mName.getText().toString();
                lpassword = mpass.getText().toString();
                Boolean res = db.checkUser(luname,lpassword);
                if(res == true)
                {

                    Intent HomePage = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(HomePage);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        m_btnSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //On clicking the signup button, move to the signup page
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}