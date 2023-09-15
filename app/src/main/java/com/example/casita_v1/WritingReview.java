package com.example.casita_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class WritingReview extends AppCompatActivity {
    EditText editText;
    Button submitBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RatingBar rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_review);
        Gson gson = new Gson();
        Intent intent = getIntent();
        DataStoreAdmin ob = gson.fromJson(intent.getStringExtra("reviewsDetails"), DataStoreAdmin.class);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String user = sh.getString("username", "");
        editText = findViewById(R.id.edit_text);
        submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latestReview = editText.getText().toString();
                if (!latestReview.isEmpty()) {
                    String ratings = String.valueOf(rt.getRating());
                    Toast.makeText(getApplicationContext(), "Inside submit review user=" + user, Toast.LENGTH_LONG).show();
                    Reviews reviews = new Reviews(user, latestReview, ratings, ob.venueName);
                    Toast.makeText(getApplicationContext(), "Outside review", Toast.LENGTH_LONG).show();

                    //*Firebase code is here

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("reviews");
                    reference.child(ob.venueName).child(user).setValue(reviews);
                    Toast.makeText(getApplicationContext(), "submitted successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please write a review", Toast.LENGTH_LONG).show();
                }

                DatabaseReference dbr = FirebaseDatabase.getInstance("adminuser").getReference().child(ob.getUsername()).child(ob.getRatings());
                String ratings = String.valueOf(rt.getRating());
                double avg = (Double.parseDouble(ob.getRatings()) + Double.parseDouble(ratings)) / 2;
                String ratingsPattern = "" + avg;
                dbr.setValue(ratingsPattern)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Uploaded review", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed review", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
            }
        });
        rt = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) rt.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

    }
}