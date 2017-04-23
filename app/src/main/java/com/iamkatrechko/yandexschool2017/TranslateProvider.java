package com.iamkatrechko.yandexschool2017;

import android.content.Context;
import android.support.annotation.NonNull;

import com.iamkatrechko.yandexschool2017.entity.Language;
import com.iamkatrechko.yandexschool2017.entity.TranslateRequest;
import com.iamkatrechko.yandexschool2017.entity.TranslateResponse;
import com.iamkatrechko.yandexschool2017.retrofit.YandexTranslateService;
import com.iamkatrechko.yandexschool2017.util.UtilPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Провайдер-синглтон для перевода текстов и получения доступных языков
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class TranslateProvider {

    /** Базовый адрес сервера */
    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    /** Исходный язык перевода */
    private static Language mLanguageFrom;
    /** Конечный язык перевода */
    private static Language mLanguageTo;
    /** Единственный экземпляр класса */
    private static TranslateProvider sTranslateProvider;

    /** Retrofit-сервис для перевода слов и получения доступных языков */
    private YandexTranslateService mTranslateService;
    /** Примитивный кэш переведенных текстов на время сессии приложения */
    private Map<TranslateRequest, String> historyCache = new HashMap<>();
    /** Список доступных языков */
    private List<Language> mLanguageList = new ArrayList<>();

    /**
     * Конструктор
     * @param context контекст
     */
    private TranslateProvider(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTranslateService = retrofit.create(YandexTranslateService.class);

        mLanguageFrom = getLastLanguageFrom(context);
        mLanguageTo = getLastLanguageTo(context);
    }

    /**
     * Возвращает единственный экземпляр класса
     * @param context контекст
     * @return единственный экземпляр класса
     */
    public static TranslateProvider get(Context context) {
        // TODO Здесь можно сделать double-check-lock, но в данном случае нет необходимости
        if (sTranslateProvider == null) {
            sTranslateProvider = new TranslateProvider(context);
        }
        return sTranslateProvider;
    }

    /**
     * Переводит текст и выполняет интерфейс обратного вызова при завершении операции
     * @param context         контекст
     * @param textToTranslate текст для перевода
     * @param callback        интерфейс обратного вызова
     */
    public void translate(@NonNull final Context context,
                          String textToTranslate,
                          @NonNull final Callback<String> callback) {
        final TranslateRequest request = new TranslateRequest(textToTranslate, mLanguageFrom.getLangCode(), mLanguageTo.getLangCode());
        if (historyCache.containsKey(request)) {
            callback.onSuccess(request, historyCache.get(request));
            return;
        }

        mTranslateService.translate(textToTranslate,
                mLanguageFrom.getLangCode() + "-" + mLanguageTo.getLangCode()).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    TranslateResponse translateResponse = TranslateResponse.from(new JSONObject(response.body().string()));
                    if (translateResponse.isSuccess()) {
                        historyCache.put(request, translateResponse.getTranslateText());
                        callback.onSuccess(request, translateResponse.getTranslateText());
                    } else {
                        String errorText;
                        switch (translateResponse.getCode()) {
                            case 501:
                                errorText = context.getString(R.string.error_501);
                                break;
                            default:
                                errorText = context.getString(R.string.error);
                                break;
                        }
                        callback.onError(new Throwable(errorText));
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    callback.onError(new Throwable(context.getString(R.string.error)));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(new Throwable(context.getString(R.string.error_no_internet)));
            }
        });
    }

    /**
     * Возвращает список доступных языков
     * @param context контекст
     * @return список доступных языков
     */
    public List<Language> getLanguages(Context context) {
        if (mLanguageList.isEmpty()) {
            mLanguageList = new JSONSerializer().getLanguages(context);
        }
        return mLanguageList;
    }

    /**
     * Возвращает последний выбранный исходный язык перевода
     * @param context контекст
     * @return последний выбранный исходный язык перевода
     */
    private Language getLastLanguageFrom(Context context) {
        String langCode = UtilPreferences.getLastLangFrom(context);
        for (Language language : getLanguages(context)) {
            if (language.getLangCode().equals(langCode)) {
                return language;
            }
        }
        return null;
    }

    /**
     * Возвращает последний выбранный конечный язык перевода
     * @param context контекст
     * @return последний выбранный конечный язык перевода
     */
    private Language getLastLanguageTo(Context context) {
        String langCode = UtilPreferences.getLastLangTo(context);
        for (Language language : getLanguages(context)) {
            if (language.getLangCode().equals(langCode)) {
                return language;
            }
        }
        return null;
    }

    /**
     * Возврвщает текущий исходный язык перевода
     * @return текущий исходный язык перевода
     */
    public Language getLanguageFrom() {
        return mLanguageFrom;
    }

    /**
     * Возврвщает текущий конечный язык перевода
     * @return текущий конечный язык перевода
     */
    public Language getLanguageTo() {
        return mLanguageTo;
    }

    /**
     * Устанавливает текущий исходный язык перевода
     * @param languageFrom исходный язык перевода
     */
    public void setLanguageFrom(Language languageFrom) {
        mLanguageFrom = languageFrom;
    }

    /**
     * Устанавливает текущий конечный язык перевода
     * @param languageTo конечный язык перевода
     */
    public void setLanguageTo(Language languageTo) {
        mLanguageTo = languageTo;
    }
}
