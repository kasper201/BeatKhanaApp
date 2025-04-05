package com.kasper201.beatkhanatest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class PlayerInfoAdapterLand extends RecyclerView.Adapter<PlayerInfoAdapterLand.ViewHolder> {

    private Context context;
    private List<PlayerInfo> playerInfoList;
    private OnItemClickListener onItemClickListener;

    public PlayerInfoAdapterLand(Context context, List<PlayerInfo> playerInfoList) {
        this.context = context;
        this.playerInfoList = playerInfoList;
    }

    public interface OnItemClickListener {
        void onItemClick(PlayerInfo playerInfo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_player_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlayerInfo playerInfo = playerInfoList.get(position);

        holder.username.setText(playerInfo.getUsername());
        holder.ssPP.setText(playerInfo.getSsPP());
        holder.ssRank.setText(playerInfo.getSsRank());
        holder.blRank.setText(playerInfo.getBlRank());
        holder.ssRankChange.setText(playerInfo.getSsRankChange());
        holder.blRankChange.setText(playerInfo.getBlRankChange());
        holder.blPP.setText(playerInfo.getBlPP());
        holder.accuracy.setText(playerInfo.getAccuracy());

        Glide.with(context).load("https://flagcdn.com/w320/" + playerInfo.getCountry().toLowerCase() + ".png").into(holder.countryFlag);
        Glide.with(context).load(playerInfo.getAvatar()).into(holder.playerProfile);

        if (playerInfo.getSsRankChange().contains("-")) {
            holder.ssRankChange.setTextColor(context.getResources().getColor(R.color.LossRed, null));
        } else if (Objects.equals(playerInfo.getSsRankChange(), "0")) {
            holder.ssRankChange.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            if (playerInfo.getRankChangeInt("ss") == 999999)
                holder.ssRankChange.setText("+∞");
            holder.ssRankChange.setTextColor(context.getResources().getColor(R.color.GainGreen, null));
        }
        if (playerInfo.getBlRankChange().contains("-")) {
            holder.blRankChange.setTextColor(context.getResources().getColor(R.color.LossRed, null));
        } else if (Objects.equals(playerInfo.getBlRankChange(), "0")) {
            holder.blRankChange.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            if (playerInfo.getRankChangeInt("bl") == 999999)
                holder.blRankChange.setText("+∞");
            holder.blRankChange.setTextColor(context.getResources().getColor(R.color.GainGreen, null));
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(playerInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView countryFlag, playerProfile;
        TextView username, ssPP, ssRank, ssRankChange, blPP, blRank, blRankChange, accuracy;

        public ViewHolder(View itemView) {
            super(itemView);
            countryFlag = itemView.findViewById(R.id.countryFlag);
            username = itemView.findViewById(R.id.username);
            ssPP = itemView.findViewById(R.id.ssPP);
            ssRank = itemView.findViewById(R.id.ssRank);
            ssRankChange = itemView.findViewById(R.id.ssRankChange);
            blPP = itemView.findViewById(R.id.blPP);
            blRank = itemView.findViewById(R.id.blRank);
            blRankChange = itemView.findViewById(R.id.blRankChange);
            accuracy = itemView.findViewById(R.id.accuracy);
            playerProfile = itemView.findViewById(R.id.playerprofile);
        }
    }
}