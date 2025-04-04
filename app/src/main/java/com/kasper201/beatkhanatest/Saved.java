package com.kasper201.beatkhanatest;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Saved {

    private static final String SAVED_PLAYERS = "saved_player_ids.txt";
    private static final String RECENT_PLAYERS = "recent_player_ids.txt";
    private Context context;

    public Saved(Context context) {
        this.context = context;
    }

    /**
     * Saves player IDs to a file.
     *
     * @param playerIds A list of player IDs to save.
     */
    public void savePlayerIds(List<String> playerIds) {
        try (FileOutputStream fos = context.openFileOutput(SAVED_PLAYERS, Context.MODE_PRIVATE)) {
            for (String id : playerIds) {
                fos.write((id + ",").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads player IDs from the saved file.
     *
     * @return A list of player IDs.
     */
    public List<String> loadSavedPlayerIds() {
        List<String> playerIds = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(SAVED_PLAYERS)) {
            int size;
            StringBuilder stringBuilder = new StringBuilder();
            while ((size = fis.read()) != -1) {
                stringBuilder.append((char) size);
            }
            String[] ids = stringBuilder.toString().split(",");
            for (String id : ids) {
                if (!id.isEmpty()) {
                    playerIds.add(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerIds;
    }

    /**
     * Adds a player ID to the saved list if it doesn't already exist.
     *
     * @param playerId The player ID to add.
     */
    public void addSavedPlayerId(String playerId) {
        List<String> playerIds = loadSavedPlayerIds();
        if (!playerIds.contains(playerId)) {
            playerIds.add(playerId);
            savePlayerIds(playerIds);
        }
    }

    /**
     * Removes a player ID from the saved list if it exists.
     *
     * @param playerId The player ID to remove.
     */
    public void removeSavedPlayerId(String playerId) {
        List<String> playerIds = loadSavedPlayerIds();
        if (playerIds.contains(playerId)) {
            playerIds.remove(playerId);
            savePlayerIds(playerIds);
        }
    }

    /**
     * Checks if a player ID exists in the saved list.
     *
     * @param playerId The player ID to check.
     * @return True if the player ID exists, false otherwise.
     */
    public boolean idSaved(String playerId) {
        List<String> playerIds = loadSavedPlayerIds();
        return playerIds.contains(playerId);
    }

    public void saveRecentPlayerIds(List<String> playerIds) {
        try (FileOutputStream fos = context.openFileOutput(RECENT_PLAYERS, Context.MODE_PRIVATE)) {
            for (String id : playerIds) {
                fos.write((id + ",").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRecentPlayerId(String playerId) {
        List<String> playerIds = loadRecentPlayerIds();
        if (!playerIds.contains(playerId)) {
            playerIds.add(playerId);
            saveRecentPlayerIds(playerIds);
        }
    }

    public List<String> loadRecentPlayerIds()
    {
        List<String> playerIds = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(RECENT_PLAYERS)) {
            int size;
            StringBuilder stringBuilder = new StringBuilder();
            while ((size = fis.read()) != -1) {
                stringBuilder.append((char) size);
            }
            String[] ids = stringBuilder.toString().split(",");
            for (String id : ids) {
                if (!id.isEmpty()) {
                    playerIds.add(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerIds;
    }
}