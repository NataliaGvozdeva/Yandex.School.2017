package com.iamkatrechko.yandexschool2017.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Утилитный класс для работы с preference-данными на устройстве
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class UtilPreferences {

    private static final String PREF_SHOW_ONLY_FAVORITE = "spOnlyFavorite";

    /**
     * Отображатся ли в списке записей только избранные
     */
    public static boolean isShowOnlyFavorite(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_SHOW_ONLY_FAVORITE, false);
    }

    /**
     * Сохраняет булевую запись о том, отображаются ли в списке записей только избранные
     */
    public static void setShowOnlyFavorite(Context context, boolean isOnlyFavorite) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_SHOW_ONLY_FAVORITE, isOnlyFavorite)
                .apply();
    }
}
