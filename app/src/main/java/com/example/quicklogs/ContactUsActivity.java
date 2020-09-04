//this activity is used to display the contact us page
package com.example.quicklogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUsActivity extends AppCompatActivity {
    Button contact_ok;
    TextView contact1;
    TextView contact2;
    TextView contact3;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_us);
        contact1= findViewById(R.id.contact1);
        contact2= findViewById(R.id.contact2);
        contact3= findViewById(R.id.contact3);
        contact_ok = (Button)findViewById(R.id.contact_ok);
           //when the ok button is clicked
            contact_ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ContactUsActivity.this, SettingsActivity.class);
                    startActivity(intent);

                }
            });

        }
    }
