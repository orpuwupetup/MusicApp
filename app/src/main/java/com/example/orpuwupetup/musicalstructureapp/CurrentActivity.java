package com.example.orpuwupetup.musicalstructureapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.orpuwupetup.musicalstructureapp.databinding.ActivityCurrentBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrentActivity extends AppCompatActivity {

    ActivityCurrentBinding binding;
    boolean isPlaying = false;
    boolean wasPaused = false;
    boolean shufflesOn = false;
    int nextShuffleSong = 0;
    int previousShuffleSong = 0;
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
            wasPaused = intent.getBooleanExtra("WasPaused", wasPaused);
            shufflesOn = intent.getBooleanExtra("isShuffle", shufflesOn);
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");

        }
        nextShuffleSong = (int)(Math.random()*songs.size());
        previousShuffleSong = (int)(Math.random()*songs.size());

        binding.nothingsPlaying.setVisibility(View.GONE);
            //check which song is current and display it on screen
            displayCurrentSong();
        if (!wasPaused && !isPlaying){
            binding.currentAlbumCover.setVisibility(View.INVISIBLE);
            binding.previousAlbumCover.setVisibility(View.INVISIBLE);
            binding.nextAlbumCover.setVisibility(View.INVISIBLE);
            binding.title.setVisibility(View.INVISIBLE);
            binding.artist.setVisibility(View.INVISIBLE);
            binding.nothingsPlaying.setVisibility(View.VISIBLE);
            binding.shadow.setVisibility(View.INVISIBLE);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.songPlayedTime.getLayoutParams();
            params.width = 1;
            binding.songPlayedTime.setLayoutParams(params);

            binding.timePlayed.setText(R.string.zero_played_time);
            binding.timeLeft.setText("-:--");
        }

        //method for next song button
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i++){
                    if (songs.get(i).current()){
                        songs.get(i).current(false);
                        if(shufflesOn){
                            songs.get(nextShuffleSong).current(true);
                            nextShuffleSong = (int)(Math.random()*songs.size());
                            previousShuffleSong = i;
                            break;
                        }else if (i == songs.size() - 1){
                            songs.get(0).current(true);
                            break;
                        }else{
                            songs.get(i+1).current(true);
                            break;
                        }
                    }
                }
                displayCurrentSong();
            }
        });

        //method for previous song button
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i++){
                    if (songs.get(i).current()){
                        songs.get(i).current(false);
                        if(shufflesOn){
                            songs.get(previousShuffleSong).current(true);
                            previousShuffleSong = (int)(Math.random()*songs.size());
                            nextShuffleSong = i;
                            break;
                        }else if (i == 0){
                            songs.get(songs.size() - 1).current(true);
                            break;
                        }else{
                            songs.get(i-1).current(true);
                            break;
                        }
                    }
                }
                displayCurrentSong();
            }
        });

        //shuffle button methods
            //set image on this image button accordingly to shuffleOn variable state
        if (shufflesOn){
            binding.shuffle.setBackgroundResource(R.drawable.ic_shuffleonnoborder);
        }else{
            binding.shuffle.setBackgroundResource(R.drawable.ic_shuffleofnoborder);
        }
        binding.shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shufflesOn){
                    shufflesOn = false;
                    binding.shuffle.setBackgroundResource(R.drawable.ic_shuffleofnoborder);
                    displayCurrentSong();
                }else{
                    shufflesOn = true;
                    binding.shuffle.setBackgroundResource(R.drawable.ic_shuffleonnoborder);
                    displayCurrentSong();
                }
            }
        });

        //Liked button actions
        binding.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i ++){
                    if (songs.get(i).current()){
                        if (songs.get(i).liked()){
                            songs.get(i).liked(false);
                            binding.liked.setBackgroundResource(R.drawable.ic_unlikednoborder);
                        }else{
                            songs.get(i).liked(true);
                            binding.liked.setBackgroundResource(R.drawable.ic_likednoborder);
                        }
                        break;
                    }
                }
            }
        });

        //Play button actions
        if (isPlaying){
            binding.playButton.setBackgroundResource(R.drawable.ic_pausenoborder);
        }else{
            binding.playButton.setBackgroundResource(R.drawable.ic_playnoborder);
        }

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    isPlaying = false;
                    binding.playButton.setBackgroundResource(R.drawable.ic_playnoborder);
                }else{
                    if (!wasPaused){
                        wasPaused = true;
                        if(!shufflesOn){
                            songs.get(0).current(true);
                        }else{
                            songs.get((int)(songs.size()*Math.random())).current(true);
                        }
                        binding.currentAlbumCover.setVisibility(View.VISIBLE);
                        binding.previousAlbumCover.setVisibility(View.VISIBLE);
                        binding.nextAlbumCover.setVisibility(View.VISIBLE);
                        binding.nothingsPlaying.setVisibility(View.GONE);
                        binding.title.setVisibility(View.VISIBLE);
                        binding.artist.setVisibility(View.VISIBLE);
                        binding.shadow.setVisibility(View.VISIBLE);
                        displayCurrentSong();
                    }
                    isPlaying = true;
                    binding.playButton.setBackgroundResource(R.drawable.ic_pausenoborder);
                }
            }
        });

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
            changeActivity.putExtra("WasPaused", wasPaused);
            changeActivity.putExtra("isShuffle", shufflesOn);
            CurrentActivity.this.startActivity(changeActivity);
        }



    }

    private void displayCurrentSong(){
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).current()) {
                binding.currentAlbumCover.setImageResource(songs.get(i).getAlbumCover());
                binding.title.setText(songs.get(i).title());
                binding.artist.setText(songs.get(i).artist());

                if (songs.get(i).liked()){
                    binding.liked.setBackgroundResource(R.drawable.ic_likednoborder);
                }else{
                    binding.liked.setBackgroundResource(R.drawable.ic_unlikednoborder);
                }

                //converting current songs length to minutes and seconds and making string out of outcome
                //(setting random played time to make it more pleasing to the eye

                int timePlayed = (int) (Math.random() * songs.get(i).length());
                //make sure that song played time is bigger than zero
                if (timePlayed == 0){
                    timePlayed = 1;
                }
                int timeLeft = songs.get(i).length() - timePlayed;
                int minutesPlayed = timePlayed / 60;
                int secondsPlayed = timePlayed % 60;
                int minutesLeft = timeLeft / 60;
                int secondsLeft = timeLeft % 60;

                //calculate how many pixels wide should be timePlayed indicator, and set its width accordingly
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.songPlayedTime.getLayoutParams();
                params.width = (timePlayed * binding.songLength.getLayoutParams().width) / songs.get(i).length();
                binding.songPlayedTime.setLayoutParams(params);

                String timePlayedText;
                if (secondsPlayed < 10) {
                    timePlayedText = "" + minutesPlayed + ":" + "0" + secondsPlayed;
                } else {
                    timePlayedText = "" + minutesPlayed + ":" + secondsPlayed;
                }

                String timeLeftText;
                if (secondsLeft < 10) {
                    timeLeftText = "" + minutesLeft + ":" + "0" + secondsLeft;
                } else {
                    timeLeftText = "" + minutesLeft + ":" + secondsLeft;
                }


                binding.timePlayed.setText(timePlayedText);
                binding.timeLeft.setText(timeLeftText);

                //setting images for next and previous album covers
                if(shufflesOn){
                    binding.previousAlbumCover.setImageResource(songs.get(previousShuffleSong).getAlbumCover());
                    binding.nextAlbumCover.setImageResource(songs.get(nextShuffleSong).getAlbumCover());
                }else if (i == 0) {
                    binding.previousAlbumCover.setImageResource(songs.get(songs.size() - 1).getAlbumCover());
                    binding.nextAlbumCover.setImageResource(songs.get(i+1).getAlbumCover());
                }else if (i == songs.size()-1){
                    binding.previousAlbumCover.setImageResource(songs.get(i-1).getAlbumCover());
                    binding.nextAlbumCover.setImageResource(songs.get(0).getAlbumCover());
                }else{
                    binding.previousAlbumCover.setImageResource(songs.get(i-1).getAlbumCover());
                    binding.nextAlbumCover.setImageResource(songs.get(i+1).getAlbumCover());
                }
                break;
            }
        }
    }
}
