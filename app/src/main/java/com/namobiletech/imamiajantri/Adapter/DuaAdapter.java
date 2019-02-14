package com.namobiletech.imamiajantri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namobiletech.imamiajantri.Model.categories_model;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.subCat_activity;

import java.util.ArrayList;

public class DuaAdapter extends RecyclerView.Adapter<DuaAdapter.duaViewholder> {

    private ArrayList<categories_model> categories;
    private Context context;

    public DuaAdapter(ArrayList<categories_model> data, Context context)
    {
        this.categories = data;
        this.context = context;
    }

    @NonNull
    @Override
    public duaViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_group, parent, false);

        return new duaViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull duaViewholder holder, final int position) {

        holder.dua_cat.setText(categories.get(position).getDua_catname());

        holder.dua_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, subCat_activity.class);
                intent.putExtra("cat_id", categories.get(position).getDua_car_id());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class duaViewholder extends RecyclerView.ViewHolder{

        TextView dua_cat;

        public duaViewholder(View itemView) {
            super(itemView);

            dua_cat = (TextView) itemView.findViewById(R.id.lblListHeader);
        }
    }
}
