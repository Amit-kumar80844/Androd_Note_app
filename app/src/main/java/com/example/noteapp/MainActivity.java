package com.example.noteapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add_btn; // Changed to FloatingActionButton
    RecyclerView recyclar_view;
    ImageView menu_btn;adapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        add_btn = findViewById(R.id.add_btn);
        menu_btn = findViewById(R.id.menubtn);
        recyclar_view = findViewById(R.id.recyclar_view);
        menu_btn.setOnClickListener((v)->show_menu());
        add_btn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, addnote.class)));
        setRecyclar_view();
        Log.d("adapter", "MainActivity created");
    }
    void show_menu(){
        PopupMenu popupMenu=new PopupMenu(this,menu_btn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                   startActivity(new Intent(MainActivity.this,LoginActivity.class));
                   finish();
                   return true;
                }
                return false;
            }
        });
    }
    void setRecyclar_view(){
        Query query=Note.getUserNotesReference().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        noteAdapter = new adapter(options,this);
        recyclar_view.setLayoutManager(new LinearLayoutManager(this));
        recyclar_view.setAdapter(noteAdapter);
     Log.d("adapter", "setRecyclar_view created");
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (noteAdapter != null) {
            noteAdapter.startListening();
            Log.d("adapter", "on start called");
        }
    }
}