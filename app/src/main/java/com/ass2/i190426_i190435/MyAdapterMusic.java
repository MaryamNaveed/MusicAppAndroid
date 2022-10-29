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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    public void onBindViewHolder(@NonNull MyAdapterMusic.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(ls.get(position).getTitle());
        holder.genre.setText(ls.get(position).getGenre());

        Picasso.get().load(ls.get(position).getImage()).into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                String link=ls.get(position).getLink();
                String image=ls.get(position).getImage();
                Query query = ref.child("music").orderByChild("link").equalTo(ls.get(position).getLink());
                ls.remove(position);
                c.startActivity(new Intent(c, MainPage.class));




                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }

                        Toast.makeText(c, "Deleted Succesfully from database", Toast.LENGTH_SHORT).show();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(c, "error", Toast.LENGTH_SHORT).show();
                    }
                });

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference ref1=storage.getReferenceFromUrl(link);

                ref1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(c, "Music deleted from storage", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                StorageReference ref2=storage.getReferenceFromUrl(image);

                ref2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(c, "Image deleted from storage", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, PlayMusic.class);

                UtilityClassMusic.getInstance().setList(ls);
                intent.putExtra("position", position);

                intent.putExtra("title",ls.get(position).getTitle());
                intent.putExtra("genre", ls.get(position).getGenre());
                intent.putExtra("description", ls.get(position).getDescription());
                intent.putExtra("image", ls.get(position).getImage());
                intent.putExtra("link", ls.get(position).getLink());

                c.startActivity(intent);

            }
        });
        holder.genre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, PlayMusic.class);



                UtilityClassMusic.getInstance().setList(ls);
                intent.putExtra("position", position);

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

                UtilityClassMusic.getInstance().setList(ls);
                intent.putExtra("position", position);

                intent.putExtra("title",ls.get(position).getTitle());
                intent.putExtra("genre", ls.get(position).getGenre());
                intent.putExtra("description", ls.get(position).getDescription());
                intent.putExtra("image", ls.get(position).getImage());
                intent.putExtra("link", ls.get(position).getLink());
                c.startActivity(intent);

            }
        });

        System.out.println(ls.get(position).getGenre());

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, genre, delete;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            delete=itemView.findViewById(R.id.delete);
            genre=itemView.findViewById(R.id.genre);
            image=itemView.findViewById(R.id.image);
        }
    }
}
