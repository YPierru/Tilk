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
    public final static String URL_GET_STATS_DAY=URL_SERVER+"/get_stats_day.php";
    public final static String URL_GET_STATS_WEEK=URL_SERVER+"/get_stats_week.php";
    public final static String URL_GET_STATS_MONTH=URL_SERVER+"/get_stats_month.php";
    public final static String URL_GET_STATS_YEAR=URL_SERVER+"/get_stats_year.php";

}
