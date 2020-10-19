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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditText titleView = findViewById(R.id.editTitle);
        EditText contentView = findViewById(R.id.editContent);


        Intent intent = getIntent();
        if (intent.hasExtra("Note")) {
            note = (Note) intent.getSerializableExtra("Note");
            if (note != null) {
                titleView.setText(note.getTitle());
                contentView.setText(note.getContents());
            }
        } else {
            note = new Note();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    // TODO saving from menu item makes app crash
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        doReturn(null);
//        return super.onOptionsItemSelected(item);
//

    public void doReturn(View v) {
        EditText titleView = findViewById(R.id.editTitle);
        EditText contentView = findViewById(R.id.editContent);

        if(titleView.getText() == null) {
            return;
        }
        note.setTitle(titleView.getText().toString());
        Toast.makeText(this, "Saving note " + note.getTitle(), Toast.LENGTH_SHORT).show();
        if (contentView.getText() != null) {
            note.setContents(contentView.getText().toString());
        }
        note.setLastEdited(new Date());

        Intent intent = new Intent();
        intent.putExtra("Note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // TODO add confirmation dialog asking if user wants to save
        doReturn(null);
        super.onBackPressed();
    }
}