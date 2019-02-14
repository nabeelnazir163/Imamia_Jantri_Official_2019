package com.namobiletech.imamiajantri.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namobiletech.imamiajantri.R;

import mehdi.sakout.aboutpage.AboutPage;

public class Aboutus_Frag extends Fragment {


    public Aboutus_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.logo)
                .addGroup("Connect with us")
                .addEmail("imamiajantriofficial@gmail.com")
                .addWebsite("http://imamiajantri.com")
                .addFacebook("https://www.facebook.com/Imamiajantriofficial")
                .setDescription(getString(R.string.aboutus_content))
                .create();
        return aboutPage;
    }

}
