package com.reinis.gaztest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Reinis on 01.01.18.
 */

public class DatabaseTranslator {

    private ArrayList<Sound> sounds;
    private Sound[] soundsArray;
    private Cursor dbCursor;
    private SQLiteDatabase database;
    String TAG="Database Translator";
    // Constructor
    DatabaseTranslator(DatabaseHelper dbHelper)
    {

        try {
            Log.d(TAG, "DatabaseTranslator: Creating database ");
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            Log.d(TAG, "DatabaseTranslator: Creating database...........EXCEPTION ");
        }
        sounds = new ArrayList<Sound>();
        try {
            database = dbHelper.openDataBase();

            //Log.d("Getting Sounds", " Database read!" + database.getPath());


            dbCursor = database.rawQuery("SELECT * FROM sounds;",
                    null);

            //Log.d("Cursor Indexing", "Preparing");
            dbCursor.moveToFirst();

            //Log.d("Cursor Indexing", "Indexing");
            /* search through all necessary info, determine Column indexes  */
            int id_index = dbCursor.getColumnIndex("ID");
            int location_index=dbCursor.getColumnIndex("Location");
            int movement_index= dbCursor.getColumnIndex("Movement");
            int radius_index = dbCursor.getColumnIndex("Radius");
            int visibility_index= dbCursor.getColumnIndex("Visibility");
            int color_index= dbCursor.getColumnIndex("Color");
            int control_index=dbCursor.getColumnIndex("Controls");
            int speed_index=dbCursor.getColumnIndex("Speed");
            int approach_dir_index=dbCursor.getColumnIndex("Approach_dir");
            int time_index=dbCursor.getColumnIndex("Time");
            int AND_OR_index=dbCursor.getColumnIndex("AND_OR");
            int NOT_index=dbCursor.getColumnIndex("NOT");
            int repeat_index = dbCursor.getColumnIndex("Repeat");
            int delay_index = dbCursor.getColumnIndex("Delay");
            //Log.d(TAG, "DatabaseTranslator: Colums index test. Delay: "+dbCursor.getColumnIndex("Delay"));
            int name_index = dbCursor.getColumnIndex("Name");
            int description_index = dbCursor.getColumnIndex("Description");
            int author_index = dbCursor.getColumnIndex("Author");
            int file_path_index = dbCursor.getColumnIndex("File_path");
            int volmod_index = dbCursor.getColumnIndex("VolMod");
            int biaural_index= dbCursor.getColumnIndex("Biaural");

            //Log.d("Cursor Indexing", "Indexing done");

             /* Add the Entry (Row, one Sound information) to the Database Array

             */
            while (!dbCursor.isAfterLast()) {
                //Log.d("Reading Cursor", "Row " + dbCursor.getString(name_index)+" <-------------------------------");
                int id = dbCursor.getInt(id_index);

                Log.d("Reading Cursor", "Location "+ dbCursor.getString(location_index));

                String[] loc=dbCursor.getString(location_index).split(",");
                double x_value = Double.parseDouble(loc[0]);
                //Log.d("Reading Cursor", "y_value");
                double y_value = Double.parseDouble(loc[1]);

                Double[][] movement;
                if (dbCursor.getString(movement_index)!= null) {
                    //Log.d("Reading Cursor", "Movement");
                    String[] movement_first_order = dbCursor.getString(movement_index).split(";");
                    String[][] movement_second_order = new String[movement_first_order.length][3];
                    for (int i = 0; i < movement_first_order.length; i++) {
                        String[] movement_row = movement_first_order[i].split(",");
                        movement_second_order[i] = movement_row;
                    }

                    movement = new Double[movement_first_order.length][3];
                    for (int i = 0; i < movement_first_order.length; i++) {
                        for (int k = 0; k < movement_second_order[i].length; k++) {
                            movement[i][k] = Double.parseDouble(movement_second_order[i][k]);
                        }
                    }
                }
                else{
                    movement=null;
                }

                //Log.d("Reading Cursor", "radius");
                double radius = dbCursor.getDouble(radius_index);
                //Log.d("Reading Cursor", "visibility");
                int vis_int=dbCursor.getInt(visibility_index);
                boolean visibility;
                if (vis_int!=1){
                    visibility=false;
                }
                else {
                    visibility=true;
                }
                //Log.d("Reading Cursor", "color");
                int color= Color.parseColor(dbCursor.getString(color_index));
                //Log.d("Reading Cursor", "controls");
                int cont_int=dbCursor.getInt(control_index);
                boolean controls;
                if (cont_int!=1){
                    controls=false;
                }
                else {
                    controls=true;
                }



                //Log.d("Reading Cursor", "Speed");

                String[] speed = dbCursor.getString(speed_index).split(",");
                double min_speed= Double.parseDouble(speed[0]);
                //Log.d("Reading Cursor", "max_speed");
                double max_speed= Double.parseDouble(speed[1]);

                //Log.d("Reading Cursor", "approach direction");
                String[] approach_first_order=dbCursor.getString(approach_dir_index).split(";");
                String[][] approach_second_order=new String[approach_first_order.length][2];
                for (int i = 0; i < approach_first_order.length; i++) {
                    String[] directions_array=approach_first_order[i].split(",");
                    approach_second_order[i]=directions_array;
                }

                double[][] approach_dir=new double[approach_second_order.length][2];
                for (int i=0;i<approach_second_order.length;i++){
                    for (int k=0; k<approach_second_order[i].length;k++){
                        approach_dir[i][k]= Double.parseDouble(approach_second_order[i][k]);
                    }

                }

                //Log.d("Reading Cursor", "AND/OR");
                String[][] AND_OR;
                if (dbCursor.getString(AND_OR_index)!= null) {
                    String[] array_or = dbCursor.getString(AND_OR_index).split(";");
                    AND_OR = new String[array_or.length][];
                    for (int i = 0; i < array_or.length; i++) {
                        String[] array_and = array_or[i].split(",");
                        AND_OR[i] = array_and;
                    }
                }
                else {
                    AND_OR =null;
                }

                //Log.d("Reading Cursor", "NOT");
                String[] NOT;
                if (dbCursor.getString(NOT_index)!=null) {
                    NOT = dbCursor.getString(NOT_index).split(",");
                }
                else {
                    NOT=null;
                }

                //Log.d("Reading Cursor", "repeat");
                int repeat = Integer.parseInt(dbCursor.getString(repeat_index));

                //Log.d("Reading Cursor", "delay");
                String[] delay_str=dbCursor.getString(delay_index).split(",");

                double[] delay=new double[delay_str.length];
                for (int i=0; i < delay_str.length ;i++){
                    delay[i]= Double.parseDouble(delay_str[i]);
                }

                //Log.d("Reading Cursor", "Time");
                String[] array_timesets= dbCursor.getString(time_index).split(";");
                //Log.d("Reading Cursor", "Times, creating arrays");
                double [][] times = new double[array_timesets.length][2];
                for (int i = 0; i < array_timesets.length; i++) {
                    String[] array_times_string = array_timesets[i].split(",");
                    double[] array_times_double = new double[array_times_string.length];
                    for (int k=0; k<array_times_string.length; k++){
                        array_times_double[k]= Double.parseDouble(array_times_string[k]);
                    }
                    times[i]=array_times_double;
                }

                //Log.d("Reading Cursor", "name");
                String name = dbCursor.getString(name_index);
                //Log.d("Reading Cursor", "description");
                String description = dbCursor.getString(description_index);
                //Log.d("Reading Cursor", "author");
                String author = dbCursor.getString(author_index);
                //Log.d("Reading Cursor", "File_path");
                String file_path = dbCursor.getString(file_path_index);
                //Log.d("Reading Cursor", "Volume modifier");
                double volMod =dbCursor.getDouble(volmod_index);
                //Log.d("Reading Cursor", "Biaural");
                int biaural_int=dbCursor.getInt(biaural_index);
                boolean biaural;
                       if  (biaural_int==1){
                           biaural=true;
                       }
                       else {
                           biaural=false;
                       }



                /*
                Create the Sound instance!
                 */

                Sound sound = new Sound(dbHelper.getMyContext(),id, x_value, y_value, movement, radius, name, author, description, repeat, delay, file_path, color, visibility, controls, min_speed, max_speed, approach_dir, times, AND_OR, NOT, volMod, biaural);

                /*
                Add to the List af all sounds in the Database!!
                 */
                sounds.add(sound);
                dbCursor.moveToNext();

            }
        }

        finally {
            if (database != null) {
                dbHelper.close();
                dbCursor.close();
            }
        }



    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }


    public Sound[] getSoundsArray(){
        soundsArray=new Sound[sounds.size()];
        //Log.d(TAG, "getSoundsArray: length:"+soundsArray.length);
        for (int i=0;i<this.sounds.size();i++){
            soundsArray[i]=sounds.get(i);
        }
        return soundsArray;
    }
}


