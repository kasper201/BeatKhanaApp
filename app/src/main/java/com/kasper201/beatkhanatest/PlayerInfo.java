// PlayerInfo.java
package com.kasper201.beatkhanatest;

public class PlayerInfo {
    private String ssPP;
    private String blRank;
    private String ssRankChange;
    private String blRankChange;
    private String blPP;
    private String accuracy;

    // Constructor
    public PlayerInfo(String ssPP, String blRank, String ssRankChange, String blRankChange, String blPP, String accuracy) {
        this.ssPP = ssPP;
        this.blRank = blRank;
        this.ssRankChange = ssRankChange;
        this.blRankChange = blRankChange;
        this.blPP = blPP;
        this.accuracy = accuracy;
    }

    // Getters and Setters
    public String getSsPP() {
        return ssPP;
    }

    public void setSsPP(String ssPP) {
        this.ssPP = ssPP;
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