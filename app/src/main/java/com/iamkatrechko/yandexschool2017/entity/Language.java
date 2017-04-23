package com.iamkatrechko.yandexschool2017.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Класс-сущность языка перевода
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class Language implements Parcelable, Comparable<Language> {

    /** Код языка (например: "ru") */
    private String langCode;
    /** Наименование языка (например: "русский") */
    private String langName;

    /**
     * Конструктор
     * @param langCode код языка
     * @param langName наименование языка
     */
    public Language(String langCode, String langName) {
        this.langCode = langCode;
        this.langName = langName;
    }

    /**
     * Конструктор для интерфейса Parcelable
     * @param in parcel данные
     */
    protected Language(Parcel in) {
        langCode = in.readString();
        langName = in.readString();
    }

    /** Creator экземпляра для интерфейса Parcelable */
    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    /**
     * Возвращает код языка
     * @return код языка
     */
    public String getLangCode() {
        return langCode;
    }

    /**
     * Возвращает наименование языка
     * @return наименование языка
     */
    public String getLangName() {
        return langName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(langCode);
        parcel.writeString(langName);
    }

    @Override
    public int compareTo(@NonNull Language language) {
        return getLangName().compareTo(language.getLangName());
    }
}
