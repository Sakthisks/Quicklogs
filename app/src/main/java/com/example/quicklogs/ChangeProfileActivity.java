//This activity is used to change the profile
package com.example.quicklogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class ChangeProfileActivity extends AppCompatActivity {
    Database db;
    private EditText editTextName, editTextEmail, editTextMobile;
    private Button s_buttonSubmit;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    //Launch the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        //load the layout
        setContentView(R.layout.activity_change_profile);
        db=new Database(this);
        //initializing awesomevalidation object
        /*
         * The library provides 3 types of validation
         * BASIC
         * COLORATION
         * UNDERLABEL
         * */
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //initializing view objects
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        s_buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editTextMobile, "^[1-9]{2}[0-9]{8}$", R.string.mobileerror);

        // On clicking the sign me up! button
        s_buttonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    public void submitForm() {
        //first validate the form then move ahead
        if (awesomeValidation.validate()) {
            String name;
            String mobile;
            String email;

            //convert the form data into string and save
            name = editTextName.getText().toString();
            mobile = editTextMobile.getText().toString();
            email = editTextEmail.getText().toString();
            //updating the existing values of the profile
            long val = db.updateUser(name,email,mobile);
            //if the query is successful
            if(val > 0){
                Toast.makeText(ChangeProfileActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                Intent moveToLogin = new Intent(ChangeProfileActivity.this,SettingsActivity.class);
                startActivity(moveToLogin);
            }
            //if the query fails
            else{
                Toast.makeText(ChangeProfileActivity.this,"Error while updating the profile",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
