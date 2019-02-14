package com.namobiletech.imamiajantri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namobiletech.imamiajantri.Model.FavouriteDuaList;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.duaActivity;

import java.util.ArrayList;

public class favouriteListAdapter extends RecyclerView.Adapter<favouriteListAdapter.favouriteListViewHolder> {

    private ArrayList<FavouriteDuaList> arrayList;
    private Context context;

    public favouriteListAdapter(ArrayList<FavouriteDuaList> data, Context context)
    {
        this.arrayList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public favouriteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_favourite, parent, false);

        return new favouriteListViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull favouriteListViewHolder holder, final int position) {

        Log.e("size", arrayList.size()+ "");

        Typeface noori = Typeface.createFromAsset(context.getAssets(), "fonts/Jameel_Noori_Nastaleeq.ttf");

        holder.title.setText(Html.fromHtml(arrayList.get(position).getFavourite_title()));
        holder.descripion.setText(Html.fromHtml(arrayList.get(position).getFavourite_text()));

        holder.title.setTypeface(noori);
        holder.descripion.setTypeface(noori);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, duaActivity.class);
                intent.putExtra("id", arrayList.get(position).getFavourite_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class favouriteListViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView descripion;

        public favouriteListViewHolder(View itemview)
        {
            super(itemview);

            title = (TextView) itemview.findViewById(R.id.duaTitle_fvrtlistitem);
            descripion = (TextView) itemview.findViewById(R.id.duaDesc_fvrtListItem);
        }
    }
}
