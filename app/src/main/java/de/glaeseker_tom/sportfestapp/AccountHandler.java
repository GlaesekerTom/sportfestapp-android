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

class AccountHandler extends AsyncTask<String, Void, String> {
    private Context context;

    AccountHandler(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String serverUrl = params[0];
        String type = params[1];
        String post_data = "";
        URL url = null;
        try {
            url = new URL(serverUrl+"account_handler.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(type.equals("login")){
                post_data = "type=login&username="+params[2]+"&password="+params[3];
        }
        else if(type.equals("register")){
                post_data = "type=register&username="+params[2]+"&password="+params[3];//+"&token="+params[4];
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection == null){
                return null;
            }
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            //Zeit setzen, wie lange die App versucht eine Verbindung zum Server aufzubauen.
            httpURLConnection.setConnectTimeout(2000);
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
        if(s!= null){
        try {
            JSONObject jsonObject= new JSONObject(s);
            if(jsonObject.names().get(0).equals("status")){
                if(jsonObject.getString("status").equals("logged_in")) {
                    int permission = jsonObject.getInt("permission");
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("permission", permission);
                    context.startActivity(intent);
                }else if (jsonObject.getString("status").equals("registered")){
                    context.startActivity(new Intent(context,LoginActivity.class));
                }else if (jsonObject.getString("status").equals("error")){
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "FEHLER: Ungültige Antwort des Servers.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }}else {
            Toast.makeText(context, "FEHLER: Es konnte keine Verbindung hergestellt werden.", Toast.LENGTH_LONG).show();
        }
    }
}
