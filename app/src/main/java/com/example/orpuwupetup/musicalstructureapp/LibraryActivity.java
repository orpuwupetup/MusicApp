package com.example.orpuwupetup.musicalstructureapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.orpuwupetup.musicalstructureapp.databinding.ActivityLibraryBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {


    //this activity variables
    ActivityLibraryBinding binding;
    boolean isPlaying = false;
    public ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library);

        //get intent with updated songs list and isPlaying variable if this activity is called by
        //another activity
        Intent intent = getIntent();
        if(intent != null){
            isPlaying = intent.getBooleanExtra("IsPlaying", isPlaying);
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");

        }
        //if nothing is playing, turn of current song display
        if(!isPlaying){
            binding.currentSong.setVisibility(View.GONE);
        }
    }

    //one method for all buttons associated with changing activity
    public void changeActivity (View v){

        Intent changeActivity = null;

        if (v.getId() == binding.favourites.getId()){
            changeActivity = new Intent(LibraryActivity.this, FavouritesActivity.class);
        }else if(v.getId() == binding.home.getId()){
            changeActivity = new Intent(LibraryActivity.this, MainActivity.class);
        }else if(v.getId() == binding.current.getId() || v.getId() == binding.aritst.getId()){
            changeActivity = new Intent(LibraryActivity.this, CurrentActivity.class);
        }

        if(changeActivity != null) {

            //create bundle with our songs array list and send it via intent to another activity
            Bundle args = new Bundle();
            args.putSerializable("SONGSLIST", (Serializable) songs);
            changeActivity.putExtra("BUNDLE", args);
            changeActivity.putExtra("IsPlaying", isPlaying);
            LibraryActivity.this.startActivity(changeActivity);
        }



    }
}
