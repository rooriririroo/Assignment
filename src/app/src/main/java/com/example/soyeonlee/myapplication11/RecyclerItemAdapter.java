package com.example.soyeonlee.myapplication11;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Util;

import java.net.URLEncoder;
import java.util.ArrayList;

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RecyclerItem> recyclerItemArrayList;
    private Context context;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageItemView;
        TextView titleItemView;
        RatingBar ratingBar;
        TextView yearItemView;
        TextView directorItemView;
        TextView actorItemView;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageItemView = (ImageView) itemView.findViewById(R.id.imageItemView);
            titleItemView = (TextView) itemView.findViewById(R.id.titleItemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            yearItemView = (TextView) itemView.findViewById(R.id.yearItemView);
            directorItemView = (TextView) itemView.findViewById(R.id.directorItemView);
            actorItemView = (TextView) itemView.findViewById(R.id.actorItemView);
        }


    }

    public RecyclerItemAdapter(Context context, ArrayList<RecyclerItem> recyclerItemArrayList) {
        this.context = context;
        this.recyclerItemArrayList = recyclerItemArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Glide.with(context).load(recyclerItemArrayList.get(position).getImage()).error(R.drawable.noimage).into(itemHolder.imageItemView);
        itemHolder.titleItemView.setText(Html.fromHtml(recyclerItemArrayList.get(position).getTitle()));
        itemHolder.ratingBar.setRating(recyclerItemArrayList.get(position).getUserRating());
        itemHolder.yearItemView.setText(recyclerItemArrayList.get(position).getPubDate());
        itemHolder.directorItemView.setText(recyclerItemArrayList.get(position).getDirector());
        itemHolder.actorItemView.setText(recyclerItemArrayList.get(position).getActor());
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recyclerItemArrayList.get(position).getLink()));
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return recyclerItemArrayList.size();
    }

}


