package com.reinis.gaztest;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 01.01.18.
 */


/**
 * The Model Class acts as the main data storage, operative memory, to be used by all Functions
 */
public class Model {

    final private String TAG= "model";

    private Sound[] sounds;
    private User user;
    private int playing_with_controls=-1;
    private double load_distance;
    private Context context;
    private ArrayList<TextView> soundsInDistance= new ArrayList<>();

    /*todo rearrange the structure to include a boolean[] checks=new boolean[sounds.length][??],
      todo put the results of all 'check' methods there. Put 'check' methods under Model! determine how many of them are there
     */
    // Constructors

    Model(Sound[] sounds,double load_radius, Context context) {
        this.sounds = sounds;   //all the sounds

        // create the user
        this.user= new User(sounds.length);
        // calculate the centroid coordinates of the coverage area
        /*
        if (user.getLocation() == null) {
            double X = 0;
            double Y = 0;

            for (int i = 0; i < sounds.length; i++) {
                X = X + sounds[i].getX_value();
                Y = Y + sounds[i].getY_value();
            }
            X = X / (sounds.length);
            Y = Y / (sounds.length);
            Location new_loc= new Location(LocationManager.GPS_PROVIDER);
            new_loc.setLatitude(X);
            new_loc.setLongitude(Y);
            this.user.setLocation(new_loc);
        }
        */
        this.user.setSpeed(0);
        this.user.setBearing(0);
        translateAND_OR();
        translateNOT();
        this.load_distance=load_radius;
        this.context=context;

    }


    public Sound[] getSounds() {
        return sounds;
    }
    public void setSounds(Sound[] sounds){
        this.sounds=sounds;
    }

    public ArrayList<TextView> getSoundsInDistance() {
        return soundsInDistance;
    }

    public User getUser() {
        return user;
    }

    public int getPlaying_with_controls() {
        return playing_with_controls;
    }

    private void translateNOT() {
        Log.d(TAG, "translateNOT: Starting");
        for (int i = 0; i < sounds.length; i++) {
            if (sounds[i].getNOT_string() == null) {
                sounds[i].setNOT(null);
            } else {
                Integer[] NOT = new Integer[sounds[i].getNOT_string().length];
                Log.d(TAG, "translateNOT: Found a Nut for:" +sounds[i].getName()+" Length: " +sounds[i].getNOT_string().length);
                for (int k = 0; k < sounds[i].getNOT_string().length; k++) {
                    for (int j = 0; j < sounds.length; j++) {
                        if (sounds[j].getFile_path().equals(sounds[i].getNOT_string()[k])) {
                            //index of the same name found
                            Log.d(TAG, "translateNOT: found a nut-case: "+j+" In place "+k);
                            NOT[k] = j;
                        }
                    }
                }
                sounds[i].setNOT(NOT);
            }
        }
    }

    private void translateAND_OR() {
        for (int i = 0; i < sounds.length; i++) {
            //if null
            Log.d(TAG, "translateAND_OR: start,  " + sounds[i].getName());
            if (sounds[i].getAND_OR_string() == null) {
                Log.d(TAG, sounds[i].getName() + " : AND_OR array is NULL");
                sounds[i].setAND_OR(null);
            }
            // if full
            else {
                Log.d(TAG, sounds[i].getName() + " : AND_OR array is NOT null");
                Integer[][] AND_OR = new Integer[sounds[i].getAND_OR_string().length][];
                Log.d(TAG, "translateAND_OR: integer index array created: "+AND_OR.length);
                for (int k = 0; k < sounds[i].getAND_OR_string().length; k++) {
                    Log.d(TAG, "translateAND_OR: NOT null : AND/OR set no."+k);
                    Integer[] items=new Integer[sounds[i].getAND_OR_string()[k].length];
                    for (int j = 0; j < sounds[i].getAND_OR_string()[k].length; j++) {
                        Log.d("Translating AND_OR", sounds[i].getFile_path() + " : adding: " + sounds[i].getAND_OR_string()[k][j]);
                        for (int l = 0; l < sounds.length; l++) {

                            if (sounds[l].getFile_path().equals(sounds[i].getAND_OR_string()[k][j])) {
                                Log.d(TAG, "translateAND_OR: found an and/or case in row "+k+" Sound Id= No.: "+l);
                                items[j]=l;
                            }
                        }
                    }
                    AND_OR[k] = items;
                }
                sounds[i].setAND_OR(AND_OR);
            }

        }

    }

    // Checking the NOT arrays against sounds already played
    public void check_NOT() {
        for (Sound s : sounds) {

            s.setCheck_NOT(true);
            if (s.getNOT()!=null) {
                for (int i = 0; i < s.getNOT().length; i++) {
                    //Log.d(TAG, "check_NOT: checking sound No: "+s.getNOT()[i]);
                    if (sounds[s.getNOT()[i]].get_played()) {
                        s.setCheck_NOT(false);
                        //Log.d(TAG, "check_NOT: forbidden element played! checkNot set to False!------>" +s.getName());
                        break;
                    }
                }
            }
            //Log.d(TAG, "check_NOT: checking: "+s.getName()+" REsult: "+s.getCheck_NOT());
        }
    }

    public void check_AND_OR() {
        //Log.d(TAG, "check_AND_OR: Starting");

        for (Sound s : sounds) {
            boolean check = false;
            //Log.d(TAG, "check_AND_OR: "+s.getName());
            if (s.getAND_OR()!=null) {
                //Log.d(TAG, "check_AND_OR: and or exists. length: " +s.getAND_OR().length);
                boolean[] rows = new boolean[s.getAND_OR().length];
                for (int i = 0; i < s.getAND_OR().length; i++) {
                    boolean[] items = new boolean[s.getAND_OR()[i].length];

                    for (int k = 0; k < s.getAND_OR()[i].length; k++) {
                        items[k] = sounds[s.getAND_OR()[i][k]].get_played();
                        //Log.d(TAG, "check_AND_OR: item "+s.getAND_OR()[i][k]+" Played: "+sounds[s.getAND_OR()[i][k]].get_played());

                    }
                    rows[i] = true;
                    for (boolean item : items) {
                        //Log.d(TAG, "check_AND_OR: item: "+item+ "ROW= "+rows[i]);
                        rows[i] = item && rows[i];
                        //Log.d(TAG, "check_AND_OR: COMBINED: "+rows[i]);
                    }
                }
                for (boolean r : rows) {
                    // at least one of preconditions must be true
                    check = r || check;
                    //Log.d(TAG, "check_AND_OR: iterate rows: " + s.getName() + ", " + check);
                }
            }
            else {
                check=true;
            }
            s.setCheck_AND_OR(check);

        }
    }

    public void set_focus() {
        // reverting all focus settings to false
        for (Sound s:sounds) {
            s.setFocused(false);
        }

        if (soundsInDistance!=null) {
            // if only one sound in distance!
            if (soundsInDistance.size() == 1) {
                //Log.d("Setting focus","Focus on only sound in radius");
                //set the focused switch in sounds array
                for (int i=0; i<sounds.length;i++) {
                    if (sounds[i].getView_ID()==0) {
                        sounds[i].setFocused(true);
                        //Log.d(TAG, "set_focus: Focus set for: "+sounds[i].getName());
                    }
                }
            }


            // if several sounds in distance
            else {
                // check if Playing, set focus!
                if (playing_with_controls!=-1) {
                    //Log.d("Setting focus","Searching for sound being played");
                    //Log.d("Setting focus","playing = " +playing_with_controls);
                    sounds[playing_with_controls].setFocused(true);
                    //Log.d("Playing sound"," "+sounds[playing_with_controls].getName());
                }

                else {
                    // have to determine which sound is closest! all sounds!! BUT NOT THE ONES WITHOUT CONTROLS!

                    int index=(int)get_closest_distance()[1];

                    //Log.d("Setting focus","Focus on closest: " +sounds[index].getName());

                    sounds[index].setFocused(true);
                }
            }
        }
    }

    private double[] get_closest_distance() {

        // array= {distance,ID}
        double[] set = new double[2];
        set[0] = 10000;

            for (int i = 0; i < sounds.length; i++) {
                // only set focus on visible sounds
                if (sounds[i].getUser_distance() < set[0] && sounds[i].getVisibility() && sounds[i].getControls()) {
                    set[0] = sounds[i].getUser_distance();
                    set[1] = i;



            }
        }
        return set;
    }

    private void setPlaying_with_controls(){
        playing_with_controls=-1;
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].getMedia_player()!=null) {

                if (sounds[i].isPlaying() && sounds[i].getControls()) {

                    playing_with_controls=i;

                    //Log.d("We are playing:"," No: " +i+" --> "+sounds[i].getName()+" ...with total control!") ;
                    break;
                }
            }
        }
    }

    public void setSoundsInDistanceView(){

        for (Sound s:sounds){
            //if sound in radius and visible-> but not yet added to view ArrayList
            if (s.isIn_distance() && s.getControls() && !s.isView_in_distance() && s.isVisible()){
                s.setView_in_distance(true);
                TextView view=s.getView();
                soundsInDistance.add(view);
                //Log.d(TAG, " View " + s.getName() + " added");
            }

            // if not in radius but in the textView array!
            else if(!s.isIn_distance() && s.isView_in_distance()){
                s.setView_in_distance(false);
                soundsInDistance.remove(s.getView());
                //Log.d(TAG, " View " + s.getName() + " removed");
            }
        }
    }

    public void set_Text_View_IDs(){

        for (Sound s:sounds){
            s.setView_ID(-1);
        }

        if (soundsInDistance!=null) {
            for (int i=0;i<soundsInDistance.size();i++) {
                for (Sound s:sounds) {
                    if (s.getName().equals(soundsInDistance.get(i).getText())) {
                        s.setView_ID(i);
                        //Log.d("Setting the View_ID","Sound FOUND " +s.getName()+" index: "+i);
                    }

                }

            }

        }

    }

    public void change_text_sizes(){
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].isFocused() && sounds[i].isView_in_distance()){
                soundsInDistance.get(sounds[i].getView_ID()).setTextSize(30);
            }
            else if (!sounds[i].isFocused() && sounds[i].isView_in_distance()){
                soundsInDistance.get(sounds[i].getView_ID()).setTextSize(12);
            }
            else {

            }
        }
    }

    public void play_sounds(Vibrator vibro) {
        setPlaying_with_controls();
        for (Sound s:sounds) {
            adjustVolume();
            s.check_play(user.getSpeed(), user.getLocation(), vibro);
            s.loadMedia_player(this.context); // check if this works
            s.play();

        }
    }

    public void set_views(){
        for (Sound s:sounds){
            // checking Circles for visibility and color and position!!!
            s.check_visibility();
            s.change_circle_color();
            s.apply_visibility();
        }
    }
    public void calculate_distances(){
        for (Sound s:sounds){
            s.calculate_user_distance(user.getLocation());
        }
    }

    /*public void load_players(){
        for (Sound s: sounds){
            s.loadMedia_player(this.context);
        }
    }
    */

    public void unloadPlayers(){
        for (Sound s:sounds){
            if (s.getMedia_player()!=null) {
                s.getMedia_player().stop();
                s.getMedia_player().release();
                s.setMedia_player(null);
            }
        }
    }

    public void adjustVolume(){
        for (Sound s:sounds){
            s.set_volume(user.getBearing());
        }
    }

    public void updatePlayerPosition(){
        for (Sound s:sounds){
            s.updatePlayPosition();
        }
    }
}
