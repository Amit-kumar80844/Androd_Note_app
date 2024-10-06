package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class addnote extends AppCompatActivity {
    ImageView save_btn;
    TextView topic, note;
    TextView pagetitle;
    String content, title, docId;
    Boolean edit_mode=false;
    FloatingActionButton delete_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addnote);
        // Adjust window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize UI componentsA
        save_btn = findViewById(R.id.save_btn);
        topic = findViewById(R.id.topic);
        note = findViewById(R.id.note);
        //edit page title
        pagetitle=findViewById(R.id.page_title);
//        gets from message passing
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("notes");
        docId=getIntent().getStringExtra("docId");
        delete_btn=findViewById(R.id.delete_btn);
        if(docId!=null && !docId.isEmpty()){
            edit_mode=true;
        }
        if(edit_mode){
            pagetitle.setText("Edit Note");
            topic.setText(title);
            note.setText(content);
            delete_btn.setVisibility(View.VISIBLE);
            delete_btn.setOnClickListener(v -> deleteNote(docId));
        }
           save_btn.setOnClickListener(v -> addnote());
    }

    void addnote() {
        String title = topic.getText().toString();
        String notes = note.getText().toString();
        if (title.isEmpty()) {
            topic.setError("Please fill all the fields");
            return;
        }
        if (notes.isEmpty()) {
            note.setError("Please fill all the fields");
            return;
        }
        Note noteData = new Note(title, notes, Timestamp.now());
        saveNoteToFirebase(noteData);
    }
    void saveNoteToFirebase(Note noteData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userNotesRef = Note.getUserNotesReference();
        DocumentReference documentReference;
        if(edit_mode){
            documentReference = userNotesRef.document(docId);
        }else{
            documentReference = userNotesRef.document();
        }
        documentReference.set(noteData).addOnSuccessListener(unused -> {
            Toast.makeText(addnote.this, "Note added successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(addnote.this, "Failed to add note", Toast.LENGTH_SHORT).show();
        });

       finish();
    }
    //to delete the note
    void deleteNote(String docId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userNotesRef = Note.getUserNotesReference();
        DocumentReference documentReference;
        documentReference = userNotesRef.document(docId);
        documentReference.delete().addOnSuccessListener(unused -> {
            Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
        });
      
        finish();
    }
}