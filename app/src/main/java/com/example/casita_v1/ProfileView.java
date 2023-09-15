package com.example.casita_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileView extends AppCompatActivity {

    TextView userName,emailId,phoneNum,userInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        userName = findViewById(R.id.user_name_textview);
        emailId = findViewById(R.id.user_email_textview);
        phoneNum = findViewById(R.id.user_phone_textview);

        getWindow().setStatusBarColor(Color.GRAY);
        userInitial = findViewById(R.id.userInitial);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String user = sh.getString("username", "");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("datauser");

        Query check_username = databaseReference.orderByChild("username").equalTo(user);

        check_username.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String email = "Email : "+snapshot.child(user).child("email").getValue(String.class);
                    String name = "Name : "+snapshot.child(user).child("name").getValue(String.class);
                    String phoneNo = "PhoneNo : "+snapshot.child(user).child("phone").getValue(String.class);
                    String userNameInitial = ""+snapshot.child(user).child("name").getValue(String.class).charAt(0);
                    userName.setText(name);
                    emailId.setText(email);
                    phoneNum.setText(phoneNo);
                    userInitial.setText(userNameInitial);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }
}