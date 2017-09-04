package de.glaeseker_tom.sportfestapp.models;

/**
 * Das PlacementModel enthält alle Daten für einen Eintrag in der ListView des PlacementFragments.
 * Des Weiteren besitzt es die benötigten Getter.
 */

public class PlacementModel {

    //Attribute
    private int placementId;
    private String team;
    private int points;
    private String goalDifference;

    //Konstruktor
    public PlacementModel(int pPlacementID , String pTeam, int pPoints, String pGoalDifference){
        placementId = pPlacementID;
        team = pTeam;
        points = pPoints;
        goalDifference = pGoalDifference;
    }

    //Get-Methoden
    public int getPlacementId() {
        return placementId;
    }

    public String getTeam() {
        return team;
    }

    public int getPoints() {
        return points;
    }

    public String getGoalDifference() {
        return goalDifference;
    }
}
