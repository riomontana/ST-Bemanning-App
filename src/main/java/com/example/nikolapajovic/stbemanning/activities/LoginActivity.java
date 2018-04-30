package com.example.nikolapajovic.stbemanning.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikolapajovic.stbemanning.R;
import com.example.nikolapajovic.stbemanning.api.Api;
import com.example.nikolapajovic.stbemanning.api.ApiListener;
import com.example.nikolapajovic.stbemanning.api.PerformNetworkRequest;
import com.example.nikolapajovic.stbemanning.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Activity for logging in to the app and register a new user
 *
 * @author Alex Giang, Sanna Roengaard, Simon Borjesson,
 * Lukas Persson, Nikola Pajovic, Linus Forsberg
 */

public class LoginActivity extends AppCompatActivity implements ApiListener {

    private EditText inputEmail;
    private EditText inputPassword;
    private TextView tvInfo;
    private Button btnLogin;
    private Button btnRegister;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString("USERNAME", "empty");
        String password = prefs.getString("PASSWORD", "empty");

        if (username != "empty" || password != "empty") {
            inputEmail.setText(username);
            inputPassword.setText(password);
            checkUserLogin();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://stskolbemanning.se/signup.php"));
                startActivity(browserIntent);
            }
        });

    }

    private void checkUserLogin() {
        String username = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        saveUsernamePassword(username, password);
        HashMap<String, String> params = new HashMap<>();
        params.put("email", username);
        params.put("password", password);
        PerformNetworkRequest request =
                new PerformNetworkRequest(Api.URL_GET_USER, params, this);
        request.execute();
    }

    private void saveUsernamePassword(String username, String password) {
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefEditor.putString("USERNAME", username);
        prefEditor.putString("PASSWORD", password);
        prefEditor.apply();
    }

    @Override
    public void apiResponse(JSONObject response) {
        Log.d("json ", response.toString());
        try {
            JSONArray jsonArray = response.getJSONArray("user");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            int userId = jsonObject.getInt("user_id");
            String firstName = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");

            Toast.makeText(getBaseContext(), "Välkommen " + firstName + " " + lastName,
                    Toast.LENGTH_SHORT).show();

            User user = new User(userId,firstName,lastName);
            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Fel användarnamn eller lösenord",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * If user has forgotten their password they get sent to the webpage
     * @param view
     */
    public void passwordHelper(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://stskolbemanning.se/newpassword.php"));
        startActivity(browserIntent);
    }

    /**
     * Disable back button
     */
    @Override
    public void onBackPressed() {
        //remove call to the super class
        //super.onBackPressed();
    }

}
