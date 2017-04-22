package com.iamkatrechko.yandexschool2017;

import com.iamkatrechko.yandexschool2017.entity.TranslateResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public interface YandexTranslateService {

    @GET("translate?key=trnsl.1.1.20161119T065705Z.f9dbf497d9c05856.7db10f95796d6cfef30486ed9715b2b473aa3a46&lang=en")
    Call<TranslateResult> translate(@Query("text") String textToTranslate);
}
