package com.example.notesapp;

import java.util.ArrayList;

public class Note {
    private String nTitle;
    private String nDescription;

    public Note(String title,String description){
        nTitle=title;
        nDescription =description;
    }

    public String getTitle() {
        return nTitle;
    }

    public String getDescription() {
        return nDescription;
    }

    public static ArrayList<Note> createNotesList(int numNotes){
        ArrayList<Note> notes = new ArrayList<Note>();

        for (int i = 1; i <= numNotes; i++) {
            notes.add(new Note("Title " + i, "Description "+ i ));
        }
        return notes;
    }
}
