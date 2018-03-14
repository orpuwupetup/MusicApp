package com.example.orpuwupetup.musicalstructureapp;

import android.media.Image;
import android.widget.ImageButton;

import java.io.Serializable;

/**
 * Created by cezar on 14.03.2018.
 */

public class Song implements Serializable{

    //declaration of variables
    private String mArtist, mTitle;
    private int mLengthInSeconds, mAlbumCover;
    private boolean mLiked, mCurrent;

    //class constructor
    public Song(String artist, String title, int length){
        mArtist = artist;
        mTitle = title;
        mLengthInSeconds = length;
        mLiked = false;

        //set default album cover
        mAlbumCover = R.drawable.default_album_cover;
    }

    //set methods for getting and setting album covers
    public int getAlbumCover(){
        return mAlbumCover;
    }

    public void setAlbumCover(int newCover){
        mAlbumCover = newCover;
    }

    //methods for getting artist and song title and length
    public String artist(){
        return mArtist;
    }

    public String title(){
        return mTitle;
    }

    public int length(){
        return mLengthInSeconds;
    }

    //methods for seeting and getting liked variable value
    public boolean liked(){
        return mLiked;
    }

    public void liked(boolean liked){
        mLiked = liked;
    }

    //methods for seeting and getting current variable (if is currently playing) value
    public boolean current(){
        return mCurrent;
    }

    public void current(boolean current) {
        mCurrent = current;
    }
}
