package de.glaeseker_tom.sportfestapp;

/**
 * Created by tomgl on 19.08.2017.
 */

public class PlacementModel {

    private int placementId;
    private String team;
    private int points;
    private String goalDifference;
    public PlacementModel(){

    }
    public PlacementModel(int pPlacementID , String pTeam, int pPoints, String pGoalDifference){
        placementId = pPlacementID;
        team = pTeam;
        points = pPoints;
        goalDifference = pGoalDifference;
    }

    public int getPlacementId() {
        return placementId;
    }

    public void setPlacementId(int placementID) {
        this.placementId = placementID;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(String goalDifference) {
        this.goalDifference = goalDifference;
    }

}
