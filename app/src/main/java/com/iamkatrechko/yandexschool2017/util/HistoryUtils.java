package com.iamkatrechko.yandexschool2017.util;

import android.content.ContentValues;
import android.content.Context;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;

/**
 * Утилитный класс по работе с историей
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class HistoryUtils {

    /**
     * Проверяет наличие записи в истории
     * @param context    контекст
     * @param source     исходный текст перевода
     * @param translate  конечный текст перевода
     * @param langFrom   исходный язык перевода
     * @param langTo     конечный язык перевода
     * @param isFavorite принадлежность к избранным
     */
    public static void addRecord(Context context, String source, String translate, String langFrom, String langTo, boolean isFavorite) {
        boolean exist = ContentProviderUtils.isExistRecord(context, source, translate, langFrom, langTo);
        if (!exist) {
            ContentValues values = Record.getContentValues(source,
                    translate,
                    langFrom,
                    langTo,
                    isFavorite);
            context.getContentResolver().insert(Record.CONTENT_URI, values);
        }
    }
}
