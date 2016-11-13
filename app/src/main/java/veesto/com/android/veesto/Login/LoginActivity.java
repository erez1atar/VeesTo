package veesto.com.android.veesto.Login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import veesto.com.android.veesto.FriendList.FriendsListActivity;
import veesto.com.android.veesto.R;


public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private final int PERMISSIONS_REQUEST_PHONE_STATE = 4;

    private EditText emailText;
    private Executor executor;
    private ILoginController contoller;
    private boolean emailUpdated = false;
    private boolean friendsUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contoller = new LoginController();

        executor = Executors.newFixedThreadPool(2);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        emailText = (EditText)findViewById(R.id.email_text);

        requestRuntimePermissions();

        Button moveOn = (Button)findViewById(R.id.move_to_menu);
        moveOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailT = emailText.getText().toString();
                if(isEmailValid(emailT)) {
                    contoller.setUserEmail(emailT);
                    emailUpdated = true;
                    friendsUpdated = true;
                    onLoginCompeleted();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Invalid email",
                            Toast.LENGTH_LONG).show();
                    emailText.setText("");
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();
        if(isLoggedIn())
        {
            LoginManager.getInstance().logOut();
        }
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.setReadPermissions("first_name");
        loginButton.setReadPermissions("last_name");
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult)
            {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        getEmailAddress(loginResult);
                        getListOfFriends();

                    }
                });
            }

            private void getEmailAddress(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());

                        try {
                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            contoller.setUserEmail(firstName + lastName);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        try {
                            // in case email address is private
                            // some of the users on facebook don't share their email even to friends.
                            String email1 = object.getString("email");
                            if(email1 != null)
                            {
                                contoller.setUserEmail(email1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("LoginActivity - email", "error");

                        }
                        emailUpdated = true;
                        onLoginCompeleted();


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            private void getListOfFriends()
            {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d("onCompleted - friens", response.toString());
                                JSONObject json = response.getJSONObject();
                                JSONArray friendArray = null;
                                try {
                                    friendArray = json.getJSONArray("data");
                                } catch (JSONException e) {
                                    Log.d("json - parse", "error");
                                }

                                for (int i = 0; i < friendArray.length(); i++) {
                                    JSONObject frnd_obj = null;
                                    try {
                                        frnd_obj = friendArray.getJSONObject(i);
                                        contoller.addFriend(frnd_obj.getString("name"));
                                        Log.d("name", frnd_obj.getString("name"));
                                    } catch (JSONException e) {
                                        Log.d("json - list", "error");
                                    }
                                }
                                friendsUpdated = true;
                                onLoginCompeleted();
                            }
                        }
                ).executeAsync();

            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {


            }
        });
    }

    private void requestRuntimePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_PHONE_STATE);
        }
    }

    private void onLoginCompeleted()
    {
        if(friendsUpdated && emailUpdated) {
            contoller.updatePhoneDetails();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent friendsListIntent = new Intent(LoginActivity.this, FriendsListActivity.class);
                    startActivity(friendsListIntent);
                    finish();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_PHONE_STATE: {
                // if the user refuse the request
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("Home", "Permission Failed");
                    finish();
                }
            }
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
