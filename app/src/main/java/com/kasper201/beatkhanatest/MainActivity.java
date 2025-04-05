package com.kasper201.beatkhanatest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kasper201.beatkhanatest.databinding.ActivityMainBinding;

import java.util.Objects;

// TODO: Implement different behavior for rotating the device.
// TODO: Implement a back stack for overview fragments

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement different behavior for rotating the device.
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit(); // Load the HomeFragment by default

        // Use if-else statements as a workaround for the switch statement not functioning properly
        if(binding.bottomNavigationView != null) {
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
                    return true;
                } else if (itemId == R.id.action_recent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RecentFragment()).commit();
                    return true;
                } else if (itemId == R.id.action_search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SearchFragment()).commit();
                    return true;
                } else if (itemId == R.id.action_saved) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SavedFragment()).commit();
                    return true;
                } else if (itemId == R.id.action_user_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new UserFragment()).commit();
                    return true;
                } else {
                    return false;
                }
            });
        }
    }
}