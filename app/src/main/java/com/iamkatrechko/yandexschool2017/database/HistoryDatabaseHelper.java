package com.iamkatrechko.yandexschool2017.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;

/**
 * База данных с историей переводов
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    /** Тег для логирования */
    private static final String TAG = HistoryDatabaseHelper.class.getSimpleName();

    /** Имя файла базы данных */
    private static final String DATABASE_NAME = "history.db";
    /** Версия базы данных */
    private static final int DATABASE_VERSION = 1;

    /** Команда SQL для создания таблицы history */
    private static final String CREATE_HISTORY_TABLE =
            "CREATE TABLE " + Record.TABLE_NAME + "(" +
                    Record._ID + " INTEGER PRIMARY KEY, " +
                    Record.COLUMN_SOURCE + " TEXT, " +
                    Record.COLUMN_TRANSLATE + " TEXT, " +
                    Record.COLUMN_FROM_LANG + " TEXT, " +
                    Record.COLUMN_TO_LANG + " TEXT, " +
                    Record.COLUMN_IS_FAVORITE + " INTEGER);";

    /**
     * Конструктор
     * @param context контекст
     */
    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HISTORY_TABLE);
        generateTestData(sqLiteDatabase);
        Log.d(TAG, "База данных успешно создана");
    }

    /**
     * Генерирует демоданные в таблице
     * @param db база данных для генерации
     */
    private void generateTestData(SQLiteDatabase db) {
        insertRecord(db, "streamline", "оптимизация", "en", "ru", true);
        insertRecord(db, "enigma", "Энигма", "en", "ru", true);
        insertRecord(db, "streamlined", "обтекаемый", "en", "ru", false);
        insertRecord(db, "Привет", "Hi", "ru", "en", false);
        insertRecord(db, "распространитель", "distributor", "ru", "en", false);
        insertRecord(db, "purveyor", "поставщик", "en", "ru", false);
        insertRecord(db, "purveyors", "заготовителей", "en", "ru", false);
        insertRecord(db, "acquaintance", "знакомство", "en", "ru", true);
    }

    /**
     * Добавляет одну запись истории в базу данных
     * @param db         база данных
     * @param source     исходный текст перевода
     * @param translate  конечный текст перевода
     * @param langFrom   исходный язык перевода
     * @param langTo     конечный язык перевода
     * @param isFavorite принадлежность записи к избранным
     */
    private void insertRecord(SQLiteDatabase db, String source, String translate, String langFrom, String langTo, boolean isFavorite) {
        String query = "INSERT INTO " + Record.TABLE_NAME + " (source, translate, from_lang, to_lang, is_favorite) values(" +
                "'" + source + "', " +
                "'" + translate + "', " +
                "'" + langFrom + "', " +
                "'" + langTo + "', " +
                (isFavorite ? "1" : "0") + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    /** Оболочка для курсора с историей */
    public static class HistoryRecordCursor extends CursorWrapper {

        /**
         * Конструктор
         * @param cursor курсор
         */
        public HistoryRecordCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         * Возвращает идентификатор записи
         * @return идентификатор записи
         */
        public long getID() {
            return getWrappedCursor().getLong(getColumnIndex(Record._ID));
        }

        /**
         * Возвращает исходный текст перевода
         * @return исходный текст перевода
         */
        public String getSource() {
            return getWrappedCursor().getString(getColumnIndex(Record.COLUMN_SOURCE));
        }

        /**
         * Возвращает переведенный текст
         * @return переведенный текст
         */
        public String getTranslate() {
            return getWrappedCursor().getString(getColumnIndex(Record.COLUMN_TRANSLATE));
        }

        /**
         * Возвращает исходный язык перевода
         * @return исходный язык перевода
         */
        public String getFromLanguage() {
            return getWrappedCursor().getString(getColumnIndex(Record.COLUMN_FROM_LANG));
        }

        /**
         * Возвращает конечный язык перевода
         * @return конечный язык перевода
         */
        public String getToLanguage() {
            return getWrappedCursor().getString(getColumnIndex(Record.COLUMN_TO_LANG));
        }

        /**
         * Является ли запись избранной
         * @return {@code true} - избранная, {@code false} - не избранная
         */
        public boolean isFavorite() {
            int buf = getWrappedCursor().getInt(getColumnIndex(Record.COLUMN_IS_FAVORITE));
            return (buf == 1);
        }
    }
}
