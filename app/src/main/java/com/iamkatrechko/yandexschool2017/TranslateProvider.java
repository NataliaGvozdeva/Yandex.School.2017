package com.iamkatrechko.yandexschool2017;

import android.content.Context;
import android.support.annotation.NonNull;

import com.iamkatrechko.yandexschool2017.entity.TranslateRequest;
import com.iamkatrechko.yandexschool2017.entity.TranslateResponse;
import com.iamkatrechko.yandexschool2017.retrofit.YandexTranslateService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Провайдер для перевода текстов и получения доступных языков
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class TranslateProvider {

    /** Retrofit-сервис для перевода слов и получения доступных языков */
    private YandexTranslateService mTranslateService;
    /** Базовый адрес сервера */
    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    /** Примитивный кэш переведенных текстов на время сессии в приложении */
    private Map<TranslateRequest, String> historyCache = new HashMap<>();

    /** Конструктор */
    public TranslateProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTranslateService = retrofit.create(YandexTranslateService.class);
    }

    public void translate(@NonNull final Context context, String textToTranslate, String langFrom, String langTo,
                          @NonNull final Callback<String> callback) {
        final TranslateRequest request = new TranslateRequest(textToTranslate, langFrom, langTo);
        if (historyCache.containsKey(request)) {
            callback.onSuccess(historyCache.get(request));
            return;
        }

        mTranslateService.translate(textToTranslate, langFrom + "-" + langTo).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    TranslateResponse translateResponse = TranslateResponse.from(new JSONObject(response.body().string()));
                    if (translateResponse.isSuccess()) {
                        historyCache.put(request, translateResponse.getTranslateText());
                        callback.onSuccess(translateResponse.getTranslateText());
                    } else {
                        callback.onError(new Throwable(context.getString(R.string.error)));
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
}
