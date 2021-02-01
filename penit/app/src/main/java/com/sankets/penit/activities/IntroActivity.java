package com.sankets.penit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.sankets.penit.R;
import com.sankets.penit.SharedPref;
import com.sankets.penit.adapters.IntroAdapter;

public class IntroActivity extends AppCompatActivity {
    IntroAdapter sectionsPagerAdapter;
    ViewPager viewPager;
   // SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        sectionsPagerAdapter = new IntroAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager_intro);
        viewPager.setAdapter(sectionsPagerAdapter);
        getZero();

    }
    public void getZero(){ //switch
        viewPager.setCurrentItem(0);
    }
    public void getFirst(){
        viewPager.setCurrentItem(1);
    } //first
    public void getSecond(){
        viewPager.setCurrentItem(2); //sec
    }//switch
    /*public void getThird(){
        viewPager.setCurrentItem(3); //three
    }//switch*/

    @Override
    protected void onStart() {
        super.onStart();
        SharedPref sharedPref = new SharedPref(this);
        if(!sharedPref.is_first_time()){
            startActivity(new Intent(IntroActivity.this,DisplayActivity.class));
            finish();
        }
    }
}