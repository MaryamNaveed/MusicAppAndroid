package com.ass2.i190426_i190435;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RecordInfo extends AppCompatActivity {

    EditText title, genre, description;
    Button record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);

        record=findViewById(R.id.record);
        title=findViewById(R.id.title);
        genre=findViewById(R.id.genre);
        description=findViewById(R.id.description);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecordInfo.this, RecordMusic.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("genre", genre.getText().toString());
                intent.putExtra("description", description.getText().toString());
                startActivity(intent);

            }
        });
    }
}