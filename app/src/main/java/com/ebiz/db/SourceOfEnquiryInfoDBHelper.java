package com.ebiz.db;

/**
 * Created by KARTHI on 19-12-2016.
 */

public class SourceOfEnquiryInfoDBHelper {
    public static final String TABLE_NAME = "source_of_enquiry";
    public static final String KEY_ID="_id";
    public static final String ORG_ID = "ORG_id";
    public static final String MEDIA_NAME	= "media_name";
    public static final String MEDIA_ID	= "media_id";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + KEY_ID + " integer primary key autoincrement,"
            + ORG_ID +" int(10),"
            + MEDIA_NAME + " int(10),"
            + MEDIA_ID + " Varchar(50)"
            +")";
}
