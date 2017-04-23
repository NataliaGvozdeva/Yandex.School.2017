package com.iamkatrechko.yandexschool2017;

import android.support.annotation.NonNull;

import com.iamkatrechko.yandexschool2017.entity.TranslateResponse;
import com.iamkatrechko.yandexschool2017.retrofit.YandexTranslateService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    public TranslateProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTranslateService = retrofit.create(YandexTranslateService.class);
    }

    public void getLanguages() {

    }

    public void translate(String textToTranslate, @NonNull final Callback<TranslateResponse> callback) {

        mTranslateService.translate(textToTranslate).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    TranslateResponse translateResponse = TranslateResponse.from(new JSONObject(response.body().string()));
                    if (translateResponse.getCode() == 200) {
                        callback.onSuccess(translateResponse);
                    } else {
                        callback.onError(new Throwable("Ошибка перевода"));
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
