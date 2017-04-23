package com.iamkatrechko.yandexschool2017;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iamkatrechko.yandexschool2017.entity.TranslateResponse;

/**
 * Фрагмент экрана перевода
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class TranslateFragment extends Fragment {

    /** Провайдер для перевода текстов и получения доступных языков */
    private TranslateProvider mTranslateProvider;
    /** Поле ввода текста для перевода */
    private EditText mEditTextEnterText;
    /** Текстовое поле с результатом перевода */
    private TextView mTextViewTranslateText;
    /** Текстовое поле с исходным текстом для перевода */
    private TextView mTextViewEnterText;
    /** Коллбэк на получение результата перевода */
    private Callback<TranslateResponse> mTranslateResultCallback = new Callback<TranslateResponse>() {

        @Override
        public void onSuccess(TranslateResponse result) {
            mTextViewTranslateText.setText(result.getTranslateText());
        }

        @Override
        public void onError(Throwable t) {
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
        mTranslateProvider = new TranslateProvider();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);

        mEditTextEnterText = (EditText) v.findViewById(R.id.edit_text_enter_text);
        mTextViewTranslateText = (TextView) v.findViewById(R.id.text_view_translate_text);
        mTextViewEnterText = (TextView) v.findViewById(R.id.text_view_enter_text);

        mEditTextEnterText.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTextViewEnterText.setText("");
                    mTextViewTranslateText.setText("");
                    return;
                }
                mTextViewEnterText.setText(String.valueOf(charSequence));
                mTranslateProvider.translate(String.valueOf(charSequence), mTranslateResultCallback);
            }
        });
        return v;
    }

    /** Очищает все поля, связанные с переводом текста */
    private void clearAll() {
        mEditTextEnterText.setText("");
        mTextViewEnterText.setText("");
        mTextViewTranslateText.setText("");
    }
}
