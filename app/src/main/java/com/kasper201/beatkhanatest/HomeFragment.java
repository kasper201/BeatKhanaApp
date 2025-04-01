package com.kasper201.beatkhanatest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// TODO: Move general player overview methods to OverviewFragment class

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private List<PlayerInfo> playerInfoList;
    private PlayerInfoAdapter adapter;
    private int totalPlayers = 0;
    private int loadedPlayers = 0;

    /**
     * Round a double to a specified number of decimal places
     *
     * @param value  the double to round
     * @param places the number of decimal places to round to
     * @return the rounded double
     * @// TODO: 3/31/25 Check if still necessary
     */
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    // Method to fetch player data
    private void fetchPlayerData() {
        String url = "https://api.beatleader.com/players?leaderboardContext=general&page=1&count=50&sortBy=pp&mapsType=ranked&ppType=general&order=desc&pp_range=,&score_range=,"; // base url

        mySingleton.getInstance(this.getContext()).addToRequestQueue(url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        totalPlayers = dataArray.length();
                        loadedPlayers = 0;
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject player = dataArray.getJSONObject(i);
                            String id = player.getString("id");
                            String name = player.getString("name");
                            double blpp = round(player.getDouble("pp"), 2);
                            int blRank = player.getInt("rank");
                            int blRankChange = blRank - player.getInt("lastWeekRank");
                            JSONObject scoreStats = player.getJSONObject("scoreStats");
                            double blAcc = round(scoreStats.getDouble("averageRankedAccuracy") * 100, 2);
                            String country = player.getString("country");
                            String avatar = player.getString("avatar");

                            // Fetch ScoreSaber data for each player
                            fetchScoreSaberData(id, name, blpp, blRank, blRankChange, blAcc, country, avatar);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
    }

    // Method to fetch ScoreSaber data
    private void fetchScoreSaberData(String id, String name, double blpp, int blRank, int blRankChange, double blAcc, String country, String avatar) {
        String ssBaseUrl = "https://scoresaber.com/api/player/";

        mySingleton.getInstance(this.getContext()).addToRequestQueue(ssBaseUrl + id + "/basic", ssResponse -> {
            try {
                JSONObject ssData = new JSONObject(ssResponse);
                double sspp = round(ssData.getDouble("pp"), 2);
                int ssRank = ssData.getInt("rank");
                String[] ssHistories = ssData.getString("histories").split(",");
                int ssRankChange = 0;
                if (ssHistories.length >= 7) {
                    ssRankChange = ssRank - Integer.parseInt(ssHistories[ssHistories.length - 7]);
                }
                else {
                    ssRankChange = 999999;
                }

                playerInfoList.add(new PlayerInfo(name, String.format("%.2fpp", sspp), "#" + ssRank, "#" + blRank, "" + ssRankChange, "" + blRankChange, String.format("%.2fpp", blpp), String.format("%.2f%%", blAcc), country, avatar));
                adapter.notifyDataSetChanged(); // Update the list view with the new data
                loadedPlayers++;

                // Sort the list when all players have been loaded
                if (loadedPlayers == totalPlayers) {
                    playerInfoList.sort(Comparator.comparingInt(PlayerInfo::getBlRankAsInt));
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = view.findViewById(R.id.listView);

        playerInfoList = new ArrayList<>();
        adapter = new PlayerInfoAdapter(getContext(), playerInfoList);
        listView.setAdapter(adapter);

        fetchPlayerData();

        return view;


    }
}