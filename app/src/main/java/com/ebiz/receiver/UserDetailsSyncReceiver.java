package com.ebiz.receiver;


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


import com.ebiz.constant.CommonUtilities;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.to.StudentEnquiryTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RANJITH KUMAR K,RAJESH V
 * This class is to Synchronize data from server automatically 
 */
public class UserDetailsSyncReceiver {

		/*extends BroadcastReceiver {

		  ArrayList<StudentEnquiryTO> stdEnqList;

		  JSONObject json_validation;
		  Context context;
		  DatabaseHandler handler;
		  Cursor cursor,cursor_sales;
		  SharedPreferences preferences;
		  CommonUtilities commonUtilities;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try
		{
		preferences=context.getSharedPreferences(CommonUtilities.PREFERENCE_KEY, 0);

		//String sales_username=preferences.getString("UserNameKey", null);
		//Toast.makeText(context, "Username : "+username, Toast.LENGTH_LONG).show();
		 stdEnqList = new ArrayList<StudentEnquiryTO>();

		 this.context=context;
		         ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
	             NetworkInfo mobileDataNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	             NetworkInfo wifiDataNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	             boolean isConnected =((mobileDataNetInfo != null||wifiDataNetInfo != null) && (mobileDataNetInfo.isConnectedOrConnecting()||wifiDataNetInfo.isConnectedOrConnecting()));   
	             if (isConnected)
	             {
	            	 
	            //	 Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();

		            	 // LOADING USER DETAILS FROM SERVER START
		            	 String userListURL = CommonUtilities.BASE_URL+"/userListJSON.jsp";
		   	             System.out.println("00000000000000"+userListURL);
		   	             new UserAsyncTask().execute(userListURL);
		   	             
		            	 // LOADING USER DETAILS FROM SERVER END
		   	             
		   	            
		    			 if (android.os.Build.VERSION.SDK_INT > 9) {
		    				 StrictMode.ThreadPolicy policy = new
		    				 StrictMode.ThreadPolicy.Builder().permitAll().build();
		    				 StrictMode.setThreadPolicy(policy);  
		    				 handler=new DatabaseHandler(context);
		    				 System.out.println("entering the data()()()()()()");
		    				 //UPDATING SERVICE REQUEST DETAILS TO SERVER START
		    				 cursor=handler.getServiceRequestDataToUpdate();
			            	 if(cursor.moveToFirst())
			            	 {
			            		 do
			            		 {
			            			 int id=cursor.getInt(0);
			            			String requestId=cursor.getString(1);
			            			String status=cursor.getString(9);
			            			String expected_date_time=cursor.getString(16);
			            			String ack_date_time=cursor.getString(17);
			            			String resp_date_time=cursor.getString(18);
			            			String res_date_time=cursor.getString(19);
			            			String closed_date_time=cursor.getString(23);
			            			String rescheduledReason=cursor.getString(24);
			            			
			            			System.out.println("testing.....++++++"+closed_date_time);
			            				
			            			 json_validation=new UserFunctions().updateServiceRequestTable(requestId, status,expected_date_time, ack_date_time, resp_date_time, res_date_time,closed_date_time,rescheduledReason);
			            			 handler.updateServiceRequestSync(id);
			            		 }
			            		 while(cursor.moveToNext());
			            	 }
			            	//UPDATING SERVICE REQUEST DETAILS TO SERVER END
			            	 
			            	//INSERTING SERVICE REQUEST ACTIVITY DETAILS TO SERVER START		            	 
			            	 cursor=handler.getServiceRequestActivityData();
			            	 if(cursor.moveToFirst())
			            	 {
			            		 do
			            		 {
			            			 int id=cursor.getInt(0);
			            			String requestId=cursor.getString(1);
			            			String username=cursor.getString(2);
			            			String ticket_no=cursor.getString(3);
			            			String service_report_no=cursor.getString(4);
			            			String status=cursor.getString(5);
			            			String activity_date_time=cursor.getString(6);		
			            			String remarks=cursor.getString(7);
			            			String arrival_time=cursor.getString(8);
			            			String dept_time=cursor.getString(9);
			            			 json_validation=new UserFunctions().insertServiceRequestActivityTable(requestId, username, ticket_no, service_report_no, status, remarks, arrival_time, dept_time, activity_date_time);
			            			 handler.updateServiceRequestActivitySync(id);
			            		 }
			            		 while(cursor.moveToNext());
			            	 }
			            	//INSERTING SERVICE REQUEST ACTIVITY DETAILS TO SERVER END
			            	 
			            	//INSERTING SALES COLD CALL ACTIVITY DETAILS TO SERVER START		            	 
			            	cursor_sales=handler.getSalesColdCall();
			            	 if(cursor_sales.moveToFirst())
			            	 {
			            		 do
			            		 {
			            			            			 
			            			 List<NameValuePair> params = new ArrayList<NameValuePair>();
			            			 	params.add(new BasicNameValuePair("request_id", String.valueOf(cursor_sales.getInt(0))));
			            			 	params.add(new BasicNameValuePair("sales_person", cursor_sales.getString(1)));
			            				params.add(new BasicNameValuePair("customer_name", cursor_sales.getString(2)));
			            				params.add(new BasicNameValuePair("hospital_name", cursor_sales.getString(3)));
			            				params.add(new BasicNameValuePair("address",cursor_sales.getString(4) ));
			            				params.add(new BasicNameValuePair("city", cursor_sales.getString(5)));
			            				params.add(new BasicNameValuePair("telephone_no", cursor_sales.getString(6)));
			            				params.add(new BasicNameValuePair("district", cursor_sales.getString(7)));
			            				params.add(new BasicNameValuePair("pincode", cursor_sales.getString(8)));
			            				params.add(new BasicNameValuePair("date_visited", cursor_sales.getString(9)));
			            				params.add(new BasicNameValuePair("company", cursor_sales.getString(10)));
			            				params.add(new BasicNameValuePair("modality", cursor_sales.getString(11)));
			            				params.add(new BasicNameValuePair("equipment",cursor_sales.getString(12) ));
			            				params.add(new BasicNameValuePair("purpose_of_visit", cursor_sales.getString(13)));
			            				params.add(new BasicNameValuePair("telephone_no", cursor_sales.getString(14)));
			            				params.add(new BasicNameValuePair("existing_mechine", cursor_sales.getString(15)));
			            				params.add(new BasicNameValuePair("existing_mechine_model_name", cursor_sales.getString(16)));
			            				params.add(new BasicNameValuePair("specialityValue", cursor_sales.getString(17)));
			            				params.add(new BasicNameValuePair("categoryValue", cursor_sales.getString(18)));
			            				params.add(new BasicNameValuePair("contactPerson", cursor_sales.getString(19)));
			            				params.add(new BasicNameValuePair("followUpFlag", cursor_sales.getString(21)));
			            				params.add(new BasicNameValuePair("actionFlag", cursor_sales.getString(22)));
			            				params.add(new BasicNameValuePair("mobileSerialNo", cursor_sales.getString(23)));
			            				json_validation=new UserFunctions().insertSalesColdCallActivityTable(params);
			            			handler.updateSalesColdCallSyncStatus(cursor_sales.getInt(0));
			            		 }
			            		 while(cursor_sales.moveToNext());
			            	 }
			            	//INSERTING SALES COLD CALL ACTIVITY DETAILS TO SERVER START	
			            	 
			            	 
			            	 cursor_sales=handler.getSalesfollowupCall();
			            	 if(cursor_sales.moveToFirst())
			            	 {
			            		 do
			            		 {
			            				
			            			            			 
			            			 List<NameValuePair> params = new ArrayList<NameValuePair>();
			            			 
			            			 	params.add(new BasicNameValuePair("customer_name", cursor_sales.getString(1)));
			            				params.add(new BasicNameValuePair("contact_person", cursor_sales.getString(2)));
			            				params.add(new BasicNameValuePair("contact_no", cursor_sales.getString(3)));
			            				params.add(new BasicNameValuePair("qty",cursor_sales.getString(4) ));
			            				params.add(new BasicNameValuePair("funnel_value", cursor_sales.getString(5)));
			            				params.add(new BasicNameValuePair("project_progress_percentage", cursor_sales.getString(6)));
			            				params.add(new BasicNameValuePair("probability_percentage", cursor_sales.getString(7)));
			            				params.add(new BasicNameValuePair("CLOSE_MONTH", cursor_sales.getString(8)));
			            				params.add(new BasicNameValuePair("ORDER_LOST_TO", cursor_sales.getString(9)));
			            				params.add(new BasicNameValuePair("COMPETITOR_PRODUCT", cursor_sales.getString(10)));
			            				params.add(new BasicNameValuePair("REASON_LOST", cursor_sales.getString(11)));
			            				params.add(new BasicNameValuePair("SYNC_STATUS", cursor_sales.getString(12)));
			            				params.add(new BasicNameValuePair("sales_person", cursor_sales.getString(13)));
			            				params.add(new BasicNameValuePair("order_lost_flag", cursor_sales.getString(14)));
			            				params.add(new BasicNameValuePair("mobile_serial_no", cursor_sales.getString(15)));
			            				params.add(new BasicNameValuePair("ORDER_TYPE", cursor_sales.getString(16)));
			            				params.add(new BasicNameValuePair("PRICE", cursor_sales.getString(17)));
			            				params.add(new BasicNameValuePair("APPROXIMATE_TIME", cursor_sales.getString(18)));
			            				params.add(new BasicNameValuePair("modality", cursor_sales.getString(19)));
			            				params.add(new BasicNameValuePair("equipment", cursor_sales.getString(20)));
			            				params.add(new BasicNameValuePair("company", cursor_sales.getString(21)));
			            				
			            				System.out.println("TTTTTTTTTTTTTTTTTT"+params);
			            				json_validation=new UserFunctions().insertSalesFollowUpActivityTable(params);
			            			handler.updateSalesFollowUpSyncStatus(cursor_sales.getInt(0));
			            		 }
			            		 while(cursor_sales.moveToNext());
			            	 }
			            	 
			            	 cursor_sales.close();
		    			 }
		    			 
		    			 
		    			 
		    			 
		    			 
		    			 //it is used to load service request data from server
		    			 String username=preferences.getString("UserNameKey", null);
		    			 if(username!=null)
		    			 {
		    				 System.out.println("*************"+username+"*************");
		    				String value[] = {username};
		    				new ServiceRequestAsyncTask().execute(value);
		    				new ActionFlagAsyncTask().execute(value);
			            	
		    			 }
	             }
	             else
	             {
	            	// Toast.makeText(context, "Not Connected", Toast.LENGTH_LONG).show();
	             }
	             
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
				 
	}
	
	//The UserAsyncTask class is used to load the user data such as username, password, user_id from server
	class UserAsyncTask extends AsyncTask<String, Void, Boolean> {
		
		
		@Override
		protected Boolean doInBackground(String... urls) {
			try {
				
				//------------------>>
				// HttpGet Method 
				HttpGet httppost = new HttpGet(urls[0]);
				
				// create Apache HttpClient 
				HttpClient httpclient = new DefaultHttpClient();
				
				//   Make http request call 
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();
				System.out.println(status);
				//  200 represents HTTP OK 
				if (status == 200) {
					
				//	 receive response as inputStream 
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					
				//this json object is used to retrieve json array
					json_validation = new JSONObject(data);
					System.out.println("1111111111"+data);
					
					JSONArray jarray = json_validation.getJSONArray("userList");
					System.out.println("22222222222222"+jarray);
					for (int i = 0; i < jarray.length(); i++) {
						//this jsonobject is used to retrieve value from json array
							JSONObject object = jarray.getJSONObject(i);
						
							UserInfoTO userInfoTO=new UserInfoTO();
							userInfoTO.setUserID(object.getInt("uid"));
							userInfoTO.setUsername(object.getString("user_id"));
							userInfoTO.setPassword(object.getString("pwd"));
							userInfoTO.setGroupId(object.getInt("group_id"));
							userInfoTO.setGroupName(object.getString("group_name"));
							userInfoTO.setDistrict(object.getString("district"));
							
							userList.add(userInfoTO);
							
						}
					return true; // Successful
				}
				
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result) {

			
			DatabaseHandler handler=new DatabaseHandler(context);
			Cursor c=handler.getallUserData();
			if(c.getCount()!=0)
			{
				for(int i=0;i<userList.size();i++)
				{
					int uid=userList.get(i).getUserID();
					String user=userList.get(i).getUsername();
					String pass=userList.get(i).getPassword();
					String group_id=String.valueOf(userList.get(i).getGroupId());
					String group_name=userList.get(i).getGroupName();
					String district=userList.get(i).getDistrict();
					c=handler.getUserData(uid);
					if(c.moveToFirst())
					{
						handler.updateUserData(uid, user, pass,group_id,group_name,district);
					}
					else
					{
						handler.insertUserData(uid,user, pass,group_id,group_name,district);
					}
				
				}
			}
			else
			{
				for(int i=0;i<userList.size();i++)
				{
					int uid=userList.get(i).getUserID();
					String user=userList.get(i).getUsername();
					String pass=userList.get(i).getPassword();
					String group_id=String.valueOf(userList.get(i).getGroupId());
					String group_name=userList.get(i).getGroupName();
					String district=userList.get(i).getDistrict();
					handler.insertUserData(uid,user, pass,group_id,group_name,district);
				}
			}	
							
				//json_validation = new UserFunctions().updateUserInfoTable(String.valueOf(uid));
			
		//	Toast.makeText(context,"Data Loaded Successfully.",Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
	//The ServiceReqeustAsyncTask is used to load the service request details from server
public class ServiceRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {

				json_validation = new UserFunctions().getServiceRequest(params[0]);
				
				System.out.println("1111111111" + json_validation);

				JSONArray jarray = json_validation.getJSONArray("serviceList");
				System.out.println("22222222222222" + jarray);
				for (int i = 0; i < jarray.length(); i++) {
					// this jsonobject is used to retrieve value from json array
					JSONObject object = jarray.getJSONObject(i);

					ServiceTO serviceTO = new ServiceTO();
					serviceTO.setRequestId(object.getString("request_id"));
					serviceTO.setCustomerName(object.getString("customer_name"));
					serviceTO.setPhoneNo(object.getString("contact_no"));
					serviceTO.setEquipmentId(object.getString("eid"));
					serviceTO.setEquipmentName(object.getString("equip_name"));
					serviceTO.setEquipmentSlNo(object.getString("equip_sl_no"));
					serviceTO.setContractType(object.getString("contract_type"));
					serviceTO.setProblem(object.getString("problem"));
					serviceTO.setStatus(object.getString("status"));
					serviceTO.setCreatedDateTime(object.getString("created_dt"));
					serviceTO.setSrType(object.getString("sr_type"));
					serviceTO.setEngineerName(object.getString("eng_name"));
					serviceTO.setGroupId(object.getString("groupID"));
					if(Integer.parseInt(object.getString("groupID"))==9){
						serviceTO.setSrNo(object.getString("sr_no"));
						serviceTO.setResolvedDateTime(object.getString("sr_activity_dt"));
						serviceTO.setTicketNo(object.getString("ticket_no"));
						serviceTO.setRemarks(object.getString("remarks"));
					}
					
					serviceList.add(serviceTO);

				}
				return true; // Successful
			
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

protected void onPostExecute(Boolean result) {

			
			DatabaseHandler handler = new DatabaseHandler(context);
			Cursor c = handler.getAllServiceRequestData();
			if (c.getCount() != 0) {
				for (int i = 0; i < serviceList.size(); i++) {
					String requestId= serviceList.get(i).getRequestId();
					String customerName= serviceList.get(i).getCustomerName();
					String phoneNo= serviceList.get(i).getPhoneNo();
					String equipmentId= serviceList.get(i).getEquipmentId();
					String equipmentName= serviceList.get(i).getEquipmentName();
					String equipmentSlNo= serviceList.get(i).getEquipmentSlNo();
					String contractType= serviceList.get(i).getContractType();
					String problem= serviceList.get(i).getProblem();
					String status= serviceList.get(i).getStatus();
					String created_dt= serviceList.get(i).getCreatedDateTime();
					String sr_Type=serviceList.get(i).getSrType();
					String eng_name=serviceList.get(i).getEngineerName();
					String groupId=serviceList.get(i).getGroupId();
					if(Integer.parseInt(groupId)==3){
						c = handler.getServiceRequestData(requestId);
						if (c.moveToFirst()) {
							//handler.updateUserData(uid, user, pass);
							handler.updateServiceRequestData(requestId, customerName, phoneNo, equipmentId, equipmentName, equipmentSlNo, contractType, problem, status, created_dt,sr_Type,eng_name);
						} else {
							handler.insertServiceRequestData(requestId, customerName, phoneNo, equipmentId, equipmentName, equipmentSlNo, contractType, problem, status, created_dt,sr_Type,eng_name);
							
						}
					}
					if(Integer.parseInt(groupId)==9){
						String srNo=serviceList.get(i).getSrNo();
						String resDateTime=serviceList.get(i).getResolvedDateTime();
						String tickNo=serviceList.get(i).getTicketNo();
						String remarks=serviceList.get(i).getRemarks();
					
						c = handler.getServiceRequestData(requestId);
						if (c.moveToFirst()) {
							String sync_flag=c.getString(c.getColumnIndex("update_admin_flag"));
							//handler.updateUserData(uid, user, pass);
							handler.updateAdminServiceRequestData(requestId, customerName, phoneNo, equipmentId, equipmentName, equipmentSlNo, contractType, problem, status, created_dt,sr_Type,eng_name,srNo,tickNo,resDateTime,remarks,sync_flag);
						} else {
							handler.insertAdminServiceRequestData(requestId, customerName, phoneNo, equipmentId, equipmentName, equipmentSlNo, contractType, problem, status, created_dt,sr_Type,eng_name,srNo,tickNo,resDateTime,remarks);
							
						}
					}
				

				}
			} else {
				for (int i = 0; i < serviceList.size(); i++) {

					String requestId= serviceList.get(i).getRequestId();
					String customerName= serviceList.get(i).getCustomerName();
					String phoneNo= serviceList.get(i).getPhoneNo();
					String equipmentId= serviceList.get(i).getEquipmentId();
					String equipmentName= serviceList.get(i).getEquipmentName();
					String equipmentSlNo= serviceList.get(i).getEquipmentSlNo();
					String contractType= serviceList.get(i).getContractType();
					String problem= serviceList.get(i).getProblem();
					String status= serviceList.get(i).getStatus();
					String created_dt= serviceList.get(i).getCreatedDateTime();
					String sr_Type=serviceList.get(i).getSrType();
					String eng_name=serviceList.get(i).getEngineerName();
					String groupId=serviceList.get(i).getGroupId();
					if(Integer.parseInt(groupId)==3){
						handler.insertServiceRequestData(requestId, customerName, phoneNo, equipmentId, equipmentName, equipmentSlNo, contractType, problem, status, created_dt,sr_Type,eng_name);
					}
					if(Integer.parseInt(groupId)==9){
						String srNo=serviceList.get(i).getSrNo();
						String resDateTime=serviceList.get(i).getResolvedDateTime();
						String tickNo=serviceList.get(i).getTicketNo();
						String remarks=serviceList.get(i).getRemarks();
						handler.insertAdminServiceRequestData(requestId, customerName, phoneNo, equipmentId, equipmentName, equipmentSlNo, contractType, problem, status, created_dt,sr_Type,eng_name,srNo,tickNo,resDateTime,remarks);
						
					}
				}
			}
			//Toast.makeText(context,"Data Loaded Successfully", Toast.LENGTH_SHORT).show();

		}

	}

public class ActionFlagAsyncTask extends AsyncTask<String, Void, Boolean> {

	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub

		try {

			json_validation = new UserFunctions().getActionFlag(params[0]);
			
			System.out.println("1111111111" + json_validation);
try{
			JSONArray jarray = json_validation.getJSONArray("followUpActionFlag");
			System.out.println("22222222222222" + jarray);
			if(jarray!=null){
				for (int i = 0; i < jarray.length(); i++) {
					// this jsonobject is used to retrieve value from json array
					JSONObject object = jarray.getJSONObject(i);

					ActionFlagInfoTO actionFlagTO = new ActionFlagInfoTO();
					actionFlagTO.setContactPerson(object.getString("CONTACT_PERSON"));
					actionFlagTO.setCustomerName(object.getString("SALES_CUST_NAME"));
					actionFlagTO.setSalesPerson(object.getString("SALES_PERSON"));
					actionFlagTO.setActionFlag(object.getString("ACTION_FLAG"));
					actionFlagTO.setContactNo(object.getString("TEL_NO"));
					actionFlagTO.setMobileSerialNo(object.getString("MOBILE_CUST_SEQ_NO"));
					
					actionflagList.add(actionFlagTO);
			}
			
			
			}
}
catch(NullPointerException e){
	System.out.println(e);
}
			return true; // Successful
		
	} catch (ParseException e) {
		e.printStackTrace();
	}catch (JSONException e) {
		e.printStackTrace();
	}
	return false;
}

protected void onPostExecute(Boolean result) {

		
		DatabaseHandler handler = new DatabaseHandler(context);
		try{
			
		
			for (int i = 0; i < actionflagList.size(); i++) {
				String salesPerson= actionflagList.get(i).getSalesPerson();
				String customerName= actionflagList.get(i).getCustomerName();
				String contactPerson= actionflagList.get(i).getContactPerson();
				String contactNo= actionflagList.get(i).getContactNo();
				String actionFlag= actionflagList.get(i).getActionFlag();
				String mobileSerialNo= actionflagList.get(i).getMobileSerialNo();
				 handler.updateActionFlagData(salesPerson,customerName,contactPerson,contactNo,actionFlag,mobileSerialNo);
			
		} 
		}catch(Exception e){
			System.out.println(e);
		}
	}


}
*/
}
