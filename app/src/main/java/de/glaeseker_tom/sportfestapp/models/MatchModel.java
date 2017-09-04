package de.glaeseker_tom.sportfestapp.models;

/**
 * Das MatchModel repäsentiert einen Spielplaneintrag. Die Attribute und die entsprechenden Get-Methoden.
 *
 */

public class MatchModel {

    //Attribute
    private String time;
    private String team1;
    private String team2;
    private String result;
    private String matchID;
    private String gym;
    private String referee;
    private String sport;
    private String group;

    //Konstruktor
    public MatchModel(String pMatchID , String pTime, String pTeam1, String pTeam2, String pResult, String pSport, String pReferee, String pGym, String pGroup){
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

    // Get-Methoden
    public String getMatchID() {
        return matchID;
    }

    public String getResult() {
        return result;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getGym() {
        return gym;
    }

    public String getSport() {
        return sport;
    }

    public String getReferee() {
        return referee;
    }

    public String getTime() {
        return time;
    }

    public String getGroup(){ return group; }
}
