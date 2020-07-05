package com.example.coinman.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Preferences;

public class pref {

    private Preferences preferences ;

    int highScore ;
    boolean audioState ;

    public pref(){
        preferences = Gdx.app.getPreferences("com.example.coinman.game") ;
        highScore = preferences.getInteger("highscore" , 0) ;
        audioState = preferences.getBoolean("musicstate" , true) ;
    }

    public void updateHighScore( int score){
        highScore = score ;
        preferences.putInteger("highscore" , score) ;
        preferences.flush();
    }

    public int getHighScore(){
        return highScore ;
    }

    public void updateMusicState( boolean state){
        preferences.putBoolean( "musicstate" , state ) ;
    }

    public boolean getMusicState(){
        return preferences.getBoolean("musicState" , true) ;
    }

}
