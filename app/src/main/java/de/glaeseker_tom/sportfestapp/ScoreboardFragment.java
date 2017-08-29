package de.glaeseker_tom.sportfestapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ScoreboardFragment extends Fragment {

    public ScoreboardFragment() {
    }

    private TextView matchId, score, teams, sport;
    private Button btnGoalTeam1a,btnGoalTeam2a,btnGoalTeam1r, btnGoalTeam2r, startTimer, resetTimer, btnCancel, btnSubmit;
    private int goalTeam1, goalTeam2, timeleft, count;
    private TextView time;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int timerTime = 300;
    private String serverUrl, team1, team2, sportType;
    private OnFragmentInteractionListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        timeleft = timerTime;
        btnGoalTeam1a = v.findViewById(R.id.sc_btn_goal_team1a);
        btnGoalTeam2a = v.findViewById(R.id.sc_btn_goal_team2a);
        btnGoalTeam1r = v.findViewById(R.id.sc_btn_goal_team1r);
        btnGoalTeam2r = v.findViewById(R.id.sc_btn_goal_team2r);
        btnSubmit = v.findViewById(R.id.sc_btn_submit);
        score = v.findViewById(R.id.sc_tv_score);
        startTimer = v.findViewById(R.id.sc_btn_timer_start);
        resetTimer = v.findViewById(R.id.sc_btn_timer_reset);
        time = v.findViewById(R.id.sc_tv_timer);
        matchId = v.findViewById(R.id.sc_tv_matchId);
        teams = v.findViewById(R.id.sc_tv_teams);
        sport = v.findViewById(R.id.sc_tv_sport);
        btnCancel = v.findViewById(R.id.sc_btn_cancel);
        String[] b = getArguments().getStringArray("mm");
        team1 = b[2];
        team2 = b[3];
        sportType =  b[5];
        teams.setText(team1 +" vs. "+ team2);
        sport.setText(sportType.toUpperCase());
        matchId.setText("MID: "+b[0]);
        serverUrl = getArguments().getString("serverUrl");
        score.setText("0:0");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goalTeam1 > 0 || goalTeam2 > 0) {
                    SendScoreboardData sendScoreboardData = new SendScoreboardData(getContext());
                    sendScoreboardData.execute(sportType, team1, team2, String.valueOf(goalTeam1), String.valueOf(goalTeam2));
                    listener.onListFragmentInteraction(sportType);
                }
            }
        });


        if(timeleft%60<10){
            time.setText(""+timeleft/60+":0"+timeleft%60);
        }else{
            time.setText(""+timeleft/60+":"+timeleft%60);
        }
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning){
                    startTimer(timeleft);
                    isRunning = true;
                    startTimer.setText("Pause");
                }else{
                    startTimer.setText("Start");
                    timeleft = count;
                    resetTimer();

                }
                
            }
        });
        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeleft = timerTime;
                resetTimer();
                startTimer.setText("Start");

            }
        });
        btnGoalTeam1a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalTeam1++;
                score.setText(goalTeam1+":"+goalTeam2);
            }
        });
        btnGoalTeam2a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalTeam2++;
                score.setText(goalTeam1+":"+goalTeam2);
            }
        });
        btnGoalTeam1r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goalTeam1> 0){
                    goalTeam1--;
                    score.setText(goalTeam1+":"+goalTeam2);
                }

            }
        });
        btnGoalTeam2r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goalTeam2> 0){
                    goalTeam2--;
                    score.setText(goalTeam1+":"+goalTeam2);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListFragmentInteraction(sportType);
            }
        });

        return v;
    }
    private void startTimer(int seconds){
       //time.setText(""+seconds);
        if(count%60<10){
            time.setText(""+seconds/60+":0"+seconds%60);
        }else{
            time.setText(""+seconds/60+":"+seconds%60);
        }
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long l) {
                count = (int)l/1000;
                if(count%60<10){
                    time.setText(""+count/60+":0"+count%60);
                }else{
                    time.setText(""+count/60+":"+count%60);
                }
//Todo Zeitwackler entfernen
            }

            @Override
            public void onFinish() {
                time.setText("Abgelaufen");
                isRunning = false;
            }
        };
        countDownTimer.start();
    }
    private void resetTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            isRunning = false;
            count = 0;
            if(timeleft%60<10){
                time.setText(""+timeleft/60+":0"+timeleft%60);
            }else{
                time.setText(""+timeleft/60+":"+timeleft%60);
            }

        }
    }
    public interface OnFragmentInteractionListener {
        void onListFragmentInteraction(String s);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private class SendScoreboardData extends AsyncTask<String, Void, String> {
        private Context context;

        SendScoreboardData(Context context){
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String serverUrl = params[0];
            String post_data = "";
            try {
                URL url = null;
                url = new URL(serverUrl+"updateMatchResult.php");
                post_data = "sport="+params[1]+"&m1="+params[2]+"&m2="+params[3]+"&t1="+params[4]+"&t2="+params[5];
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
                    if(jsonObject.names().get(0).equals("success")){
                        Toast.makeText(context, "ERFOLG: "+ jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"FEHLER: "+ jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }}else {
                Toast.makeText(context, "Es konnte keine Verbindung hergestellt werden!", Toast.LENGTH_LONG).show();
            }
        }
    }

}