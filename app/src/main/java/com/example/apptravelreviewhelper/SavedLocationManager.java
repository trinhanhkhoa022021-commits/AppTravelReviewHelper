package com.example.apptravelreviewhelper;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class SavedLocationManager {
    private static final String PREF_NAME = "saved_locations";
    private static final String KEY_IDS = "saved_ids";

    public static boolean isSaved(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> savedIds = prefs.getStringSet(KEY_IDS, new HashSet<>());
        return savedIds != null && savedIds.contains(id);
    }

    public static boolean toggleSave(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> savedIds = new HashSet<>(prefs.getStringSet(KEY_IDS, new HashSet<>()));
        boolean nowSaved;
        if (savedIds.contains(id)) {
            savedIds.remove(id);
            nowSaved = false;
        } else {
            savedIds.add(id);
            nowSaved = true;
        }
        prefs.edit().putStringSet(KEY_IDS, savedIds).apply();
        return nowSaved;
    }

    public static Set<String> getSavedIds(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_IDS, new HashSet<>());
    }
}
