package com.oliviarojas.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    private Note note;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditText titleView = findViewById(R.id.editTitle);
        EditText contentView = findViewById(R.id.editContent);


        Intent intent = getIntent();
        if (intent.hasExtra("Note")) {
            note = (Note) intent.getSerializableExtra("Note");
            position = intent.getIntExtra("Position", 0);
            if (note != null) {
                titleView.setText(note.getTitle());
                contentView.setText(note.getContents());
            }
        } else {
            note = new Note();
            position = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        doReturn(null);
        return super.onOptionsItemSelected(item);
    }


    public void doReturn(View v) {
        EditText titleView = findViewById(R.id.editTitle);
        EditText contentView = findViewById(R.id.editContent);

        if (titleView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Could not save note with no title", Toast.LENGTH_SHORT).show();
        } else {
            note.setTitle(titleView.getText().toString());
            Toast.makeText(this, "Saving note " + note.getTitle(), Toast.LENGTH_SHORT).show();
            if (contentView.getText() != null) {
                note.setContents(contentView.getText().toString());
            }
            note.setLastEdited(new Date());

            Intent intent = new Intent();
            intent.putExtra("Note", note);
            if (position != null) {
                intent.putExtra("Position", position);
            }
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // TODO add confirmation dialog asking if user wants to save
        doReturn(null);
        super.onBackPressed();
    }
}