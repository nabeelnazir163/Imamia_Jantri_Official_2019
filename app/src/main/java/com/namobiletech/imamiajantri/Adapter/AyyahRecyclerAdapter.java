package com.namobiletech.imamiajantri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namobiletech.imamiajantri.Model.AyyatList;
import com.namobiletech.imamiajantri.Model.SurahList;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.readSurrahActivity;

import java.util.ArrayList;

public class AyyahRecyclerAdapter extends RecyclerView.Adapter<AyyahRecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<AyyatList> arrayListArbi = new ArrayList<>();
    private ArrayList<AyyatList> arrayListEnglish = new ArrayList<>();
    Context context;

    public AyyahRecyclerAdapter(ArrayList<AyyatList> arrayListarbi, ArrayList<AyyatList> arrayListEng, Context context) {
        this.arrayListArbi = arrayListarbi;
        this.arrayListEnglish = arrayListEng;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ayyah, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

//      final SurahList surahList = arrayListArbi.get(position);

        Typeface muhammadic_QURANI = Typeface.createFromAsset(context.getAssets(), "fonts/MUHAMMADI_QURANIC_FONT.ttf");
        Typeface open_Sans_font = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans_Regular.ttf");

        holder.ayyahArbi.setText(arrayListArbi.get(position).ayah_text);
        holder.ayyahArbi.setTypeface(muhammadic_QURANI);

        holder.ayyahEng.setText(arrayListEnglish.get(position).ayah_text);
        holder.ayyahEng.setTypeface(open_Sans_font);

        holder.ayyahcount.setText(arrayListEnglish.get(position).ayah_no);

      /*holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(context, readSurrahActivity.class);
              intent.putExtra("surrahNumber", surahList.getSurah_number());
              intent.putExtra("surrahName", surahList.getSurah_name());
              intent.putExtra("type", surahList.getRevel_type());
              context.startActivity(intent);
          }
      });*/

    }

    @Override
    public int getItemCount() {
        return arrayListArbi.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView ayyahArbi;
        TextView ayyahEng;
        TextView ayyahcount;

        RecyclerViewHolder(View view) {

            super(view);

            ayyahArbi = view.findViewById(R.id.ayyat_arbi);
            ayyahEng = view.findViewById(R.id.ayyat_english);
            ayyahcount = view.findViewById(R.id.ayyah_textview);
        }
    }
}
