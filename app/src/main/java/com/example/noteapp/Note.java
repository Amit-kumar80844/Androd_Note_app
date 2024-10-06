package com.example.noteapp;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Note {
    private String title;
    private String notes;
    private Timestamp timestamp;

    public Note() {

    }

    public Note(String title, String notes, Timestamp timestamp) {
        this.title = title;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public static CollectionReference getUserNotesReference() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            // Reference to the authenticated user's 'mynotes' collection
            return FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(firebaseUser.getUid())
                    .collection("mynotes");
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }
}
