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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final int NEW_NOTE = 123;
    private static final int EDIT_NOTE = 124;
    private static final String POSITION = "Position";
    private static final String NOTE = "Note";
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
        int position = recyclerView.getChildLayoutPosition(v);
        Note note = notes.get(position);

        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(NOTE, note);
        intent.putExtra(POSITION, position);
        startActivityForResult(intent, EDIT_NOTE);
    }

    @Override
    public boolean onLongClick(View v) {
        // TODO
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == NEW_NOTE) {
            Note note = (Note) data.getSerializableExtra(NOTE);
            if (note != null) {
                notes.add(note);
            }
        } else if (requestCode == EDIT_NOTE) {
            Note note = (Note) data.getSerializableExtra(NOTE);
            int position = data.getIntExtra(POSITION, notes.size());
            if (note != null) {
                notes.set(position, note);
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