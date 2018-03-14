package com.example.orpuwupetup.musicalstructureapp;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.orpuwupetup.musicalstructureapp.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //initiation of app activity variables
        //I'm using data binding library to cut out some of unnecessary code lines
    ActivityMainBinding binding;
    boolean isPlaying = false;
    public ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //create list of songs to use in program
        songs = new ArrayList<Song>();

        //add songs to the list
        songs.add(new Song("Beyonce", "Best Beyonce Song", 195));
        songs.add(new Song("Shakira", "Best Shakira Song", 127));
        songs.add(new Song("Madonna", "Best Madonna Song", 153));
        songs.add(new Song("BeeGees", "Best BeeGees Song", 169));
        songs.add(new Song("Jeniffer Lopez", "Best Jeniffer Lopez Song", 201));
        songs.add(new Song("Nirvana", "Best Nirvana Song", 218));
        songs.add(new Song("Hunter", "Best Hunter Song", 172));
        songs.add(new Song("Michael Jackson", "Best Michael Jackson Song", 180));
        songs.add(new Song("Katy Perry", "Best Katy Perry Song", 241));
        songs.add(new Song("Taylor Swift", "Best Taylor Swift Song", 132));
        songs.add(new Song("Bos Hos", "Best Bos Hos Song", 213));
        songs.add(new Song("Metallica", "Best Metallica Song", 147));

        //get intent with updated songs list and isPlaying variable if this activity is called by
        //another activity
        Intent intent = getIntent();
        if(intent != null){
            isPlaying = intent.getBooleanExtra("IsPlaying", isPlaying);
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            if(intent.getBundleExtra("BUNDLE") != null) {
                songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");
            }

        }

        //hide current playing song display when nothing is playing)
        if(!isPlaying) {
            binding.currentSong.setVisibility(View.GONE);
        }else{
            binding.currentSong.setVisibility(View.VISIBLE);
        }




    }

    // one method for all the buttons associated with changing of activities to minimalize code quantity
    public void changeActivity (View v){

        Intent changeActivity = null;

        if (v.getId() == binding.favourites.getId()){
            changeActivity = new Intent(MainActivity.this, FavouritesActivity.class);
        }else if(v.getId() == binding.library.getId()){
            changeActivity = new Intent(MainActivity.this, LibraryActivity.class);
        }else if(v.getId() == binding.title.getId() || v.getId() == binding.aritst.getId()){
            changeActivity = new Intent(MainActivity.this, CurrentActivity.class);
        }

        if(changeActivity != null) {

            //create bundle with our songs array list and send it via intent to another activity
            Bundle args = new Bundle();
            args.putSerializable("SONGSLIST", (Serializable) songs);
            changeActivity.putExtra("BUNDLE", args);
            changeActivity.putExtra("IsPlaying", isPlaying);

            MainActivity.this.startActivity(changeActivity);
        }



    }
}

