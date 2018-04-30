package com.example.nikolapajovic.stbemanning.api;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Class performing network requests extending AsyncTask
 *
 * @author Alex Giang, Sanna Roengaard, Simon Borjesson,
 * Lukas Persson, Nikola Pajovic, Linus Forsberg
 */

public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

    //the url where we need to send the request
    private String url;
    //the parameters
    private HashMap<String, String> params;
    private ApiListener apiListener;


    // Constructor
    public PerformNetworkRequest(String url, HashMap<String, String> params, ApiListener apiListener) {
        this.url = url;
        this.params = params;
        this.apiListener = apiListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    //this method will give the response from the request
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject object = new JSONObject(s);
            apiListener.apiResponse(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //the network operation will be performed in the background
    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        return requestHandler.sendPostRequest(url, params);

    }
}
