package com.example.casita_v1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpAdmin extends AppCompatActivity {
    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            });

    private ImageView imageView;
    private ProgressBar progressBar;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    TextInputLayout full_name, user_name, phoneNumber, password_number;
    TextInputLayout venueName, cityName, completeAddress, price, capacity, aboutVenue;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String typeSelected;

    final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    TextInputLayout typeSelection;
    AutoCompleteTextView type;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);

        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.upload_image);

        progressBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("gal", "zero");
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                ActivityResultLauncher.launch(galleryIntent);
            }
        });

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
        arrayList.add("Corporate Events");
        arrayList.add("Family Function");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayList);
        type.setAdapter(arrayAdapter);
        type.setThreshold(1);
        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_LONG).show();
                typeSelected = parent.getAdapter().getItem(position).toString();

            }
        });


    }

    public void onRegisterAdminClick(View view) {
        String fullName = full_name.getEditText().getText().toString();
        String userName = user_name.getEditText().getText().toString();
        String phone = phoneNumber.getEditText().getText().toString();
        String pass = password_number.getEditText().getText().toString();
        String venueN = venueName.getEditText().getText().toString();
        String cityN = cityName.getEditText().getText().toString();
        String completeAdd = completeAddress.getEditText().getText().toString();
        String priceRate = price.getEditText().getText().toString();
        String capacityLimit = capacity.getEditText().getText().toString();
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
                        String cityName_ds = cityName.getEditText().getText().toString();
                        String completeAddress_ds = completeAddress.getEditText().getText().toString();
                        String price_ds = price.getEditText().getText().toString();
                        String capacity_ds = capacity.getEditText().getText().toString();
                        String typer_ds = typeSelected;
                        String aboutVenues_ds = aboutVenue.getEditText().getText().toString();
                        if (imageUri != null) {

                            //Upload image is here
                            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DataStoreAdmin dataStoreAdmin = new DataStoreAdmin(fullName_ds, userName_ds, phone_ds, pass_ds, venueName_ds
                                                    , cityName_ds, completeAddress_ds, price_ds, capacity_ds, typer_ds, uri.toString(), "0.0", aboutVenues_ds);

                                            reference.child(userName_ds).setValue(dataStoreAdmin);
                                            Log.e("fire", "Pushed in firebase");

                                            Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), AdminLogin2Changes.class);
                                            startActivity(intent);
                                            finish();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            imageView.setImageResource(R.drawable.imageuploader_icon);
                                        }
                                    });
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                        }
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

    public static boolean validate(String emailStr, Pattern VALID_EMAIL_ADDRESS_REGEX) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void openSomeActivityForResult(View view) {
        Log.e("gal", "first");
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            imageView.setImageURI(imageUri);
                        }
                    }
                });

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        someActivityResultLauncher.launch(galleryIntent);
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}