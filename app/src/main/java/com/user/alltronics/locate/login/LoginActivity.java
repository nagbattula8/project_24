package com.user.alltronics.locate.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.user.alltronics.locate.Config_booking;
import com.user.alltronics.locate.MainActivity;
import com.user.alltronics.locate.R;
import com.user.alltronics.locate.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callbackManager;
    private SessionManager session;
    private SQLiteHandler db;
    String token;
    String user_name, email_id;
    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);


        SignIn = (SignInButton) findViewById(R.id.bn_login);
        SignIn.setOnClickListener(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Log.d("MainActivity  ", "User name: " + "hh" );

            handleResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleResult( GoogleSignInResult result ) {

        if( result.isSuccess() ) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String created_at = "2017-03-28 15:52:25";

            db.addUser(name, email, email, created_at);

            //String img_url = account.getPhotoUrl().toString();

            session.setLogin(true);


            //Log.d("MainActivity  ", "User name: " + name + " " + email );

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(intent);



        }

        else {

        }
    }

    protected void getUserDetails(final LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {


                        try {
                            String email = json_object.getString("email");
                            String name = json_object.getString("name");
                            String uid = json_object.getString("id");
                            String created_at = "2017-03-28 15:52:25";

                            user_name = name;
                            email_id = email;
                            db.addUser(name, email, uid, created_at);
                            user_add();





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        token = loginResult.getAccessToken().getToken();
                        //Log.w("Token", token);

                        session.setLogin(true);


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();


    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bn_login:
                signIn();
                break;
        }
    }

        private void signIn() {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            //Log.d("MainActivity  ", "User name: " + "hh" );
            startActivityForResult(intent,REQ_CODE);

        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void user_add(){



        class AddUser extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(LoginActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
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
