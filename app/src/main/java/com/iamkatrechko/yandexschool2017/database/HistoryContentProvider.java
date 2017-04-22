package com.iamkatrechko.yandexschool2017.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.iamkatrechko.yandexschool2017.R;
import com.iamkatrechko.yandexschool2017.database.DatabaseDescription.Record;

/**
 * Контент провайдер для работы с базой данных истории переводов
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class HistoryContentProvider extends ContentProvider {

    /** Тег для логирования */
    private static final String TAG = HistoryContentProvider.class.getSimpleName();

    /** Помощник для работы с базой данных */
    private HistoryDatabaseHelper dbHelper;
    /** UriMatcher, помогающий провайдеру определить выполняемую операцию */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Константы, используемые для определения выполняемой операции
    /** Запрос на работу с одиночной записью */
    private static final int ONE_RECORD = 1;                                                          // Одна запись
    /** Запрос для работы с несколькими записями */
    private static final int RECORDS = 2;                                                             // Таблица записей

    // Статический блок для настройки UriMatcher объекта ContentProvider
    static {
        // Uri для записи с заданным идентификатором
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, Record.TABLE_NAME + "/#", ONE_RECORD);
        // Uri для работы с несколькими записями
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, Record.TABLE_NAME, RECORDS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new HistoryDatabaseHelper(getContext());
        Log.d(TAG, "Объект ContentProvider создан успешно");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "Запрос на выборку");
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case ONE_RECORD:
                queryBuilder.setTables(Record.TABLE_NAME);
                queryBuilder.appendWhere(
                        Record._ID + "=" + uri.getLastPathSegment());
                break;
            case RECORDS:
                queryBuilder.setTables(Record.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // Выполнить запрос для получения одной или всех записей
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        // Настройка отслеживания изменений в контенте
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "Запрос на вставку");
        Uri newUri;
        switch (uriMatcher.match(uri)) {
            case RECORDS:
                // При успехе возвращается идентификатор новой записи
                long rowId = dbHelper.getWritableDatabase().insert(
                        Record.TABLE_NAME, null, values);
                // Если запись была вставлеан, создать подходящий Uri;
                // в противном случае выдать исключение
                if (rowId > 0) {
                    newUri = Record.buildClipUri(rowId);

                    // Оповестить наблюдателей об изменениях в базе данных
                    getContext().getContentResolver().notifyChange(uri, null);
                } else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] selectionArgs) {
        Log.d(TAG, "Запрос на удаление");
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ONE_RECORD:
                // Получение из URI идентификатора записи
                String id = uri.getLastPathSegment();

                // Удаление записи
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        Record.TABLE_NAME, Record._ID + "=" + id, selectionArgs);
                break;
            case RECORDS:
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(Record.TABLE_NAME, s, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // Оповестить наблюдателей об изменениях в базе данных
        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] selectionArgs) {
        Log.d(TAG, "Запрос на обновление");
        int numberOfRowsUpdated; // 1, если обновление успешно; 0 при неудаче

        switch (uriMatcher.match(uri)) {
            case ONE_RECORD:
                // Получение идентификатора записи из Uri
                String id = uri.getLastPathSegment();

                // Обновление записи
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        Record.TABLE_NAME, values, Record._ID + "=" + id,
                        selectionArgs);
                break;
            case RECORDS:
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        Record.TABLE_NAME,
                        values,
                        s,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }

        // Если были внесены изменения, оповестить наблюдателей
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
