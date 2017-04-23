package com.iamkatrechko.yandexschool2017.util;

import android.content.Context;
import android.database.Cursor;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;

/**
 * Утилитный класс по работе с контент провайдером
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class ContentProviderUtils {

    /**
     * Проверяет наличие записи в истории
     * @param context   контекст
     * @param source    исходный текст перевода
     * @param translate конечный текст перевода
     * @param langFrom  исходный язык перевода
     * @param langTo    конечный язык перевода
     * @return имеется ли текущая запись в истории
     */
    public static boolean isExistRecord(Context context, String source, String translate, String langFrom, String langTo) {
        Cursor cursor = context.getContentResolver().query(Record.CONTENT_URI,
                null,
                Record.COLUMN_SOURCE + " = ? AND " + Record.COLUMN_TRANSLATE + " = ? AND " +
                        Record.COLUMN_FROM_LANG + " = ? AND " + Record.COLUMN_TO_LANG + " = ?",
                new String[]{source, translate, langFrom, langTo},
                null);

        return (cursor != null ? cursor.getCount() : 0) != 0;
    }
}
