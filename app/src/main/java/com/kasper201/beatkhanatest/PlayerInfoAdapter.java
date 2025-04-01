package com.kasper201.beatkhanatest;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Objects;

import com.bumptech.glide.Glide;

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

        ImageView countryFlag = convertView.findViewById(R.id.countryFlag);
        TextView username = convertView.findViewById(R.id.username);
        TextView ssPP = convertView.findViewById(R.id.ssPP);
        TextView ssRank = convertView.findViewById(R.id.ssRank);
        TextView ssRankChange = convertView.findViewById(R.id.ssRankChange);
        TextView blPP = convertView.findViewById(R.id.blPP);
        TextView blRank = convertView.findViewById(R.id.blRank);
        TextView blRankChange = convertView.findViewById(R.id.blRankChange);
        TextView accuracy = convertView.findViewById(R.id.accuracy);
        ImageView playerProfile = convertView.findViewById(R.id.playerprofile);

        username.setText(playerInfo.getUsername());
        ssPP.setText(playerInfo.getSsPP());
        ssRank.setText(playerInfo.getSsRank());
        blRank.setText(playerInfo.getBlRank());
        ssRankChange.setText(playerInfo.getSsRankChange());
        blRankChange.setText(playerInfo.getBlRankChange());
        blPP.setText(playerInfo.getBlPP());
        accuracy.setText(playerInfo.getAccuracy());

        // load country flag from url
        Glide.with(context).load("https://flagcdn.com/w320/" + playerInfo.getCountry().toLowerCase() + ".png").into(countryFlag);

        // load playerProfile picture from url
        Glide.with(context).load(playerInfo.getAvatar()).into(playerProfile);

        // TODO: Set correct themes for rank change
        // Set correct color for rank change
        if (playerInfo.getSsRankChange().contains("-")) {
            ssRankChange.setTextColor(context.getResources().getColor(R.color.LossRed, null));
        } else if (Objects.equals(playerInfo.getSsRankChange(), "0")) {
            ssRankChange.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            ssRankChange.setTextColor(context.getResources().getColor(R.color.GainGreen, null));
        }
        if (playerInfo.getBlRankChange().contains("-")) {
            blRankChange.setTextColor(context.getResources().getColor(R.color.LossRed, null));
        } else if (Objects.equals(playerInfo.getBlRankChange(), "0")) {
            blRankChange.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            blRankChange.setTextColor(context.getResources().getColor(R.color.GainGreen, null));
        }

        return convertView;
    }
}