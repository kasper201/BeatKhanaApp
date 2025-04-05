package com.kasper201.beatkhanatest;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerDetailFragment#} factory method to
 * create an instance of this fragment.
 */
public class PlayerDetailFragment extends Fragment {

    private String playerId;
    private boolean playerSaved = false;

    @Override
    public void onPause() {
        super.onPause();
        if(playerSaved)
        {
            Saved saved = new Saved(getContext());
            saved.addSavedPlayerId(playerId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Saved saved = new Saved(getContext());

        // TODO: Possibly retrieve the information from the API in this fragment instead of passing it from the previous fragment to avoid null playerInfo
        // Handle arguments
        Bundle arguments = getArguments();
        if (arguments == null) { // TODO: handle null arguments
            return;
        }
        PlayerInfo playerInfo = (PlayerInfo) arguments.getSerializable("playerInfo");
        if (playerInfo == null) { // TODO: handle null playerInfo
            Toast.makeText(getContext(), "Player info is null", Toast.LENGTH_SHORT).show();
            return;
        }

        setPlayerInfo(playerInfo);
        playerId = playerInfo.getID();

        // TODO: Make back button functional
        // TODO: Correct colour and font type of toolbar
        // Setup toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setTitle("Player Details");
        }

        // Set up the toolbar menu and make sure menu is loaded before the menu
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // handle saving

                playerSaved = saved.idSaved(playerId);
                if (playerSaved) {
                    MenuItem saveItem = toolbar.getMenu().findItem(R.id.action_save);
                    if (saveItem != null) {
                        saveItem.setIcon(R.drawable.save_icon_filled);
                    }
                }

                // setup data
                getData(view, playerId, playerId);

                // add to recent players
                saved.addRecentPlayerId(playerId);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_player_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            // Handle back button click
            return true;
        } else if (id == R.id.action_save) {
            // TODO: Adapt for (un)save functionality
            Saved saved = new Saved(getContext());
            if(!playerSaved) {
                playerSaved = true;
                saved.addSavedPlayerId(playerId);
                item.setIcon(R.drawable.save_icon_filled);
            } else {
                playerSaved = false;
                saved.removeSavedPlayerId(playerId);
                item.setIcon(R.drawable.save_icon);
            }
            Toast.makeText(getContext(), playerSaved ? "Saved" : "Removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the player information in the views
     * @param playerInfo The player information to set in the views. Gathered from the API response in previous fragments
     */
    private void setPlayerInfo(@NonNull PlayerInfo playerInfo)
    {
        // Set player information in the views
        TextView username = getView().findViewById(R.id.username);
        TextView ssRank = getView().findViewById(R.id.ssRank);
        TextView ssRankChange = getView().findViewById(R.id.ssRankChange);
        TextView ssPP = getView().findViewById(R.id.ssPP);
        TextView blRank = getView().findViewById(R.id.blRank);
        TextView blRankChange = getView().findViewById(R.id.blRankChange);
        TextView blPP = getView().findViewById(R.id.blPP);
        ImageView playerProfile = getView().findViewById(R.id.playerprofile);
        ImageView countryFlag = getView().findViewById(R.id.countryFlag);

        username.setText(playerInfo.getUsername());
        ssRank.setText(playerInfo.getSsRank());
        ssRankChange.setText(playerInfo.getSsRankChange());
        ssPP.setText(playerInfo.getSsPP());
        blRank.setText(playerInfo.getBlRank());
        blRankChange.setText(playerInfo.getBlRankChange());
        blPP.setText(playerInfo.getBlPP());

        // Load images using Glide
        Glide.with(this).load(playerInfo.getAvatar()).into(playerProfile);
        // Assuming you have a method to get the country flag URL
        Glide.with(this).load("https://flagcdn.com/w640/" + playerInfo.getCountry().toLowerCase() + ".png").into(countryFlag);

    }

    /**
     * Fetches the data from the API and adds them to the layout
     *
     * @param view The view to add the data to
     * @param blId The BeatLeader ID of the player
     * @param ssId The ScoreSaber ID of the player
     */
    private void getData(View view, String blId, String ssId)
    {
        String ssBaseUrl = "https://scoresaber.com/api/player/";
        String blBaseUrl = "https://api.beatleader.xyz/player/";
        List<String> ssBadgeUrls = new ArrayList<>();
        List<String> ssBadgeDescriptions = new ArrayList<>();
        List<String> ssRankHistory = new ArrayList<>();
        List<String> blBadgeUrls = new ArrayList<>();
        List<String> blBadgeDescriptions = new ArrayList<>();
        List<String> blRankHistory = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(2); // There are 2 (relevant) asynchronous tasks
        // TODO: Add badge descriptions

        // Wait for both tasks to complete
        new Thread(() -> {
            try {
                latch.await(); // Wait until the latch count reaches zero
                requireActivity().runOnUiThread(() -> {
                    LineChart chart = view.findViewById(R.id.lineChart);
                    setupLineChart(chart, blRankHistory, ssRankHistory);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Get ScoreSaber data
        mySingleton.getInstance(this.getContext()).addToRequestQueue(ssBaseUrl + ssId + "/full", ssResponse -> {
            try {
                JSONObject ssData = new JSONObject(ssResponse);
                if(ssData.has("error")) {
                    Toast.makeText(getContext(), "Error: " + ssData.getString("error"), Toast.LENGTH_LONG).show();
                    return;
                }
                JSONArray ssBadgesArray = ssData.getJSONArray("badges");
                for (int i = 0; i < ssBadgesArray.length(); i++) {
                    JSONObject badgeObject = ssBadgesArray.getJSONObject(i);
                    String badgeDescription = badgeObject.getString("description");
                    String badgeUrl = badgeObject.getString("image");

                    ssBadgeDescriptions.add(badgeDescription);
                    ssBadgeUrls.add(badgeUrl);
                }

                // Get rank history
                String[] ssHistoriesArray = ssData.getString("histories").split(",");
                Collections.addAll(ssRankHistory, ssHistoriesArray);

                // Add badges to the layout
                LinearLayout ssBadges = view.findViewById(R.id.ssBadgeImages);
                addBadgesToLinearLayout(ssBadges, ssBadgeUrls, "N/A");
            }
            catch (JSONException e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                latch.countDown(); // Decrement the latch count
            }
                },
                Throwable::printStackTrace);

        // get BeatLeader Badges
        mySingleton.getInstance(this.getContext()).addToRequestQueue(blBaseUrl + blId + "?stats=true&keepOriginalId=false&leaderboardContext=510", blResponse -> {
                    try {
                        JSONObject blData = new JSONObject(blResponse);
                        if(blData.has("error")) {
                            Toast.makeText(getContext(), "Error: " + blData.getString("error"), Toast.LENGTH_LONG).show();
                            return;
                        }
                        JSONArray blBadgesArray = blData.getJSONArray("badges");
                        for (int i = 0; i < blBadgesArray.length(); i++) {
                            JSONObject badgeObject = blBadgesArray.getJSONObject(i);
                            String badgeDescription = badgeObject.getString("description");
                            String badgeUrl = badgeObject.getString("image");

                            blBadgeDescriptions.add(badgeDescription);
                            blBadgeUrls.add(badgeUrl);
                        }

                        // Add badges to the layout
                        LinearLayout blBadges = view.findViewById(R.id.blBadgeImages);
                        addBadgesToLinearLayout(blBadges, blBadgeUrls, "N/A");
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);

        // get BeatLeader rank history
        mySingleton.getInstance(this.getContext()).addToRequestQueue(blBaseUrl + blId + "/history/compact?count=30&leaderboardContext=general", blHistoryResponse -> {
            try {
                JSONArray blHistoryArray = new JSONArray(blHistoryResponse);
                for (int i = 0; i < blHistoryArray.length(); i++) {
                    JSONObject historyObject = blHistoryArray.getJSONObject(i);
                    String rank = historyObject.getString("rank");
                    blRankHistory.add(rank);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                latch.countDown(); // Decrement the latch count
            }
        }, Throwable::printStackTrace);
    }

    // TODO: Add custom value formatter for Y-axis to use whole numbers
    // TODO: Correct Icon colours, labels and icons
    /**
     * Sets up the line chart with the given data
     *
     * @param lineChart The LineChart to set up
     * @param blRankHistory The BeatLeader rank history data
     * @param ssRankHistory The ScoreSaber rank history data
     */
    private void setupLineChart(LineChart lineChart, List<String> blRankHistory, List<String> ssRankHistory) {
        List<Entry> ssEntries = new ArrayList<>();
        List<Entry> blEntries = new ArrayList<>();

        int blOffset = 0;
        int ssOffset = 0;

        if(blRankHistory.size() < ssRankHistory.size())
        {
            blOffset = ssRankHistory.size() - blRankHistory.size();
        }
        else
        {
            ssOffset = blRankHistory.size() - ssRankHistory.size();
        }

        for (int i = 0; i < (blRankHistory.size() - ssOffset); i++) {
            blEntries.add(new Entry(i, Float.parseFloat(blRankHistory.get(i + ssOffset))));
        }

        for (int i = 0; i < (ssRankHistory.size() - blOffset); i++) {
            ssEntries.add(new Entry(i, Float.parseFloat(ssRankHistory.get(i + blOffset))));
        }

        // TODO: Correct colours, lables and icons
        LineDataSet blDataSet = new LineDataSet(blEntries, "BeatLeader Rank");
        blDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        blDataSet.setColor(Color.BLUE);
        blDataSet.setDrawCircles(false);
        blDataSet.setDrawValues(false);
        blDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet ssDataSet = new LineDataSet(ssEntries, "ScoreSaber Rank");
        ssDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ssDataSet.setColor(Color.RED);
        ssDataSet.setDrawCircles(false);
        ssDataSet.setDrawValues(false);
        ssDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(blDataSet, ssDataSet);
        lineChart.setData(lineData);

        // Set custom value formatter for Y-axis to use whole numbers
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setInverted(true);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setInverted(true);
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawGridLines(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setGranularityEnabled(false);
//        xAxis.setDrawLabels(false);

        lineChart.setVisibleXRangeMaximum(Math.min(blRankHistory.size(), ssRankHistory.size()));
        lineChart.moveViewToX(0);

        lineChart.invalidate(); // refresh
    }

    /**
     * Adds badge images to a LinearLayout in a dynamic grid
     *
     * @param parentLayout The LinearLayout to add the badges to
     * @param badgeUrls    The URLs of the badge images
     */
    private void addBadgesToLinearLayout(LinearLayout parentLayout, List<String> badgeUrls, String description) {
        int badgesPerRow = 4;
        int totalBadges = badgeUrls.size();
        int rows = (int) Math.ceil((double) totalBadges / badgesPerRow);

        for (int i = 0; i < rows; i++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            for (int j = 0; j < badgesPerRow; j++) {
                int badgeIndex = i * badgesPerRow + j;
                if (badgeIndex < totalBadges) {
                    ImageView imageView = new ImageView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(80), dpToPx(30));
                    params.setMargins(0, 0, dpToPx(8), dpToPx(8));
                    imageView.setLayoutParams(params);
                    Glide.with(this).load(badgeUrls.get(badgeIndex)).into(imageView);
                    imageView.setContentDescription(description);
                    rowLayout.addView(imageView);
                }
            }

            parentLayout.addView(rowLayout);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}