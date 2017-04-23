package com.iamkatrechko.yandexschool2017;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iamkatrechko.yandexschool2017.dialog.DialogChoiceLanguage;
import com.iamkatrechko.yandexschool2017.entity.Language;
import com.iamkatrechko.yandexschool2017.entity.TranslateRequest;
import com.iamkatrechko.yandexschool2017.util.HistoryUtils;
import com.iamkatrechko.yandexschool2017.util.Util;

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
    /** Константа временной паузы, после которой выполняется перевод текста. Решение плохое */
    private static final int TRANSLATE_DELAY = 1000;

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
    /** Кнопка очистки полей с переводом */
    private ImageButton mImageButtonClear;
    /** Кнопка "добавить в избранное" */
    private ImageButton mImageButtonFavorite;
    /** Кнопка "поделиться переводом" */
    private ImageButton mImageButtonShare;
    /** Кнопка выбора исходного языка */
    private Button mButtonLangFrom;
    /** Кнопка выбора конечного языка */
    private Button mButtonLangTo;

    /** Коллбэк на получение результата перевода */
    private Callback<String> mTranslateResultCallback = new Callback<String>() {

        @Override
        public void onSuccess(TranslateRequest request, String resultText) {
            mTextViewTranslateText.setText(resultText);
            boolean isFavorite = HistoryUtils.isFavoriteRecord(getActivity(),
                    request.getSourceText(), resultText, request.getLangFrom(), request.getLangTo());
            if (isFavorite) {
                mImageButtonFavorite.setColorFilter(ContextCompat.getColor(getActivity(), R.color.text_color_list_history_bookmark_on));
            }
            // TODO Переместить метод сохранения истории в более подходящее и правильное место
            // TODO Да, решение не очень красиво, но пока это первое и самое быстрое, что пришло в голову за ограниченное время
            addToHistory();
        }

        @Override
        public void onError(Throwable t) {
            mTextViewTranslateText.setText("");
            showSnackbar(t.getLocalizedMessage());
        }
    };

    /** Handler для отложенного перевода текста */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String query = mEditTextEnterText.getText().toString();
            if (query.length() == 0) {
                mTextViewEnterText.setText("");
                mTextViewTranslateText.setText("");
                return;
            }
            mTextViewEnterText.setText(query);
            mTranslateProvider.translate(getActivity(), query, mTranslateResultCallback);
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
        mTranslateProvider = TranslateProvider.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);

        mEditTextEnterText = (EditText) v.findViewById(R.id.edit_text_enter_text);
        mTextViewTranslateText = (TextView) v.findViewById(R.id.text_view_translate_text);
        mTextViewEnterText = (TextView) v.findViewById(R.id.text_view_enter_text);
        mImageButtonArrows = (ImageButton) v.findViewById(R.id.image_button_arrows);
        mImageButtonClear = (ImageButton) v.findViewById(R.id.image_button_clear_text);
        mImageButtonFavorite = (ImageButton) v.findViewById(R.id.image_button_bookmark);
        mImageButtonShare = (ImageButton) v.findViewById(R.id.image_button_share);
        mButtonLangFrom = (Button) v.findViewById(R.id.button_lang_from);
        mButtonLangTo = (Button) v.findViewById(R.id.button_lang_to);

        mEditTextEnterText.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mImageButtonFavorite.setColorFilter(ContextCompat.getColor(getActivity(), R.color.icons_tint));
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0, TRANSLATE_DELAY);
            }
        });

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

        mImageButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFavorite();
            }
        });

        mImageButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
            }
        });

        mImageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.shareText(getActivity(), mTextViewTranslateText.getText().toString());
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

    /**
     * Отображает диалог выбора языка
     * @param requestCode идентификатор диалога
     */
    private void showChoiceLanguageDialog(int requestCode) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogChoiceLanguage fragmentDialog = DialogChoiceLanguage.newInstance();
        fragmentDialog.setTargetFragment(this, requestCode);
        fragmentDialog.show(fragmentManager, "DIALOG_CHOICE_LANGUAGE");
    }

    /** Добавляет текущую запись в историю */
    private void addToHistory() {
        String source = mTextViewEnterText.getText().toString();
        String translate = mTextViewTranslateText.getText().toString();
        String fromLang = mTranslateProvider.getLanguageFrom().getLangCode();
        String toLang = mTranslateProvider.getLanguageTo().getLangCode();
        HistoryUtils.addRecord(getActivity(),
                source,
                translate,
                fromLang,
                toLang,
                false);
    }

    /** Меняет состояние избранности записи на обратное */
    private void changeFavorite() {
        String source = mTextViewEnterText.getText().toString();
        String translate = mTextViewTranslateText.getText().toString();
        String fromLang = mTranslateProvider.getLanguageFrom().getLangCode();
        String toLang = mTranslateProvider.getLanguageTo().getLangCode();

        boolean isFavorite = HistoryUtils.changeFavoriteRecord(getActivity(),
                source,
                translate,
                fromLang,
                toLang);
        // TODO Исправить баг, когда в истории запись убирают из избранных, а на экране перевода ее статус не меняется
        mImageButtonFavorite.setColorFilter(ContextCompat.getColor(getActivity(),
                isFavorite ? R.color.text_color_list_history_bookmark_on : R.color.text_color_list_history_bookmark_off));
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
        mHandler.removeMessages(0);
        if (mEditTextEnterText.getText().length() != 0) {
            mTranslateProvider.translate(getActivity(), mEditTextEnterText.getText().toString(), mTranslateResultCallback);
        }
    }

    /**
     * Отображает всплывающее уведомление
     * @param text текст уведомления
     */
    private void showSnackbar(String text) {
        final Snackbar snackbar = Snackbar.make(mEditTextEnterText, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.close, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        }).show();
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
