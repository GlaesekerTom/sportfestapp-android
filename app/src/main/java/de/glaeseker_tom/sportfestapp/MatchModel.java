package de.glaeseker_tom.sportfestapp;

/**
 * Created by tomgl on 13.08.2017.
 */

public class MatchModel {

    private String time;
    private String team1;
    private String team2;
    private String result;
    private String number;

    public MatchModel(){

    }
    public MatchModel(String pnumber , String ptime, String pteam1, String pteam2, String presult){
        number = pnumber;
        time = ptime;
        team1 = pteam1;
        team2 = pteam2;
        result = presult;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
