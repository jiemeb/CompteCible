package com.herault.comptecible;

public class Note {
    public String note;
    public long keyIdNote;


    public Note() {
        note = "";
        keyIdNote = 0;
    }

    Note(String noteArcher, long IdNote) {
        note = noteArcher;
        keyIdNote= IdNote;
    }


    public String getNote() {
        return note;
    }

    public long getIdNote() {
        return keyIdNote;
    }

    public void setNote(String NoteArcher) {
        note = NoteArcher;
    }

    public void setIdNote(long IdNote) {
        keyIdNote = IdNote;
    }
}
