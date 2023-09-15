package com.example.casita_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpRequest;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;

public class VenueList extends AppCompatActivity {

    /*************NOTE: If the foursquare api does not provide result then check the type of respond error and then search on internet for client id and
    * client secret id change them here along with &v in the make call parameter */




    ArrayList venuesList;
    ArrayList<String> phonelist;

    ListView listView;

    String temp1,temp2;
    //api key is "fsq3mnDC+oiTF2/trUbbt9Ks5uT2m5moClZJpOSeBkQjYpA="
    //fsq3akS5IX4NPS7G5zeC6Wo9Gf5/kFq8VpIdCOU81FLU0hk=
    // the foursquare client_id and the client_secret
//    final String CLIENT_ID = "2JPJDS1LGBYRQBV0BKOPRD33DWNLOCWSVDIRFA3LPXP4NVV4";
//    final String CLIENT_SECRET = "DQBCF5U3205B5DNKVMFXNJC0RBMFD51NOLVM2Z5JFRYMJOHP";
    final String CLIENT_ID = "X0OXAXFEIOUKZUOUNFUZKKZZPK3VYU1Q5DH1LQRQJAC5D5AO";
    final String CLIENT_SECRET = "AADM2KMGD3ZYSJ4J0VXXWIT54DFOWQONXZXVB4LZIDEEFNRW";
    // we will need to take the latitude and the logntitude from a certain point

    String latitude = "";
    String longtitude = "";
    String vId;


    ArrayAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);
        listView = (ListView)findViewById(R.id.list);
        phonelist = new ArrayList<>();

        SharedPreferences sh = getSharedPreferences("MyLocation", MODE_PRIVATE);
        latitude = sh.getString("lat","");
        longtitude = sh.getString("longt","");
        if(latitude.isEmpty()|| longtitude.isEmpty()){
            VenueList.this.finish();
            System.exit(0);
        }


        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Pre_Execution
                    }
                });
//https://api.foursquare.com/v2/venues/4ac518eff964a52064ad20e3/photos?limit=10&client_id=xxxx&client_secret=xxxx&v=20180602

                //"https://api.foursquare.com/v3/places/search?query=abcd&ll=564554&radius=10000&categories=1000&chains=4444"
                //search?categoryId=4bf58dd8d48988d121941735,4bf58dd8d48988d11f941735,4bf58dd8d48988d1d8941735,4bf58dd8d48988d1e9931735,4bf58dd8d48988d1e7931735
                temp1 = makeCall("https://api.foursquare.com/v2/venues/search?categoryId=4bf58dd8d48988d171941735,4d4b7105d754a06373d81259&radius=100000&" +
                        "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20210525&ll="+latitude+","+longtitude);

                Log.e("yeswork",temp1);
                Log.d("debu",temp1);
                //Post_Excecution
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp1 == null) {
                            // we have an error to the call
                            // we can also stop the progress bar
                        } else {
                            // all things went right

                            // parseFoursquare venues search result

                            venuesList = (ArrayList) parseFoursquare(temp1);

                            ArrayList<String> VenuesIdsList = new ArrayList<>();
                            ArrayList<String> ContactList = new ArrayList<>();

                            List listTitle = new ArrayList();
                            ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();

                            //------------Testing parallel api call from here
                            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(venuesList.size());
                            for (int i = 0; i < venuesList.size(); i++) {
                                FoursquareVenue poi = (FoursquareVenue) venuesList.get(i);
                                vId = poi.getId();
                               fixedThreadPool.execute(new Runnable() {
                                   @Override
                                   public void run() {


                                       String temp2 = makeContactCall("https://api.foursquare.com/v2/venues/"+vId+"?client_id="+CLIENT_ID+  "&client_secret="+CLIENT_SECRET +"&v=20210525");
                                       Log.v("conts",temp2);
//
                                       //This below line starts a whole new seperate thread
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               String res = parseContactsFoursquare(temp2);
                                               Log.e("Visited",res);
                                               ContactList.add(res);
                                           }
                                       });




                                   }
                               });
                               // fixedThreadPool.execute();



                                VenuesIdsList.add(vId);

//                                temp2 = makeCall("https://api.foursquare.com/v2/venues/"+vId+"?client_id=SVIBXYYXOEARXHDI4FWAHXGO5ZXOY204TCF1QJFXQLY5FPV4&client_secret=BAAJO1KXRWESGTJJGVON4W3WUFHAQDAJPLRIYJJ5OPHFQ5VI&v=20220815");
//                                String res = parseContactsFoursquare(temp2);
                                String res ="";

                                Log.e("tempCheck",temp2+"   "+vId);

                                Log.e("MyRule",poi.getName()+"     "+res);

                                ModelClass mc = new ModelClass(poi.getName(),poi.getCity(),res);

                                // make a list of the venus that are loaded in the list.
                                // show the name, the category and the city
                                listTitle.add(i,
                                        poi.getName() + ", " + poi.getCategory() + "" + poi.getCity());
                                modelClassArrayList.add(i,mc);
                            }
                            Toast.makeText(getApplicationContext(),"Size is: "+ContactList.size(),Toast.LENGTH_SHORT).show();
                            for(int i=0;i<ContactList.size();i++){
                                Toast.makeText(getApplicationContext(),ContactList.get(i),Toast.LENGTH_SHORT).show();
                                Log.e("ContacstLis",ContactList.get(i));
                            }

                            //------------Stopped testing parallel api testing here



                            // set the results to the list
                            // and show them in the xml
                            // myAdapter = new ArrayAdapter(MainActivity.this, R.layout.activity_main, listTitle);
                            VenueAdapter venueAdapter = new VenueAdapter(getApplicationContext(),R.layout.layout_item,modelClassArrayList);
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_listing,R.id.text2,listTitle);
//                            listView.setAdapter(arrayAdapter);
                            listView.setAdapter(venueAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                                    SharedPreferences sharedPreferences = getSharedPreferences("MyDetails", MODE_PRIVATE);

                                    SharedPreferences.Editor myEdits = sharedPreferences.edit();

                                    String specificId = VenuesIdsList.get(position);

                                    myEdits.putString("VenueID", specificId);

                                    myEdits.commit();
                                    Toast.makeText(getApplicationContext(),"yes specific",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(VenueList.this,SpecificVenue.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }

                });



               // later();
            }


        });
        // new fourquare().execute();

    }







    public static String makeCall(String url) {

        // string buffers the url
  //      String imageUrl = "https://api.foursquare.com/v3/places/fsq_id/photos";
        StringBuffer buffer_string = new StringBuffer(url);
        //StringBuffer buffer_stringImage = new StringBuffer(imageUrl);
        String replyString = "";
        String imageString = "";


        Log.v("hell",url);

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            int responseCode = connection.getResponseCode();
            Log.e("yellow","responecode   "+responseCode);
            //Log.v("hell", "Server responded with: " + responseCode);

            // And if the code was HTTP_OK then parse the contents
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Convert request content to string
                InputStream is = connection.getInputStream();
                String content = convertInputStream(is, "UTF-8");
                is.close();
                connection.disconnect();

                return content;
            }
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
//            InputStream inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while((line=bufferedReader.readLine())!=null){
//                replyString = replyString+line;
//            }
//            httpURLConnection.disconnect();





            // get the responce of the httpclient execution of the url
//            HttpResponse response = httpclient.execute(httpget);
//            InputStream is = response.getEntity().getContent();
//
//            // buffer input stream the result
//            BufferedInputStream bis = new BufferedInputStream(is);
//            ByteArrayBuffer baf = new ByteArrayBuffer(20);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
//            // the result as a string is ready for parsing
//            replyString = new String(baf.toByteArray());
        } catch (MalformedURLException e){

            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        // trim the whitespaces

        return replyString;
    }
    private static String convertInputStream(InputStream is, String encoding) {
        Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static ArrayList parseFoursquare(final String response) {
        Log.v("Verb",response);
        Log.e("Noh",response);

        ArrayList temp = new ArrayList();
        try {

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        FoursquareVenue poi = new FoursquareVenue();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).getString("name"));
                            Log.e("Hat",poi.getName());

                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
                                        poi.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
                                    }
                                    if (jsonArray.getJSONObject(i).has("categories")) {
                                        if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
                                                poi.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                                            }
                                        }
                                        if(jsonArray.getJSONObject(i).has("id")){
                                            poi.setId(jsonArray.getJSONObject(i).getString("id"));



                                        }
                                    }
                                    temp.add(poi);
                                    Log.e("Mat",poi.getCity());
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            return new ArrayList();
        }
        return temp;

    }




    public void logout(View view) {
       // FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);


        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        String username_sp = "";
        String password_sp = "";
        myEdit.putString("username", username_sp);
        myEdit.putString("password", password_sp);
        myEdit.commit();
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
    }

    public void later(String venueIds){
        // String vIds = "54bbd110498e764e8dae973f";

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {


                String temp2 = makeContactCall("https://api.foursquare.com/v2/venues/"+venueIds+"?client_id="+CLIENT_ID+  "&client_secret="+CLIENT_SECRET +"&v=20210525");
                Log.v("conts",temp2);
//
                //This below line starts a whole new seperate thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String res = parseContactsFoursquare(temp2);

                        // onLoading(res);
                        Log.v("conts",res);
                    }
                });




            }
        });


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
    public String makeContactCall(String url) {

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
//    private static String convertInputStream(InputStream is, String encoding) {
//        Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
//        return scanner.hasNext() ? scanner.next() : "";
//    }


}