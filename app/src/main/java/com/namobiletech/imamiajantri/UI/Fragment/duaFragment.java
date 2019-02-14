package com.namobiletech.imamiajantri.UI.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.namobiletech.imamiajantri.Adapter.DuaAdapter;
import com.namobiletech.imamiajantri.Model.categories_model;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;
import java.util.ArrayList;

public class duaFragment extends Fragment {

    View v;

    DbHelper dbHelper;
    Cursor cursor;

    private RecyclerView mDuaRecyclerview;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<categories_model> ar = new ArrayList<>();


    public duaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_dua, container, false);

        loadCategories();

        return v;
    }

    private void loadCategories() {

        mDuaRecyclerview = v.findViewById(R.id.dua_categories);
        layoutManager = new LinearLayoutManager(getContext());
        mDuaRecyclerview.setLayoutManager(layoutManager);
        mDuaRecyclerview.setHasFixedSize(true);

        dbHelper = new DbHelper(getContext());

        cursor = dbHelper.getDuaInformation(dbHelper);

        cursor.moveToNext();

        do{

            categories_model categories_model = new categories_model(cursor.getString(0), cursor.getString(1));
            ar.add(categories_model);

        }
        while(cursor.moveToNext());

        dbHelper.close();

        adapter = new DuaAdapter(ar, getContext());
        mDuaRecyclerview.setAdapter(adapter);
    }

}
