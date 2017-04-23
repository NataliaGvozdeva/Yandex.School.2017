package com.iamkatrechko.yandexschool2017.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;
import static com.iamkatrechko.yandexschool2017.database.HistoryDatabaseHelper.*;

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
        boolean exist = isExistRecord(context, source, translate, langFrom, langTo);
        if (!exist) {
            ContentValues values = Record.getContentValues(source,
                    translate,
                    langFrom,
                    langTo,
                    isFavorite);
            context.getContentResolver().insert(Record.CONTENT_URI, values);
        }
    }

    /**
     * Удаляет запись из истории переводов
     * @param context контекст
     * @param id      идентификатор удаляемой записи
     */
    public static void deleteRecord(Context context, long id) {
        context.getContentResolver().delete(Record.buildClipUri(id), null, null);
    }

    /**
     * Возвращает запись по ее параметрам
     * @param context   контекст
     * @param source    исходный текст перевода
     * @param translate конечный текст перевода
     * @param langFrom  исходный язык перевода
     * @param langTo    конечный язык перевода
     * @return имеется ли текущая запись в истории
     */
    public static Cursor getRecord(Context context, String source, String translate, String langFrom, String langTo) {
        return context.getContentResolver().query(Record.CONTENT_URI,
                null,
                Record.COLUMN_SOURCE + " = ? AND " + Record.COLUMN_TRANSLATE + " = ? AND " +
                        Record.COLUMN_FROM_LANG + " = ? AND " + Record.COLUMN_TO_LANG + " = ?",
                new String[]{source, translate, langFrom, langTo},
                null);
    }

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
        Cursor cursor = getRecord(context, source, translate, langFrom, langTo);
        return (cursor != null ? cursor.getCount() : 0) != 0;
    }

    /**
     * Проверяет наличие записи в истории
     * @param context   контекст
     * @param source    исходный текст перевода
     * @param translate конечный текст перевода
     * @param langFrom  исходный язык перевода
     * @param langTo    конечный язык перевода
     * @return имеется ли текущая запись в истории
     */
    public static boolean isFavoriteRecord(Context context, String source, String translate, String langFrom, String langTo) {
        Cursor cursor = context.getContentResolver().query(Record.CONTENT_URI,
                null,
                Record.COLUMN_SOURCE + " = ? AND " + Record.COLUMN_TRANSLATE + " = ? AND " +
                        Record.COLUMN_FROM_LANG + " = ? AND " + Record.COLUMN_TO_LANG + " = ? AND " +
                        Record.COLUMN_IS_FAVORITE + " = 1",
                new String[]{source, translate, langFrom, langTo},
                null);

        return (cursor != null ? cursor.getCount() : 0) != 0;
    }

    /**
     * Меняет принадлежность записи к избранным.
     * Если запись была избранной, то делает ее обычной и наоборот.
     * @param context   контекст
     * @param source    исходный текст перевода
     * @param translate конечный текст перевода
     * @param langFrom  исходный язык перевода
     * @param langTo    конечный язык перевода
     * @return новое состояние записи (избранность)
     */
    public static boolean changeFavoriteRecord(Context context, String source, String translate, String langFrom, String langTo) {
        Cursor cursor = getRecord(context, source, translate, langFrom, langTo);
        if (cursor == null || cursor.getCount() == 0) {
            addRecord(context, source, translate, langFrom, langTo, true);
            return true;
        }
        cursor.moveToFirst();
        HistoryRecordCursor recordCursor = new HistoryRecordCursor(cursor);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Record.COLUMN_IS_FAVORITE, recordCursor.isFavorite() ? 0 : 1);
        context.getContentResolver().update(Record.buildClipUri(recordCursor.getID()),
                contentValues,
                null,
                null);
        return !recordCursor.isFavorite();
    }
}
