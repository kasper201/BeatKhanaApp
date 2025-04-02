// PlayerInfo.java
package com.kasper201.beatkhanatest;

import java.io.Serializable;
import java.util.List;

public class PlayerInfo implements Serializable {
    private String id;
    private String username;
    private String ssPP;

    private String ssRank;
    private String ssRankChange;
    private String blRankChange;
    private String blPP;
    private String blRank;
    private String accuracy; // fetched from BeatLeader
    private String country; // fetched from BeatLeader
    private String avatar; // fetched from BeatLeader
    private List<String> blRankHistory;
    private List<String> ssRankHistory;

    // Constructor
    public PlayerInfo(String id, String username ,String ssPP, String ssRank, String blRank, String ssRankChange, String blRankChange, String blPP, String accuracy, String country, String avatar) {
        this.id = id;
        this.username = username;
        this.ssPP = ssPP;
        this.ssRank = ssRank;
        this.blPP = blPP;
        this.blRank = blRank;
        this.ssRankChange = ssRankChange;
        this.blRankChange = blRankChange;
        this.accuracy = accuracy;
        this.country = country;
        this.avatar = avatar;
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

    public int getBlRankAsInt() {
        return Integer.parseInt(blRank.replace("#", ""));
    }

    public int getRankChangeInt(String leaderboard) {
        if(leaderboard.equals("ss")) {
            return Integer.parseInt(ssRankChange);
        } else {
            return Integer.parseInt(blRankChange);
        }
    }

    public String getSsRankChange() {
        if(Integer.parseInt(ssRankChange) > 0)
            return "+" + ssRankChange;
        return ssRankChange;
    }

    public void setSsRankChange(String ssRankChange) {
        this.ssRankChange = ssRankChange;
    }

    public String getBlRankChange() {
        if(Integer.parseInt(blRankChange) > 0)
            return "+" + blRankChange;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
}