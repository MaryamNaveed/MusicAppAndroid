package com.ass2.i190426_i190435;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapterContacts extends RecyclerView.Adapter<MyAdapterContacts.MyViewHolder> {
    List<User> ls;
    Context c;

    public MyAdapterContacts(List<User> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }


    @NonNull
    @Override
    public MyAdapterContacts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.contacts_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterContacts.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(ls.get(position).getName());
        holder.num.setText(ls.get(position).getNum());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                Query query = ref.child("user").orderByChild("num").equalTo(ls.get(position).getNum());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean present = false;
                        for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                            User value = appleSnapshot.getValue(User.class);
                            Intent intent = new Intent(c, MessageActivity.class);
                            intent.putExtra("name", ls.get(position).getName());
                            intent.putExtra("phone", ls.get(position).getNum());
                            intent.putExtra("profile", value.getDp());
                            intent.putExtra("id", value.getId());
                            c.startActivity(intent);
                            present=true;
                        }

                        if(present==false){
                            Toast.makeText(c, "User not registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }});
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, num, msg;
        CircleImageView dp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            num=itemView.findViewById(R.id.num);
            dp=itemView.findViewById((R.id.dp));
        }
    }
}

