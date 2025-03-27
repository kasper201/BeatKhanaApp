package com.kasper201.beatkhanatest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ListView listView;
    private PlayerInfoAdapter adapter;
    private List<PlayerInfo> playerInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.listView);

        // Initialize playerInfoList with data
        playerInfoList = new ArrayList<>();
        playerInfoList.add(new PlayerInfo("1000pp", "Rank 1"));
        playerInfoList.add(new PlayerInfo("900pp", "Rank 2"));
        // Add more items as needed

        adapter = new PlayerInfoAdapter(getContext(), playerInfoList);
        listView.setAdapter(adapter);

        return view;
    }
}