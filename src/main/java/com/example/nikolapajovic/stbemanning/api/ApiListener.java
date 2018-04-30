package com.example.nikolapajovic.stbemanning.api;

import org.json.JSONObject;

/**
 * Callback interface for returning API returns from the server
 *
 * @author Alex Giang, Sanna Roengaard, Simon Borjesson,
 * Lukas Persson, Nikola Pajovic, Linus Forsberg
 */

public interface ApiListener {

    void apiResponse(JSONObject response);
}
