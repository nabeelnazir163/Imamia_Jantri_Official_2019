package com.namobiletech.imamiajantri.Model;

public class ListAyyat {

    public String ayah_number;
    public String ayah_text;
    public String ayah_no;
    public String ayah_juzz;
    public String ayah_manzil;
    public String quran_page;
    public String ruku_number;
    public String hizzb;
    public String sajdah;
    public String surrahNumber;

    public ListAyyat(String ayah_number, String ayah_text, String ayah_no, String ayah_juzz, String ayah_manzil, String quran_page, String ruku_number, String hizzb, String sajdah, String surrahNumber) {
        this.ayah_number = ayah_number;
        this.ayah_text = ayah_text;
        this.ayah_no = ayah_no;
        this.ayah_juzz = ayah_juzz;
        this.ayah_manzil = ayah_manzil;
        this.quran_page = quran_page;
        this.ruku_number = ruku_number;
        this.hizzb = hizzb;
        this.sajdah = sajdah;
        this.surrahNumber = surrahNumber;
    }

    public ListAyyat() {
    }

    public String getAyah_number() {
        return ayah_number;
    }

    public void setAyah_number(String ayah_number) {
        this.ayah_number = ayah_number;
    }

    public String getAyah_text() {
        return ayah_text;
    }

    public void setAyah_text(String ayah_text) {
        this.ayah_text = ayah_text;
    }

    public String getAyah_no() {
        return ayah_no;
    }

    public void setAyah_no(String ayah_no) {
        this.ayah_no = ayah_no;
    }

    public String getAyah_juzz() {
        return ayah_juzz;
    }

    public void setAyah_juzz(String ayah_juzz) {
        this.ayah_juzz = ayah_juzz;
    }

    public String getAyah_manzil() {
        return ayah_manzil;
    }

    public void setAyah_manzil(String ayah_manzil) {
        this.ayah_manzil = ayah_manzil;
    }

    public String getQuran_page() {
        return quran_page;
    }

    public void setQuran_page(String quran_page) {
        this.quran_page = quran_page;
    }

    public String getRuku_number() {
        return ruku_number;
    }

    public void setRuku_number(String ruku_number) {
        this.ruku_number = ruku_number;
    }

    public String getHizzb() {
        return hizzb;
    }

    public void setHizzb(String hizzb) {
        this.hizzb = hizzb;
    }

    public String getSajdah() {
        return sajdah;
    }

    public void setSajdah(String sajdah) {
        this.sajdah = sajdah;
    }

    public String getSurrahNumber() {
        return surrahNumber;
    }

    public void setSurrahNumber(String surrahNumber) {
        this.surrahNumber = surrahNumber;
    }
}
