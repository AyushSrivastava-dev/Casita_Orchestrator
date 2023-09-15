package com.example.casita_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class ShowActivity extends AppCompatActivity {

 private RecyclerView recyclerView;
 private ArrayList<DataStoreAdmin> list;
 private MyAdapter adapter;
 private RadioGroup radioGroup, radioGroup1;
 private RadioGroup radioGroup2;
 private RadioButton radioButton, radioButton2, radioButton3;
 ShimmerFrameLayout shimmerFrameLayout;
 FloatingActionButton floatingActionButton;
 Toolbar toolbar;


 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_show);

  floatingActionButton = findViewById(R.id.filter_fab);
  Drawable drawable = getResources().getDrawable(R.drawable.circular); // the circular shape drawable with white center and red outline
  floatingActionButton.setBackgroundDrawable(drawable);
  ViewCompat.setElevation(floatingActionButton, 8f);

  DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
  Glide.with(this)
          .asGif()
          .load(R.drawable.icons_funnel)
          .into(floatingActionButton);

  DatabaseReference root = FirebaseDatabase.getInstance().getReference("adminuser");
  toolbar = findViewById(R.id.toolbar);
  setSupportActionBar(toolbar);
  getWindow().setStatusBarColor(Color.GRAY);
  shimmerFrameLayout = findViewById(R.id.shimmerLayout);
  shimmerFrameLayout.startShimmer();

  recyclerView = findViewById(R.id.recyclerView);
  recyclerView.setHasFixedSize(true);
  recyclerView.setLayoutManager(new LinearLayoutManager(this));
  list = new ArrayList<>();
  adapter = new MyAdapter(this, list);
  recyclerView.setAdapter(adapter);

  root.addValueEventListener(new ValueEventListener() {

   @Override
   public void onDataChange(@NonNull DataSnapshot snapshot) {
    Log.e("vImage", "inside on onDataChange");
    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
     DataStoreAdmin model = dataSnapshot.getValue(DataStoreAdmin.class);
     list.add(model);

    }
    shimmerFrameLayout.stopShimmer();
    shimmerFrameLayout.setVisibility(View.GONE);
    adapter.notifyDataSetChanged();
   }

   @Override
   public void onCancelled(@NonNull DatabaseError error) {
    Toast.makeText(getApplicationContext(), "No data exchange", Toast.LENGTH_LONG).show();

   }
  });
  root.keepSynced(true);


  floatingActionButton.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
    showAlertDialogButtonClicked(v);
   }
  });


 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  getMenuInflater().inflate(R.menu.menue_item, menu);
  return true;
 }

 @Override
 public boolean onOptionsItemSelected(@NonNull MenuItem item) {
  int item_id = item.getItemId();
  if (item_id == R.id.home) {
   Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
   home();
  } else if (item_id == R.id.profile_field) {
   Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_LONG).show();
   profile();
  } else if (item_id == R.id.about_field) {
   Toast.makeText(getApplicationContext(), "about", Toast.LENGTH_LONG).show();

   AlertDialog dialog;
   AlertDialog.Builder builder = new AlertDialog.Builder(this);
   builder.setTitle("About Application");
   builder.setMessage(R.string.about_application);
   builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int id) {
     dialog.dismiss();
    }
   }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
     dialog.dismiss();
    }
   });

   dialog = builder.create();
   dialog.setOnShowListener(new DialogInterface.OnShowListener() {
    @Override
    public void onShow(DialogInterface arg0) {
     dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
     dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }
   });

   dialog.show();
  } else if (item_id == R.id.logout_field) {
   Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
   logout();
  } else if (item_id == R.id.contactUs_filed) {
   Toast.makeText(getApplicationContext(), "Contact Us", Toast.LENGTH_LONG).show();
   calling();

  }
  return true;
 }

 public void logout() {
  SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
  SharedPreferences.Editor myEdit = sharedPreferences.edit();
  Boolean checker = sharedPreferences.getBoolean("checker", false);
  myEdit.putString("username", "");
  myEdit.putString("password", "");
  myEdit.putBoolean("checker", false);
  myEdit.commit();
  if (checker) {
   Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
   startActivity(intent);
   finish();
  }
  Intent intent = new Intent(getApplicationContext(), LogOut.class);
  startActivity(intent);
  finish();
 }

 public void home() {
  Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
  startActivity(intent);
  finish();
 }

 public void profile() {
  Intent intent = new Intent(getApplicationContext(), ProfileView.class);
  startActivity(intent);


 }

 public void showAlertDialogButtonClicked(View view) {
  // Create an alert builder

  AlertDialog dialog;
  AlertDialog.Builder builder = new AlertDialog.Builder(this);
  builder.setTitle("Sort By");

  // set the custom layout
  final View customLayout = getLayoutInflater().inflate(R.layout.sort_by, null);
  builder.setView(customLayout);


  builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
            // sign in the user ...

            Toast.makeText(getApplicationContext(), "Heelo", Toast.LENGTH_LONG).show();
            radioGroup = (RadioGroup) customLayout.findViewById(R.id.radioGroup_a);
            radioGroup2 = (RadioGroup) customLayout.findViewById(R.id.radioGroup2_a);
            radioGroup1 = (RadioGroup) customLayout.findViewById(R.id.radioGroupType);
            int id1 = radioGroup.getCheckedRadioButtonId();
            int id2 = radioGroup2.getCheckedRadioButtonId();
            int id3 = radioGroup1.getCheckedRadioButtonId();
            radioButton = (RadioButton) customLayout.findViewById(id1);
            radioButton2 = (RadioButton) customLayout.findViewById(id2);
            radioButton3 = (RadioButton) customLayout.findViewById(id3);


            Toast.makeText(getApplicationContext(), "Heelgdgfddsso2", Toast.LENGTH_LONG).show();
            sendDialogDataToActivity(radioButton.getText().toString(), radioButton2.getText().toString(), radioButton3.getText().toString());
            dialog.dismiss();
           }
          })
          .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {

            dialog.dismiss();
           }
          });


  dialog = builder.create();
  dialog.setOnShowListener(new DialogInterface.OnShowListener() {
   @Override
   public void onShow(DialogInterface arg0) {
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
   }
  });

  dialog.show();
 }

 private void sendDialogDataToActivity(String a, String b, String c) {
  Toast.makeText(getApplicationContext(), a + "  " + b + "   " + c, Toast.LENGTH_LONG).show();

  if (b.equalsIgnoreCase("Descending")) {
   if (a.equalsIgnoreCase("Rate")) {
    Collections.sort(list, new PriceCompareDesc());
   } else if (a.equalsIgnoreCase("Capacity")) {
    Collections.sort(list, new CapacityDesc());
   }

  } else {
   if (a.equalsIgnoreCase("Rate")) {
    Collections.sort(list, new PriceCompareAsc());
   } else if (a.equalsIgnoreCase("Capacity")) {
    Collections.sort(list, new CapacityAsc());
   }

  }
  ArrayList<DataStoreAdmin> arrayList = new ArrayList<>();
  for (DataStoreAdmin dsa : list) {
   if (dsa.typeSelected.equalsIgnoreCase(c)) {
    arrayList.add(dsa);
   }
  }
  list.clear();
  list.addAll(arrayList);
  adapter.notifyDataSetChanged();
 }

 public void calling() {

  AlertDialog dialog;
  AlertDialog.Builder builder = new AlertDialog.Builder(this);
  builder.setTitle("Contact Details");
  builder.setMessage(R.string.contact_details);
  builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
   @Override
   public void onClick(DialogInterface dialog, int id) {
    dialog.dismiss();
   }
  }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
   @Override
   public void onClick(DialogInterface dialog, int which) {
    dialog.dismiss();
   }
  });

  dialog = builder.create();
  dialog.setOnShowListener(new DialogInterface.OnShowListener() {
   @Override
   public void onShow(DialogInterface arg0) {
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
   }
  });
  dialog.show();

 }
}