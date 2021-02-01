package com.sankets.penit;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BrushView extends View {
    public Paint brush = new Paint();
    public Path path = new Path();
    //public Button btnEraseAll;
    public FloatingActionButton btnEraseAll;
    public byte[] bytes;
    public LayoutParams params;
    public boolean want_erase;

   // private boolean erase=false;

    public BrushView(Context context) {
        super(context);
        brush.setAntiAlias(true);
        brush.setColor(getResources().getColor(R.color.brush_lazy));
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(10f);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

       // setBackgroundColor(getResources().getColor(R.color.colorButtonText));
        setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorButtonText));





      /*  btnEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//reset the path
                path.reset();

//invalidate the view
                postInvalidate();
            }
        });*/
    }



    @Override
    public void setDrawingCacheBackgroundColor(int color) {
        super.setDrawingCacheBackgroundColor(color);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }
    /* public void setColor(String newColor){
        invalidate();
        int paintColor = Color.WHITE;
        brush.setColor(Color.WHITE);
    }
*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float pointX = event.getX();
        float pointY = event.getY();

        // Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;

            default:
                return false;
        }

        // Force a view to draw again
      //  drawCanvas.drawPath(path, brush);//--> Draw on canvasBitmap
        postInvalidate();
        return false;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBrush(Paint brush) {
        this.brush = brush;
    }

   /* public void setWant_erase(boolean want_erase) {
        this.want_erase = want_erase;
    }

    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) {
            brush.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            brush.setColor(Color.WHITE);
        }

        else {
            brush.setXfermode(null);
            brush.setColor(Color.BLACK);
        }
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
       // canvas.drawPath(path, brush);
     //   canvas.restore();

        brush.setColor(getResources().getColor(R.color.brush_lazy));

        /*if(!want_erase){
            brush.setAntiAlias(true);
            brush.setColor(Color.BLACK);
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeJoin(Paint.Join.ROUND);
            brush.setStrokeWidth(10f);

        }
        else{
            brush.setAntiAlias(true);
            brush.setColor(Color.WHITE);
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeJoin(Paint.Join.ROUND);
            brush.setStrokeWidth(10f);
        }*/
        if(bytes!=null){
            Bitmap scratch = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
          //  canvas.drawColor(Color.BLACK);
           // canvas.drawColor(getResources().getColor(R.color.colorButtonText));
            System.out.println("path.getFillType().name() = " + path.getFillType().name());

            canvas.drawPath(path, brush);
//            scratch.eraseColor(1);
            canvas.drawBitmap(scratch, 0, 0, null);

            //("bytess" + bytes);
           // canvas.drawPath(path, brush);

        }
        else{
            canvas.drawPath(path, brush);
        }

       // canvas.drawBitmap(canvasBitmap, 0, 0, brush);
     //   canvas.save();

        //("path = " + path);

    }




}
