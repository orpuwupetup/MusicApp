package com.example.orpuwupetup.musicalstructureapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by cezar on 18.01.2018.
 */

public class SongAdapter extends ArrayAdapter<Song> {
    @NonNull
    private static final String LOG_TAG = SongAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param songs   A List of AndroidFlavor objects to display in a list
     */
    public SongAdapter(Activity context, ArrayList<Song> songs) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //get current song from Song class
        Song currentSong = getItem(position);

        // finding view for showing title of the song
        TextView title = (TextView) listItemView.findViewById(R.id.title);

        // setting text of title to the title of current song
        title.setText(currentSong.title());

        // finding view for showing current song artist
        TextView artist = (TextView) listItemView.findViewById(R.id.artist);

        // setting text of this view to artist of current song
        artist.setText(currentSong.artist());

        // finding View for showing lenght of the current song
        TextView songLength = (TextView) listItemView.findViewById(R.id.songLength);

        //converting length of song from seconds to minutes and seconds and showing it in TextView
        int minutes = currentSong.length()/60;
        int seconds = currentSong.length()%60;
        String lengthOfTheSong = " ";
        if (seconds<10){
            lengthOfTheSong = "" + minutes + ":" + "0" + seconds;
        }else{
            lengthOfTheSong = "" + minutes + ":" + seconds;
        }
        songLength.setText(lengthOfTheSong);

        //finding view for showing album cover of the song and displaying it
        ImageView coverImage = (ImageView) listItemView.findViewById(R.id.coverImage);
        coverImage.setImageResource(currentSong.getAlbumCover());

        // return whole view with song informations
        return listItemView;
    }
}