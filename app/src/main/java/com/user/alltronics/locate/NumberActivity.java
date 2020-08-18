package com.user.alltronics.locate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.user.alltronics.locate.history.Markers;
import com.user.alltronics.locate.history.SQLiteDatabaseHandler;

import java.util.List;

public class NumberActivity extends AppCompatActivity {

    TextView number_id;
    public SQLiteDatabaseHandler markDb;
    public List<Markers> markersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        //number_id = (TextView) findViewById(R.id.number_id);
        //markDb = new SQLiteDatabaseHandler(getApplicationContext());

        //markersList = markDb.getAllMarkers();

        //int id = markersList.size();
        //String numb_id = Integer.toString(id);

        //number_id.setText(numb_id);
    }
}
