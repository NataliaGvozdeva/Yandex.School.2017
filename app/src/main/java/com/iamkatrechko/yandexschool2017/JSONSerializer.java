package com.iamkatrechko.yandexschool2017;

import android.content.Context;

import com.iamkatrechko.yandexschool2017.entity.Language;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, сериализующий файл JSON с доступными для перевода языками
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class JSONSerializer {

    /** Имя JSON-файла с доступными языками */
    private static final String LANGUAGES_FILE_NAME = "languages.json";

    /**
     * Возвращает строку JSON с языками из файла в ассетах
     * @param context контекст
     * @return строка JSON с языками
     */
    private String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(LANGUAGES_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Возвращает список поддерживаемых языков
     * @param context контекст
     * @return список поддерживаемых языков
     */
    public List<Language> getLanguages(Context context) {
        List<Language> languageList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(context));
            JSONObject langs = jsonObject.getJSONObject("langs");
            JSONArray array = langs.names();
            for (int i = 0; i < array.length(); i++) {
                languageList.add(new Language(array.getString(i), langs.getString(array.getString(i))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(languageList);
        return languageList;
    }
}
