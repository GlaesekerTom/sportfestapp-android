package de.glaeseker_tom.sportfestapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tomgl on 13.08.2017.
 */

public class AccountHandler extends AsyncTask<String, Void, String> {
    Context context;

    AccountHandler(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String serverUrl = params[0];
        String type = params[1];
        String post_data = "";
        URL url = null;
        if(type.equals("login")){
            try{
                post_data = "email="+params[2]+"&password="+params[3];
                url = new URL(serverUrl+"login.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else if(type.equals("register")){
            try{
                post_data = "email="+params[2]+"&password="+params[3];
                url = new URL(serverUrl+"register.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.getOutputStream().write(post_data.getBytes("UTF-8"));
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String json_string;
            while ((json_string = br.readLine())!=null) {
                stringBuilder.append(json_string);
            }
            br.close();
            inputStream.close();
            httpURLConnection.disconnect();
            String finalJson = stringBuilder.toString().trim();
            System.out.println("finalJson:"+finalJson);
            return finalJson;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject= new JSONObject(s);
            if(jsonObject.names().get(0).equals("success")){
                Toast.makeText(context, "SUCCESS "+ jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainActivity.class));
            }else{
                Toast.makeText(context,"Error "+  jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}