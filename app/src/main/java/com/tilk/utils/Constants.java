package com.tilk.utils;

/**
 * Created by YPierru on 05/01/2017.
 */

public class Constants {

    public final static String TAG_LOG="TILK";

    public final static int RESPONSE_CODE_OK=1;

    public final static String SESSION_FIRSTRUN="firstrun";
    public final static String SESSION_STATUS="status";
    public final static String SESSION_WATERLOAD="waterload";
    public final static String SESSION_NUMBER_WATERLOADS="number_waterloads";
    public final static String SESSION_ROOM="room";
    public final static String SESSION_NUMBER_ROOMS="number_rooms";
    public final static String SESSION_ID_USER="id_user";
    public final static String SESSION_ID_TILK="id_tilk";

    private final static String URL_SERVER="http://tilk.laurentjerber.com";
    public final static String URL_LOGIN=URL_SERVER+"/login.php";
    public final static String URL_GET_LOADS=URL_SERVER+"/get_loads.php";
    public final static String URL_GET_CURRENTFLOW=URL_SERVER+"/get_currentflow.php";

}
