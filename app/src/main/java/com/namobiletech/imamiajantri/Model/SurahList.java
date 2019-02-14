package com.namobiletech.imamiajantri.Model;

public class SurahList {


    public String surah_number;
    public String surah_name;
    public String english_name;
    public String eng_translation;
    public String revel_type;

    public SurahList(String surah_number, String surah_name, String english_name, String eng_translation, String revel_type) {
        this.surah_number = surah_number;
        this.surah_name = surah_name;
        this.english_name = english_name;
        this.eng_translation = eng_translation;
        this.revel_type = revel_type;
    }

    public SurahList() {

    }

    public String getSurah_number() {
        return surah_number;
    }

    public void setSurah_number(String surah_number) {
        this.surah_number = surah_number;
    }

    public String getSurah_name() {
        return surah_name;
    }

    public void setSurah_name(String surah_name) {
        this.surah_name = surah_name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getEng_translation() {
        return eng_translation;
    }

    public void setEng_translation(String eng_translation) {
        this.eng_translation = eng_translation;
    }

    public String getRevel_type() {
        return revel_type;
    }

    public void setRevel_type(String revel_type) {
        this.revel_type = revel_type;
    }
}
