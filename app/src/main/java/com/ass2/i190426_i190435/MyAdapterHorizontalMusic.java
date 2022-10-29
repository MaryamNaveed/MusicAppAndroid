package com.ass2.i190426_i190435;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterHorizontalMusic extends RecyclerView.Adapter<MyAdapterHorizontalMusic.MyViewHolder> {
    List<Music> ls;
    Context c;

    public MyAdapterHorizontalMusic(List<Music> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.horizontal_row_music, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(ls.get(position).getTitle());

        Picasso.get().load(ls.get(position).getImage()).into(holder.image);



        holder.title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, PlayMusic.class);
                intent.putExtra("title",ls.get(position).getTitle());
                intent.putExtra("genre", ls.get(position).getGenre());
                intent.putExtra("description", ls.get(position).getDescription());
                intent.putExtra("image", ls.get(position).getImage());
                intent.putExtra("link", ls.get(position).getLink());

                c.startActivity(intent);

            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, PlayMusic.class);
                intent.putExtra("title",ls.get(position).getTitle());
                intent.putExtra("genre", ls.get(position).getGenre());
                intent.putExtra("description", ls.get(position).getDescription());
                intent.putExtra("image", ls.get(position).getImage());
                intent.putExtra("link", ls.get(position).getLink());
                c.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            image=itemView.findViewById(R.id.image);
        }
    }
}

