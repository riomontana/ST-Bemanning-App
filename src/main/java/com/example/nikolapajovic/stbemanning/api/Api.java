package com.example.nikolapajovic.stbemanning.api;

/**
 * Class describing constants for API calls
 * @author Alex Giang, Sanna Roengaard, Simon Borjesson,
 * Lukas Persson, Nikola Pajovic, Linus Forsberg
 */

public class Api {

    private static final String ROOT_URL = "https://stskolbemanning.se/api/v1/Api.php?apicall=";

    public static final String URL_GET_USER = ROOT_URL + "getUser";
    public static final String URL_GET_WORK_SHIFTS = ROOT_URL + "getWorkShifts";
    public static final String URL_UPDATE_WORK_SHIFT = ROOT_URL + "updateWorkShift";


}
