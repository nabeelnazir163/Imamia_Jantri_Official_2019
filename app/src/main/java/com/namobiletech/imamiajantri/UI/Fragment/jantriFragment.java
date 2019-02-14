package com.namobiletech.imamiajantri.UI.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.ReadJantri;
import com.namobiletech.imamiajantri.Utils.UserSessionManager;

public class jantriFragment extends Fragment {

    //View
    View v;

    //WIDGETS
    //TextView
    private TextView imamiaJantri_tv;

    //Linear Layout
    private RelativeLayout jantriLyout;

//    AdView adView;

    //user session referecne
    UserSessionManager sessionManager;

    private final String PDF_LINK = "https://imamiajantri.com/imamiajantrimedium.pdf";

    public jantriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_jantri, container, false);

        Typeface open_Sans_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans_Regular.ttf");

        imamiaJantri_tv = (TextView) v.findViewById(R.id.imamiaJantriText);
        imamiaJantri_tv.setTypeface(open_Sans_font);

        jantriLyout = (RelativeLayout) v.findViewById(R.id.jantriLayout_jantriFrag);

        jantriLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager = new UserSessionManager(getContext());

                startActivity(new Intent(getContext(), ReadJantri.class));

            }
        });

        return v;
    }
}
