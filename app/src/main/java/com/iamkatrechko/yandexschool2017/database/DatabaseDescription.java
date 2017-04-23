package com.iamkatrechko.yandexschool2017.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Содержит описание таблицы
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class DatabaseDescription {

    /** Имя провайдера */
    public static final String AUTHORITY = "com.iamkatrechko.yandexschool2017.database";
    /** Базовый URI для взаимодействия с провайдером */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /** Вложенный класс, определяющий содержимое таблицы history */
    public static final class Record implements BaseColumns {

        /** Имя таблицы */
        public static final String TABLE_NAME = "history";
        /** Имя столбца с исходным текстом */
        public static final String COLUMN_SOURCE = "source";
        /** Имя столбца с переведенным текстом */
        public static final String COLUMN_TRANSLATE = "translate";
        /** Имя столбца с исходыным языком перевода */
        public static final String COLUMN_FROM_LANG = "from_lang";
        /** Имя столбца с конечным языком перевода */
        public static final String COLUMN_TO_LANG = "to_lang";
        /** Имя столбца с принадлежностью к избранным */
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        /** Объект Uri для таблицы history */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        /** Создание Uri для конкретной записи */
        public static Uri buildClipUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /** Возвращает пары ключ-значения по-умолчанию */
        public static ContentValues getDefaultContentValues() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SOURCE, "Привет");
            contentValues.put(COLUMN_TRANSLATE, "Hi");
            contentValues.put(COLUMN_FROM_LANG, "RU");
            contentValues.put(COLUMN_TO_LANG, "EN");
            contentValues.put(COLUMN_IS_FAVORITE, "0");
            return contentValues;
        }

        /**
         * Возвращает одну запись в виде пар ключ-значение
         * @param source     исходный текст перевода
         * @param translate  конечный текст перевода
         * @param langFrom   исходный язык перевода
         * @param langTo     конечный язык перевода
         * @param isFavorite принадлежность записи к избранным
         */
        public static ContentValues getContentValues(String source, String translate, String langFrom, String langTo, boolean isFavorite) {
            ContentValues contentValues = getDefaultContentValues();
            contentValues.put(COLUMN_SOURCE, source);
            contentValues.put(COLUMN_TRANSLATE, translate);
            contentValues.put(COLUMN_FROM_LANG, langFrom);
            contentValues.put(COLUMN_TO_LANG, langTo);
            contentValues.put(COLUMN_IS_FAVORITE, isFavorite ? "1" : "0");
            return contentValues;
        }
    }
}
