package com.ebiz.db;

/**
 * UserInfoDBHelper class is used for creating tables for Services Request 
 * @author RANJITH KUMAR K
 */
public class StudentEnqInfoDBHelper {

	public static final String TABLE_NAME = "student_enquiry";
	public static final String STD_ENQ_LIST_TABLE_NAME = "student_enquiry_list";
	public static final String KEY_ID="_id";
	public static final String ENQ_LIST_ID = "_id";
	public static final String ORG_ID = "org_id";
	public static final String lOC_ID = "loc_id";
	public static final String MOBILE_NO = "mobile_no";
	public static final String STD_NAME = "std_Name";
	public static final String DOB = "date_of_Birth";
	public static final String GENDER = "gender";
	public static final String E_MAIL = "e_mail";
	public static final String COURSE	= "course";
	public static final String COURSE_ID	= "course_id";
	public static final String SOURCE_OF_ENQ	= "source_of_enq";
	public static final String SOURCE_OF_ENQ_ID	= "source_of_enq_id";
	public static final String ENQUIRY_DT	= "enquiry_dt";
	public static final String STUDENT_ID	= "student_id";
	public static final String SYNC_STATUS="sync_status";
	public static final String ERROR_FLAG="error_flag";
	public static final String ERROR_MSG="error_msg";
	public static final String DO_NOT_CALL="do_not_call";

	public static final String CREATE_TABLE = "create table " + TABLE_NAME
			+ "("
			+ KEY_ID + " integer primary key autoincrement,"
			+ lOC_ID +" int(10),"
			+ ORG_ID +" int(10),"
			+ STUDENT_ID +" int(10),"
			+ MOBILE_NO +"  Varchar(20),"
			+ STD_NAME + " Varchar(255),"
			+ GENDER + " Varchar(10),"
			+ DOB + " Varchar(50),"
			+ E_MAIL + " Varchar(20),"
			+ COURSE_ID + " int(10),"
			+ COURSE + " Varchar(50),"
			+ SOURCE_OF_ENQ_ID + " int(10),"
			+ SOURCE_OF_ENQ + " Varchar(100),"
			+ ENQUIRY_DT + " Varchar(50),"
			+ SYNC_STATUS + " int(2),"
			+ ERROR_FLAG + " Varchar(10),"
			+ ERROR_MSG + " Varchar(300),"
			+DO_NOT_CALL+ "  Varchar(1)"
			+")";

	public static final String LIST_CREATE_TABLE = "create table " + STD_ENQ_LIST_TABLE_NAME
			+ "("
			+ ENQ_LIST_ID + " integer primary key autoincrement,"
			+ MOBILE_NO +"  Varchar(20),"
			+ STD_NAME + " Varchar(255),"
			+ DOB + " Varchar(50),"
			+ E_MAIL + " Varchar(20),"
			+ COURSE_ID + " int(10),"
			+ COURSE + " Varchar(50),"
			+ ENQUIRY_DT + " Varchar(50)"
			+")";

}
