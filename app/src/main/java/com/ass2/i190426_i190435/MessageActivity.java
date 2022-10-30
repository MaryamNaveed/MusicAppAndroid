package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile;
    TextView name;
    EditText msg;
    ImageButton send;

    FirebaseUser mAuth;

    String userName, userDp, id;

    RecyclerView rv;

    MessageAdapter adapter;
    List<Chat> ls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        name=findViewById(R.id.name);
        profile=findViewById(R.id.profile);
        send=findViewById(R.id.send);
        msg=findViewById(R.id.msg);
        ls=new ArrayList<>();
        rv=findViewById(R.id.rv);

        adapter=new MessageAdapter(ls, MessageActivity.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MessageActivity.this);
        rv.setLayoutManager(lm);

        userName=getIntent().getStringExtra("name");
        name.setText(userName);

        mAuth= FirebaseAuth.getInstance().getCurrentUser();

        userDp=getIntent().getStringExtra("profile");
        Picasso.get().load(Uri.parse(userDp)).into(profile);

        String id = getIntent().getStringExtra("id");

        //Toast.makeText(MessageActivity.this, "id: "+id+"user: "+mAuth.getUid(), Toast.LENGTH_LONG).show();
        getMessage(mAuth.getUid(), id);

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(msg.getText().toString().trim().equals("")){
                    Toast.makeText(MessageActivity.this, "Message is Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage(mAuth.getUid(),id,msg.getText().toString());
                    msg.setText("");
                }
            }
        });

        Query ref = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("id").equalTo(id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getMessage(mAuth.getUid(), id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(String sender, String receiver, String message){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        Chat newChat = new Chat(sender, receiver, message, dtf.format(now));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chat");

        DatabaseReference abc = myRef.push();
        abc.setValue(newChat);


    }
    public void getMessage(String myId, String id1){
        ls=new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chat");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ls.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat c= dataSnapshot.getValue(Chat.class);
//                    Toast.makeText(MessageActivity.this, c.getSender()+" "+c.getMessage()+"  "+c.getReceiver(),Toast.LENGTH_LONG).show();
                    if((c.getReceiver().equals(myId) && c.getSender().equals(id1)) ||
                           ( c.getReceiver().equals(id1) && c.getSender().equals(myId))){
//                        Toast.makeText(MessageActivity.this, "Added: "+c.getMessage(),Toast.LENGTH_LONG).show();
                        ls.add(c);

                    }
                    adapter=new MessageAdapter(ls, MessageActivity.this);
                    rv.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}