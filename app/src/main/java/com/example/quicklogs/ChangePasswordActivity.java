//This activity is used to change the password
package com.example.quicklogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class ChangePasswordActivity extends AppCompatActivity {
    TextView editPassword;
    TextView editoldpassword;
    TextView editRetype;
    Button save;
    Database db;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    //Launch the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        editPassword= findViewById(R.id.editPassword);
        editRetype= findViewById(R.id.editRetype);
        editoldpassword = findViewById(R.id.editoldpassword);
        save = (Button)findViewById(R.id.save);
        db=new Database(this);
            //initializing awesomevalidation object
            /*
             * The library provides 3 types of validation
             * BASIC
             * COLORATION
             * UNDERLABEL
             * */
            awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


            //adding validation to edittexts
            String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

            awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passerror);
            // to validate a confirmation field (pass word and retype password)
            awesomeValidation.addValidation(this, R.id.editRetype, R.id.editPassword, R.string.retypeerror);
            save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    submitForm();
                }
            });
        }

        public void submitForm() {
            //first validate the form then move ahead
            if (awesomeValidation.validate()) {
                String password,oldpassword;
                //convert the form data into string and save
                oldpassword=editoldpassword.getText().toString();
                password = editPassword.getText().toString();
                long val=0;
                //check if the old password is correct and then change it to new password
                val = db.changePassword(oldpassword,password);
                //if the query is successfull
                if(val > 0){
                    Toast.makeText(ChangePasswordActivity.this,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                    Intent moveToLogin = new Intent(ChangePasswordActivity.this,SettingsActivity.class);
                    startActivity(moveToLogin);
                }
                //if the query fails
                else{
                    Toast.makeText(ChangePasswordActivity.this,"Error, Wrong Old Password!",Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
