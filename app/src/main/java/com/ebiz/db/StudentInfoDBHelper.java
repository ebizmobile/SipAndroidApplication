package com.ebiz.db;

/**
 * Created by Rajesh Kumar on 29-Dec-16.
 */

public class StudentInfoDBHelper {
    public static final String TABLE_NAME = "student_info";
    public static final String KEY_ID="_id";
    public static final String lOC_ID = "loc_id";
    public static final String STUDENT_NAME	= "student_name";
    public static final String STUDENT_ID	= "student_id";
    public static final String SYNC_STATUS	= "sync_status";
    public static final String INVOICE_ID	= "invoiceId";
    public static final String INVOICE_DATE	= "Date";
    public static final String INVOICE_AMT	= "Amount";
    public static final String BAL_AMT	= "balAmt";
    public static final String PAID_AMT	= "Paid";
    public static final String PAID_STAUS	= "paidStatus";
    public static final String STD_MOBILE_NO	= "mobileNo";
    public static final String MONTH	= "Month";
    public static final String INVOICE_NO	= "invoiceNo";
    public static final String YEAR	= "Year";
    public static final String MONTHYEAR	= "MonthYear";
    public static final String INVOICEDUEDT	= "DueDt";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + KEY_ID + " integer primary key autoincrement,"
            + lOC_ID +" int(10),"
            + STUDENT_ID + " int(20),"
            + STUDENT_NAME + " Varchar(100),"
            +STD_MOBILE_NO+" Varchar(20),"
            + SYNC_STATUS + " int(5)"
            +")";


}


