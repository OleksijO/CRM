package com.becomejavasenior.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@XmlRootElement
public class Language implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String languageCode;
    private boolean delete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Language)) return false;
        Language language = (Language) other;
        if (id != language.id) return false;
        return name != null ? name.equals(language.name) : language.name == null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", delete=" + delete +
                '}';
    }
}