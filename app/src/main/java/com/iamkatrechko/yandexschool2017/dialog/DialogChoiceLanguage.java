package com.iamkatrechko.yandexschool2017.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.iamkatrechko.yandexschool2017.TranslateProvider;
import com.iamkatrechko.yandexschool2017.entity.Language;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Диалог выбора языка перевода
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class DialogChoiceLanguage extends DialogFragment {

    /** Список доступных языков */
    private List<Language> mLanguages = new ArrayList<>();

    /**
     * Возвращает новый экземпляр фрагмента
     * @return новый экземпляр фрагмента
     */
    public static DialogChoiceLanguage newInstance() {
        return new DialogChoiceLanguage();
    }

    /**
     * Возвращает результат
     * @param lang выбранный язык
     */
    private void sendResult(Language lang) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("lang", lang);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        mLanguages = new TranslateProvider(getActivity()).getLanguages(getActivity());
        String[] stockArr = new String[mLanguages.size()];
        for (Language language : mLanguages) {
            stockArr[mLanguages.indexOf(language)] = language.getLangName();
        }
        return new AlertDialog.Builder(getActivity())
                .setItems(stockArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        sendResult(mLanguages.get(item));
                    }
                }).create();
    }
}
