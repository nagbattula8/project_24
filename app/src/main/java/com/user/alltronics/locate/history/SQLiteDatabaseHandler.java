package com.user.alltronics.locate.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "markers";

    // Country table name
    private static final String TABLE_PROJECT_24= "project_24";

    // Country Table Columns names
    private static final String KEY_ID = "id";
    private static final String START = "starttime";
    private static final String LATITUDE = "latitude";
    private static final String END = "endtime";
    private static final String LONGITUDE = "longitude";
    private static final String MAKE = "make";
    private static final String MODEL = "Model";


    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROJECT_24_TABLE = "CREATE TABLE " + TABLE_PROJECT_24 + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LATITUDE + " REAL,"
                + LONGITUDE + " REAL," +

                " UNIQUE ( " + LATITUDE + ", " + LONGITUDE + " ) " +

                ")";
        db.execSQL(CREATE_PROJECT_24_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_24);
        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new country

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        db.execSQL("delete from "+ TABLE_PROJECT_24);
        //db.execSQL("TRUNCATE table " + TABLE_BOOKINGS);
        db.close();
    }




    public void addMarkers(Markers markers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,markers.getId());
        values.put(LATITUDE, markers.getLatitude()); // Country Name
        values.put(LONGITUDE, markers.getLongitude()); // Country Population
        // Inserting Row
        db.insert(TABLE_PROJECT_24, null, values);
        db.close(); // Closing database connection
    }

    // Getting single country
   /* Country getCountry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTRY, new String[] { KEY_ID,
                        LAT, LNG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Country country = new Country(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getLong(2));
        // return country
        return country;
    }*/

    // Getting All Countries
    public List<Markers> getAllMarkers() {
        List<Markers> markersList = new ArrayList<Markers>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT_24 + " ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Markers markers = new Markers();
                markers.setId(Integer.parseInt(cursor.getString(0)));
                markers.setLatitude(cursor.getString(1));
                markers.setLongitude(cursor.getString(2));
                // Adding country to list
                markersList.add(markers);
            } while (cursor.moveToNext());
        }

        // return country list
        return markersList;
    }

    public List<Markers> boundMarkers(LatLngBounds bounds) {
        List<Markers> markersList = new ArrayList<Markers>();

        LatLng north = bounds.northeast;
        LatLng south = bounds.southwest;

        double latitude_bound_1 = north.latitude;
        double longitude_bound_1 = north.longitude;
        double latitude_bound_2 = south.latitude;
        double longitude_bound_2 = south.longitude;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT_24 + " WHERE " + LATITUDE + " <= "
                +latitude_bound_1 + " AND " + LATITUDE + " >= " + latitude_bound_2 +" AND " + LONGITUDE +" <= " +
                longitude_bound_1 + " AND " + LONGITUDE + " >= " + longitude_bound_2
                ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Markers markers = new Markers();
                markers.setId(Integer.parseInt(cursor.getString(0)));
                markers.setLatitude(cursor.getString(1));
                markers.setLongitude(cursor.getString(2));
                // Adding country to list
                markersList.add(markers);
            } while (cursor.moveToNext());
        }

        // return country list
        return markersList;
    }






    // Updating single country
    /*public int updateCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LAT, country.getCountryName());
        values.put(LNG, country.getPopulation());

        // updating row
        return db.update(TABLE_COUNTRY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
    }

    // Deleting single country
    public void deleteCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
        db.close();
    }

    // Deleting all countries
    public void deleteAllCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY,null,null);
        db.close();
    }

    // Getting countries Count
    public int getCountriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COUNTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/
}