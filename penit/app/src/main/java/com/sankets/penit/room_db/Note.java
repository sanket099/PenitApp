package com.sankets.penit.room_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note  {

    private String title;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String time;
    private byte[] bytes;


   // private String pathString;
    //private byte[] image;

    public Note() {
    }
    public Note(String title, String content, String date,byte[] bytes) {
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}