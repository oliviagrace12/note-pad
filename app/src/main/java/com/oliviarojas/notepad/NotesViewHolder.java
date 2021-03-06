package com.oliviarojas.notepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder  {

    public TextView title;
    TextView contents;
    TextView lastEdited;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        contents = itemView.findViewById(R.id.content);
        lastEdited = itemView.findViewById(R.id.lastEdited);
    }
}
