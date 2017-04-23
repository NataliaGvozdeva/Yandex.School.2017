package com.iamkatrechko.yandexschool2017.entity;

import org.json.JSONObject;

/**
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class TranslateResponse {

    /** Код ответа */
    private final int mCode;
    /** Сообщение */
    private String mMessage;
    /** Переведенный текст */
    private String mTranslateText;
    /** Языки перевода */
    private String mLanguage;

    public TranslateResponse(int code) {
        mCode = code;
    }

    public static TranslateResponse from(JSONObject jsonObject) {
        TranslateResponse response = new TranslateResponse(jsonObject.optInt("code"));
        response.mTranslateText = jsonObject.optJSONArray("text").optString(0);
        response.mLanguage = jsonObject.optString("lang");
        response.mMessage = jsonObject.optString("message");
        return response;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getTranslateText() {
        return mTranslateText;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public boolean isSuccess() {
        return getCode() == 200;
    }
}
