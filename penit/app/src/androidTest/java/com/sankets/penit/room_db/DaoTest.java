package com.sankets.penit.room_db;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DaoTest {

    private NoteDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        this.database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NoteDatabase.class
        ).allowMainThreadQueries().build(); // no multithreading in tests

    }

    @After
    public void teardown() {
        database.close();
    }

    // DATA SET FOR TEST
    private static final long USER_ID = 1;
    private static final Note USER_DEMO = new Note();


    @Test
    public void Insert() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.noteDao().Insert(USER_DEMO);
        // TEST
        Note user = LiveDataUtil.getValue(this.database.noteDao().getNote());
        assertTrue(user.getTitle().equals(USER_DEMO.getTitle()) && user.getId() == USER_ID);
    }
}
