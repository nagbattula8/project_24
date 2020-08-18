package com.user.alltronics.locate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.user.alltronics.locate.history.Config_markers;
import com.user.alltronics.locate.history.Markers;
import com.user.alltronics.locate.history.SQLiteDatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    private String JSON_STRING;


    public SQLiteDatabaseHandler db;
    public Markers markers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteDatabaseHandler(getApplicationContext());
        //getJSON();
        // Start home activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));


        // close splash activity
        //finish();
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SplashActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showMap();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config_markers.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }



    private void showMap(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config_markers.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String lat = jo.getString(Config_markers.TAG_LATITUDE);
                String lng = jo.getString(Config_markers.TAG_LONGITUDE);

                markers = new Markers(i,lat,lng);
                db.addMarkers(markers);

                double latitude = Double.parseDouble(lat);
                double longitude = Double.parseDouble(lng);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
