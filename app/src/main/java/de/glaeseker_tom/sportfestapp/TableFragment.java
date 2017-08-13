package de.glaeseker_tom.sportfestapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class TableFragment extends Fragment {
    TableLayout tableLayout;
    ArrayList<TextView> tvArray;
    String tableType;
    EditText et_id;
    EditText et_points_m1;
    EditText et_points_m2;
    Button send;
    String serverUrl ="";

    public TableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tvArray = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_table,container,false);
        tableLayout = (TableLayout) v.findViewById(R.id.tableLayout);
        tableLayout.setColumnStretchable(0, true);
        tableLayout.setColumnStretchable(1, true);
        send = (Button) v.findViewById(R.id.btn_send_result);
        et_id = (EditText) v.findViewById(R.id.et_id);
        et_points_m1 = (EditText) v.findViewById(R.id.et_points_m1);
        et_points_m2 = (EditText) v.findViewById(R.id.et_points_m2);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Pattern matching only numbers and id bigger than zero.
                String matchID = et_id.getText().toString();
                String pointsM1 = et_points_m1.getText().toString();
                String pointsM2 = et_points_m2.getText().toString();
                new SendDataChanges().execute(matchID,pointsM1,pointsM2);
            }
        });
        new GetJsonData().execute();
        return v;
    }
    public void setTableType(String pTableType){
        tableType = pTableType;
    }

    public void setServerURL(String pUrl){
        serverUrl = pUrl;
    }
    public void addRow(int rowsNumber) {
        for (int i = 0; i < rowsNumber; i++) {
            TableRow tr = new TableRow(getActivity());
            tr.setId(i);
            if (i % 2 == 0) {
                tr.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                tr.setBackgroundColor(Color.parseColor("#E0E0E0"));
            }
            for (int j = 0; j < 5; j++) {
                TextView tv = new TextView(getActivity());
                tv.setTextSize(20);
                StringBuilder s = new StringBuilder();
                tv.setGravity(Gravity.CENTER);
                tv.setText("0");
                tvArray.add(tv);
                tr.addView(tv);
            }
            tableLayout.addView(tr);
        }
    }

    public void setTvContent(int tvID, String content) {
        TextView tvs = tvArray.get(tvID);
        tvs.setText(content);
        tvs.setGravity(Gravity.CENTER);

    }

     /*
    #
    #
    #             SEND DATA CHANGES - Background Task
    #
    #
     */

    public class SendDataChanges extends AsyncTask<String, String, String[]> {


        URL url;
        @Override
        protected void onPreExecute() {
            try {
                url = new URL (serverUrl+"get_test.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String[] doInBackground(String... params) {
            try {
                String json_string;
                String textparam = "sportname="+tableType+"_data&id="+params[0]+"&pM1="+params[1]+"&pM2="+params[2];
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.getOutputStream().write(textparam.getBytes("UTF-8"));

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = br.readLine())!=null){
                    stringBuilder.append(json_string);
                }
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String finalJson = stringBuilder.toString().trim();
                System.out.println("finalJson:"+finalJson);
                System.out.println(finalJson);

                JSONObject parentObject = new JSONObject(finalJson);
                if(parentObject.names().get(0).equals("error")) {
                    System.out.println("----------------ERROR "+parentObject.get("error").toString());
                    return null;
                }
                String[] resultlist = new String[2];
                resultlist[0] = parentObject.get("id").toString();
                resultlist[1] = parentObject.get("result").toString();

                return resultlist;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] resultlist) {
            super.onPostExecute(resultlist);
            if(resultlist != null) {
                setTvContent(Integer.parseInt(resultlist[0]) * 4, resultlist[1]);
                et_points_m1.setText("");
                et_points_m2.setText("");
                et_id.setText("");
            }else{
                Toast.makeText(getActivity(), "FEHLER: Ein Fehler ist aufgetreten!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    #
    #
    #             GET JSON DATA - Background Task
    #
    #
     */
    public class GetJsonData extends AsyncTask<String, String, ArrayList<MatchModel>> {

        URL url;
        @Override
        protected void onPreExecute() {
            try {
                url = new URL (serverUrl+"get_json_data.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<MatchModel> doInBackground(String... params) {
            try {
                String json_string;
                String textparam = "sportname="+ tableType+"_data";
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setFixedLengthStreamingMode(textparam.getBytes().length);

                OutputStreamWriter contentWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                contentWriter.write(textparam);
                contentWriter.flush();
                contentWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = br.readLine())!=null){
                    stringBuilder.append(json_string);
                }
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String finalJson = stringBuilder.toString().trim();
                System.out.println("finalJson:"+finalJson);
                System.out.println(finalJson);

                JSONObject parentObject = new JSONObject(finalJson);
                if(parentObject.names().get(0).equals("error")) {
                    System.out.println("------------- JSON ERROR: "+parentObject.get("error").toString());
                    return null;
                }

                JSONArray parentArray = parentObject.getJSONArray(tableType);

                ArrayList<MatchModel> resultlist = new ArrayList<>();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    MatchModel mm = new MatchModel(finalObject.getString("number"), finalObject.getString("time"), finalObject.getString("team1"),
                            finalObject.getString("team2"), finalObject.getString("result"));
                    resultlist.add(mm);
                }
                return resultlist;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<MatchModel> resultlist) {
            super.onPostExecute(resultlist);

            if (resultlist != null) {
                if(resultlist.size() > tvArray.size()/4){
                    addRow(resultlist.size()- tvArray.size()/4);
                }
                int j = 0;
                for (int i = 0; i < resultlist.size(); i++) {
                    setTvContent(j, resultlist.get(i).getNumber());
                    setTvContent(j + 1, resultlist.get(i).getTime());
                    setTvContent(j + 2, resultlist.get(i).getTeam1());
                    setTvContent(j + 3, resultlist.get(i).getTeam2());
                    setTvContent(j + 4, resultlist.get(i).getResult());
                    j+=5;
                }
            } else {
                Toast toast = Toast.makeText(getActivity(),"FEHLER: Keine Daten gefunden.",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
