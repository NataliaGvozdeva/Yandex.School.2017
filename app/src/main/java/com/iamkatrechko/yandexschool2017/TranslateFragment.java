package com.iamkatrechko.yandexschool2017;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iamkatrechko.yandexschool2017.dialog.DialogChoiceLanguage;
import com.iamkatrechko.yandexschool2017.entity.Language;
import com.iamkatrechko.yandexschool2017.util.HistoryUtils;

/**
 * Фрагмент экрана перевода
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class TranslateFragment extends Fragment {

    /** Код запроса открытия диалога выбора исходного языка */
    private static final int DIALOG_CHOICE_LANGUAGE_FROM = 521251;
    /** Код запроса открытия диалога выбора конечного языка */
    private static final int DIALOG_CHOICE_LANGUAGE_TO = 612612;

    /** Провайдер для перевода текстов и получения доступных языков */
    private TranslateProvider mTranslateProvider;

    /** Поле ввода текста для перевода */
    private EditText mEditTextEnterText;
    /** Текстовое поле с результатом перевода */
    private TextView mTextViewTranslateText;
    /** Текстовое поле с исходным текстом для перевода */
    private TextView mTextViewEnterText;
    /** Кнопка смены языков между собой */
    private ImageButton mImageButtonArrows;
    /** Кнопка выбора исходного языка */
    private Button mButtonLangFrom;
    /** Кнопка выбора конечного языка */
    private Button mButtonLangTo;

    /** Коллбэк на получение результата перевода */
    private Callback<String> mTranslateResultCallback = new Callback<String>() {

        @Override
        public void onSuccess(String resultText) {
            mTextViewTranslateText.setText(resultText);
        }

        @Override
        public void onError(Throwable t) {
            mTextViewTranslateText.setText("");
            Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Возвращает новый экземпляр фрагмента
     * @return новый экземпляр фрагмента
     */
    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTranslateProvider = new TranslateProvider(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);

        mEditTextEnterText = (EditText) v.findViewById(R.id.edit_text_enter_text);
        mTextViewTranslateText = (TextView) v.findViewById(R.id.text_view_translate_text);
        mTextViewEnterText = (TextView) v.findViewById(R.id.text_view_enter_text);
        mImageButtonArrows = (ImageButton) v.findViewById(R.id.image_button_arrows);
        mButtonLangFrom = (Button) v.findViewById(R.id.button_lang_from);
        mButtonLangTo = (Button) v.findViewById(R.id.button_lang_to);

        mEditTextEnterText.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTextViewEnterText.setText("");
                    mTextViewTranslateText.setText("");
                    return;
                }
                mTextViewEnterText.setText(String.valueOf(charSequence));
                mTranslateProvider.translate(getActivity(), String.valueOf(charSequence), mTranslateResultCallback);
            }
        });
        mTranslateProvider.getLanguages(getActivity());

        mImageButtonArrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_rotate));
                Language langFrom = mTranslateProvider.getLanguageFrom();
                mTranslateProvider.setLanguageFrom(mTranslateProvider.getLanguageTo());
                mTranslateProvider.setLanguageTo(langFrom);
                updateButtonLanguages();
            }
        });

        mButtonLangFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceLanguageDialog(DIALOG_CHOICE_LANGUAGE_FROM);
            }
        });

        mButtonLangTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceLanguageDialog(DIALOG_CHOICE_LANGUAGE_TO);
            }
        });

        updateButtonLanguages();
        return v;
    }

    private void showChoiceLanguageDialog(int requestCode) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogChoiceLanguage fragmentDialog = DialogChoiceLanguage.newInstance();
        fragmentDialog.setTargetFragment(TranslateFragment.this, requestCode);
        fragmentDialog.show(fragmentManager, "setDateTimeDialog");
    }

    /** Добавляет текущую запись в историю */
    private void addToHistory() {
        String source = mTextViewEnterText.getText().toString();
        String translate = mTextViewTranslateText.getText().toString();
        String fromLang = "ru";
        String toLang = "en";
        HistoryUtils.addRecord(getActivity(),
                source,
                translate,
                fromLang,
                toLang,
                false);
    }

    /** Очищает все поля, связанные с переводом текста */
    private void clearAll() {
        mEditTextEnterText.setText("");
        mTextViewEnterText.setText("");
        mTextViewTranslateText.setText("");
    }

    /** Обновляет заголовки кнопок выбора языков и обновляет перевод */
    private void updateButtonLanguages() {
        mButtonLangFrom.setText(mTranslateProvider.getLanguageFrom().getLangName());
        mButtonLangTo.setText(mTranslateProvider.getLanguageTo().getLangName());
        if (mEditTextEnterText.getText().length() != 0) {
            mTranslateProvider.translate(getActivity(), mEditTextEnterText.getText().toString(), mTranslateResultCallback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == DIALOG_CHOICE_LANGUAGE_FROM) {
            Language language = data.getParcelableExtra("lang");
            mTranslateProvider.setLanguageFrom(language);
            updateButtonLanguages();
        }
        if (resultCode == Activity.RESULT_OK && requestCode == DIALOG_CHOICE_LANGUAGE_TO) {
            Language language = data.getParcelableExtra("lang");
            mTranslateProvider.setLanguageTo(language);
            updateButtonLanguages();
        }
    }
}
