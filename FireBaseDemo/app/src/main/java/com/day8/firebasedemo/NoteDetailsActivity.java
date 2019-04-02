package com.day8.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoteDetailsActivity extends AppCompatActivity {

    TextView titleTextView,bodyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        titleTextView =findViewById(R.id.titleDetails);
        bodyTextView = findViewById(R.id.bodyDetails);

        titleTextView.setText(getIntent().getStringExtra("title").toString());
        bodyTextView.setText(getIntent().getStringExtra("body").toString());
    }
}
