package com.kasper201.beatkhanatest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerDetailFragment#} factory method to
 * create an instance of this fragment.
 */
public class PlayerDetailFragment extends Fragment {

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

        String playerId = playerInfo.getID();

        // setup badges
        getBadges(view, playerId, playerId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_player_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets the player information in the views
     * @param playerInfo The player information to set in the views. Gathered from the API response in previous fragments
     */
    private void setPlayerInfo(@NonNull PlayerInfo playerInfo)
    {
        // TODO: Possibly retreive the information from the API in this fragment instead of passing it from the previous fragment to avoid null playerInfo
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

    private void getBadges(View view, String blId, String ssId)
    {
        String ssBaseUrl = "https://scoresaber.com/api/player/";
        String blBaseUrl = "https://api.beatleader.xyz/player/";
        List<String> ssBadgeUrls = new ArrayList<>();
        List<String> ssBadgeDescriptions = new ArrayList<>();
        List<String> blBadgeUrls = new ArrayList<>();
        List<String> blBadgeDescriptions = new ArrayList<>();
        // TODO: Change array to be actual badge URLs
        // TODO: Add badge descriptions

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

                // Add badges to the layout
                LinearLayout ssBadges = view.findViewById(R.id.ssBadgeImages);
                addBadgesToLinearLayout(ssBadges, ssBadgeUrls, "N/A");
            }
            catch (JSONException e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
                },
                Throwable::printStackTrace);

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