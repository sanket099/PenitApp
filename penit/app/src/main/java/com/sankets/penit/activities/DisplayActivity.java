package com.sankets.penit.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sankets.penit.BuildConfig;
import com.sankets.penit.SwipeToDeleteCallback;
import com.sankets.penit.viewModels.NoteViewModel;
import com.sankets.penit.R;
import com.sankets.penit.SharedPref;
import com.sankets.penit.adapters.NoteAdapter;
import com.sankets.penit.room_db.Note;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DisplayActivity extends AppCompatActivity implements NoteAdapter.OnNoteList {

    private FloatingActionButton fab_main, fab_text, fab_voice, fab_draw;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    RecyclerView recyclerView;
    NoteViewModel noteViewModel;
    NoteAdapter noteAdapter;
    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    SharedPref sharedPref;
    AlertDialog.Builder builder;
    private ImageView imageView;
    private TextView textView;
    CoordinatorLayout coordinatorLayout;
    /*List<Note> noteArrayList;
    List<String> imageArrayList;*/

    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        fab_main = findViewById(R.id.fab);
        fab_text = findViewById(R.id.fab1);
        fab_voice = findViewById(R.id.fab2);
        fab_draw = findViewById(R.id.fab3);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);
        imageView = findViewById(R.id.iv_add);
        recyclerView = findViewById(R.id.recyclerview);
        textView = findViewById(R.id.tv_add);
        builder = new AlertDialog.Builder(DisplayActivity.this,R.style.my_dialog_theme);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class); //init

        linearLayoutManager = new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        sharedPref = new SharedPref(DisplayActivity.this);
        /*imageArrayList = new ArrayList<>();
        noteArrayList = new ArrayList<Note>();
*/

        ObjectAnimator animY = ObjectAnimator.ofFloat(fab_main, "translationY", -150f, 10f); //btn animation
        animY.setDuration(1000);//1sec
        animY.setInterpolator(new BounceInterpolator());
        animY.setRepeatMode(ValueAnimator.REVERSE);
        animY.setRepeatCount(2);
        animY.start();

        setTitle(getString(R.string.app_name));

        initRecycler();
        enableSwipeToDeleteAndUndo();

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f; //speed
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                final int position = viewHolder.getAdapterPosition();
                final Note item = noteAdapter.getNoteAt(position);
                noteViewModel.delete(noteAdapter.getNoteAt(position));

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.item_removed, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteViewModel.insert(item);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();

            }
        }).attachToRecyclerView(recyclerView);*/

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(notes!=null){
                    noteAdapter.submitList(notes);
                    //(notes);
                    islistEmpty(notes.isEmpty());
                }
            }
        });

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    fab_voice.startAnimation(fab_close);
                    fab_text.startAnimation(fab_close);
                    fab_draw.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab_voice.setClickable(false);
                    fab_text.setClickable(false);
                    fab_draw.setClickable(false);

                    isOpen = false;
                } else {

                    fab_voice.startAnimation(fab_open);
                    fab_text.startAnimation(fab_open);
                    fab_draw.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab_voice.setClickable(true);
                    fab_text.setClickable(true);
                    fab_draw.setClickable(true);
                    isOpen = true;
                }

            }
        });


        fab_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en");
               /* if(sharedPreference.get_lang() == 0)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en");
                else if(sharedPreference.get_lang() == 1){
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi-In");
                }*/

                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,3);

                }
                else{
                    Toast.makeText(DisplayActivity.this,R.string.device_not_sup,Toast.LENGTH_SHORT).show();

                }

            }
        });

        fab_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this,EditTypeActivity.class);

                startActivityForResult(intent,1);

            }
        });

        fab_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(DisplayActivity.this, DrawActivity.class), 4);

            }
        });

    }
    private void initRecycler(){
        recyclerView.setHasFixedSize(true);
        noteAdapter = new NoteAdapter(this,this);
        //recyclerView.setLayoutManager(linearLayoutManager);

        if(sharedPref.get_flag() == 0){
        recyclerView.setLayoutManager(linearLayoutManager);}
        else{
            recyclerView.setLayoutManager(staggeredGridLayoutManager);

        }
        recyclerView.setAdapter(noteAdapter);



    }
    private void islistEmpty(Boolean b){
        if(b){
            imageView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            String title = data.getStringExtra(EditTypeActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(EditTypeActivity.EXTRA_DESCRIPTION);

            String date = data.getStringExtra(EditTypeActivity.EXTRA_DATE);

            Note note = new Note();
            note.setContent(desc);
            note.setTime(date);
            note.setBytes(new byte[1]);
            note.setTitle(title);
            noteViewModel.insert(note);
           // noteArrayList.add(note);
           // noteAdapter.notifyDataSetChanged();

            //(note.getTitle());


        }
        else if(requestCode == 2 && resultCode == RESULT_OK){

            Intent intent = getIntent();
            //(intent);

            assert data != null;
            int id = data.getIntExtra(EditTypeActivity.EXTRA_ID,-1);
            // String string = getIntent().getStringExtra(AddNoteActivity.EXTRA_TITLE);

            if(id == -1){
                //invalid id
                //(id);
                return;
            }


            String title = data.getStringExtra(EditTypeActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(EditTypeActivity.EXTRA_DESCRIPTION);

            String date = data.getStringExtra(EditTypeActivity.EXTRA_DATE);

            //("title = " + title);
            //("desc = " + desc);
            //("date = " + date);

            Note note = new Note();
            note.setTitle(title);
            note.setContent(desc);
            note.setTime(date);
            note.setBytes(new byte[1]);
            note.setId(id);
           // noteArrayList.add(note);
            noteViewModel.update(note);
           // noteAdapter.notifyDataSetChanged();

            //note updated
            //("update");

        }
        else if(requestCode == 3 ){
            ArrayList<String> result = null;
            if (data != null) {
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //("result.get(0) = " + result.get(0));
              //  Note note = new Note("Voice Note", result.get(0),getDate(),new byte[1]);
                Note note = new Note();
                note.setBytes(new byte[1]);
                note.setTime(getDate());
                note.setContent(result.get(0));
                note.setTitle("Voice Note");
                noteViewModel.insert(note);
               // noteArrayList.add(note);

                //noteAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == 4 && resultCode == RESULT_OK){
           /// String image = data.getStringExtra(DrawActivity.IMAGE);
            byte[] bytes = data.getByteArrayExtra(DrawActivity.BYTE_ARRAY);

            int id = data.getIntExtra(EditTypeActivity.EXTRA_ID,-1);
            // String string = getIntent().getStringExtra(AddNoteActivity.EXTRA_TITLE);

            Note note = new Note();
            note.setTitle("");
            note.setContent("");
            note.setTime("");
            note.setTime(getDate());
            if(id == -1){
                //invalid id
                //  note.setBytes(image);
                note.setBytes(bytes);

                noteViewModel.insert(note);
            }
            else{
                note.setId(id);
                //  note.setBytes(image);
                note.setBytes(bytes);

                noteViewModel.update(note);

            }


        }
        else{
            //note not saved
            Toast.makeText(this, R.string.note_not_saved, Toast.LENGTH_SHORT).show();
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    private String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String date = dateFormat.format(calendar.getTime());
        return date;
    }


    @Override
    public void OnnoteClick(Note note, int viewType) {
        if(viewType == NoteAdapter.TEXT_NOTE) {
            startActivityForResult(new Intent(DisplayActivity.this, EditTypeActivity.class)
                            .putExtra(EditTypeActivity.EXTRA_TITLE, note.getTitle())
                            .putExtra(EditTypeActivity.EXTRA_DESCRIPTION, note.getContent())
                            .putExtra(EditTypeActivity.EXTRA_DATE, note.getTime())
                            .putExtra(EditTypeActivity.EXTRA_ID, note.getId())
                    , 2
            );
        }
        else if(viewType == NoteAdapter.DRAW_NOTE){
            startActivityForResult(new Intent(DisplayActivity.this, DrawActivity.class)
                            .putExtra(DrawActivity.BYTE_ARRAY,note.getBytes())
                            .putExtra(EditTypeActivity.EXTRA_ID, note.getId())
                    , 4
            );
        }

    }

    @Override
    public void OnLongClick(Note downloadObject) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN); //press back twice
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_display, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                if(!noteAdapter.getCurrentList().isEmpty()) {
                    builder.setMessage(getString(R.string.are_ya_sure))
                            .setCancelable(true)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    noteViewModel.deleteAllNotes();

                                    //chatAdapter.notifyDataSetChanged();
                                    Toast.makeText(DisplayActivity.this, R.string.all_notes_del, Toast.LENGTH_SHORT).show();
                                    islistEmpty(true);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle(getString(R.string.delete_all_notes));
                    alert.show();

                    Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                   /* positive.setBackgroundColor(Color.WHITE);
                    positive.setTextColor(getColor(R.color.colorPrimaryDark));*/

                    Button negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                   /* negative.setTextColor(Color.BLACK);
                    negative.setBackgroundColor(Color.WHITE);*/
                }
                else{
                    Toast.makeText(this, "Nothing to Delete", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.view:
                if(sharedPref.get_flag() == 0) {
                    sharedPref.save_flag(1);
                    // 0 linear
                    item.setIcon(R.drawable.ic_round_view_stream_24);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                }
                else{
                    sharedPref.save_flag(0);
                    item.setIcon(R.drawable.ic_round_view_quilt_24);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                return true;
            case R.id.share_app:
                ShareThisApp();
                return true;
            case R.id.rate_app:
                rateMe();
                return true;
            case R.id.dark_mode:
                ChangeModes(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Note item = noteAdapter.getNoteAt(position);
                noteViewModel.delete(noteAdapter.getNoteAt(position));

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.item_removed, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteViewModel.insert(item);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f; //speed
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public Uri getLocalBitmapUri() {


        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.drawable.web)).getBitmap(); ;

            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(DisplayActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return bmpUri;
    }

    private void ShareThisApp() {
        try { //share app function


            Uri bmpUri = getLocalBitmapUri(); //get uri from bitmap
            //  Uri uri = FileProvider.getUriForFile()
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image

                Intent shareIntent = new Intent();

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app));
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                try {
                    shareIntent.setPackage("com.whatsapp");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.shareapp)));
                }
            } else {
                // user did'n get bitmap uri, sharing failed
                Toast.makeText(DisplayActivity.this, R.string.error_in_sharing, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, R.string.please_try_again, Toast.LENGTH_SHORT).show();
        }





    }

    private void ChangeModes(MenuItem item) {
        if(sharedPref.get_mode() == 0){
            sharedPref.save_mode(1);
           // item.setIcon(getResources().getDrawable(R.drawable.ic_round_nights_stay_24));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        else if(sharedPref.get_mode() == 1){
            sharedPref.save_mode(0);
            //item.setIcon(getResources().getDrawable(R.drawable.ic_round_nights_stay_24));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPref.get_mode() == 0){


            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        else if(sharedPref.get_mode() == 1){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

    }
}