// This activity is used to display the content in about us page
package com.example.quicklogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
    Button about_ok;
    TextView about1;
    TextView about2;
    TextView about3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        about1= findViewById(R.id.about1);
        about2= findViewById(R.id.about2);
        about3= findViewById(R.id.about3);
        about_ok = (Button)findViewById(R.id.about_ok);
        // on clicking the ok button
        about_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });

    }
}