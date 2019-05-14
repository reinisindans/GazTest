package com.reinis.gaztest;

import android.location.Location;
import android.util.Log;

/**
 * Created by root on 28.03.18.
 */

public class User {

    private Location location;
    private double speed;
    private double bearing;
    private double[] directions_to_sounds;


    public User(int sound_count) {
        this.location = null;
        this.speed=0;
        this.bearing=0;
        this.directions_to_sounds=new double[sound_count];
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public double[] getDirections_to_sounds(Sound[] sounds) {
        return directions_to_sounds;
    }

    public void calculate_user_direction(Sound[] sounds){
        Log.d("User direction", "calculate_user_direction: ");

        for (int i=0;i<sounds.length;i++){
            double user_dir=location.bearingTo(sounds[i].getLocation());

            Log.d("User direction", ": "+sounds[i].getName()+"::: "+user_dir);
            directions_to_sounds[i]=user_dir;
        }

    }
}
