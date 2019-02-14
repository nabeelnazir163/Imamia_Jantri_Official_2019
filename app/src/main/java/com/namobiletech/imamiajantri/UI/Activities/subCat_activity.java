package com.namobiletech.imamiajantri.UI.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.namobiletech.imamiajantri.Adapter.ExpandableListAdapter;
import com.namobiletech.imamiajantri.Model.subcat_model;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.Utils.dbUtility.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class subCat_activity extends AppCompatActivity {

    ExpandableListView duaExpandableList;
    private ExpandableListAdapter myAdapter;

    //Heading List ... !!
    ArrayList<subcat_model> subcategories = new ArrayList<>();
    ArrayList<subcat_model> postarray;

    DbHelper dbHelper;
    Cursor cursor;
    Cursor subcatcursor;

    Map<Integer, ArrayList<String>> kMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat_activity);

        ImageView backbtn = findViewById(R.id.backimage_subcat);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String cat_id = intent.getStringExtra("cat_id");

        duaExpandableList = (ExpandableListView) findViewById(R.id.duasExpandableListview);

        dbHelper = new DbHelper(this);

        cursor = dbHelper.getCatInformation(dbHelper, cat_id);

        HashMap<String, ArrayList<subcat_model>> childList = new HashMap<>();

        cursor.moveToNext();

        do {

            subcat_model subcat_model = new subcat_model(cursor.getString(2), cursor.getString(3));
            subcategories.add(subcat_model);

        }
        while (cursor.moveToNext());

        dbHelper.close();

        ArrayList<String> subCatid = new ArrayList<>();
        ArrayList<String> subCatname = new ArrayList<>();

        for (int i = 0; i < subcategories.size(); i++) {
            String id = subcategories.get(i).getDua_subcat_id();
            String name = subcategories.get(i).getDua_subcat_name();
            subCatid.add(id);
            subCatname.add(name);
        }

        kMap = new HashMap<>();

        for (int j = 0; j < subCatid.size(); j++) {
            postarray = new ArrayList<>();
            subcatcursor = dbHelper.getsubCatInformation(dbHelper, subCatid.get(j));

            subcatcursor.moveToNext();

            do {

                if(subcatcursor.getString(6) != null) {
                    subcat_model subcat_model = new subcat_model(subcatcursor.getString(4), subcatcursor.getString(6));
                    postarray.add(subcat_model);
                }
                else
                {
                    subcat_model subcat_model = new subcat_model(subcatcursor.getString(4), subcatcursor.getString(5));
                    postarray.add(subcat_model);
                }

            }
            while (subcatcursor.moveToNext());

            String categoryName = subCatname.get(j);

            childList.put(categoryName, postarray);

            myAdapter = new ExpandableListAdapter(this, subcategories, childList);

            duaExpandableList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            dbHelper.close();

        }


        //Childs List
      /*  ArrayList<String> child1 = new ArrayList<String>();
        ArrayList<String> child2 = new ArrayList<String>();
        ArrayList<String> child3 = new ArrayList<String>();

        HashMap<String, ArrayList<String>> childList = new HashMap<>();

        String headingItems[] = getActivity().getResources().getStringArray(R.array.header_titles);

        String l1[] = getActivity().getResources().getStringArray(R.array.h1);
        String l2[] = getActivity().getResources().getStringArray(R.array.h2);
        String l3[] = getActivity().getResources().getStringArray(R.array.h3);

        for (String title : headingItems) {
            subcategories.add(title);
        }

        for (String child : l1) {
            child1.add(child);
        }

        for (String child : l2) {
            child2.add(child);
        }

        for (String child : l3) {
            child3.add(child);
        }

        childList.put(subcategories.get(0), child1);
        childList.put(subcategories.get(1), child2);
        childList.put(subcategories.get(2), child3);

        myAdapter = new ExpandableListAdapter(this, subcategories, childList);

        duaExpandableList.setAdapter(myAdapter);
*/
    }

}
