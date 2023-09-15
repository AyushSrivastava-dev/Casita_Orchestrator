package com.example.casita_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminLogin2Changes extends AppCompatActivity {


    TextInputLayout full_name,user_name, phoneNumber, password_number;
    TextInputLayout venueName,cityName,completeAddress,price,capacity,aboutVenue;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String typeSelected;

    DatabaseReference databaseReference;
    TextInputLayout typeSelection;
    AutoCompleteTextView type;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login2_changes);







        full_name = findViewById(R.id.full_name);
        user_name = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_field);
        password_number = findViewById(R.id.pass_field);
        venueName = findViewById(R.id.venue_name);
        cityName = findViewById(R.id.city_name);
        completeAddress = findViewById(R.id.completeAddress_name);
        price = findViewById(R.id.rate_price);
        capacity = findViewById(R.id.Capacity_limit);
        aboutVenue = findViewById(R.id.about_venue);



        typeSelection = findViewById(R.id.selection_type);
        type = findViewById(R.id.type_venue);

        arrayList = new ArrayList<>();
        arrayList.add("Wedding");
        arrayList.add("Birthday Party");
        arrayList.add("Corporate Events");
        arrayList.add("New Year Party");
        arrayList.add("Engagement");
        arrayList.add("Family Function");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,arrayList);
        type.setAdapter(arrayAdapter);
        type.setThreshold(1);
        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),parent.getAdapter().getItem(position).toString(),

                        Toast.LENGTH_LONG).show();
                typeSelected = parent.getAdapter().getItem(position).toString();
                Log.e("typer","In  "+typeSelected);

            }
        });


    }


    public void onUpdateClick(View view) {
        {
            Log.e("typer", "In  " + typeSelected);
            Log.e("fire", "In register1");

            Log.e("fire", "In register2");
            String fullName = full_name.getEditText().getText().toString();
            String userName = user_name.getEditText().getText().toString();
            String phone = phoneNumber.getEditText().getText().toString();
            String pass = password_number.getEditText().getText().toString();
            Log.e("fire", "In register3");
            String venueN = venueName.getEditText().getText().toString();
            String cityN = cityName.getEditText().getText().toString();
            String completeAdd = completeAddress.getEditText().getText().toString();
            String priceRate = price.getEditText().getText().toString();
            String capacityLimit = capacity.getEditText().getText().toString();
            Log.e("fire", "In register4");
            if (!fullName.isEmpty()) {
                full_name.setError(null);
                full_name.setErrorEnabled(false);
                if (!userName.isEmpty()) {
                    user_name.setError(null);
                    user_name.setErrorEnabled(false);
                    Log.e("fire", "In username");

                    if (!phone.isEmpty()) {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);
                        if (!pass.isEmpty()) {
                            password_number.setError(null);
                            password_number.setErrorEnabled(false);


                            firebaseDatabase = FirebaseDatabase.getInstance();

                            reference = firebaseDatabase.getReference("adminuser");


                            String fullName_ds = full_name.getEditText().getText().toString();
                            String userName_ds = user_name.getEditText().getText().toString();
                            String phone_ds = phoneNumber.getEditText().getText().toString();
                            String pass_ds = password_number.getEditText().getText().toString();

                            String venueName_ds = venueName.getEditText().getText().toString();
//                            String cityName_ds = cityName.getEditText().getText().toString();
//                            String completeAddress_ds = completeAddress.getEditText().getText().toString();
                            String price_ds = price.getEditText().getText().toString();
                            String capacity_ds = capacity.getEditText().getText().toString();
                            String typer_ds = typeSelected;
                            String aboutVenues_ds = aboutVenue.getEditText().getText().toString();

                            HashMap map = new HashMap();
                            map.put("fullName",fullName_ds);
                            map.put("userName",userName_ds);
                            map.put("phoneNumber",phone_ds);
                            map.put("password",pass_ds);
                            map.put("venueName",venueName_ds);
                            map.put("price",price_ds);
                            map.put("capacity",capacity_ds);
                            map.put("type",typer_ds);
                            map.put("aboutVenue",aboutVenues_ds);

                            databaseReference = FirebaseDatabase.getInstance().getReference("adminuser");
                            databaseReference.child(userName_ds).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });






//                            String imageUriToString = imageUri.toString();
//                            Toast.makeText(getApplicationContext(),""+imageUriToString,Toast.LENGTH_LONG).show();
//                            Log.e("imagers",imageUriToString);

                        } else {
                            password_number.setError("Please enter the password");
                        }
                    } else {
                        phoneNumber.setError("Please enter the Phone number");
                    }

                } else {
                    user_name.setError("Please enter the user name");
                }
            } else {
                full_name.setError("Please enter the Full Name");

            }


        }

    }
}