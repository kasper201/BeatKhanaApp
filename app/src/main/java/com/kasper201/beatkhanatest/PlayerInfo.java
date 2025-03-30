// PlayerInfo.java
package com.kasper201.beatkhanatest;

public class PlayerInfo {
    private String username;
    private String ssPP;

    private String ssRank;
    private String ssRankChange;
    private String blRankChange;
    private String blPP;
    private String blRank;
    private String accuracy; // fetched from BeatLeader

    // Constructor
    public PlayerInfo(String username ,String ssPP, String ssRank, String blRank, String ssRankChange, String blRankChange, String blPP, String accuracy) {
        this.username = username;
        this.ssPP = ssPP;
        this.ssRank = ssRank;
        this.blPP = blPP;
        this.blRank = blRank;
        this.ssRankChange = ssRankChange;
        this.blRankChange = blRankChange;
        this.accuracy = accuracy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and Setters
    public String getSsPP() {
        return ssPP;
    }

    public void setSsPP(String ssPP) {
        this.ssPP = ssPP;
    }

    public String getSsRank() {
        return ssRank;
    }

    public void setSsRank(String ssRank) {
        this.ssRank = ssRank;
    }

    public String getBlRank() {
        return blRank;
    }

    public void setBlRank(String blRank) {
        this.blRank = blRank;
    }

    public String getSsRankChange() {
        return ssRankChange;
    }

    public void setSsRankChange(String ssRankChange) {
        this.ssRankChange = ssRankChange;
    }

    public String getBlRankChange() {
        return blRankChange;
    }

    public void setBlRankChange(String blRankChange) {
        this.blRankChange = blRankChange;
    }

    public String getBlPP() {
        return blPP;
    }

    public void setBlPP(String blPP) {
        this.blPP = blPP;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}