package com.example.casita_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpecificVenue extends AppCompatActivity {

    final String CLIENT_ID = "X0OXAXFEIOUKZUOUNFUZKKZZPK3VYU1Q5DH1LQRQJAC5D5AO";
    final String CLIENT_SECRET = "AADM2KMGD3ZYSJ4J0VXXWIT54DFOWQONXZXVB4LZIDEEFNRW";
     TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_venue);
        SharedPreferences sh = getSharedPreferences("MyDetails", MODE_PRIVATE);
        String venueID = sh.getString("VenueID","");
        Toast.makeText(getApplicationContext(),"Entered   ",Toast.LENGTH_LONG).show();

//        textView.findViewById(R.id.contact);
//        textView.setText(venueID);

        later(venueID);



    }
    public void later(String venueIds){
        // String vIds = "54bbd110498e764e8dae973f";

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {


                String temp2 = makeCall("https://api.foursquare.com/v2/venues/"+venueIds+"?client_id="+CLIENT_ID+  "&client_secret="+CLIENT_SECRET +"&v=20210525");
                Log.v("conts",temp2);
//
                //This below line starts a whole new seperate thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String res = parseContactsFoursquare(temp2);
                        Toast.makeText(getApplicationContext(),"Contact: "+res, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Contact: "+res, Toast.LENGTH_LONG).show();
                       // onLoading(res);
                        Log.v("conts",res);
                    }
                });




            }
        });


    }

    private void onLoading(String res) {

        textView.setText(res);
    }

    private String parseContactsFoursquare(final String response) {


        String temp = "";
        try {

            Log.v("cont","try");
            JSONObject jsonObject = new JSONObject(response);
            Log.v("cont","json");
            if (jsonObject.has("response")) {
                Log.v("cont","resp");
                if (jsonObject.getJSONObject("response").has("venue")) {
                    Log.v("cont","ven");
                    JSONObject venueJson = jsonObject.getJSONObject("response").getJSONObject("venue");


                    if(venueJson.getJSONObject("contact").has("phone")){
                        Log.v("cont","phone");
                        JSONObject phoneJson = venueJson.getJSONObject("contact");
                        Log.v("cont","contactsnew");
                        temp = phoneJson.getString("phone");
                        Log.v("cont",temp);



                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }


        return temp;
    }
    public String makeCall(String url) {

        // string buffers the url
        //      String imageUrl = "https://api.foursquare.com/v3/places/fsq_id/photos";
        StringBuffer buffer_string = new StringBuffer(url);
        //StringBuffer buffer_stringImage = new StringBuffer(imageUrl);
        String replyString = "";
        String imageString = "";


        Log.v("hell",url);

        try {
            URL url1 = new URL(url);
            Log.v("hell","url1");
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            Log.v("hell","connection");
            int responseCode = connection.getResponseCode();
            Log.v("hell", "Server responded with: " + responseCode);

            // And if the code was HTTP_OK then parse the contents
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Convert request content to string
                InputStream is = connection.getInputStream();
                String content = convertInputStream(is, "UTF-8");
                is.close();

                Log.v("hell",content);
                return content;
            }





        } catch (MalformedURLException e){

            Log.v("hell","malformedUrl");
            e.printStackTrace();
        }  catch (Exception e) {
            Log.v("hell","other exception  "+e.getClass().getName());
            e.printStackTrace();
        }

        // trim the whitespaces

        return replyString;
    }
    private static String convertInputStream(InputStream is, String encoding) {
        Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}