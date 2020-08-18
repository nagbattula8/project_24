package com.user.alltronics.locate;

/**
 * Created by Belal on 10/24/2015.
 */
public class Config_booking {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://www.instrosoft.com/project_24/user/addUser.php";
    public static final String URL_GET_ALL = "http://10.0.0.2/Android/databases/booking/getAllBooking.php";
    public static final String URL_GET_BOOKING = "http://10.0.0.2/Android/databases/booking/getBooking.php?id=";
    public static final String URL_UPDATE_BOOKING = "http://10.0.0.2/Android/databases/booking/updateBooking.php";
    public static final String URL_DELETE_BOOKING = "http://10.0.0.2/Android/databases/booking/deleteBooking.php?id=";
    public static final String URL_GET_ALL_MARKERS = "http://10.0.0.2/Android/latest/markers/getAllMarkers.php";
    public static final String URL_GET_MARKERS = "http://www.instrosoft.com/project_24/markers/markers.php?page=";


    //Keys that will be used to send the request to php scripts
    public static final String KEY_BOOKING_ID = "id";
    public static final String KEY_BOOKING_CURRENT_BOOKING = "current_booking";
    public static final String KEY_BOOKING_BOOKING_ID = "booking_id";
    public static final String KEY_BOOKING_PAYMENT_STATUS = "payment_status";
    public static final String KEY_BOOKING_ORDER_HISTORY_ID = "order_history_id";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_CURRENT_BOOKING = "current_booking";
    public static final String TAG_BOOKING_ID = "booking_id";
    public static final String TAG_PAYMENT_STATUS = "payment_status";
    public static final String TAG_ORDER_HISTORY_ID = "order_history_id";


    public static final String TAG_USER_NAME = "name";
    public static final String TAG_EMAIL_ID = "email";

    public static final String TAG_ID_MARKER = "id";
    public static final String TAG_NAME_MARKER = "name";
    public static final String TAG_LAT = "latitude";
    public static final String TAG_LNG = "longitude";


    //employee id to pass with intent
    public static final String BOOKING_ID = "id";
}
