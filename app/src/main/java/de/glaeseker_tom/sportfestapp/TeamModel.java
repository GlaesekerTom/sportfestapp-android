package de.glaeseker_tom.sportfestapp;

/**
 * Created by tomgl on 27.08.2017.
 */

public class TeamModel {

    private String teamName;
    private String teamManager;

    public TeamModel(String teamName, String teamManager){
        this.teamManager = teamManager;
        this.teamName = teamName;
    }

    public String getTeamName(){
        return teamName;
    }
    public String getTeamManager(){
        return teamManager;
    }

    public void setTeamName(String teamName){
        this.teamName = teamName;
    }
    public void setTeamManager(String teamManager){
        this.teamManager = teamManager;
    }
}
