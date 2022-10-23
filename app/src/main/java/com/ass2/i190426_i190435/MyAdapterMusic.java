package com.ass2.i190426_i190435;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterMusic extends RecyclerView.Adapter<MyAdapterMusic.MyViewHolder> {

    List<Music> ls;
    Context c;

    public MyAdapterMusic(List<Music> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterMusic.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.music_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterMusic.MyViewHolder holder, int position) {
        holder.title.setText(ls.get(position).getTitle());
        holder.genre.setText(ls.get(position).getGenre());

        Picasso.get().load(ls.get(position).getImage()).into(holder.image);

        System.out.println(ls.get(position).getGenre());

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, genre;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            genre=itemView.findViewById(R.id.genre);
            image=itemView.findViewById(R.id.image);
        }
    }
}
