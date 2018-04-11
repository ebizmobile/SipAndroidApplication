package com.ebiz.db;

import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONObject;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.widget.Toast;

import com.ebiz.constant.CommonUtilities;
import com.ebiz.to.StudentEnquiryTO;
import com.ebiz.to.StudentInfoTO;

/**
 * @author RANJITH KUMAR K
 */
public class DatabaseHandler {
	JSONObject json_validation;
	Context context;
	DatabaseHelper helper;
	SharedPreferences preferences;

	public DatabaseHandler(Context context) {
		this.context = context;
		helper = new DatabaseHelper(context);
		preferences=context.getSharedPreferences(CommonUtilities.PREFERENCE_KEY, 0);
	}

	/* STUDENT ENQUIRY START*/
	/**
	 * inserting all user data into user_information table
	 *
	 *            the userID to store
	 * @param mobileNo
	 *            the mobileNo to store
	 * @param stdName
	 *            the stdName to store
	 */
	public void insertStdEnqDtl(Integer locId,Integer orgId,String mobileNo, String stdName,int stdId,String dateOfBirth,String emailId,String course,int courseId,int status,String enquiryDt,String gender,String sourceOfEnq,int sourceOfEnqId,String doNotCall) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(StudentEnqInfoDBHelper.MOBILE_NO, mobileNo);
			contentValues.put(StudentEnqInfoDBHelper.lOC_ID, locId);
			contentValues.put(StudentEnqInfoDBHelper.ORG_ID, orgId);
			contentValues.put(StudentEnqInfoDBHelper.STUDENT_ID, stdId); // Initially its zero, its student Info table primary id
			contentValues.put(StudentEnqInfoDBHelper.STD_NAME, stdName);
			contentValues.put(StudentEnqInfoDBHelper.GENDER, gender);
			contentValues.put(StudentEnqInfoDBHelper.DOB, dateOfBirth);
			contentValues.put(StudentEnqInfoDBHelper.E_MAIL, emailId);
			contentValues.put(StudentEnqInfoDBHelper.COURSE_ID, courseId);
			contentValues.put(StudentEnqInfoDBHelper.COURSE, course);
			contentValues.put(StudentEnqInfoDBHelper.SOURCE_OF_ENQ_ID, sourceOfEnqId);
			contentValues.put(StudentEnqInfoDBHelper.SOURCE_OF_ENQ, sourceOfEnq);
			contentValues.put(StudentEnqInfoDBHelper.ENQUIRY_DT, enquiryDt);
			contentValues.put(StudentEnqInfoDBHelper.SYNC_STATUS, status);
			contentValues.put(StudentEnqInfoDBHelper.ERROR_FLAG, "N");
			contentValues.put(StudentEnqInfoDBHelper.ERROR_MSG, "");
			contentValues.put(StudentEnqInfoDBHelper.DO_NOT_CALL, doNotCall);
			db.insert(StudentEnqInfoDBHelper.TABLE_NAME, null, contentValues);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	public void insertCourseDtl(StudentEnquiryTO studentEnquiryTO) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(CourseInfoDBHelper.lOC_ID, studentEnquiryTO.getLocId());
			contentValues.put(CourseInfoDBHelper.COURSE_ID, studentEnquiryTO.getCourseId());
			contentValues.put(CourseInfoDBHelper.COURSE, studentEnquiryTO.getCourseName());
			db.insert(CourseInfoDBHelper.TABLE_NAME, null, contentValues);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public Cursor getCourseDtl(int locId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { CourseInfoDBHelper.COURSE_ID,
				CourseInfoDBHelper.COURSE};
		return db.query(CourseInfoDBHelper.TABLE_NAME, columns,
				CourseInfoDBHelper.lOC_ID + "=" + locId, null, null, null, null,null);
	}
	public Cursor getCourseIdDtl(String courseName) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { CourseInfoDBHelper.COURSE_ID,
				CourseInfoDBHelper.COURSE};
		return db.query(CourseInfoDBHelper.TABLE_NAME, columns,
				CourseInfoDBHelper.COURSE + "='" + courseName+"'", null, null, null, null,null);
	}
	public Cursor validateCourseExist(int courseId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { CourseInfoDBHelper.COURSE_ID,
				CourseInfoDBHelper.COURSE};
		return db.query(CourseInfoDBHelper.TABLE_NAME, columns,
				CourseInfoDBHelper.COURSE_ID + "='" + courseId+"'", null, null, null, null,null);
	}
	public Cursor validateMobileExist(String mobileNo,String studentName,int locId) {
		SQLiteDatabase db = helper.getWritableDatabase();
        int status =0;
		String[] columns = {StudentEnqInfoDBHelper.ENQ_LIST_ID,
							StudentEnqInfoDBHelper.MOBILE_NO,
							StudentEnqInfoDBHelper.STUDENT_ID,
							StudentEnqInfoDBHelper.STD_NAME,
							StudentEnqInfoDBHelper.GENDER,
							StudentEnqInfoDBHelper.COURSE,
							StudentEnqInfoDBHelper.COURSE_ID,
							StudentEnqInfoDBHelper.lOC_ID,
							StudentEnqInfoDBHelper.ENQUIRY_DT};
		return db.query(StudentEnqInfoDBHelper.TABLE_NAME, columns,
				        StudentEnqInfoDBHelper.MOBILE_NO + "='" + mobileNo+"' AND "+
						StudentEnqInfoDBHelper.STD_NAME + "='" +studentName+"' AND "+
		                StudentEnqInfoDBHelper.lOC_ID + "='" +locId+"' AND " +
						StudentEnqInfoDBHelper.SYNC_STATUS + "="+status , null, null, null, null,null);
	}


	public void updateDoNotCallFlag(Integer locId,Integer orgId,Integer primaryKey) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(StudentEnqInfoDBHelper.DO_NOT_CALL, "Y");
			db.update(StudentEnqInfoDBHelper.TABLE_NAME, contentValues,StudentEnqInfoDBHelper.KEY_ID + "='" + primaryKey+"'", null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

     /* STUDENT ENQUIRY END*/

	/**
	 * This method retrieve all the values from the user_information table
	 * 
	 * @return the all user informations
	 */
	public Cursor getallStudentEnquiryList(int locId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = {
				StudentEnqInfoDBHelper.ENQ_LIST_ID,
				StudentEnqInfoDBHelper.MOBILE_NO,
				StudentEnqInfoDBHelper.STUDENT_ID,
				StudentEnqInfoDBHelper.STD_NAME,
				StudentEnqInfoDBHelper.GENDER,
				StudentEnqInfoDBHelper.COURSE,
				StudentEnqInfoDBHelper.COURSE_ID,
				StudentEnqInfoDBHelper.lOC_ID,
				StudentEnqInfoDBHelper.ENQUIRY_DT,
				StudentEnqInfoDBHelper.DO_NOT_CALL};
		return db.query(StudentEnqInfoDBHelper.TABLE_NAME, columns,StudentEnqInfoDBHelper.lOC_ID+ "=" + locId +" AND "+ StudentEnqInfoDBHelper.DO_NOT_CALL+ "='N'", null, null, null,
				StudentEnqInfoDBHelper.ENQUIRY_DT+" DESC", null);
		
	}

	public Cursor validateStudentId(int studentId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { StudentEnqInfoDBHelper.STUDENT_ID,
				StudentEnqInfoDBHelper.STD_NAME};
		return db.query(StudentEnqInfoDBHelper.TABLE_NAME, columns,
				StudentEnqInfoDBHelper.STUDENT_ID + "=" + studentId, null, null, null, null);
	}

	/**
	 * This method retrieve all the values from the student_enquiry table ( un sysnc date)
	 */
	public Cursor getStudentDtl() {
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = {
				StudentEnqInfoDBHelper.STUDENT_ID,
				StudentEnqInfoDBHelper.MOBILE_NO,
				StudentEnqInfoDBHelper.STD_NAME
				};

		return db.query(true, StudentEnqInfoDBHelper.TABLE_NAME, columns, null, null, null, null, null, null);

	}

	public Cursor getUnSynDataDtl() {
		SQLiteDatabase db = helper.getWritableDatabase();
        int status =0 ;
		String[] columns = {
				            StudentEnqInfoDBHelper.KEY_ID,
							StudentEnqInfoDBHelper.MOBILE_NO,
							StudentEnqInfoDBHelper.STD_NAME,
							StudentEnqInfoDBHelper.COURSE,
				            StudentEnqInfoDBHelper.COURSE_ID,
							StudentEnqInfoDBHelper.SOURCE_OF_ENQ,
							StudentEnqInfoDBHelper.SOURCE_OF_ENQ_ID,
				            StudentEnqInfoDBHelper.GENDER,
							StudentEnqInfoDBHelper.lOC_ID,
							StudentEnqInfoDBHelper.E_MAIL,
				            StudentEnqInfoDBHelper.DOB,
							StudentEnqInfoDBHelper.ENQUIRY_DT,
							StudentEnqInfoDBHelper.DO_NOT_CALL};

		return db.query(StudentEnqInfoDBHelper.TABLE_NAME, columns,
				StudentEnqInfoDBHelper.SYNC_STATUS + "=" + status, null, null, null, null,null);
	}

	public void updateStudentStatus(Integer primaryKey,Integer studentId, String errorFlag,String errorMsg) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(StudentEnqInfoDBHelper.ERROR_FLAG,errorFlag);
			contentValues.put(StudentEnqInfoDBHelper.ERROR_MSG,errorMsg);
			contentValues.put(StudentEnqInfoDBHelper.STUDENT_ID,studentId);
			contentValues.put(StudentEnqInfoDBHelper.SYNC_STATUS,1);

			db.update(StudentEnqInfoDBHelper.TABLE_NAME, contentValues,StudentEnqInfoDBHelper.KEY_ID + "='" + primaryKey+"'", null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public Cursor validateSourceOfEnqCourseExist(int courseId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { SourceOfEnquiryInfoDBHelper.MEDIA_ID,
				SourceOfEnquiryInfoDBHelper.MEDIA_NAME};
		return db.query(SourceOfEnquiryInfoDBHelper.TABLE_NAME, columns,
				SourceOfEnquiryInfoDBHelper.MEDIA_ID + "='" + courseId+"'", null, null, null, null,null);
	}

	public void insertSourceOfEnqDtl(int orgId,int mediaId,String mediaName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(SourceOfEnquiryInfoDBHelper.ORG_ID,orgId);
			contentValues.put(SourceOfEnquiryInfoDBHelper.MEDIA_ID,mediaId);
			contentValues.put(SourceOfEnquiryInfoDBHelper.MEDIA_NAME, mediaName);
			db.insert(SourceOfEnquiryInfoDBHelper.TABLE_NAME, null, contentValues);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	public Cursor getSourceOfEnquiry(int orgId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { SourceOfEnquiryInfoDBHelper.MEDIA_ID,
				SourceOfEnquiryInfoDBHelper.MEDIA_NAME};
		return db.query(SourceOfEnquiryInfoDBHelper.TABLE_NAME, columns,
				SourceOfEnquiryInfoDBHelper.ORG_ID + "=" + orgId, null, null, null, null,null);
	}
	public Cursor getSourceOfEnqIdDtl(String sourceName) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = { SourceOfEnquiryInfoDBHelper.MEDIA_ID,
				SourceOfEnquiryInfoDBHelper.MEDIA_NAME};
		return db.query(SourceOfEnquiryInfoDBHelper.TABLE_NAME, columns,
				SourceOfEnquiryInfoDBHelper.MEDIA_NAME + "='" + sourceName+"'", null, null, null, null,null);
	}
//student receipt screen

	public void insertStudentInfoDtl(StudentInfoTO studentInfoTO) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(StudentInfoDBHelper.lOC_ID, studentInfoTO.getLocId());
			contentValues.put(StudentInfoDBHelper.STUDENT_NAME, studentInfoTO.getStudentName());
			contentValues.put(StudentInfoDBHelper.STUDENT_ID, studentInfoTO.getStudentId());
			contentValues.put(StudentInfoDBHelper.STD_MOBILE_NO, studentInfoTO.getMobileNo());
			db.insert(StudentInfoDBHelper.TABLE_NAME, null, contentValues);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public void clearTableStudentInfo() {

		SQLiteDatabase db = helper.getWritableDatabase();
		try {
		 	db.execSQL("DELETE FROM  "+StudentInfoDBHelper.TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	public Cursor getStudentInfoDtl(int locId) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = {StudentInfoDBHelper.STUDENT_ID,
				StudentInfoDBHelper.STUDENT_NAME,
				StudentInfoDBHelper.STD_MOBILE_NO,
				StudentInfoDBHelper.lOC_ID};
		return db.query(StudentInfoDBHelper.TABLE_NAME, columns,
				StudentInfoDBHelper.lOC_ID + "=" + locId, null, null, null, StudentInfoDBHelper.STUDENT_NAME+" ASC",null);
	}

	public Cursor getStudentSearchDtl(String searchName, String srcForSearch) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if(srcForSearch.equals("mobileno")){
			String[] columns = { StudentInfoDBHelper.STUDENT_ID,
					StudentInfoDBHelper.STUDENT_NAME,
					StudentInfoDBHelper.STD_MOBILE_NO};
			return db.query(StudentInfoDBHelper.TABLE_NAME, columns,
					StudentInfoDBHelper.STD_MOBILE_NO + "='" + searchName+"'", null, null, null, null,null);
		}else{
			String[] columns = { StudentInfoDBHelper.STUDENT_ID,
					StudentInfoDBHelper.STUDENT_NAME,
					StudentInfoDBHelper.STD_MOBILE_NO};
			return db.query(StudentInfoDBHelper.TABLE_NAME, columns,
					StudentInfoDBHelper.STUDENT_NAME + "='" + searchName+"'", null, null, null, null,null);
		}
	}

	public Cursor getUnSynDoNotCallDataDtl() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int status =0 ;
		String[] columns = {
				StudentEnqInfoDBHelper.KEY_ID,
				StudentEnqInfoDBHelper.STUDENT_ID,
				StudentEnqInfoDBHelper.STD_NAME,
				StudentEnqInfoDBHelper.lOC_ID,
				StudentEnqInfoDBHelper.DO_NOT_CALL};

		return db.query(StudentEnqInfoDBHelper.TABLE_NAME, columns,
				StudentEnqInfoDBHelper.DO_NOT_CALL + "= 'Y'" , null, null, null, null,null);
	}


	public void updateDoNotCallFlagSyn(Integer locId,Integer studId,String flag) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put(StudentEnqInfoDBHelper.DO_NOT_CALL, flag);
			db.update(StudentEnqInfoDBHelper.TABLE_NAME, contentValues,StudentEnqInfoDBHelper.STUDENT_ID + "='" + studId+"' ", null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

}
