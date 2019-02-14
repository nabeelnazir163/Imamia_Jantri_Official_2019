package com.namobiletech.imamiajantri.Model;

public class categories_model {

    String dua_car_id;
    String dua_catname;

    public categories_model(String dua_car_id, String dua_catname) {
        this.dua_car_id = dua_car_id;
        this.dua_catname = dua_catname;
    }

    public categories_model() {
    }

    public String getDua_car_id() {
        return dua_car_id;
    }

    public void setDua_car_id(String dua_car_id) {
        this.dua_car_id = dua_car_id;
    }

    public String getDua_catname() {
        return dua_catname;
    }

    public void setDua_catname(String dua_catname) {
        this.dua_catname = dua_catname;
    }
}

