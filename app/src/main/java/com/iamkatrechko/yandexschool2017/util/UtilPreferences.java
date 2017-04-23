package com.iamkatrechko.yandexschool2017.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Утилитный класс для работы с preference-данными на устройстве
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class UtilPreferences {

    /** Ключ настройки. Отображаются ли в истории только избранные записи */
    private static final String PREF_SHOW_ONLY_FAVORITE = "SP_ONLY_FAVORITE";
    /** Ключ настройки. Последний выбранный исходный язык перевода */
    private static final String PREF_LAST_LANG_FROM = "SP_LAST_LANG_FROM";
    /** Ключ настройки. Последний выбранный конечный язык перевода */
    private static final String PREF_LAST_LANG_TO = "SP_LAST_LANG_TO";

    /**
     * Отображатся ли в списке записей истории только избранные
     * @param context контекст
     * @return {@code true} - отображаются только избранные, {@code false} - все
     */
    public static boolean isShowOnlyFavorite(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_SHOW_ONLY_FAVORITE, false);
    }

    /**
     * Сохраняет булевую запись о том, отображаются ли в списке записей только избранные
     * @param context        контекст
     * @param isOnlyFavorite {@code true} - отображаются только избранные, {@code false} - все
     */
    public static void setShowOnlyFavorite(Context context, boolean isOnlyFavorite) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_SHOW_ONLY_FAVORITE, isOnlyFavorite)
                .apply();
    }

    /**
     * Возвращает последний выбранный исходный язык перевода
     * @param context конекст
     * @return последний выбранный исходный язык перевода
     */
    public static String getLastLangFrom(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_LANG_FROM, "ru");
    }

    /**
     * Устанавливает последний выбранный исходный язык перевода
     * @param context  конекст
     * @param langFrom последний выбранный исходный язык перевода
     */
    public static void setLastLangFrom(Context context, String langFrom) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_LANG_FROM, langFrom)
                .apply();
    }

    /**
     * Возвращает последний выбранный конечный язык перевода
     * @param context конекст
     * @return последний выбранный конечный язык перевода
     */
    public static String getLastLangTo(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_LANG_TO, "en");
    }

    /**
     * Устанавливает последний выбранный конечный язык перевода
     * @param context конекст
     * @param langTo  последний выбранный конечный язык перевода
     */
    public static void setLastLangTo(Context context, String langTo) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_LANG_TO, langTo)
                .apply();
    }
}
