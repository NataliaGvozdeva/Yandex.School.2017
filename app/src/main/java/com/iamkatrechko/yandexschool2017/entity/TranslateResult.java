package com.iamkatrechko.yandexschool2017.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class TranslateResult {

    private Integer code;
    private String lang;
    private List<String> text = null;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
