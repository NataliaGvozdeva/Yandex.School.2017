package com.iamkatrechko.yandexschool2017;

import com.iamkatrechko.yandexschool2017.entity.TranslateRequest;

/**
 * Коллбэк запроса перевода
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public interface Callback<T> {

    /**
     * Запрос выполнен успешно
     * @param request запрос
     * @param result  результат
     */
    void onSuccess(TranslateRequest request, T result);

    /**
     * Ошибка запроса
     * @param t ошибка
     */
    void onError(Throwable t);
}
