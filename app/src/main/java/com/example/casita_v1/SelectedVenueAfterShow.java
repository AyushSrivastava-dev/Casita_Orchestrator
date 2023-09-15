package com.example.casita_v1;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifDrawable;

public class SelectedVenueAfterShow extends AppCompatActivity {
 ImageView imageView;
 TextView venueName, address, capacity, bestFor, aboutVenue, latestReviews, ratings, rate;
 AppBarLayout appBarLayout;
 TabLayout tabLayout;
 FloatingActionButton floatingActionButton;
 Button writeReview, showReview;
 ArrayList<Reviews> list = new ArrayList<>();
 String ratingPattern;
 DataStoreAdmin ob;
 ImageView img1, img2, img3, img4, img5, img6;
 TextView txt1, txt2, txt3, txt4, txt5, txt6;


 DrawableCrossFadeFactory factory =
         new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_selected_venue_after_show);
  Gson gson = new Gson();
  Intent intent = getIntent();
  DataStoreAdmin ob = gson.fromJson(intent.getStringExtra("myDetailsOfVenue"), DataStoreAdmin.class);

  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  getWindow().setStatusBarColor(Color.TRANSPARENT);

  appBarLayout = findViewById(R.id.appbar);
  tabLayout = findViewById(R.id.tab_layout);

  floatingActionButton = findViewById(R.id.call_fab);

  imageView = findViewById(R.id.venue_image);
  venueName = findViewById(R.id.venueFull_name);
  address = findViewById(R.id.address_content);
  capacity = findViewById(R.id.capacity_num);
  bestFor = findViewById(R.id.type_selected);
  writeReview = findViewById(R.id.write_review);
  aboutVenue = findViewById(R.id.aboutDescription);
  latestReviews = findViewById(R.id.reviews);
  ratings = findViewById(R.id.ratings_field);
  rate = findViewById(R.id.rate_of_venue);

  img1 = findViewById(R.id.services_image1);
  img2 = findViewById(R.id.services_image2);
  img3 = findViewById(R.id.services_image3);
  img4 = findViewById(R.id.services_image4);
  img5 = findViewById(R.id.services_image5);
  img6 = findViewById(R.id.services_image6);

  txt1 = findViewById(R.id.text_view1);
  txt2 = findViewById(R.id.text_view2);
  txt3 = findViewById(R.id.text_view3);
  txt4 = findViewById(R.id.text_view4);
  txt5 = findViewById(R.id.text_view5);
  txt6 = findViewById(R.id.text_view6);

  Glide.with(getApplicationContext()).asBitmap().load(ob.getImageUrl())
          .fitCenter().transition(withCrossFade(factory)).into(imageView);
  venueName.setText(ob.getVenueName());
  address.setText(ob.getCompleteAdd());
  capacity.setText(ob.getCapacity());
  bestFor.setText(ob.getTypeSelected());
  aboutVenue.setText(ob.getAbout());
  rate.setText(ob.getPrice());

  //Firebase is here


  appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
   @Override
   public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
     imageView.setVisibility(View.GONE);
     tabLayout.setBackgroundColor(getResources().getColor(R.color.white));
     getWindow().setStatusBarColor(Color.WHITE);

    } else {
     imageView.setVisibility(View.VISIBLE);
     tabLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
     getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
   }
  });

  floatingActionButton.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
    Toast.makeText(getApplicationContext(), "Calling demo", Toast.LENGTH_LONG).show();
   }
  });

  writeReview.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
    Gson gson = new Gson();
    String myJson = gson.toJson(ob);
    Intent intent1 = new Intent(getApplicationContext(), WritingReview.class);
    intent1.putExtra("reviewsDetails", myJson);
    startActivity(intent1);
    finish();
   }
  });

  imageView.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
    Intent intent1 = new Intent(getApplicationContext(), ImageShowingActivity.class);
    intent1.putExtra("imgUrl", ob.getImageUrl());
    startActivity(intent1);

   }
  });


  servicesManager(ob.typeSelected);


 }

 @Override
 protected void onStart() {
  super.onStart();
  Gson gson = new Gson();
  Intent intent = getIntent();
  ob = gson.fromJson(intent.getStringExtra("myDetailsOfVenue"), DataStoreAdmin.class);
  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
  DatabaseReference databaseReference = firebaseDatabase.getReference("reviews");
  DatabaseReference dbr = databaseReference.child(ob.getVenueName());

  ExecutorService service = Executors.newSingleThreadExecutor();
  service.execute(new Runnable() {
   @Override
   public void run() {
    Log.e("isthis", "after query");
    dbr.addValueEventListener(new ValueEventListener() {
     @Override
     public void onDataChange(@NonNull DataSnapshot snapshot) {
      Log.e("isthis", "inside datasnapshot");
      double sum = 0.0;
      for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
       Reviews model = dataSnapshot.getValue(Reviews.class);
       Log.e("isthis", "ratings:  " + model.getRatings());

       sum = sum + Double.parseDouble(model.getRatings());

       list.add(model);
      }
      String latestRatings = "0";
      String reviewing = "No Review";

      if (list.size() != 0 && latestRatings.equals("0")) {
       double total_ratings = sum / list.size();
       latestRatings = "" + total_ratings;
       reviewing = "";
       for (int i = 0; i < list.size(); i++) {
        reviewing = reviewing + (i + 1) + ") " + list.get(i).getReview() + ".\n";
       }

      }

      latestReviews.setText(reviewing);
      ratingPattern = latestRatings + " (" + list.size() + ")";

      ratings.setText(ratingPattern);
     }

     @Override
     public void onCancelled(@NonNull DatabaseError error) {

     }
    });


    runOnUiThread(new Runnable() {
     @Override
     public void run() {
      new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {
        settingRatings();

       }
      }, 8000);
     }
    });
   }
  });

 }

 public void settingRatings() {
  Log.e("checkeris", "adminuser" + ob.getUsername() + ratingPattern + "   ratings");
  DatabaseReference dbrs = FirebaseDatabase.getInstance().getReference().child("adminuser").child(ob.getUsername()).child("ratings");


  dbrs.setValue(ratingPattern)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
            Toast.makeText(getApplicationContext(), "Yes admin updated", Toast.LENGTH_LONG).show();
           }
          })
          .addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
            Toast.makeText(getApplicationContext(), "No No admin updated", Toast.LENGTH_LONG).show();
           }
          });


 }

 public void servicesManager(String type) {
  if (type.equalsIgnoreCase("Wedding")) {
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_foodcatering)
           .fitCenter().transition(withCrossFade(factory)).into(img1);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_djimage)
           .fitCenter().transition(withCrossFade(factory)).into(img2);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_decorators)
           .fitCenter().transition(withCrossFade(factory)).into(img3);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_cakeimage)
           .fitCenter().transition(withCrossFade(factory)).into(img4);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.sangeet)
           .fitCenter().transition(withCrossFade(factory)).into(img5);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.decoration)
           .fitCenter().transition(withCrossFade(factory)).into(img6);

   txt1.setText("Food Catering");
   txt2.setText("Wedding DJ");
   txt3.setText("Decorators");
   txt4.setText("Wedding Cake");
   txt5.setText("Sangeet");
   txt6.setText("Destination");


  } else if (type.equalsIgnoreCase("Corporate Events")) {
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.corporate_flexiblespace)
           .fitCenter().transition(withCrossFade(factory)).into(img1);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_djimage)
           .fitCenter().transition(withCrossFade(factory)).into(img2);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.corporate_wifi)
           .fitCenter().transition(withCrossFade(factory)).into(img3);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.corporate_indoor_andoutdoor_space)
           .fitCenter().transition(withCrossFade(factory)).into(img4);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.corporate_room)
           .fitCenter().transition(withCrossFade(factory)).into(img5);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.corporate_cocktails)
           .fitCenter().transition(withCrossFade(factory)).into(img6);

   txt1.setText("Flexible Space");
   txt2.setText("Party DJ");
   txt3.setText("Wifi/Resources");
   txt4.setText("Indoor/Outdoor Space");
   txt5.setText("Meeting Room");
   txt6.setText("Cocktails/Bars");

  } else {
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_foodcatering)
           .fitCenter().transition(withCrossFade(factory)).into(img1);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_djimage)
           .fitCenter().transition(withCrossFade(factory)).into(img2);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_decorators)
           .fitCenter().transition(withCrossFade(factory)).into(img3);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.wedding_cakeimage)
           .fitCenter().transition(withCrossFade(factory)).into(img4);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.sangeet)
           .fitCenter().transition(withCrossFade(factory)).into(img5);
   Glide.with(getApplicationContext()).asBitmap().load(R.drawable.decoration)
           .fitCenter().transition(withCrossFade(factory)).into(img6);

   txt1.setText("Food Catering");
   txt2.setText("Wedding DJ");
   txt3.setText("Decorators");
   txt4.setText("Wedding Cake");
   txt5.setText("Sangeet");
   txt6.setText("Destination");
  }
 }
}