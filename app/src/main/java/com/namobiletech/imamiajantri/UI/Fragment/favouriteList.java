package com.namobiletech.imamiajantri.UI.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.namobiletech.imamiajantri.Adapter.favouriteListAdapter;
import com.namobiletech.imamiajantri.Model.FavouriteDuaList;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import java.util.ArrayList;

public class favouriteList extends Fragment {

    View v;

    DbHelper dbHelper;
    Cursor cursor;

    private RecyclerView recyclerView;

    private favouriteListAdapter adapter;

    public ArrayList<FavouriteDuaList> arrayList;

    private CardView cardView;
    RecyclerView.LayoutManager layoutManager;

    public favouriteList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        arrayList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.favouriteListview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        cardView = v.findViewById(R.id.no_fvrt_cv);

        dbHelper = new DbHelper(getContext());

        cursor = dbHelper.getfvrtList(dbHelper);
        cursor.moveToNext();


        if(cursor.getCount() != 0)
        {
            cardView.setVisibility(View.GONE);

            do{

                FavouriteDuaList fvrtlist = new FavouriteDuaList(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                arrayList.add(fvrtlist);

                Log.e("title", cursor.getString(1));

            }
            while(cursor.moveToNext());

            dbHelper.close();

            adapter = new favouriteListAdapter(arrayList, getContext());
            recyclerView.setAdapter(adapter);

        }

        else {
            cardView.setVisibility(View.VISIBLE);
        }
        return v;
    }

}
