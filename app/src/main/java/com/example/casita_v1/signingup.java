package com.example.casita_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signingup extends AppCompatActivity {
    TextInputLayout full_name, user_name, email, phoneNumber, password_number;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signingup);

        full_name = findViewById(R.id.full_name);
        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email_field);
        phoneNumber = findViewById(R.id.phone_field);
        password_number = findViewById(R.id.pass_field);
    }

    public void loginButtonClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    public void onRegisterClick(View view) {
        String fullName = full_name.getEditText().getText().toString();
        String userName = user_name.getEditText().getText().toString();
        String email_name = email.getEditText().getText().toString();
        String phone = phoneNumber.getEditText().getText().toString();
        String pass = password_number.getEditText().getText().toString();

        if (!fullName.isEmpty()) {
            full_name.setError(null);
            full_name.setErrorEnabled(false);
            if (!userName.isEmpty()) {
                user_name.setError(null);
                user_name.setErrorEnabled(false);
                if (!email_name.isEmpty()) {
                    email.setError(null);
                    email.setErrorEnabled(false);
                    if (!phone.isEmpty()) {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);
                        if (!pass.isEmpty()) {
                            password_number.setError(null);
                            password_number.setErrorEnabled(false);

                            if (validate(email_name, VALID_EMAIL_ADDRESS_REGEX)) {
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                reference = firebaseDatabase.getReference("datauser");

                                String fullName_ds = full_name.getEditText().getText().toString();
                                String userName_ds = user_name.getEditText().getText().toString();
                                String email_name_ds = email.getEditText().getText().toString();
                                String phone_ds = phoneNumber.getEditText().getText().toString();
                                String pass_ds = password_number.getEditText().getText().toString();

                                DataStore dataStore = new DataStore(fullName_ds, userName_ds, email_name_ds, phone_ds, pass_ds);
                                reference.child(userName_ds).setValue(dataStore);

                                Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                                startActivity(intent);
                                finish();


                            } else {
                                email.setError("Invalid email");
                            }
                        } else {
                            password_number.setError("Please enter the password");
                        }
                    } else {
                        phoneNumber.setError("Please enter the Phone number");
                    }
                } else {
                    email.setError("Please enter the email address");
                }
            } else {
                user_name.setError("Please enter the user name");
            }
        } else {
            full_name.setError("Please enter the Full Name");

        }

    }

    public static boolean validate(String emailStr, Pattern VALID_EMAIL_ADDRESS_REGEX) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}