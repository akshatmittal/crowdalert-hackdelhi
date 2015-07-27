package com.roalts.hackdelhiclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button fbLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fbLoginButton = (Button) findViewById(R.id.fb_login_button);
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> permissions = Arrays.asList(
                        "public_profile", "email"/*, "user_birthday", "user_location"*/); //Use when we get reviewed
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e != null) {
                            e.printStackTrace();
                        }
                        if (isDestroyed()) {
                            return;
                        }
                        if (parseUser == null) {
                            if (e != null) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Facebook Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else if (parseUser.isNew()) {
                            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject fbUser,
                                                                GraphResponse response) {
                                            ParseUser parseUser = ParseUser.getCurrentUser();
                                            if (fbUser != null && parseUser != null
                                                    && fbUser.optString("name").length() > 0) {
                                                parseUser.put("name", fbUser.optString("name"));
                                                parseUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        loginSuccess();
                                                    }
                                                });
                                            }
                                            loginSuccess();
                                        }
                                    }
                            ).executeAsync();
                        } else {

                            loginSuccess();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
