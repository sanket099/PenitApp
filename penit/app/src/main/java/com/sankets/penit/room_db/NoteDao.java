package com.sankets.penit.room_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao { //either interface or abstract class as we dont provide method body we just annotate
    //multiple args or even a list

    @Insert
    void Insert(Note note);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void Update(Note note);

    @Delete
    void Delete(Note note);

    @Query("DELETE FROM note_table")
    void DeleteAllNotes();

    @Query("SELECT * From note_table WHERE id = 3")
    LiveData<Note> getNote();

    @Query("SELECT * FROM note_table Order By time Desc ")
    LiveData<List<Note>> getAllNotes();  //updates and returns


}
