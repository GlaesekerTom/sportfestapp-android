package de.glaeseker_tom.sportfestapp;

/**
 * Created by tomgl on 19.08.2017.
 */

public class MatchModel2 {

    private String time;
    private String team1;
    private String team2;
    private String result;
    private String matchID;
    private String gym;
    private String referee;
    private String sport;
    private String group;

    public MatchModel2(){

    }
    public MatchModel2(String pMatchID , String pTime, String pTeam1, String pTeam2, String pResult, String pSport, String pReferee, String pGym, String pGroup){
        matchID = pMatchID;
        time = pTime;
        team1 = pTeam1;
        team2 = pTeam2;
        result = pResult;
        gym = pGym;
        referee = pReferee;
        sport = pSport;
        group = pGroup;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String pMatchID) {
        this.matchID = pMatchID;
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

    public String getGym() {
        return gym;
    }

    public void setGym(String pGym) {
        this.gym = pGym;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String pSport) {
        this.sport = pSport;
    }
    public String getReferee() {
        return referee;
    }

    public void setReferee(String pReferee) {
        this.referee = pReferee;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String pTime) {
        this.time = pTime;
    }

    public String getGroup(){ return group; }

    public void setGroup(String group) { this.group = group; }
}
