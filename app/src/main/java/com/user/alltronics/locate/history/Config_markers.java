package com.user.alltronics.locate.history;

/**
 * Created by yash on 7/9/17.
 */

public class Config_markers {

    //Address of our scripts of the CRUD
    public static final String URL_GET_USER = "http://10.0.0.2/User/databases/user_details/getUser.php?id=";
    public static final String URL_GET_ALL = "http://10.0.0.5/Android/project_24/markers/getAllMarkers.php";
    public static final String URL_GET_MARKERS = "http://10.0.0.2/Android/project_24/markers/markers.php?page=";

    public static final String URL_UPDATE_USER = "http://10.0.0.2/User/databases/user_details/updateUser.php?id=";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_LONGITUDE= "longitude";
    public static final String TAG_START_TIME = "starttime";
    public static final String TAG_END_TIME = "endtime";
    public static final String TAG_MAKE = "make";
    public static final String TAG_MODEL = "Model";



}
