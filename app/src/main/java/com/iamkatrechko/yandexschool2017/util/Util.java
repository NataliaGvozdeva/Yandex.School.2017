package com.iamkatrechko.yandexschool2017.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.iamkatrechko.yandexschool2017.R;

/**
 * Утилитный класс
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class Util {

    /**
     * Отправляет текст письмом
     * @param shareText содержимое письма
     */
    public static void shareText(Context context, String shareText){
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.select_app_to_share)));
    }

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
