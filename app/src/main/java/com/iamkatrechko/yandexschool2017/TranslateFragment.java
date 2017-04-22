package com.iamkatrechko.yandexschool2017;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iamkatrechko.yandexschool2017.entity.TranslateResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Фрагмент экрана перевода
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class TranslateFragment extends Fragment {

    /** Retrofit-сервис для перевода слов */
    private YandexTranslateService mTranslateService;

    /** Поле ввода текста для перевода */
    private EditText mEditTextEnterText;
    private TextView mTextViewTranslateText;
    private TextView mTextViewEnterText;

    private Callback<TranslateResult> mTranslateResultCallback = new Callback<TranslateResult>() {
        @Override
        public void onResponse(Call<TranslateResult> call, Response<TranslateResult> response) {
            mTextViewTranslateText.setText(response.body().getText().get(0));
        }

        @Override
        public void onFailure(Call<TranslateResult> call, Throwable t) {
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTranslateService = retrofit.create(YandexTranslateService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);

        mEditTextEnterText = (EditText) v.findViewById(R.id.edit_text_enter_text);
        mTextViewTranslateText = (TextView) v.findViewById(R.id.text_view_translate_text);
        mTextViewEnterText = (TextView) v.findViewById(R.id.text_view_enter_text);

        mEditTextEnterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTranslateService.translate(String.valueOf(charSequence)).enqueue(mTranslateResultCallback);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }
}
