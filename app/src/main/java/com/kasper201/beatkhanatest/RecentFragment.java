package com.kasper201.beatkhanatest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentFragment extends Fragment {

    private List<PlayerInfo> playerInfoList;
    private PlayerInfoAdapter adapter;
    private int loadedPlayers = 0;
    List<String> recentPlayers;

    public RecentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ListView listView = view.findViewById(R.id.listView);

        // Setup toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        playerInfoList = new ArrayList<>();
        adapter = new PlayerInfoAdapter(getContext(), playerInfoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            PlayerInfo selectedPlayer = playerInfoList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("playerInfo", selectedPlayer);
            // Add other player details to the bundle as needed

            PlayerDetailFragment playerDetailFragment = new PlayerDetailFragment();
            playerDetailFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, playerDetailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        Saved saved = new Saved(getContext());
        // List of recently viewed player IDs
        recentPlayers = saved.loadRecentPlayerIds();

        for (int i = 0; i < recentPlayers.size(); i++) {
            String playerId = recentPlayers.get(i);
            // Fetch player data using the playerId
            fetchPlayerData(playerId, playerId); // Assuming blId and ssId are the same for testing
        }

        return view;
    }

    /**
     * Fetch player data from BeatLeader and ScoreSaber APIs.
     * @param blId BeatLeader ID
     * @param ssId ScoreSaber ID
     */
    private void fetchPlayerData(String blId, String ssId) {
        String blUrl = "https://api.beatleader.xyz/player/" + blId + "?stats=true&keepOriginalId=false&leaderboardContext=510";
        String ssUrl = "https://scoresaber.com/api/player/" + ssId + "/basic";



        mySingleton.getInstance(this.getContext()).addToRequestQueue(blUrl, blResponse -> {
            try {
                JSONObject blData = new JSONObject(blResponse);
                if (blData.has("error")) {
                    // Handle error
                    return;
                }

                String name = blData.getString("name");
                double blpp = blData.getDouble("pp");
                int blRank = blData.getInt("rank");
                int blRankChange = blData.getInt("lastWeekRank") - blRank;
                JSONObject scoreStats = blData.getJSONObject("scoreStats");
                double acc = scoreStats.getDouble("averageRankedAccuracy");
                String country = blData.getString("country");
                String avatar = blData.getString("avatar");

                fetchPlayerSSData(blId, ssUrl, name, blpp, blRank, blRankChange, acc, country, avatar);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

    }

    private void fetchPlayerSSData(String id, String ssUrl, String name, double blpp, int blRank, int blRankChange, double blAcc, String country, String avatar)
    {
        mySingleton.getInstance(this.getContext()).addToRequestQueue(ssUrl, ssResponse -> {
            try {
                JSONObject ssData = new JSONObject(ssResponse);
                if (ssData.has("error")) {
                    // Handle error
                    return;
                }

                double sspp = ssData.getDouble("pp");
                int ssRank = ssData.getInt("rank");

                String[] ssHistories = ssData.getString("histories").split(",");
                int ssRankChange = 0;
                if (ssHistories.length >= 7 && !ssHistories[ssHistories.length - 7].isEmpty()) {
                    ssRankChange = Integer.parseInt(ssHistories[ssHistories.length - 7]) - ssRank;
                } else if (ssHistories.length > 1) {
                    ssRankChange = Integer.parseInt(ssHistories[1]) - ssRank;
                } else {
                    ssRankChange = 999999;
                }

                playerInfoList.add(new PlayerInfo(id, name, String.format("%.2fpp", sspp), "#" + ssRank, "#" + blRank, "" + ssRankChange, "" + blRankChange, String.format("%.2fpp", blpp), String.format("%.2f%%", blAcc), country, avatar));
                loadedPlayers++;

                // When all players are loaded, sort and update the list
                if (loadedPlayers == recentPlayers.size()) {
                    playerInfoList.sort((p1, p2) -> {
                        int index1 = recentPlayers.indexOf(p1.getID());
                        int index2 = recentPlayers.indexOf(p2.getID());
                        return Integer.compare(index1, index2);
                    });
                }

                adapter.notifyDataSetChanged(); // Update the list view with the new data

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
    }
}