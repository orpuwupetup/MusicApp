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
    boolean wasPaused = false;
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
            wasPaused = intent.getBooleanExtra("WasPaused", wasPaused);
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            if(intent.getBundleExtra("BUNDLE") != null) {
                songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");
            }

        }

        //hide current playing song display when nothing is playing)
        if(!isPlaying) {
            binding.currentSong.setVisibility(View.GONE);
            binding.playButton.setBackgroundResource(R.drawable.ic_play);
            binding.currentPlayButton.setBackgroundResource(R.drawable.ic_play);
        }else{
            binding.playButton.setBackgroundResource(R.drawable.ic_pause);
            binding.currentPlayButton.setBackgroundResource(R.drawable.ic_pause);
            binding.currentSong.setVisibility(View.VISIBLE);

        }
        if(wasPaused){
            binding.currentSong.setVisibility(View.VISIBLE);
        }

        //set on click listener on Play button
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int playedCount = 0;
                if (!isPlaying){
                    isPlaying = true;
                    binding.currentSong.setVisibility(View.VISIBLE);
                    binding.playButton.setBackgroundResource(R.drawable.ic_pause);
                    binding.currentPlayButton.setBackgroundResource(R.drawable.ic_pause);
                    if(!wasPaused){
                        binding.title.setText(songs.get(0).title());
                        binding.aritst.setText(songs.get(0).artist());
                        for(int i = 0; i < songs.size(); i++){
                            songs.get(i).current(false);
                        }
                        songs.get(0).current(true);
                    }
                }else{
                    isPlaying = false;
                    wasPaused = true;
                    binding.playButton.setBackgroundResource(R.drawable.ic_play);
                    binding.currentPlayButton.setBackgroundResource(R.drawable.ic_play);
                }
            }
        });

        //listeners for changing song to next and previous
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i ++){
                    if(songs.get(i).current()){
                        if (i == 0){
                            songs.get(songs.size()-1).current(true);
                            binding.title.setText(songs.get(songs.size()-1).title());
                            binding.aritst.setText(songs.get(songs.size()-1).artist());
                        }else{
                            songs.get(i-1).current(true);
                            binding.title.setText(songs.get(i-1).title());
                            binding.aritst.setText(songs.get(i-1).artist());
                        }
                        songs.get(i).current(false);
                        break;
                    }
                }
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i ++){
                    if(songs.get(i).current()){
                        if (i == songs.size()-1){
                            songs.get(0).current(true);
                            binding.title.setText(songs.get(0).title());
                            binding.aritst.setText(songs.get(0).artist());
                        }else{
                            songs.get(i+1).current(true);
                            binding.title.setText(songs.get(i+1).title());
                            binding.aritst.setText(songs.get(i+1).artist());
                        }
                        songs.get(i).current(false);
                        break;
                    }
                }
            }
        });

        //method for setting current song view title and artist TextViews to right values on create
        for (int i = 0; i<songs.size(); i++){
            if (songs.get(i).current()){
                binding.title.setText(songs.get(i).title());
                binding.aritst.setText(songs.get(i).artist());
                break;
            }
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
            changeActivity.putExtra("WasPaused", wasPaused);

            MainActivity.this.startActivity(changeActivity);
        }



    }
}

