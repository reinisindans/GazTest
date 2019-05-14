package com.reinis.gaztest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class loadDatabaseTranslator {

        private Cursor dbCursor;
        private SQLiteDatabase database;
        String TAG = "load saved database";

        private ArrayList<Integer> ID_list =new ArrayList<>();
        private ArrayList<Integer> timesPlayed_list= new ArrayList<>();
        private ArrayList<Integer> delayEndTime_list= new ArrayList<>();
        private ArrayList<Boolean> manualStart_list= new ArrayList<>();
        private ArrayList<Boolean> playbackStarted_list=new ArrayList<>();
        private ArrayList<Integer> playerPosition_list= new ArrayList<>();
        private ArrayList<Integer> lastLocation_list= new ArrayList<>();
        private ArrayList<Double> xValue_list= new ArrayList<>();
        private ArrayList<Double> yValue_list= new ArrayList<>();



        //constructor
    loadDatabaseTranslator(saveDatabaseHelper dbWriter) {
    try {
        database = dbWriter.openDataBase();

        Log.d("Getting Saves!", " Database read!" + database.getPath());


        dbCursor = database.rawQuery("SELECT * FROM saved_state;",
                null);
        Log.d(TAG, "loadDatabaseTranslator: Selected");

        //Log.d("Cursor Indexing", "Preparing");
        dbCursor.moveToFirst();

        ///! here continue coding!!!!!!!!
        int id_index = dbCursor.getColumnIndex("ID");
        int timesPlayed_index = dbCursor.getColumnIndex("times_played");
        int delay_index = dbCursor.getColumnIndex("delay_end_time");
        int manualStart_index = dbCursor.getColumnIndex("manual_start");
        int playbackStarted_index = dbCursor.getColumnIndex("started_playback");
        int position_index = dbCursor.getColumnIndex("player_position");
        int lastLocation_index= dbCursor.getColumnIndex("lastLocation");
        int xValue_index= dbCursor.getColumnIndex("x_value");
        int yValue_index= dbCursor.getColumnIndex("y_value");


        //Log.d(TAG, "Indexing done");

        while (!dbCursor.isAfterLast()) {
            Log.d("Reading Cursor", "Row " + dbCursor.getString(id_index)+" <-------------------------------");
            int id = dbCursor.getInt(id_index);
            //Log.d(TAG, "loadDatabaseTranslator: ID got =" +id);
            int timesPlayed=dbCursor.getInt(timesPlayed_index);
            int delayEndTime=dbCursor.getInt(delay_index);
            //Log.d(TAG, "loadDatabaseTranslator: delay end time got " + delayEndTime);
            boolean manualStart;

            if (dbCursor.getInt(manualStart_index)==1){
                manualStart=true;
            }
            else {
                manualStart=false;
            }
            //Log.d(TAG, "loadDatabaseTranslator: manual start got: "+manualStart);

            boolean playbackStarted;
            if (dbCursor.getInt(playbackStarted_index)==1){
                playbackStarted=true;
            }
            else {
                playbackStarted=false;
            }
            //Log.d(TAG, "loadDatabaseTranslator: playbackStarted got: "+playbackStarted);

            int playerPosition= dbCursor.getInt(position_index);

            //Log.d(TAG, "loadDatabaseTranslator: mediaPlayer position got: "+playerPosition);

            int lastLocation=dbCursor.getInt(lastLocation_index);

            double xValue= dbCursor.getDouble(xValue_index);
            double yValue= dbCursor.getDouble(yValue_index);
            //add values to value arrays

            ID_list.add(id);
            timesPlayed_list.add(timesPlayed);
            delayEndTime_list.add(delayEndTime);
            manualStart_list.add(manualStart);
            playbackStarted_list.add(playbackStarted);
            playerPosition_list.add(playerPosition);
            lastLocation_list.add(lastLocation);
            xValue_list.add(xValue);
            yValue_list.add(yValue);
            //Log.d(TAG, "loadDatabaseTranslator: values added to list: "+ id);

            dbCursor.moveToNext();

        }
    }

    finally {
        if (database != null) {
            dbWriter.close();
            dbCursor.close();
            }
        }
    }

    public ArrayList<Integer> getID_list() {
        return ID_list;
    }

    public ArrayList<Integer> getTimesPlayed_list() {
        return timesPlayed_list;
    }

    public ArrayList<Integer> getDelayEndTime_list() {
        return delayEndTime_list;
    }

    public ArrayList<Boolean> getManualStart_list() {
        return manualStart_list;
    }

    public ArrayList<Boolean> getPlaybackStarted_list() {
        return playbackStarted_list;
    }

    public ArrayList<Integer> getPlayerPosition_list() {
        return playerPosition_list;
    }

    public ArrayList<Integer> getLastLocation_list() {
        return lastLocation_list;
    }

    public ArrayList<Double> getxValue_list() {
        return xValue_list;
    }

    public ArrayList<Double> getyValue_list() {
        return yValue_list;
    }
}
