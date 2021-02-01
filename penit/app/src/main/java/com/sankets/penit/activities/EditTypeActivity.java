package com.sankets.penit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.sankets.penit.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditTypeActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "title";
    public static final String EXTRA_DESCRIPTION =
            "desc";
    public static final String EXTRA_ID =
            "id";
    public static final String EXTRA_DATE =
            "date";
    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_type);
        editTextDescription = findViewById(R.id.edit_content);
        editTextTitle = findViewById(R.id.edit_title);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(getString(R.string.edit_note));
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));

        } else {
            setTitle(getString(R.string.add_note));
        }


    }
    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();


        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, R.string.please_insert, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();

        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);


        int id =getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1){
            //("idid" +id);
            data.putExtra(EXTRA_ID,id);

        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String date = dateFormat.format(calendar.getTime());
        data.putExtra(EXTRA_DATE,date);

        setResult(RESULT_OK, data);


        finish();//close
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_note:

                    shareNote();


                return true;
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareNote() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareTitle = editTextTitle.getText().toString();
        String shareBody = editTextDescription.getText().toString();


        //sharingIntent.putExtra(Intent.EXTRA_TEXT,shareTitle);
        sharingIntent.putExtra(Intent.EXTRA_TEXT,shareTitle + " : " + shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Note Text using"));
        //Toast.makeText(EditTypeActivity.this,"shared the contents of the note",Toast.LENGTH_SHORT).show();

    }
}