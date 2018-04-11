package com.ebiz.db;

/**
 * Created by KARTHI on 09-12-2016.
 */

public class CourseInfoDBHelper {

    public static final String TABLE_NAME = "course_dtl";
    public static final String KEY_ID="_id";
    public static final String ENQ_LIST_ID = "_id";
    public static final String lOC_ID = "loc_id";
    public static final String COURSE	= "course";
    public static final String COURSE_ID	= "course_id";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + KEY_ID + " integer primary key autoincrement,"
            + lOC_ID +" int(10),"
            + COURSE_ID + " int(10),"
            + COURSE + " Varchar(50)"
            +")";


}
