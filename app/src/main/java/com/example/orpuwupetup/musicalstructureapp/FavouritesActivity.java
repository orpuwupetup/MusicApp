package com.example.orpuwupetup.musicalstructureapp;

        import android.content.Intent;
        import android.databinding.DataBindingUtil;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.example.orpuwupetup.musicalstructureapp.databinding.ActivityFavouritesBinding;

        import java.io.Serializable;
        import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    public ArrayList<Song> songs;
    public ArrayList<Song> likedSongs = new ArrayList<Song>();
    ActivityFavouritesBinding binding;
    boolean wasPaused = false;
    boolean isPlaying = false;
    boolean shufflesOn = false;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favourites);

        //get intent with updated songs list and isPlaying variable if this activity is called by
        //another activity
        Intent intent = getIntent();
        if (intent != null) {
            isPlaying = intent.getBooleanExtra("IsPlaying", isPlaying);
            wasPaused = intent.getBooleanExtra("WasPaused", wasPaused);
            shufflesOn = intent.getBooleanExtra("isShuffle", shufflesOn);
            Bundle args2 = intent.getBundleExtra("BUNDLE");
            songs = (ArrayList<Song>) args2.getSerializable("SONGSLIST");
        }

        //if nothing is playing, turn of current song display
        if (!isPlaying) {
            binding.currentSong.setVisibility(View.GONE);
            binding.currentPlayButton.setBackgroundResource(R.drawable.ic_play);
        } else {
            binding.currentPlayButton.setBackgroundResource(R.drawable.ic_pause);
        }
        if (wasPaused) {
            binding.currentSong.setVisibility(View.VISIBLE);
        }

        //set which activity is open for all of the songs in songs array, so that SongAdapter could
        //set different images for some view in list, according to which activity is opened and add
        //liked songs to likedSongs array to show them on screen
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setWhichActivityIsOn("favourites");
            if (songs.get(i).liked()) {
                likedSongs.add(songs.get(i));
            }
        }
        if (likedSongs.size() != 0) {

            //hide text about no liked songs yet
            binding.noLikedSongs.setVisibility(View.GONE);

            //declaring and setting adapter for showing songs
            SongAdapter itemAdaper = new SongAdapter(FavouritesActivity.this, likedSongs);
            ListView listView = binding.list;
            listView.setAdapter(itemAdaper);

        } else {

            //hide list View
            binding.list.setVisibility(View.GONE);
        }

        //listeners for changing song to next and previous
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).current()) {
                        if (shufflesOn) {
                            int random = (int)(Math.random()*songs.size());
                            songs.get(random).current(true);
                            binding.title.setText(songs.get(random).title());
                            binding.aritst.setText(songs.get(random).artist());
                        }else if (i == 0) {
                            songs.get(songs.size() - 1).current(true);
                            binding.title.setText(songs.get(songs.size() - 1).title());
                            binding.aritst.setText(songs.get(songs.size() - 1).artist());
                        } else {
                            songs.get(i - 1).current(true);
                            binding.title.setText(songs.get(i - 1).title());
                            binding.aritst.setText(songs.get(i - 1).artist());
                        }
                        songs.get(i).current(false);
                        break;
                    }
                }
                refreshColors();
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).current()) {
                        if (shufflesOn) {
                            int random = (int)(Math.random()*songs.size());
                            songs.get(random).current(true);
                            binding.title.setText(songs.get(random).title());
                            binding.aritst.setText(songs.get(random).artist());
                        }else if (i == songs.size() - 1) {
                            songs.get(0).current(true);
                            binding.title.setText(songs.get(0).title());
                            binding.aritst.setText(songs.get(0).artist());
                        } else {
                            songs.get(i + 1).current(true);
                            binding.title.setText(songs.get(i + 1).title());
                            binding.aritst.setText(songs.get(i + 1).artist());
                        }
                        songs.get(i).current(false);
                        break;
                    }
                }
                refreshColors();

            }
        });

        //click listener for play button
        binding.currentPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    isPlaying = false;
                    binding.currentPlayButton.setBackgroundResource(R.drawable.ic_play);
                }else{
                    isPlaying = true;
                    binding.currentPlayButton.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });

        //method for setting current song view title and artist TextViews to right values on create
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).current()) {
                binding.title.setText(songs.get(i).title());
                binding.aritst.setText(songs.get(i).artist());
                break;
            }
        }



        // method for choosing song to play from the list itself
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                refreshColors();
//                if (title != null){
//                    title.setTextColor(getResources().getColor(R.color.title_color));
//                }


                binding.title.setText(likedSongs.get(position).title());

                String currentTitle = likedSongs.get(position).title();

                for (int i = 0; i < binding.list.getChildCount(); i ++) {

                    title = (TextView) binding.list.getChildAt(i).findViewById(R.id.title);
                    if (title.getText().equals(currentTitle)) {
                        title.setTextColor(getResources().getColor(R.color.playing_song));
                    }else{
                        title.setTextColor(getResources().getColor(R.color.title_color));
                    }


                }

                binding.aritst.setText(likedSongs.get(position).artist());

                // set all of the songs in songs array as not current
                for (int j = 0; j < songs.size(); j ++){
                    if (songs.get(j).current()){
                        songs.get(j).current(false);
                    break;
                    }
                }

                //set song in song array to current, accordingly to which element was clicked
                for (int j = 0; j < songs.size(); j ++){
                    if (songs.get(j).title().equals(likedSongs.get(position).title())){
                        songs.get(j).current(true);
                        break;
                    }
                }




                binding.currentSong.setVisibility(View.VISIBLE);
                isPlaying = true;
                wasPaused = true;
                binding.currentPlayButton.setBackgroundResource(R.drawable.ic_pause);
            }
        });
    }

    //one method for all buttons associated with changing activity
    public void changeActivity(View v) {

        Intent changeActivity = null;

        if (v.getId() == binding.library.getId()) {
            changeActivity = new Intent(FavouritesActivity.this, LibraryActivity.class);
        } else if (v.getId() == binding.home.getId()) {
            changeActivity = new Intent(FavouritesActivity.this, MainActivity.class);
        } else if (v.getId() == binding.current.getId() || v.getId() == binding.aritst.getId() || v.getId() == binding.title.getId()) {
            changeActivity = new Intent(FavouritesActivity.this, CurrentActivity.class);
        }

        if (changeActivity != null) {

            //create bundle with our songs array list and send it via intent to another activity
            Bundle args = new Bundle();
            args.putSerializable("SONGSLIST", (Serializable) songs);
            changeActivity.putExtra("BUNDLE", args);
            changeActivity.putExtra("IsPlaying", isPlaying);
            changeActivity.putExtra("WasPaused", wasPaused);
            changeActivity.putExtra("isShuffle", shufflesOn);
            FavouritesActivity.this.startActivity(changeActivity);
        }


    }

    void refreshColors (){

        String currentTitle = " ";
        for (int j = 0; j < songs.size(); j++) {
            if (songs.get(j).current()) {
                currentTitle = songs.get(j).title();
                break;
            }
        }

        for (int i = 0; i < binding.list.getChildCount(); i ++) {

            title = (TextView) binding.list.getChildAt(i).findViewById(R.id.title);
            if (title.getText().equals(currentTitle)) {
                title.setTextColor(getResources().getColor(R.color.playing_song));
            }else{
                title.setTextColor(getResources().getColor(R.color.title_color));
            }


        }
    }
}
