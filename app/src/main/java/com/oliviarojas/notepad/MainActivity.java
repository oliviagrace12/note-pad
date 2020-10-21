package com.oliviarojas.notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

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

        recyclerView = findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        adapter = new NotesAdapter(notes, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Note> savedNotes = loadSavedNotes();
        notes.addAll(savedNotes);
        adapter.notifyDataSetChanged();
    }

    private List<Note> loadSavedNotes() {
        List<Note> savedNotes = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject noteJson = jsonArray.getJSONObject(i);
                Note note = new Note();
                note.setTitle(noteJson.getString(getString(R.string.title)));
                note.setContents(noteJson.getString(getString(R.string.contents)));
                note.setLastEdited(new Date(noteJson.getLong(getString(R.string.last_edited))));
                savedNotes.add(note);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadSavedNotes: ", e);
        }
        return savedNotes;
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
                startActivityForResult(new Intent(this, EditNoteActivity.class), NEW_NOTE);
                break;
            case R.id.info:
                startActivityForResult(new Intent(this, InfoActivity.class), NEW_NOTE);
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
    public boolean onLongClick(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.baseline_save_24);
        builder.setPositiveButton("OK", (dialog, id) -> {
            int position = recyclerView.getChildLayoutPosition(v);
            notes.remove(position);
            adapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> {});
        builder.setMessage("Delete note?");
        builder.setTitle("Delete?");
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
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
        notes.sort((n1, n2) -> ((n1.getLastEdited().compareTo(n2.getLastEdited())) * -1));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        if (!notes.isEmpty()) {
            saveNotes();
        }

        super.onPause();
    }

    private void saveNotes() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            buildJson(writer);

            // LOGGING
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            buildJson(writer);
            Log.d(TAG, "Saving notes: \n" + sw.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildJson(JsonWriter writer) throws IOException {
        writer.setIndent("  ");
        writer.beginArray();
        for (Note note : notes) {
            writer.beginObject();
            writer.name(getString(R.string.title)).value(note.getTitle());
            writer.name(getString(R.string.contents)).value(note.getContents());
            writer.name(getString(R.string.last_edited)).value(note.getLastEdited().getTime());
            writer.endObject();
        }
        writer.endArray();
        writer.close();
    }
}