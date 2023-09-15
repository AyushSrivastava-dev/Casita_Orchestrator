package com.example.casita_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {
    Button signupbtn, loginbtn;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        signupbtn = findViewById(R.id.signup_button);
        loginbtn = findViewById(R.id.login_button);

        username = findViewById(R.id.username_text_field);
        password = findViewById(R.id.password_input_field);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_s = username.getEditText().getText().toString();
                String password_s = password.getEditText().getText().toString();

                if (!username_s.isEmpty()) {
                    username.setError(null);
                    if (!password_s.isEmpty()) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        final String username_data = username.getEditText().getText().toString();
                        final String password_data = password.getEditText().getText().toString();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("adminuser");
                        Query check_username = databaseReference.orderByChild("username").equalTo(username_data);
                        check_username.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    username.setError(null);
                                    username.setErrorEnabled(false);
                                    String passCheck = snapshot.child(username_data).child("pass").getValue(String.class);

                                    if (passCheck.equals(password_data)) {
                                        password.setError(null);
                                        password.setErrorEnabled(false);
                                        Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), AdminLogin2Changes.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        password.setError("Wrong Password");
                                    }
                                } else {
                                    username.setError("User does not exists");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        password.setError("Please enter the password");
                    }
                } else {
                    username.setError("please enter the username");
                }
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpAdmin.class);
                startActivity(intent);

            }
        });

    }
}