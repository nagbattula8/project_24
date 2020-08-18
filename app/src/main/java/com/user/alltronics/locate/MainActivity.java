package com.user.alltronics.locate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.user.alltronics.locate.history.Markers;
import com.user.alltronics.locate.history.SQLiteDatabaseHandler;
import com.user.alltronics.locate.login.LoginActivity;
import com.user.alltronics.locate.login.SQLiteHandler;
import com.user.alltronics.locate.login.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//import com.user.alltronics.nrusimhabikesuser.bookings.DropdownActivity;

public class MainActivity extends AppCompatActivity implements

        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener,
        LocationListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    // protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    public SQLiteDatabaseHandler markDb;
    float zooming;
    Marker lastOpenned = null;
    int id=0;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private AutoCompleteTextView edtSeach;

    private static final String LOG_TAG = "MainActivity";

    String user_name;
    String email_id;


    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView pickup_point;
    private AutoCompleteTextView drop_point;
    //private GoogleApiClient googleApiClient;

    private PlaceArrayAdapter mPlaceArrayAdapter;
    private PlaceArrayAdapter pickupAdapter;

    private ArrayList<Marker> markersToClear = new ArrayList<Marker>();

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    static final int REQUEST_LOCATION = 1;
    private GoogleApiClient googleApiClient;


    private static final LatLng ORR_AIRPORT = new LatLng(17.253181, 78.378268);
    private static final LatLng GANDI_MAISAMMA = new LatLng(17.576958, 78.422264);
    private static final LatLng AMBERPET_RAMANTHAPUR = new LatLng(17.395604, 78.527362);
    private static final LatLng SHAMIRPET = new LatLng(17.591168, 78.570857);
    private static final LatLng BANJARA_HILLS = new LatLng(17.4159557, 78.4182496);
    private static final LatLng ABIDS = new LatLng(17.390244, 78.476292);
    private static final LatLng BARADARI = new LatLng(17.381935, 78.470542);
    private static final LatLng SAPTHAGIRI_THEATRE = new LatLng(17.407676, 78.497019);
    private static final LatLng MELBOURNE = new LatLng(17.518958, 78.284842);
    //private static LatLng SYDNEY;
    private static final LatLng ADELAIDE = new LatLng(17.539842, 78.276048);
    private static final LatLng PERTH = new LatLng(17.514498, 78.292737);
    //private static LatLng HYDERABAD;
    private static final LatLng SYDNEY = new LatLng(12.292503, 77.965381);
    private static final LatLng HYDERABAD = new LatLng(12.324704, 77.767627);
    private static LatLng CURRENT;
    private LatLngBounds bounds;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();


    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000f;

    public SQLiteHandler db;
    public Markers markers;
    public SessionManager session;

    String parameter;


    Button buttonB;

    ArrayList<LatLng> locations = new ArrayList<>();


    private GoogleMap mMap = null;

    private boolean mPermissionDenied = false;

    private String JSON_STRING;
    private View navHeader;

    public List<Markers> markersList;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private Marker mSelectedMarker;

    private Marker touchMarker = null;

    private boolean first = true;
    private boolean lt = false;

    DrawerLayout drawer;
    private Button button1;
    View.OnClickListener listener1 = null;
    private TextView txtName, txtWebsite, txtDisclaimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SQLiteHandler(getApplicationContext());
        markDb = new SQLiteDatabaseHandler(getApplicationContext());

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, GOOGLE_API_CLIENT_ID, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).
                addApi(Places.GEO_DATA_API).addConnectionCallbacks(this)
                .build();


        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);

        pickupAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);


        markersList = markDb.getAllMarkers();

        // session manager
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        user_name = user.get("name");
        email_id = user.get("email");




        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ViewAllNotifications.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //db = new SQLiteHandler(getApplicationContext());

        //buttonB = (Button) findViewById(R.id.buttonB);

        //buttonB.setOnClickListener(this);
        // session manager

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        toolbar.setNavigationIcon(null);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        rightNavigationView.setItemIconTintList(null);
        Menu nav_menu = rightNavigationView.getMenu();

        navHeader = rightNavigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        String nam = "Project 24";
        //String disclaimer = "Disclaimer: The locations in the app are not exact";

        //nav_menu.findItem(R.id.nav_faq).setVisible(false);
        nav_menu.findItem(R.id.nav_customer_support).setVisible(false);

        //txtDisclaimer.setText(disclaimer);

        if(!session.isLoggedIn()) {
            txtName.setText(nam);
            txtName.setTypeface(null, Typeface.BOLD);
            txtWebsite.setText(nam);
            nav_menu.findItem(R.id.nav_logout_login).setVisible(false);
            nav_menu.findItem(R.id.add_marker).setVisible(false);
        }
        else {
            txtName.setText(name);
            String no_records = Integer.toString(id);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            txtWebsite.setVisibility(View.VISIBLE);
            txtWebsite.setText(email);
            nav_menu.findItem(R.id.nav_insurance).setVisible(false);
            nav_menu.findItem(R.id.add_marker).setVisible(true);
        }

        //menu.findItem(R.id.nav_logout).setVisible(false);
        //nav_menu.findItem(R.id.nav_logout).setVisible(false);

        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                //rightNavigationView.setItemIconTintList(null);

                // Handle Right navigation view item clicks here.
               int id = item.getItemId();

               if (id == R.id.nav_insurance) {
                    Intent i= new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);

                }

                if (id == R.id.add_marker) {
                    Intent i= new Intent(getApplicationContext(),AddActivity.class);
                    double lat = CURRENT.latitude;
                    double lon = CURRENT.longitude;
                    String latlon = Double.toString(lat) + "," + Double.toString(lon);
                    i.putExtra("lat_lon", latlon);
                    startActivity(i);

                }

                else if (id == R.id.nav_faq) {
                    Intent i= new Intent(getApplicationContext(),FAQ.class);
                    startActivity(i);


                } else if (id == R.id.nav_customer_support) {
                    /*Intent i= new Intent(getApplicationContext(),MainActivityCustomer.class);
                    startActivity(i);*/


                }else if (id == R.id.nav_contact_us) {
                    Intent i= new Intent(getApplicationContext(),NumberActivity.class);
                    startActivity(i);


                }else if (id == R.id.nav_about_us) {

                    Intent i= new Intent(getApplicationContext(),AboutUs.class);
                    startActivity(i);

                }

                else if (id == R.id.why ) {

                    Intent i= new Intent(getApplicationContext(),DisclaimerActivity.class);
                    startActivity(i);
                }


                else if (id == R.id.nav_logout_login) {

                   /* session.setLogin(false);

                    db.deleteUsers();*/

                    // Launching the login activity
                    //Intent intent = new Intent(MainActivity.this, Login.class);
                    //startActivity(intent);
                    //finish();


                    session.setLogin(false);

                    db.deleteUsers();

                    // Launching the login activity
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    LoginManager.getInstance().logOut();

                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                        }
                    });

                    startActivity(intent);

                }

                else if (id == R.id.powered_by) {

                    Uri uri = Uri.parse("http://alltronics.in/"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });

        //getJSON();
    }


    void getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(bestProvider,0,100,this);

            if (location != null){
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                CURRENT = new LatLng(latti,longi);
                lt = true;
                onLocationChanged(location);

            } else {

                Toast.makeText(this, "Location null",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MainActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // loading.dismiss();
                JSON_STRING = s;
                showMap();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config_booking.URL_GET_MARKERS + "'" + parameter + "'");
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
            JSONArray result = jsonObject.getJSONArray(Config_booking.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config_booking.TAG_ID);
                String lat = jo.getString(Config_booking.TAG_LAT);
                String lng = jo.getString(Config_booking.TAG_LNG);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);

                //String name = jo.getString(Config_booking.TAG_NAME_MARKER);

                double latitude = Double.parseDouble(lat);
                double longitude = Double.parseDouble(lng);
                LatLng dest = new LatLng(latitude,longitude);

                int marker_id = Integer.parseInt(id);


                markers = new Markers(marker_id,lat,lng);
                markDb.addMarkers(markers);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        markersList = markDb.boundMarkers(bounds);
        local_display();

    }




    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);


        enableMyLocation();
        getLocation();


        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        map.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

        /*LatLngBounds bounds = new LatLngBounds.Builder()
                .include(HYDERABAD)
                .include(SYDNEY)
                .include(ADELAIDE)
                .include(DULAPALLY)
                .include(MELBOURNE)
                .build();*/

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);

        mMap.clear();


        locationUpdate();





        /*mMap.addMarker(new MarkerOptions()
                .position(ORR_AIRPORT)
                .title("Outer Ring road near airport")
                .icon(icon)

        );

        mMap.addMarker(new MarkerOptions()
                .position(GANDI_MAISAMMA)
                .title("Gandi Maisamma roads")
                .icon(icon)

        );

        mMap.addMarker(new MarkerOptions()
                .position(AMBERPET_RAMANTHAPUR)
                .title("Amberpet")
                .icon(icon)

        );

        mMap.addMarker(new MarkerOptions()
                .position(SHAMIRPET)
                .title("Shamirpet")
                .icon(icon)

        );

        mMap.addMarker(new MarkerOptions()
                .position(SHAMIRPET)
                .title("Shamirpet")
                .icon(icon)

        );

        mMap.addMarker(new MarkerOptions()
                .position(BANJARA_HILLS)
                .title("Banjara Hills")
                .icon(icon)
        );

        mMap.addMarker(new MarkerOptions()
                .position(ABIDS)
                .title("Abids circle")
                .icon(icon)
        );

        mMap.addMarker(new MarkerOptions()
                .position(BARADARI)
                .title("Baradari ground")
                .icon(icon)
        );

        mMap.addMarker(new MarkerOptions()
                .position(SAPTHAGIRI_THEATRE)
                .title("Baradari ground")
                .icon(icon)
        );*/

        /*for (Markers markers: markersList ) {

            String lat = markers.getLatitude();
            String lon = markers.getLongitude();

            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);

            LatLng dest = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions()
                    .position(dest)
                    .title(lat)
            );




        }*/



    }

    @Override
    public void onLocationChanged(Location location) {
      //  HYDERABAD = new LatLng(location.getLatitude(), location.getLongitude() );
        //SYDNEY = new LatLng(location.getLatitude(), location.getLongitude() );
        //locationUpdate();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CURRENT = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }

    private void locationUpdate() {



        if ( lt ) {
            builder.include(CURRENT);
           // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
           // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 15.0f));

            bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            float zooming = mMap.getCameraPosition().zoom;

            if( zooming >= 10.0f ) {

                LatLng noreas = bounds.northeast;
                LatLng southwe = bounds.southwest;
                double latitude_bound_1 = noreas.latitude;
                double longitude_bound_1 = noreas.longitude;
                double latitude_bound_2 = southwe.latitude;
                double longitude_bound_2 = southwe.longitude;
                parameter = Double.toString(latitude_bound_1) + "," + Double.toString(longitude_bound_1) + ","
                        + Double.toString(latitude_bound_2) + "," + Double.toString(longitude_bound_2);


                LatLng center = mMap.getCameraPosition().target;

                markersList = markDb.boundMarkers(bounds);

                local_display();

                if( isNetworkAvailable() ) {
                    getJSON();

                }

                markersList = markDb.boundMarkers(bounds);

                local_display();


                /*mMap.addMarker(new MarkerOptions()
                        .position(center)
                        .title("Center")
                );

                mMap.addMarker(new MarkerOptions()
                        .position(noreas)
                        .title("North east")
                );*/

                //Log.d("MapFragment: ", "Center From camera: Long: " + center.longitude
                  //      + " Lat" + center.latitude);
            }

        }

        else {
            //Log.i("dfg",Double.toString(CURRENT.latitude));
            getLocation();
            builder.include(HYDERABAD);
            builder.include(SYDNEY);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 10.0f));

            float zooming = mMap.getCameraPosition().zoom;

            if( zooming >= 10.f ) {


                LatLng center = mMap.getCameraPosition().target;

                bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

                LatLng noreas = bounds.northeast;
                LatLng southwe = bounds.southwest;
                double latitude_bound_1 = noreas.latitude;
                double longitude_bound_1 = noreas.longitude;
                double latitude_bound_2 = southwe.latitude;
                double longitude_bound_2 = southwe.longitude;
                parameter = Double.toString(latitude_bound_1) + "," + Double.toString(longitude_bound_1) + ","
                        + Double.toString(latitude_bound_2) + "," + Double.toString(longitude_bound_2);


                markersList = markDb.boundMarkers(bounds);

                local_display();

                if( isNetworkAvailable() ) {
                    getJSON();
                }

                markersList = markDb.boundMarkers(bounds);

                local_display();


                //Log.d("MapFragment: ", "Center From camera: Long: " + center.longitude
                  //      + " Lat" + center.latitude);

            }




        }

    }



    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            /*FollowMeLocationSource locationSource = new FollowMeLocationSource();
            locationSource.getBestAvailableProvider();
            mMap.setLocationSource(locationSource);*/
            mMap.setMyLocationEnabled(true);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }



        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }


    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }




    private void addMarkersToMap() {
       /* mMap.addMarker(new MarkerOptions()
                .position(DULAPALLY)
                .title("DULAPALLY")
                .snippet("Population: 2,074,200"));

        mMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
                .snippet("Population: 4,627,300"));

        mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400"));

        mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .title("Perth")
                .snippet("Population: 1,738,800"));

        mMap.addMarker(new MarkerOptions()
                .position(ADELAIDE)
                .title("Adelaide")
                .snippet("Population: 1,213,000"));*/


    }

    @Override
    public void onMapClick(final LatLng point) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;


       /* if (first == true) {
            //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            touchMarker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("TouchPoint"));

            first = false;
        }

        else {
            touchMarker.remove();

            touchMarker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("TouchPoint"));
        } */


    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        // The user has re-tapped on the marker which was already showing an info window.
        if (lastOpenned != null) {
            // Close the info window
            lastOpenned.hideInfoWindow();

            // Is the marker the same marker that was already open
            if (lastOpenned.equals(marker)) {
                // Nullify the lastOpenned object
                lastOpenned = null;
                // Return so that the info window isn't openned again
                return true;
            }
        }

        // Open the info window for the marker
        marker.showInfoWindow();
        // Re-assign the last openned such that we can close it later
        lastOpenned = marker;

        // Event was handled by our code do not launch default behaviour.
        return true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        zooming = mMap.getCameraPosition().zoom;

        local_display();
        return false;
    }





   /* private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent i= new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }*/
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  //Closes the Appropriate Drawer
            drawer.closeDrawer(GravityCompat.END);
        }
        else if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        else {
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openLeft) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        if( id == R.id.action_search ) {

            handleMenuSearch();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void doSearch() {
//
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_magnifier));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true);
            action.setDisplayShowTitleEnabled(false); //show the title in the action bar
//enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            //action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (AutoCompleteTextView) action.getCustomView().findViewById(R.id.edtSearch);
            edtSeach.setDropDownBackgroundResource(R.color.colorWhite);

            int width = edtSeach.getMaxWidth();

            //Log.i("Width", Integer.toString(width));

            edtSeach.setThreshold(3);
            edtSeach.setOnItemClickListener(mAutocompleteClickListener);
            edtSeach.setAdapter(pickupAdapter);
            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.RESULT_UNCHANGED_SHOWN);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_magnifier));

            isSearchOpened = true;
        }
    }

    @Override
    public void onClick(View v) {

        if( v == buttonB ) {
           // Intent intent = new Intent(this, DropdownActivity.class);
            //startActivity(intent);
        }

    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent i= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // Log.e(LOG_TAG, "Google Places API connection failed with error code: "
         //       + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraMove() {
        /*bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        zooming = mMap.getCameraPosition().zoom;*/



        /*LatLng noreas = bounds.northeast;

        float zoom = mMap.getCameraPosition().zoom;

        if(zoom < 10.0) {
            mMap.clear();
        }

        mMap.addMarker(new MarkerOptions()
                .position(noreas)
                .title("North East")
        );*/
    }

    @Override
    public void onCameraIdle() {

        //Log.i("Camera", "Movement stopped");
        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        zooming = mMap.getCameraPosition().zoom;

        if( zooming >= 10.0f ) {
            LatLng noreas = bounds.northeast;
            LatLng southwe = bounds.southwest;
            double latitude_bound_1 = noreas.latitude;
            double longitude_bound_1 = noreas.longitude;
            double latitude_bound_2 = southwe.latitude;
            double longitude_bound_2 = southwe.longitude;
            parameter = Double.toString(latitude_bound_1) + "," + Double.toString(longitude_bound_1) + ","
                    + Double.toString(latitude_bound_2) + "," + Double.toString(longitude_bound_2);

            if(isNetworkAvailable()) {

                    Toast.makeText(this, "Loading...",
                            Toast.LENGTH_LONG).show();
                local_display();
                getJSON();

            }
            else {
                local_display();
            }



        }

         //bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        //LatLng north = bounds.northeast;

       /* mMap.addMarker(new MarkerOptions()
                .position(north)
                .title("North East")
        );*/

    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void local_display() {
        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        markersList = markDb.boundMarkers(bounds);
        remove_markers();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ldpi);
        for (Markers markers: markersList ) {

            String lat = markers.getLatitude();
            String lon = markers.getLongitude();


            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);

            String address = getCompleteAddressString(latitude,longitude);

            LatLng dest = new LatLng(latitude, longitude);


            Marker marker1 =mMap.addMarker(new MarkerOptions()
                    .position(dest)
                    .icon(icon)
                    .title(address)
                  //  .icon(bitmapDescriptorFromVector(this, R.drawable.ic_warning_sign))

            );

            id = markers.getId();
           markersToClear.add(marker1);

        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();

                strAdd = add+" "+city;
            } else {
                //Log.w("Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }

    private void remove_markers() {
        for (Marker marker: markersToClear ) {
            marker.remove();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(googleApiClient);
        pickupAdapter.setGoogleApiClient(googleApiClient);

        //Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        pickupAdapter.setGoogleApiClient(googleApiClient);
        //Log.e(LOG_TAG, "Google Places API connection suspended.");

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = pickupAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
               // Log.e(LOG_TAG, "Place query did not complete. Error: " +
                 //       places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            LatLng search = place.getLatLng();


            String latlng = Html.fromHtml(place.getAddress()+"").toString();//.split(",");

            //Log.i(LOG_TAG, latlng) ;

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(search, 10.0f));
            bounds = mMap.getProjection().getVisibleRegion().latLngBounds;



            mMap.addMarker(new MarkerOptions()
                    .position(search)
                    .title(latlng)
            );


            float zooming = mMap.getCameraPosition().zoom;

            if( zooming >= 12.0f ) {

                LatLng noreas = bounds.northeast;
                LatLng southwe = bounds.southwest;
                double latitude_bound_1 = noreas.latitude;
                double longitude_bound_1 = noreas.longitude;
                double latitude_bound_2 = southwe.latitude;
                double longitude_bound_2 = southwe.longitude;
                parameter = Double.toString(latitude_bound_1) + "," + Double.toString(longitude_bound_1) + ","
                        + Double.toString(latitude_bound_2) + "," + Double.toString(longitude_bound_2);


                LatLng center = mMap.getCameraPosition().target;

                markersList = markDb.boundMarkers(bounds);

                remove_markers();


                getJSON();
                //Log.d("MapFragment: ", "Center From camera: Long: " + center.longitude
                  //      + " Lat" + center.latitude);
            }
        }
    };


    /* Our custom LocationSource.
 * We register this class to receive location updates from the Location Manager
 * and for that reason we need to also implement the LocationListener interface. */



    @Override
    public void onCameraMoveStarted(int i) {
        remove_markers();
    }

    private void addUser(){



        class AddUser extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config_booking.TAG_USER_NAME,user_name);
                params.put(Config_booking.TAG_EMAIL_ID,email_id);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config_booking.URL_ADD, params);
                return res;
            }
        }

        AddUser ae = new AddUser();
        ae.execute();
    }
}
