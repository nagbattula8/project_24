package com.user.alltronics.locate.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.user.alltronics.locate.MainActivity;
import com.user.alltronics.locate.R;

import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    JSONObject response, profile_pic_data, profile_pic_url;
    public SQLiteHandler db;
    public SessionManager session;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        final Context context = this;
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();



        TextView name_f = (TextView) findViewById(R.id.name);
        TextView email_id = (TextView) findViewById(R.id.email_id);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                session.setLogin(false);

                db.deleteUsers();

                // Launching the login activity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                LoginManager.getInstance().logOut();

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });

                startActivity(intent);
                finish();
            }

        });

        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        name_f.setText(name);
        email_id.setText(email);

        String jsondata = intent.getStringExtra("userProfile");
//        Log.w("Jsondata", jsondata);
        TextView user_name = (TextView) findViewById(R.id.UserName);
        //ImageView user_picture = (ImageView) findViewById(R.id.profilePic);
        TextView user_email = (TextView) findViewById(R.id.email);
        try {
            response = new JSONObject(jsondata);
            //user_email.setText(response.get("email").toString());
            //user_name.setText(response.get("name").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            //Picasso.with(this).load(profile_pic_url.getString("url"))
              //      .into(user_picture);

        } catch(Exception e){
            e.printStackTrace();
        }
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
    public void onClick(View view) {
        //if (view == R.id.logout )
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
