package com.day8.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserNote extends AppCompatActivity {

    EditText body,title;
    Button next,cancel;
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_note);
        body=findViewById(R.id.bodyDetails);
        title=findViewById(R.id.titleDetails);
        next=findViewById(R.id.btnNext);
        cancel=findViewById(R.id.btnCancel);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("Notes");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(title.getText().toString().equals("")) && !(body.getText().toString().equals(""))) {
                    writeNewNote(title.getText().toString(), body.getText().toString());
                    Toast.makeText(UserNote.this, "saaaaaaaaaaaaaaaaa7", Toast.LENGTH_SHORT).show();
                    title.setText("");
                    body.setText("");
                }

                Intent intent= new Intent(UserNote.this,NoteList.class);
                startActivity(intent);
            }
        });
    }
    private void writeNewNote( String title, String body) {
        Note note = new Note(title, body);
        mDatabase.child(mDatabase.push().getKey()).setValue(note);
    }
}
