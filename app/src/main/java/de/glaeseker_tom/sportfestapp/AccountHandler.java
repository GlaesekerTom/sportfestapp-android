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
 *
 *Dient zur Kommunikation mit dem Server in der Login- und in der Registeracitvity.
 * Sendet einen HTTP-Post an das PHP-Skript, welches sich auf dem Server befindet.
 * Die Antwort wird ausgewertet und je nach Fall, die Mainactivity gestartet.
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
        //Je nachdem, ob sich der Benutzer einloggen oder registrieren möchte, wird hier der Postinhalt zusammengesetzt.
        if(type.equals("login")){
                post_data = "type=login&username="+params[2]+"&password="+params[3];
        }
        else if(type.equals("register")){
                post_data = "type=register&username="+params[2]+"&password="+params[3];
        }
        try {
            //Verbindungsaufbau
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection == null){
                return null;
            }
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            //Zeit setzen, wie lange die App versucht eine Verbindung zum Server aufzubauen.
            httpURLConnection.setConnectTimeout(2000);
            //Schickt dem Postinhalt zum Server
            httpURLConnection.getOutputStream().write(post_data.getBytes("UTF-8"));
            //Fängt die Antwort auf
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String json_string;
            while ((json_string = br.readLine())!=null) {
                stringBuilder.append(json_string);
            }
            br.close();
            inputStream.close();
            //Verbindung wird geschlossen
            httpURLConnection.disconnect();
            //Trim() entfernt unnutze Leerzeichen am Anfang und am Ende
            //String wird zurückgegeben und an onPostExecute() geleitet
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Überprüft, ob der Parameter null ist, was bei einem Fehler der Fall wäre.
        if(s!= null){
            try {
                //Ein neues JsonObject wird aus dem String erstellt.
                JSONObject jsonObject= new JSONObject(s);
                if(jsonObject.names().get(0).equals("status")){
                    if(jsonObject.getString("status").equals("logged_in")) {
                        //Sucht die Rechte des Benutzer aus dem JsonObject
                        int permission = jsonObject.getInt("permission");
                        //Eine neue Activity wird gestartet und permission wird übergeben.
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
            }
        }else {
            Toast.makeText(context, "FEHLER: Es konnte keine Verbindung hergestellt werden.", Toast.LENGTH_LONG).show();
        }
    }
}
