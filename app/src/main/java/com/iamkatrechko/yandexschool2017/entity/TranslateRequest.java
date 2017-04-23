package com.iamkatrechko.yandexschool2017.entity;

/**
 * Класс-сущность запроса на перевод текста
 * @author iamkatrechko
 *         Date: 23.04.2017
 */
public class TranslateRequest {

    /** Текст для перевода */
    private String sourceText;
    /** Исходный перевода */
    private String langFrom;
    /** Конечный язык перевода */
    private String langTo;

    /**
     * Конструктор
     * @param sourceText текст для перевода
     * @param langFrom   исходный перевода
     * @param langTo     конечный язык перевода
     */
    public TranslateRequest(String sourceText, String langFrom, String langTo) {
        this.sourceText = sourceText;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    @Override
    public int hashCode() {
        return sourceText.hashCode() + langFrom.hashCode() + langTo.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TranslateRequest)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        TranslateRequest another = (TranslateRequest) obj;
        return this.sourceText.equals(another.sourceText) &&
                this.langFrom.equals(another.langFrom) &&
                this.langTo.equals(another.langTo);
    }
}
