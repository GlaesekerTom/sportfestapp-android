package de.glaeseker_tom.sportfestapp.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import de.glaeseker_tom.sportfestapp.R;

/*
* Das ScoreboardFragment unterstützt den Schiedsrichter beim Punkte zählen und
* gibt ihm die Möglichkeit eine Stopuhr zustellen. Nach Abschluss des Spiels
* kann das Ergebnis zum Server gesendet werden.
*/
public class ScoreboardFragment extends Fragment implements View.OnClickListener{

    public ScoreboardFragment() {
    }

    private TextView  score;
    private Button startTimer;
    private int goalTeam1, goalTeam2, timeLeft, count;
    private TextView time;
    private EditText editText;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int timerTime = 600;
    private String serverUrl, team1, team2, sportType;
    private OnFragmentInteractionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        timeLeft = timerTime;
        if(getArguments() != null){
            serverUrl = getArguments().getString("serverUrl");
        }
        Button btnGoalTeam1a = v.findViewById(R.id.sc_btn_goal_team1a);
        Button btnGoalTeam2a = v.findViewById(R.id.sc_btn_goal_team2a);
        Button btnGoalTeam1r = v.findViewById(R.id.sc_btn_goal_team1r);
        Button btnGoalTeam2r = v.findViewById(R.id.sc_btn_goal_team2r);
        Button btnSetTime = v.findViewById(R.id.sc_btn_set_time);
        Button btnSubmit = v.findViewById(R.id.sc_btn_submit);
        Button btnCancel = v.findViewById(R.id.sc_btn_cancel);
        editText = v.findViewById(R.id.sc_et_input_time);
        score = v.findViewById(R.id.sc_tv_score);
        startTimer = v.findViewById(R.id.sc_btn_timer_start);
        Button resetTimer = v.findViewById(R.id.sc_btn_timer_reset);
        time = v.findViewById(R.id.sc_tv_timer);
        TextView groupChar = v.findViewById(R.id.sc_tv_matchId);
        TextView teams = v.findViewById(R.id.sc_tv_teams);
        TextView sport = v.findViewById(R.id.sc_tv_sport);

        String[] b = getArguments().getStringArray("mm");
        team1 = b[3];
        team2 = b[4];
        sportType =  b[6];
        teams.setText(team1 +" vs. "+ team2);
        sport.setText(sportType.toUpperCase());
        groupChar.setText("Grp. "+b[1]);
        serverUrl = getArguments().getString("serverUrl");
        score.setText("0:0");
        if(timeLeft %60<10){
            time.setText(""+ timeLeft /60+":0"+ timeLeft %60);
        }else{
            time.setText(""+ timeLeft /60+":"+ timeLeft %60);
        }

        btnSubmit.setOnClickListener(this);
        btnSetTime.setOnClickListener(this);
        startTimer.setOnClickListener(this);
        resetTimer.setOnClickListener(this);
        btnGoalTeam1a.setOnClickListener(this);
        btnGoalTeam1r.setOnClickListener(this);
        btnGoalTeam2a.setOnClickListener(this);
        btnGoalTeam2r.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

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
            startTimer.setText("Start");
            startTimer.setBackgroundColor(getResources().getColor(R.color.color_start_button));
            if(timeLeft %60<10){
                time.setText(""+ timeLeft /60+":0"+ timeLeft %60);
            }else{
                time.setText(""+ timeLeft /60+":"+ timeLeft %60);
            }

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sc_btn_timer_start:{
                if (!isRunning) {
                    startTimer(timeLeft);
                    isRunning = true;
                    startTimer.setText("Stop");
                    startTimer.setBackgroundColor(getResources().getColor(R.color.color_pause_button));
                } else {
                    timeLeft = count;
                    resetTimer();
                }
                break;
            }
            case R.id.sc_btn_timer_reset: {
                timeLeft = timerTime;
                resetTimer();
                break;
            }
            case R.id.sc_btn_set_time: {
                String inputTime = editText.getText().toString();
                if (!inputTime.isEmpty() && Pattern.matches("\\d{2}:\\d{2}", inputTime)) {
                    String[] parts = inputTime.split(":");
                    if (Integer.valueOf(parts[0]) < 60 && Integer.valueOf(parts[1]) < 60) {
                        timerTime = Integer.valueOf(parts[0]) * 60 + Integer.valueOf(parts[1]);
                        timeLeft = timerTime;
                        resetTimer();

                        if (timeLeft % 60 < 10) {
                            time.setText("" + timeLeft / 60 + ":0" + timeLeft % 60);
                        } else {
                            time.setText("" + timeLeft / 60 + ":" + timeLeft % 60);
                        }
                    } else {
                        Toast.makeText(getContext(), "Falsches Format. Zahlen müssen kleiner als 60 sein.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Falsches Format. Beispiel: 12:12", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.sc_btn_cancel:{
                listener.onFragmentInteraction(sportType);
                break;
            }
            case R.id.sc_btn_submit:{
                if(goalTeam1 > 0 || goalTeam2 > 0) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                    builder.setTitle("Ergebnis abschicken?");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            SendScoreboardData sendScoreboardData = new SendScoreboardData(getContext());
                            sendScoreboardData.execute(sportType, team1, team2, String.valueOf(goalTeam1), String.valueOf(goalTeam2));
                            listener.onFragmentInteraction(sportType);
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }else{
                    Toast.makeText(getContext(), "Kein Spielergebnis eingetragen!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.sc_btn_goal_team1a:{
                goalTeam1++;
                score.setText(goalTeam1+":"+goalTeam2);
                break;
            }
            case R.id.sc_btn_goal_team1r:{
                if(goalTeam1> 0){
                    goalTeam1--;
                    score.setText(goalTeam1+":"+goalTeam2);
                }
                break;
            }
            case R.id.sc_btn_goal_team2a:{
                goalTeam2++;
                score.setText(goalTeam1+":"+goalTeam2);
                break;
            }
            case R.id.sc_btn_goal_team2r:{
                if(goalTeam2> 0){
                    goalTeam2--;
                    score.setText(goalTeam1+":"+goalTeam2);
                }
                break;
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String s);
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
            String post_data = "";
            try {//TODo anpassen
                URL url = null;
                url = new URL(serverUrl+"updateMatchResult.php");
                //Beschlossen so
                post_data = "sport="+params[1]+"&spielId="+params[2]+"&m1="+params[3]+"&m2="+params[4]+"&t1="+params[5]+"&t2="+params[6];
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