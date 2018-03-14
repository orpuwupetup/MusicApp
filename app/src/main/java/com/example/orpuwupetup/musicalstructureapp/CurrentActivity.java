package com.example.orpuwupetup.musicalstructureapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.orpuwupetup.musicalstructureapp.databinding.ActivityCurrentBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrentActivity extends AppCompatActivity {

    ActivityCurrentBinding binding;
    boolean isPlaying = false;
    public ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current);

        //get intent with updated songs list and isPlaying variable if this activity is called by
        //another activity
        Intent intent = getIntent();
        if(intent != null){
            isPlaying = intent.getBooleanExtra("IsPlaying", isPlaying);
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");

        }
    }


    //one method for all buttons associated with changing activity
    public void changeActivity (View v){

        Intent changeActivity = null;

        if (v.getId() == binding.favourites.getId()){
            changeActivity = new Intent(CurrentActivity.this, FavouritesActivity.class);
        }else if(v.getId() == binding.home.getId()){
            changeActivity = new Intent(CurrentActivity.this, MainActivity.class);
        }else if(v.getId() == binding.library.getId()){
            changeActivity = new Intent(CurrentActivity.this, LibraryActivity.class);
        }

        if(changeActivity != null) {

            //create bundle with our songs array list and send it via intent to another activity
            Bundle args = new Bundle();
            args.putSerializable("SONGSLIST", (Serializable) songs);
            changeActivity.putExtra("BUNDLE", args);
            changeActivity.putExtra("IsPlaying", isPlaying);
            CurrentActivity.this.startActivity(changeActivity);
        }



    }
}
