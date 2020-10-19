package com.oliviarojas.notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final int NEW_NOTE = 123;
    private RecyclerView recyclerView;
    private List<Note> notes = new ArrayList<>();
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO load notes from user preferences and add to notes list

        recyclerView = findViewById(R.id.recycler);
        adapter = new NotesAdapter(notes, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newNote:
                Intent intent = new Intent(this, EditNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE);
                break;
            case R.id.info:
                Toast.makeText(this, "Get info", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (notes.isEmpty()) {
            return;
        }
        int pos = recyclerView.getChildLayoutPosition(v);
        Note note = notes.get(pos);

        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("Note", note);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Note note = (Note) data.getSerializableExtra("Note");
                    if (note != null) {
                        Toast.makeText(this, note.getTitle(), Toast.LENGTH_SHORT).show();
                        notes.add(note);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO save notes to user preferences
    }
}