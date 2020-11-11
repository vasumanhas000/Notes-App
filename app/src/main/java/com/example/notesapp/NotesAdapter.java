package com.example.notesapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private OnNoteListener mOnNoteListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;
        OnNoteListener onNoteListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView ,OnNoteListener onNoteListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description);
            imageView =(ImageView) itemView.findViewById(R.id.imageView);
            this.onNoteListener=onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
    private List<Note> mNotes;

    // Pass in the contact array into the constructor
    public NotesAdapter(List<Note> notes,OnNoteListener onNoteListener) {
        mNotes = notes;
        this.mOnNoteListener=onNoteListener;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View noteView = inflater.inflate(R.layout.list_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(noteView,mOnNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mNotes.get(position);
        TextView title = holder.titleTextView;
        title.setText(note.getTitle());
        TextView description = holder.descriptionTextView;
        description.setText(note.getDescription());
        description.setEllipsize(TextUtils.TruncateAt.END);
        description.setMaxLines(1);
//        if(note.getDescription().length()<40){
//            description.setText(note.getDescription());
//        }
//        else{
//            ArrayList<String> parts = new ArrayList<>();
//            int length = note.getDescription().length();
//            for (int i = 0; i < length; i += 30) {
//                parts.add(note.getDescription().substring(i, Math.min(length, i + 30)));
//            }
//            String text = parts.get(0)+"....";
//            description.setText(text);
//        }
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
    public List<Note> getData() {
        return mNotes;
    }
}
