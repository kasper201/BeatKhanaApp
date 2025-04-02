package com.kasper201.beatkhanatest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;

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

        // setup badges
        // TODO: Change array to be actual badge URLs
        // TODO: Add badge descriptions
        List<String> badgeUrls = new ArrayList<>(Arrays.asList("https://cdn.scoresaber.com/badges/supporter.png", "https://cdn.scoresaber.com/badges/BSWC-2020-1.png", "https://cdn.scoresaber.com/badges/BSWC-2022-1.gif", "https://cdn.scoresaber.com/badges/BSWC-2021-1.png", "https://cdn.scoresaber.com/badges/BSWC-2021-1.png"));

        LinearLayout ssBadges = view.findViewById(R.id.ssBadgeImages);
        LinearLayout blBadges = view.findViewById(R.id.blBadgeImages);
        addBadgesToLinearLayout(ssBadges, badgeUrls, "N/A");
        addBadgesToLinearLayout(blBadges, badgeUrls, "N/A");
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
     * Adds badge images to a LinearLayout in a dynamic grid
     * @param parentLayout The LinearLayout to add the badges to
     * @param badgeUrls The URLs of the badge images
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