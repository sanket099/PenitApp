package com.sankets.penit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sankets.penit.BrushView;
import com.sankets.penit.BuildConfig;
import com.sankets.penit.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawActivity extends AppCompatActivity {
    BrushView view;
    Bitmap bitmap;
    Bitmap mutableBitmap;
    boolean WantEraser;
   /// public static final String IMAGE = "img";
    public static final String BYTE_ARRAY= "bytearray";
    Bitmap newBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_draw);
        view=new BrushView(this);
        setContentView(view);

       // addContentView(view.btnEraseAll,view.params);
        if(getIntent().getByteArrayExtra(BYTE_ARRAY)!=null){
            byte[] bitmapdata = getIntent().getByteArrayExtra(BYTE_ARRAY);

            //Bitmap bitmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
            bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
           // Canvas c = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            view.setBytes(bitmapdata);

          newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(getColor(R.color.colorButtonText));
            canvas.drawBitmap(bitmap, 0F, 0F, null);

            paint.setColor(getColor(R.color.brush));
          //  c.drawBitmap(mutableBitmap,0,0,paint);
            //("mutableBitmap = " + mutableBitmap);

        }

        //("view.path = " + view.path);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_draw_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_all:
                clearAll();
                return true;
            case R.id.save_draw_note:
                saveDrawNote();
                return true;

           /* case R.id.erase:
                erase(item);
                return true;*/
            case R.id.share_note_draw:
                if(newBitmap!=null)
                shareNote();
                else
                    Toast.makeText(this, "Save the note first", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void erase(MenuItem item) {
        if(!WantEraser) {
            //view.setWant_erase(true);
            item.setIcon(getDrawable(R.drawable.ic_round_brush_24));
            WantEraser = true;



            view.setErase(true);
            view.setColor("#fff");
        }
        else{
            //view.setWant_erase(false);

            item.setIcon(getDrawable(R.drawable.ic_icon_awesome_eraser));
            WantEraser = false;

            view.setErase(false);
        }

    }*/

    private void clearAll() {
        view.path.reset();
        view.postInvalidate();
       // view.path = new Path();


    }

    private Uri getUriFromBitmap(Bitmap resource){
        Uri uri = null;
        try {
            // URL url = new URL(link);
            // showProgress(true);
            // This way, you don't need to request external read/write permission.

            File file = new File(DrawActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            resource.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            uri  = FileProvider.getUriForFile(DrawActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (IOException e) {
            //e.printStackTrace();
            //    //(e.getMessage());
            //.println(e.getMessage());
        }
        //("uri.toString() = " + uri.toString());
        return uri;
    }




    private void shareNote() {
      /*  SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                //By continuing you agree to our\n Terms of Service and Privacy Policy
                "Shared From Pen-It App : ");
        spanTxt.append("Download Now");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.download_link)));
                startActivity(browserIntent);
            }
        }, 24,spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM,getUriFromBitmap(newBitmap));
        shareIntent.putExtra
                (Intent.EXTRA_TEXT,getString(R.string.download_link));

        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_note)));
    }

    private void saveDrawNote() {

        Bitmap bitmap = Bitmap.createBitmap(1024,1920, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


        Intent data = new Intent();
        //("bitmap1 = " + bitmap);
/*

        data.putExtra(IMAGE, BitMapToString(bitmap));*/

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //("byteArray = " + Arrays.toString(byteArray));
        //("BitMapToString(bitmap) = " + BitMapToString(bitmap));
        data.putExtra(BYTE_ARRAY,byteArray);


        int id =getIntent().getIntExtra(EditTypeActivity.EXTRA_ID,-1);
        if(id != -1){

            data.putExtra(EditTypeActivity.EXTRA_ID,id);

        }


        setResult(RESULT_OK, data);

        finish();//close

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}