package com.ebiz.receiver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.StrictMode;


import com.eazibiz.sipandroidapplication.MenuActivity;
import com.ebiz.constant.CommonUtilities;
import com.ebiz.constant.MasterConstants;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.db.StudentEnqInfoDBHelper;
import com.ebiz.json.JSONParser;
import com.ebiz.json.UserFunctions;
import com.ebiz.to.StudentEnquiryTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;

public class InitialSyncReceiver extends BroadcastReceiver {

	ArrayList<StudentEnquiryTO> studentEnquiryList;
	ArrayList<StudentEnquiryTO> studentEnquirySaveList;
	JSONObject json_validation;
	  Context context;
	  DatabaseHandler handler;
	  Cursor cursor;
	  SharedPreferences preferences;
	  CommonUtilities commonUtilities;
	private ProgressDialog progressBar;
	private int  locId;
	private int orgId;

	
	/*RECEIVER METHOD START*/
	@Override
	public void onReceive(Context context, Intent intent) {
		try{
		preferences=context.getSharedPreferences(CommonUtilities.PREFERENCE_KEY, 0);
		 StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	     StrictMode.setThreadPolicy(threadPolicy);
		 studentEnquiryList = new ArrayList<StudentEnquiryTO>();

		 this.context=context;
		         ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
	             NetworkInfo mobileDataNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	             NetworkInfo wifiDataNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	             boolean isConnected =((mobileDataNetInfo != null||wifiDataNetInfo != null) && (mobileDataNetInfo.isConnectedOrConnecting()||wifiDataNetInfo.isConnectedOrConnecting()));   
	             if (isConnected) {
					 SharedPreferences loginDtl = context.getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
					 Boolean syncStatus= loginDtl.getBoolean("syncStatus",false); // Default value is false
					 if(syncStatus) {
						 progressBar = new ProgressDialog(context);
						 progressBar.setCancelable(true);
						 progressBar.setMessage("Synchronizing...");
						 progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
						 progressBar.setProgress(0);
						 progressBar.setMax(100);
						 progressBar.show();
					 }
	              	 //String urlStatus=this.checkUrlStatus(CommonUtilities.BASE_URL);
	 	            //Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
 	            	// if(urlStatus.equals("Success")){


						locId = loginDtl.getInt("location_id",0);// Default value is false
						orgId = loginDtl.getInt("org_id",0);// Default value is false
						 String a[] = {String.valueOf(locId),String.valueOf(orgId)};
		   	             new StudentEnqListAsyncTask().execute(a);
						 new StudentEnqSaveAsyncTask().execute(a);
					 	 new StudentEnqDoNotCallAsyncTask().execute(a);
		            	 // LOADING USER DETAILS FROM SERVER END
	             	}
	             //}
	             else{
	            	//Toast.makeText(context, "Not Connected", Toast.LENGTH_LONG).show();
	             }
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	}
	/*RECEIVER METHOD END*/
	
	/*STUDENT ENQUIRY LIST START*/

	class StudentEnqListAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... urls) {
			try {
				    json_validation = new UserFunctions().getStudentEnqListDtl(urls[0]);
					JSONArray jarray = json_validation.getJSONArray("studentEnquiryList");
					//System.out.println("22222222222222"+jarray);
					for (int i = 0; i < jarray.length(); i++) {
						//this jsonobject is used to retrieve value from json array
							JSONObject object = jarray.getJSONObject(i);
						    StudentEnquiryTO studentEnquiryTO=new StudentEnquiryTO();
						    studentEnquiryTO.setName(object.getString("std_name"));
							studentEnquiryTO.setMobileNo(object.getString("std_mobileno"));
							studentEnquiryTO.setEmailId(object.getString("email_id"));
							studentEnquiryTO.setCourseName(object.getString("course_name"));
							studentEnquiryTO.setCourseId(object.getInt("course_id"));
							studentEnquiryTO.setEnquiryDtDisp(object.getString("enq_date"));
							studentEnquiryTO.setDateOfBirthDisp(object.getString("dob"));
						    studentEnquiryTO.setStudentId(object.getInt("student_id"));
						    //studentEnquiryTO.setGender(object.getString("gender"));
							studentEnquiryTO.setSourceOfEnq(object.getString("media_name"));
							studentEnquiryTO.setSourceOfEnqId(object.getInt("advertisement_media_id"));
							studentEnquiryTO.setDoNotCall(object.getString("do_not_call"));
						    studentEnquiryList.add(studentEnquiryTO);
						}
					return true; // Successful

				
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result) {
			DatabaseHandler handler=new DatabaseHandler(context);
			Cursor cursor = handler.getStudentDtl();
			if(cursor.getCount()!=0) {
				for(int i=0;i<studentEnquiryList.size();i++) {
					StudentEnquiryTO studentEnquiryTO = new StudentEnquiryTO();
					String stdName = studentEnquiryList.get(i).getName();
					int stdId = studentEnquiryList.get(i).getStudentId();
					String mobileNo = studentEnquiryList.get(i).getMobileNo();
					String emailId = studentEnquiryList.get(i).getEmailId();
					String course = studentEnquiryList.get(i).getCourseName();
					int courseId = studentEnquiryList.get(i).getCourseId();
					String enqDate = studentEnquiryList.get(i).getEnquiryDtDisp();
					String dateOfBirth = studentEnquiryList.get(i).getDateOfBirthDisp();
					//String gender = studentEnquiryList.get(i).getGender();
					String sourceOfEnq = studentEnquiryList.get(i).getSourceOfEnq();
					int sourceOfEnqId = studentEnquiryList.get(i).getSourceOfEnqId();
					String doNotCall = studentEnquiryList.get(i).getDoNotCall();
					studentEnquiryTO.setName(stdName);
					studentEnquiryTO.setMobileNo(mobileNo);
					studentEnquiryTO.setEmailId(emailId);
					studentEnquiryTO.setCourseName(course);
					studentEnquiryTO.setCourseId(courseId);
					studentEnquiryTO.setEnquiryDtDisp(enqDate);
					studentEnquiryTO.setEnquiryDtDisp(dateOfBirth);
					studentEnquiryTO.setDoNotCall(doNotCall);
					//studentEnquiryTO.setGender(gender);

					cursor = handler.validateStudentId(stdId);
					if (!cursor.moveToFirst()) {
						handler.insertStdEnqDtl( locId,orgId,mobileNo, stdName,stdId,dateOfBirth, emailId, course,courseId,1, enqDate,"M",sourceOfEnq,sourceOfEnqId,doNotCall);
					}else{
						handler.updateDoNotCallFlagSyn(locId,stdId,doNotCall);
					}

				}
			}else{
					for(int i=0;i<studentEnquiryList.size();i++)
					{
						StudentEnquiryTO studentEnquiryTO=new StudentEnquiryTO();
						String stdName=studentEnquiryList.get(i).getName();
						int stdId = studentEnquiryList.get(i).getStudentId();
						String mobileNo=studentEnquiryList.get(i).getMobileNo();
						String emailId=studentEnquiryList.get(i).getEmailId();
						String course=studentEnquiryList.get(i).getCourseName();
						int courseId=studentEnquiryList.get(i).getCourseId();
						String enqDate=studentEnquiryList.get(i).getEnquiryDtDisp();
						String dateOfBirth=studentEnquiryList.get(i).getDateOfBirthDisp();
						//String gender = studentEnquiryList.get(i).getGender();
						String sourceOfEnq = studentEnquiryList.get(i).getSourceOfEnq();
						int sourceOfEnqId = studentEnquiryList.get(i).getSourceOfEnqId();
						String doNotCall = studentEnquiryList.get(i).getDoNotCall();

						studentEnquiryTO.setName(stdName);
						studentEnquiryTO.setMobileNo(mobileNo);
						studentEnquiryTO.setEmailId(emailId);
						studentEnquiryTO.setCourseName(course);
						studentEnquiryTO.setCourseId(courseId);
						studentEnquiryTO.setEnquiryDtDisp(enqDate);
						studentEnquiryTO.setEnquiryDtDisp(dateOfBirth);
						studentEnquiryTO.setDoNotCall(doNotCall);
						//studentEnquiryTO.setGender(gender);


						handler.insertStdEnqDtl( locId,orgId,mobileNo, stdName,stdId,dateOfBirth, emailId, course,courseId,1, enqDate,"M",sourceOfEnq,sourceOfEnqId,doNotCall);

					}
				}
		}
	}
	/*STUDENT ENQUIRY LIST START*/

	/*STUDENT ENQUIRY SAVE START*/

	class StudentEnqSaveAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... urls) {
			InputStream inputStream = null;
			String jsonStr;
			String resultTemp;
			int studentId=0;
			try {
				DatabaseHandler handler=new DatabaseHandler(context);
				Cursor cursor1 = handler.getUnSynDataDtl();
				while (cursor1.moveToNext()) {
					int primaryKey=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.KEY_ID));
					int locId1=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.lOC_ID));
					String name=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.STD_NAME));
					String mobileNo=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.MOBILE_NO));
					String emailId=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.E_MAIL));
					int courseId=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.COURSE_ID));
					String course=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.COURSE));
					String dob=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.DOB));
					String enquirtDt=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.ENQUIRY_DT));
					String gender=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.GENDER));
					String sourceOfEnq=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.SOURCE_OF_ENQ));
					int sourceOfEnqId=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.SOURCE_OF_ENQ_ID));
					String doNotCall=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.DO_NOT_CALL));
					if(emailId != null && !emailId.equals("")){

					}else{
						emailId = null;
					}
					if(dob != null && !dob.equals("")){

					}else{
						dob = null;
					}
					String url = "";
					url=CommonUtilities.BASE_URL+"/"+"rest"+"/"+"StudentEnquiry"+"/"+"saveStudentEnquiry"+"/"+orgId+"/"+locId1+"/"+name+"/"+mobileNo+"/"+emailId+"/"+course+"/"+courseId+"/"+enquirtDt+"/"+dob+"/"+gender+"/"+sourceOfEnqId+"/"+sourceOfEnq+"/"+doNotCall;
					url=url.replace(" ", "%20");
					String urlStatus=checkUrlStatus(url);
					//Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
					if(urlStatus.equals("Success")) {
						HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

						DefaultHttpClient client = new DefaultHttpClient();

						SchemeRegistry registry = new SchemeRegistry();
						SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
						socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
						registry.register(new Scheme("https", socketFactory, 443));
						SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
						DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

						// Set verifier
						HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
						String values[] = {url};
						// / HttpGet Method /
						HttpGet httppost = new HttpGet(url);
						// / create Apache HttpClient /
						//HttpClient httpClient = new DefaultHttpClient();
						//  / Make http request call /
						HttpResponse response = httpClient.execute(httppost);
						// StatusLine stat = response.getStatusLine();
						int status = response.getStatusLine().getStatusCode();
						System.out.println(status);
						// 200 represents HTTP OK /
						if (status == 200) {
							System.out.println("********SUCCESS**********");
							// receive response as inputStream
							inputStream = response.getEntity().getContent();
							if (inputStream != null) {
								jsonStr = convertInputStreamToString(inputStream);
								//JSONObject studentEnqResult= new JSONObject(jsonStr);
								String statusTemp = jsonStr;
								if (statusTemp != null && !statusTemp.equals("")) {
									String value[] = statusTemp.split("-");
									resultTemp = value[0];

									if (resultTemp.equals("Success")) {
										if (value[1] != null && !value[1].equals("")) {
											studentId = Integer.valueOf(value[1]);
										}
										handler.updateStudentStatus(primaryKey, studentId, "N", "");
									} else if (resultTemp.equals("Mobile No already exist")) {
										handler.updateStudentStatus(primaryKey, 0, "Y", "Mobile No already exist");
									} else { // Failure status
										handler.updateStudentStatus(primaryKey, 0, "Y", statusTemp);
									}
								} else {
									handler.updateStudentStatus(primaryKey, 0, "Y", "Error");
								}
							}
						}
					}else{
						//
					}
					//return true; // Successful
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		protected void onPostExecute(Boolean result) {

			SharedPreferences loginDtl = context.getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
			Boolean syncStatus= loginDtl.getBoolean("syncStatus",false); // Default value is false
			if(syncStatus) {
				if(progressBar != null){
					loginDtl.edit().putBoolean("syncStatus",false).commit();
					progressBar.dismiss();
				}
			}
		}
	}
	/*STUDENT ENQUIRY LIST START*/


	public String checkUrlStatus(String address){
		String status="";
	 try {
		 System.out.println(address);
		 URL url = new URL(address);
	   HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	   int responseCode = huc.getResponseCode();
	   System.out.println(responseCode);
	   if (responseCode == 200) { //200 Response OK -The request has succeeded
	        status="Success";
	     }else{
	    	status="Invalid Url";
	     }
	 }
	 catch (Exception e) {
	  System.out.println(e.getMessage());
	  }
	 return status;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

/*do not call update*/

	class StudentEnqDoNotCallAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... urls) {
			InputStream inputStream = null;
			String jsonStr;
			String resultTemp;
			int studentId=0;
			try {
				DatabaseHandler handler=new DatabaseHandler(context);
				Cursor cursor1 = handler.getUnSynDoNotCallDataDtl();
				while (cursor1.moveToNext()) {
					int primaryKey=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.KEY_ID));
					int locId1=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.lOC_ID));
					int stdId=cursor1.getInt(cursor1.getColumnIndex(StudentEnqInfoDBHelper.STUDENT_ID));
					String name=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.STD_NAME));
					String doNotCall=cursor1.getString(cursor1.getColumnIndex(StudentEnqInfoDBHelper.DO_NOT_CALL));

					String url = "";
					url=CommonUtilities.BASE_URL+"/"+"rest"+"/"+"StudentEnquiry"+"/"+"updateDoNotCallStatus"+"/"+locId1+"/"+stdId;
					url=url.replace(" ", "%20");
					String urlStatus=checkUrlStatus(url);
					//Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
					if(urlStatus.equals("Success")) {
						HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

						DefaultHttpClient client = new DefaultHttpClient();

						SchemeRegistry registry = new SchemeRegistry();
						SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
						socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
						registry.register(new Scheme("https", socketFactory, 443));
						SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
						DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

						// Set verifier
						HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
						String values[] = {url};
						// / HttpGet Method /
						HttpGet httppost = new HttpGet(url);
						// / create Apache HttpClient /
						//HttpClient httpClient = new DefaultHttpClient();
						//  / Make http request call /
						HttpResponse response = httpClient.execute(httppost);
						// StatusLine stat = response.getStatusLine();
						int status = response.getStatusLine().getStatusCode();
						System.out.println(status);
						// 200 represents HTTP OK /
						if (status == 200) {
							System.out.println("********SUCCESS**********");
							// receive response as inputStream
							inputStream = response.getEntity().getContent();
							if (inputStream != null) {
								jsonStr = convertInputStreamToString(inputStream);
								//JSONObject studentEnqResult= new JSONObject(jsonStr);
								String statusTemp = jsonStr;
								if (statusTemp != null && !statusTemp.equals("")) {

									if (statusTemp.equals("SUCCESS")) {

										handler.updateStudentStatus(primaryKey, studentId, "N", "");
									} else if (statusTemp.equals("Mobile No already exist")) {
										handler.updateStudentStatus(primaryKey, 0, "Y", "Mobile No already exist");
									} else { // Failure status
										handler.updateStudentStatus(primaryKey, 0, "Y", statusTemp);
									}
								} else {
									handler.updateStudentStatus(primaryKey, 0, "Y", "Error");
								}
							}
						}
					}else{
						//
					}
					//return true; // Successful
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		protected void onPostExecute(Boolean result) {

			SharedPreferences loginDtl = context.getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
			Boolean syncStatus= loginDtl.getBoolean("syncStatus",false); // Default value is false
			if(syncStatus) {
				if(progressBar != null){
					loginDtl.edit().putBoolean("syncStatus",false).commit();
					progressBar.dismiss();
				}
			}
		}
	}

}
