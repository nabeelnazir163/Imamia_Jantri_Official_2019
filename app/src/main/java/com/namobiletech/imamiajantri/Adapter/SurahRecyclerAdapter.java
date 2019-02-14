package com.namobiletech.imamiajantri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namobiletech.imamiajantri.Model.SurahList;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.readSurrahActivity;

import java.util.ArrayList;

public class SurahRecyclerAdapter extends RecyclerView.Adapter<SurahRecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<SurahList> arrayList = new ArrayList<>();
    Context context;

    public SurahRecyclerAdapter(ArrayList<SurahList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_surah, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        final SurahList surahList = arrayList.get(position);

        Typeface open_Sans_font = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans_Regular.ttf");

        holder.namespara.setText(surahList.getEnglish_name());
        holder.descriptionspara.setText(surahList.getEng_translation());
        holder.type.setText(surahList.getRevel_type());
        holder.surahNumber.setText(String.valueOf(position + 1));

        holder.namespara.setTypeface(open_Sans_font);
        holder.descriptionspara.setTypeface(open_Sans_font);
        holder.type.setTypeface(open_Sans_font);
        holder.surahNumber.setTypeface(open_Sans_font);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, readSurrahActivity.class);
                intent.putExtra("surrahNumber", surahList.getSurah_number());
                intent.putExtra("surrahName", surahList.getSurah_name());
                intent.putExtra("type", surahList.getRevel_type());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView namespara, descriptionspara, type;
        TextView surahNumber;

        RecyclerViewHolder(View view) {

            super(view);

            namespara = view.findViewById(R.id.name_tv_sapara);
            type = view.findViewById(R.id.type);
            descriptionspara = view.findViewById(R.id.description_tv_sapara);
            surahNumber = view.findViewById(R.id.surah_textview);

        }
    }
}
