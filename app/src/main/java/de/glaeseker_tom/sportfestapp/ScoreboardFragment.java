package de.glaeseker_tom.sportfestapp;


import android.content.Context;
import android.net.Uri;
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

import java.util.List;


public class ScoreboardFragment extends Fragment {

    public ScoreboardFragment() {
    }

    private TextView matchId, score, teams, sport;
    private Button btnGoalTeam1a,btnGoalTeam2a,btnGoalTeam1r, btnGoalTeam2r, startTimer, resetTimer, btnCancel;
    private int goalTeam1, goalTeam2, timeleft, count;
    private TextView time;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int timerTime = 300;
    private String serverUrl;
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
        score = v.findViewById(R.id.sc_tv_score);
        startTimer = v.findViewById(R.id.sc_btn_timer_start);
        resetTimer = v.findViewById(R.id.sc_btn_timer_reset);
        time = v.findViewById(R.id.sc_tv_timer);
        matchId = v.findViewById(R.id.sc_tv_matchId);
        teams = v.findViewById(R.id.sc_tv_teams);
        sport = v.findViewById(R.id.sc_tv_sport);
        btnCancel = v.findViewById(R.id.sc_btn_cancel);
        String[] b = getArguments().getStringArray("mm");
        serverUrl = getArguments().getString("serverUrl");
        score.setText("0:0");

        matchId.setText("MID: "+b[0]);
        teams.setText(b[2]+" vs. "+b[3]);
        sport.setText(b[5].toUpperCase());
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
                listener.onListFragmentInteraction("volleyball");
               /* FragmentManager fragmentManager = getChildFragmentManager();
                List<Fragment> fragmentList = fragmentManager.getFragments();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                System.out.println("Fragment Namen::----------------------------------------");
                for (int i = 0; i < fragmentList.size(); i++) {
                    System.out.println(fragmentList.get(i).toString());
                    transaction.remove(fragmentList.get(i));
                }
                Table2Fragment frag = new Table2Fragment();
                frag.setTableType("volleyball");
                frag.setServerURL(serverUrl);
                transaction.add(R.id.content_main, frag, "tableVolleyball");
                transaction.commit();*/
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
}