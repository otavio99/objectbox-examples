package io.objectbox.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;

import io.objectbox.Box;

public class NoteActivity extends Activity {

    private EditText editText;
    private View addNoteButton;
    private View listNoteButton;

    private Box<Note> notesBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        setUpViews();

        notesBox = ObjectBox.get().boxFor(Note.class);
    }

    protected void setUpViews() {
        addNoteButton = findViewById(R.id.buttonAdd);
        addNoteButton.setEnabled(false);

        listNoteButton = findViewById(R.id.buttonList);

        editText = findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addNote();
                return true;
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void onAddButtonClick(View view) {
        addNote();
    }
    public void onListButtonClick(View view) {
        gotToListNoteActivity();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        notesBox.put(note);
        Log.d(App.TAG, "Inserted new note, ID: " + note.getId());
    }
    private void gotToListNoteActivity(){
        Intent intent = new Intent();
        intent.setClass(this, ListNoteActivity.class);
        startActivity(intent);
    }
}