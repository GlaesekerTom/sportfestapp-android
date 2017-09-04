package de.glaeseker_tom.sportfestapp.models;

/**
 * Repräsentiert ein Team bei der Turniererstellung.
 */

public class TeamModel {
    //Attribute
    private String teamName;
    private String teamManager;

    //Konstruktor
    public TeamModel(String teamName, String teamManager){
        this.teamManager = teamManager;
        this.teamName = teamName;
    }

    //Get-Methoden
    public String getTeamName(){
        return teamName;
    }

    public String getTeamManager(){
        return teamManager;
    }
}
