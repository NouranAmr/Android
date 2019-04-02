package com.day8.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NoteList extends AppCompatActivity {

    ListView noteListView;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ArrayAdapter<Note> arrayAdapter;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        noteListView=findViewById(R.id.noteListView);
        addBtn=findViewById(R.id.addNoteBtn);
        firebaseDatabase=FirebaseDatabase.getInstance();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        databaseReference=firebaseDatabase.getReference("Notes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Note note= dataSnapshot.getValue(Note.class);
                arrayAdapter.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    arrayAdapter.add(snapshot.getValue(Note.class));
                }
                arrayAdapter.notifyDataSetChanged();
                noteListView.setAdapter(arrayAdapter);
                Log.i("RealTimeDatabase" , "value is: " + note);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note=(Note) parent.getItemAtPosition(position);
                Intent intent= new Intent(NoteList.this,NoteDetailsActivity.class);
                String noteeeetitle=note.getTitle();
                String noteeeeebody=note.getBody();
                intent.putExtra("title",note.getTitle());
                intent.putExtra("body",note.getBody());
                startActivity(intent);
               // Toast.makeText(NoteList.this, , Toast.LENGTH_SHORT).show();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(NoteList.this,UserNote.class);
                startActivity(intent);
            }
        });


    }
}
