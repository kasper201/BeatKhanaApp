package com.kasper201.beatkhanatest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class PlayerInfoAdapter extends BaseAdapter {

    private Context context;
    private List<PlayerInfo> playerInfoList;

    public PlayerInfoAdapter(Context context, List<PlayerInfo> playerInfoList) {
        this.context = context;
        this.playerInfoList = playerInfoList;
    }

    @Override
    public int getCount() {
        return playerInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return playerInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_player_info, parent, false);
        }

        PlayerInfo playerInfo = playerInfoList.get(position);

        TextView username = convertView.findViewById(R.id.username);
        TextView ssPP = convertView.findViewById(R.id.ssPP);
        TextView ssRank = convertView.findViewById(R.id.ssRank);
        TextView ssRankChange = convertView.findViewById(R.id.ssRankChange);
        TextView blPP = convertView.findViewById(R.id.blPP);
        TextView blRank = convertView.findViewById(R.id.blRank);
        TextView blRankChange = convertView.findViewById(R.id.blRankChange);
        TextView accuracy = convertView.findViewById(R.id.accuracy);

        username.setText(playerInfo.getUsername());
        ssPP.setText(playerInfo.getSsPP());
        ssRank.setText(playerInfo.getSsRank());
        blRank.setText(playerInfo.getBlRank());
        ssRankChange.setText(playerInfo.getSsRankChange());
        blRankChange.setText(playerInfo.getBlRankChange());
        blPP.setText(playerInfo.getBlPP());
        accuracy.setText(playerInfo.getAccuracy());

        return convertView;
    }
}