package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {
    static ArrayList<Note> notes;
    static RecyclerView recyclerView;
    static ConstraintLayout constraintLayout;
    static SharedPreferences sharedPreferences;
    FloatingActionButton floatingActionButton;
    static NotesAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("com.example.notesapp",MODE_PRIVATE);
        findViewById();
        try {
             gson = new Gson();
            String response = sharedPreferences.getString("notes","");
            if(response==""){
                notes = new ArrayList<>();
            }else{
                notes =(ArrayList<Note>) gson.fromJson(response,new TypeToken<ArrayList<Note>>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        if(notes.size()==0){
            recyclerView.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        }
        adapter = new NotesAdapter(notes,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                startActivity(intent);
            }
        });
        enableSwipeToDeleteAndUndo();
    }

    void findViewById(){
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    @Override
    public void onNoteClick(int position) {
        Log.i("position",position+"");
        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Note item = adapter.getData().get(position);
                notes.remove(position);
                adapter.notifyDataSetChanged();
                if(notes.size()==0){
                    recyclerView.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
                }
                editSharedPreferences();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Note was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(notes.size()==0){
                            recyclerView.setVisibility(View.VISIBLE);
                            constraintLayout.setVisibility(View.GONE);
                        }
                        notes.add(position,item);
                        adapter.notifyDataSetChanged();
                        editSharedPreferences();
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.BLACK);
                snackbar.setTextColor(Color.BLACK);
                snackbar.setBackgroundTint(Color.parseColor("#9699f1"));
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public void editSharedPreferences(){
        try {
            String response = gson.toJson(notes);
            MainActivity.sharedPreferences.edit().putString("notes",response).apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}