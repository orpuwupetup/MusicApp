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
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");

        }

        //check which song is current and display it on screen
        displayCurrentSong();
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
            CurrentActivity.this.startActivity(changeActivity);
        }



    }

    private void displayCurrentSong(){
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).current()){
                binding.currentAlbumCover.setImageResource(songs.get(i).getAlbumCover());
                binding.title.setText(songs.get(i).title());
                binding.artist.setText(songs.get(i).artist());

                //converting current songs length to minutes and seconds and making string out of outcome
                //(setting random played time to make it more pleasing to the eye

                int timePlayed = (int) (Math.random()*songs.get(i).length());
                int timeLeft = songs.get(i).length() - timePlayed;
                int minutesPlayed = timePlayed/60;
                int secondsPlayed = timePlayed%60;
                int minutesLeft = timeLeft/60;
                int secondsLeft = timeLeft%60;

                //calculate how many pixels wide should be timePlayed indicator, and set its width accordingly
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.songPlayedTime.getLayoutParams();
                params.width = (timePlayed * binding.songLength.getLayoutParams().width)/ songs.get(i).length();
                binding.songPlayedTime.setLayoutParams(params);

                String timePlayedText;
                if (secondsPlayed<10){
                    timePlayedText = "" + minutesPlayed + ":" + "0" + secondsPlayed;
                }else{
                    timePlayedText = "" + minutesPlayed + ":" + secondsPlayed;
                }

                String timeLeftText;
                if (secondsLeft<10){
                    timeLeftText = "" + minutesLeft + ":" + "0" + secondsLeft;
                }else{
                    timeLeftText = "" + minutesLeft + ":" + secondsLeft;
                }

                binding.timePlayed.setText(timePlayedText);
                binding.timeLeft.setText(timeLeftText);

                //setting images for next and previous album covers
                if (i == 0) {
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
