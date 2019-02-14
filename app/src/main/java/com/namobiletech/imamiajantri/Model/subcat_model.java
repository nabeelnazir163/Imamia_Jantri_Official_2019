package com.namobiletech.imamiajantri.Model;

public class subcat_model {

    String dua_subcat_id;
    String dua_subcat_name;

    public subcat_model(String dua_subcat_id, String dua_subcat_name) {
        this.dua_subcat_id = dua_subcat_id;
        this.dua_subcat_name = dua_subcat_name;
    }

    public subcat_model() {
    }

    public String getDua_subcat_id() {
        return dua_subcat_id;
    }

    public void setDua_subcat_id(String dua_subcat_id) {
        this.dua_subcat_id = dua_subcat_id;
    }

    public String getDua_subcat_name() {
        return dua_subcat_name;
    }

    public void setDua_subcat_name(String dua_subcat_name) {
        this.dua_subcat_name = dua_subcat_name;
    }
}
