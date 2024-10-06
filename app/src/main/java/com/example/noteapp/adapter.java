package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class adapter extends FirestoreRecyclerAdapter<Note, adapter.NoteViewHolder> {
    Context context;

    public adapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
        Log.d("adapter", "adapter created");
    }
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
        // Strip title and notes to 50 characters
        String title = model.getTitle().trim();
        String notes = model.getNotes().trim();
        holder.title.setText(title.length() > 50 ? title.substring(0, 50) + "..." : title);
        holder.notes.setText(notes.length() > 100 ? notes.substring(0, 100) + "..." : notes);
        String formattedDate = formatTimestamp(model.getTimestamp().toDate());
        holder.timestamp.setText(formattedDate);
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,addnote.class);
            intent.putExtra("title",model.getTitle());
            intent.putExtra("notes",model.getNotes());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new NoteViewHolder(view);
    }
    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, notes, timestamp;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            notes = itemView.findViewById(R.id.notes);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
    private String formatTimestamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return dateFormat.format(date);
    }
}
