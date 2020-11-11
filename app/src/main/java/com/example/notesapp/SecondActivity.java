package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class SecondActivity extends AppCompatActivity {
    EditText titleText,descriptionText;
    Integer position;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById();
        Intent intent =getIntent();
        position = intent.getIntExtra("position",-1);
        displayNote();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    void findViewById(){
        titleText = (EditText) findViewById(R.id.edit_text_title);
        descriptionText=(EditText) findViewById(R.id.edit_text_description);
        floatingActionButton=(FloatingActionButton) findViewById(R.id.fab);
    }

    void displayNote(){
        if(position!=-1){
            titleText.setText(MainActivity.notes.get(position).getTitle());
            descriptionText.setText(MainActivity.notes.get(position).getDescription());
            if(descriptionText.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
        else{
            if(titleText.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }

    void saveNote(){
        Gson gson = new Gson();
        String title= titleText.getText().toString();
        String description= descriptionText.getText().toString();
        if(title.length()!=0 && description.length()!=0){
        if(position!=-1){
            MainActivity.notes.set(position,new Note(title,description));
            MainActivity.adapter.notifyDataSetChanged();
        }
        else{
            Log.i("add","added");
            MainActivity.notes.add(new Note(title,description));
            MainActivity.adapter.notifyDataSetChanged();
            MainActivity.recyclerView.setVisibility(View.VISIBLE);
            MainActivity.constraintLayout.setVisibility(View.GONE);
        }
        try {
            String response = gson.toJson(MainActivity.notes);
            MainActivity.sharedPreferences.edit().putString("notes",response).apply();
            Log.i("add note", "saveNote: added");
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }else{
            if(title.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            }
            if(description.length()==0){
                Toast.makeText(this,"Description cannot be empty",Toast.LENGTH_SHORT).show();
            }
        }
    }

}