package com.namobiletech.imamiajantri.Model;

public class FavouriteDuaList {

    String favourite_id;
    String favourite_title;
    String favourite_text;

    public FavouriteDuaList() {
    }

    public FavouriteDuaList(String favourite_id, String favourite_title, String favourite_text) {
        this.favourite_id = favourite_id;
        this.favourite_title = favourite_title;
        this.favourite_text = favourite_text;
    }

    public String getFavourite_id() {
        return favourite_id;
    }

    public void setFavourite_id(String favourite_id) {
        this.favourite_id = favourite_id;
    }

    public String getFavourite_title() {
        return favourite_title;
    }

    public void setFavourite_title(String favourite_title) {
        this.favourite_title = favourite_title;
    }

    public String getFavourite_text() {
        return favourite_text;
    }

    public void setFavourite_text(String favourite_text) {
        this.favourite_text = favourite_text;
    }
}
