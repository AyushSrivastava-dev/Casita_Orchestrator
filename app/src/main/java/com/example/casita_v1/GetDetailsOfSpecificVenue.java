package com.example.casita_v1;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetDetailsOfSpecificVenue implements Runnable{
    final String CLIENT_ID = "X0OXAXFEIOUKZUOUNFUZKKZZPK3VYU1Q5DH1LQRQJAC5D5AO";
    final String CLIENT_SECRET = "AADM2KMGD3ZYSJ4J0VXXWIT54DFOWQONXZXVB4LZIDEEFNRW";
    private String venueId;

    GetDetailsOfSpecificVenue(String venueId){
        this.venueId = venueId;
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
    private static String convertInputStream(InputStream is, String encoding) {
        Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    @Override
    public void run() {

        String temp2 = makeContactCall("https://api.foursquare.com/v2/venues/"+venueId+"?client_id="+CLIENT_ID+  "&client_secret="+CLIENT_SECRET +"&v=20210525");
        Log.v("conts",temp2);
        String res = parseContactsFoursquare(temp2);

        // onLoading(res);
        Log.v("conts",res);
    }
}
