//the activity that shows up when the help is clicked
package com.example.quicklogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    Button help_ok;
    TextView help1;
    TextView help2;
    TextView help3;
    TextView help4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        help1= findViewById(R.id.help1);
        help2= findViewById(R.id.help2);
        help3= findViewById(R.id.help3);
        help4= findViewById(R.id.help4);
        help_ok = (Button)findViewById(R.id.help_ok);
        //when the ok button is clicked
            help_ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(HelpActivity.this, SettingsActivity.class);
                    startActivity(intent);

                }
            });

        }
    }
