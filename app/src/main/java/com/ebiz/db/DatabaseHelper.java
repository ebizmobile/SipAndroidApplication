package com.ebiz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author RANJITH KUMAR K
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME="sams";
	public static final int DATABASE_VERSION=3;
	Context context;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try
		{
			db.execSQL(CourseInfoDBHelper.CREATE_TABLE);
		    db.execSQL(StudentEnqInfoDBHelper.CREATE_TABLE);
            db.execSQL(SourceOfEnquiryInfoDBHelper.CREATE_TABLE);
			db.execSQL(StudentInfoDBHelper.CREATE_TABLE);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS course_dtl");
		db.execSQL("DROP TABLE IF EXISTS student_enquiry");
		db.execSQL("DROP TABLE IF EXISTS source_of_enquiry");
		db.execSQL("DROP TABLE IF EXISTS student_info");

		onCreate(db);
		/*if(oldVersion!=newVersion){
			db.execSQL("ALTER TABLE " + ColdCallDBHelper.TABLE_NAME + " ADD COLUMN "
                    + ColdCallDBHelper.FOLLOW_UP_FLAG + " Varchar(2)");
		}*/

	}

}
