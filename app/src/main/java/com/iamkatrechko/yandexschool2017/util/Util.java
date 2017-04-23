package com.iamkatrechko.yandexschool2017.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Утилитный класс
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class Util {

    /**
     * Открывает страницу разработчика в Google Play
     * @param context контекст
     */
    public static void goToGooglePlayDeveloper(Context context) {
        final String appName = "I'm Katrechko";
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + appName)));
        }
    }
}
