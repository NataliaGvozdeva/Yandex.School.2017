package com.iamkatrechko.yandexschool2017;

/**
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public interface Callback<T> {

    void onSuccess(T result);

    void onError(Throwable t);
}
