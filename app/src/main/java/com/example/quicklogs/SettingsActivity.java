// settings page
package com.example.quicklogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Button ContactUs;
    private Button AboutUs;
    private Button ChangePassword;
    private Button ChangeProfile;
    private Button Help;
    private Button Home;
    Context context;

    @Override
    //launch the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//load the layout
        setContentView(R.layout.settings_activity);
        context = getApplicationContext();
//initializing the view objects
        ContactUs = (Button)findViewById(R.id.ContactUs);
        AboutUs = (Button)findViewById(R.id.AboutUs);
        ChangePassword = (Button)findViewById(R.id.ChangePassword);
        ChangeProfile = (Button)findViewById(R.id.ChangeProfile);
        Help = (Button)findViewById(R.id.Help);
        Home = (Button)findViewById(R.id.home);

        ContactUs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        AboutUs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        ChangeProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangeProfileActivity.class);
                startActivity(intent);
            }
        });
        Help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

