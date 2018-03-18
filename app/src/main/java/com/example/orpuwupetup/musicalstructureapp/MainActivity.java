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
    boolean shufflesOn = false;
    public ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //create list of songs to use in program
        songs = new ArrayList<Song>();

        //add songs to the list, and add album covers to some of them
        songs.add(new Song("Beyonce", "Best Beyonce Song", 195));
        songs.get(0).setAlbumCover(R.drawable.beyonce);
        songs.add(new Song("Shakira", "Best Shakira Song", 127));
        songs.get(1).setAlbumCover(R.drawable.shakira);
        songs.add(new Song("Madonna", "Best Madonna Song", 153));
        songs.get(2).setAlbumCover(R.drawable.madonna);
        songs.add(new Song("BeeGees", "Best BeeGees Song", 169));
        songs.get(3).setAlbumCover(R.drawable.beegees);
        songs.add(new Song("Jeniffer Lopez", "Best Jeniffer Lopez Song", 201));
        songs.get(4).setAlbumCover(R.drawable.jlo);
        songs.add(new Song("Nirvana", "Best Nirvana Song", 218));
        songs.get(5).setAlbumCover(R.drawable.nirvana);
        songs.add(new Song("Hunter", "Best Hunter Song", 172));
        songs.add(new Song("Michael Jackson", "Best Michael Jackson Song", 180));
        songs.get(7).setAlbumCover(R.drawable.jackson);
        songs.add(new Song("Katy Perry", "Best Katy Perry Song", 241));
        songs.get(8).setAlbumCover(R.drawable.katyperry);
        songs.add(new Song("Taylor Swift", "Best Taylor Swift Song", 132));
        songs.add(new Song("Boss Hoss", "Best Boss Hoss Song", 213));
        songs.get(10).setAlbumCover(R.drawable.bosshoss);
        songs.add(new Song("Metallica", "Best Metallica Song", 147));
        songs.get(11).setAlbumCover(R.drawable.metallica);

        //get intent with updated songs list and isPlaying variable if this activity is called by
        //another activity
        Intent intent = getIntent();
        if(intent != null){
            isPlaying = intent.getBooleanExtra("IsPlaying", isPlaying);
            wasPaused = intent.getBooleanExtra("WasPaused", wasPaused);
            shufflesOn = intent.getBooleanExtra("isShuffle", shufflesOn);
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
                        if (shufflesOn) {
                            int random = (int)(Math.random()*songs.size());
                            songs.get(random).current(true);
                            binding.title.setText(songs.get(random).title());
                            binding.aritst.setText(songs.get(random).artist());
                        }else if (i == 0){
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
                        if (shufflesOn) {
                            int random = (int)(Math.random()*songs.size());
                            songs.get(random).current(true);
                            binding.title.setText(songs.get(random).title());
                            binding.aritst.setText(songs.get(random).artist());
                        }else if (i == songs.size()-1){
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

        //click listener for play button
        binding.currentPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    isPlaying = false;
                    binding.playButton.setBackgroundResource(R.drawable.ic_play);
                    binding.currentPlayButton.setBackgroundResource(R.drawable.ic_play);
                }else{
                    isPlaying = true;
                    binding.playButton.setBackgroundResource(R.drawable.ic_pause);
                    binding.currentPlayButton.setBackgroundResource(R.drawable.ic_pause);
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
        }else if(v.getId() == binding.title.getId() || v.getId() == binding.aritst.getId() || v.getId() == binding.title.getId()){
            changeActivity = new Intent(MainActivity.this, CurrentActivity.class);
        }

        if(changeActivity != null) {

            //create bundle with our songs array list and send it via intent to another activity
            Bundle args = new Bundle();
            args.putSerializable("SONGSLIST", (Serializable) songs);
            changeActivity.putExtra("BUNDLE", args);
            changeActivity.putExtra("IsPlaying", isPlaying);
            changeActivity.putExtra("WasPaused", wasPaused);
            changeActivity.putExtra("isShuffle", shufflesOn);

            MainActivity.this.startActivity(changeActivity);
        }
    }
}

