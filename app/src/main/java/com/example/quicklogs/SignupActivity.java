//signup activity
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
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    //The view objects
    Database db;
    private EditText editTextName, editTextEmail, editTextMobile,
            editPassword, editRetype;
    private Button s_buttonSubmit;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    //Launch the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        //load the layout
        setContentView(R.layout.activity_signup);
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
        editPassword = (EditText) findViewById(R.id.editPassword);
        editRetype = (EditText) findViewById(R.id.editRetype);
        s_buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editTextMobile, "^[1-9]{2}[0-9]{8}$", R.string.mobileerror);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passerror);
        // to validate a confirmation field (pass word and retype password)
        awesomeValidation.addValidation(this, R.id.editRetype, R.id.editPassword, R.string.retypeerror);

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
            String password;
            String mobile;
            String email;


            //convert the form data into string and save
            name = editTextName.getText().toString();
            password = editPassword.getText().toString();
            mobile = editTextMobile.getText().toString();
            email = editTextEmail.getText().toString();

            long val = db.addUser(name,password,email,mobile);
            // if the query is successful
            if(val > 0){
                Toast.makeText(SignupActivity.this,"You have registered",Toast.LENGTH_SHORT).show();
                Intent moveToLogin = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(moveToLogin);
            }
            //if the query fails
            else{
                Toast.makeText(SignupActivity.this,"Registeration Error",Toast.LENGTH_SHORT).show();
            }

            }

        }
    }
