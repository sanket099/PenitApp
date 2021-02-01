package com.sankets.penit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.sankets.penit.R;
import com.sankets.penit.room_db.Note;

import java.util.Arrays;

public class NoteAdapter extends ListAdapter {
    public static final int TEXT_NOTE = 1;
    public static final int DRAW_NOTE = 2 ;
    private final LayoutInflater inflater;
    //public List<Note> dataModelArrayList;
    public final OnNoteList onNoteList;


    public NoteAdapter( Context context, OnNoteList onNoteList) {
        super(DIFF_CALLBACK);
        //*this.noteArrayList = notes;*//*
        inflater =LayoutInflater.from(context);
        this.onNoteList = onNoteList;

        // sharedPreference = new SharedPreference(context);

      //  noteViewModel = new ViewModelProvider((DisplayTasksActivity) context).get(NoteViewModel.class); //init

    }
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        //@SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getContent().equals(newItem.getContent())
                    &&
                    Arrays.equals(oldItem.getBytes(), newItem.getBytes())
                    && oldItem.getTime().equals(newItem.getTime())

                    ;


        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //("viewType = " + viewType);
        if(viewType == TEXT_NOTE){
            View view = inflater.inflate(R.layout.row_layout, parent, false);
            TextViewHolder holder = new TextViewHolder(view,onNoteList);
            return holder;
        }
        else {
            View view = inflater.inflate(R.layout.draw_show_layout, parent, false);
            DrawViewHolder holder = new DrawViewHolder(view,onNoteList);
            return holder;
        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TEXT_NOTE){
         //   //(holder.getItemViewType());
            ((TextViewHolder)holder).title.setText(((Note)getItem(position)).getTitle());
            ((TextViewHolder)holder).content.setText(((Note)getItem(position)).getContent());
            ((TextViewHolder)holder).date.setText(((Note)getItem(position)).getTime());


        }
        else{

            byte[] bitmapdata = (((Note) getItem(position)).getBytes());
            DisplayMetrics dm = new DisplayMetrics();


            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            //("bitmap.getWidth() = " + bitmap.getWidth());
            //("bitmap.getHeight() = " + bitmap.getHeight());

            //Bitmap bitmap = Bitmap.createScaledBitmap(StringToBitMap(((Note)getItem(position)).getPathString()), 120, 120, false);
            ((DrawViewHolder)holder).tv_date.setText(((Note) getItem(position)).getTime());
            ((DrawViewHolder)holder).iv_show.setImageBitmap(bitmap);


/*             To check
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //("byteArray2 = " + Arrays.toString(byteArray));*/



        }

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            //("e.getMessage() = " + e.getMessage());
            return null;
        }
    }

    public Note getNoteAt(int position){
        return ((Note)getItem(position));
    }

    public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener  {
        TextView title , content, date;

        OnNoteList onNoteList;

        public TextViewHolder(@NonNull View itemView , OnNoteList onNoteList) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            date = itemView.findViewById(R.id.tv_date);



            this.onNoteList = onNoteList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);




        }

        @Override
        public void onClick(View v) {
            onNoteList.OnnoteClick((Note) getItem(getAdapterPosition()),TEXT_NOTE);

        }




        @Override
        public boolean onLongClick(View v) {
            onNoteList.OnLongClick((Note) getItem(getAdapterPosition()));
            return true;
        }
    }

    public class DrawViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener  {
        ImageView iv_show;
        TextView tv_date;

        OnNoteList onNoteList;

        public DrawViewHolder(@NonNull View itemView , OnNoteList onNoteList) {
            super(itemView);
           iv_show = itemView.findViewById(R.id.iv_show);
            tv_date = itemView.findViewById(R.id.tv_img_date);

            this.onNoteList = onNoteList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onNoteList.OnnoteClick((Note) getItem(getAdapterPosition()),DRAW_NOTE);

        }




        @Override
        public boolean onLongClick(View v) {
            onNoteList.OnLongClick((Note) getItem(getAdapterPosition()));
            return true;
        }
    }
    public interface OnNoteList {
        void OnnoteClick(Note userClass, int viewType);
        void OnLongClick(Note downloadObject);


    }

    /*public void filteredlist(ArrayList<Note> filterlist){
        dataModelArrayList = filterlist;
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemViewType(int position) {
     //   //("position = " + position);
        if(((Note)getItem(position)).getBytes() == null){
            return TEXT_NOTE;
        }
        if(((Note)getItem(position)).getBytes().length==1){


            return TEXT_NOTE;
        }
        else{
            return DRAW_NOTE;
        }
        //return super.getItemViewType(position);
    }
}


