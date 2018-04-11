package com.ebiz.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.ebiz.constant.CommonUtilities;

/**
 * @author 
 */
public class UserFunctions {

	private JSONParser jsonParser;

	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	
	/**
	 * This method is used to get all the service requests based on username
	 * @param username the username to get service request data
	 * @return service request data in JSON form
	 */
	public JSONObject getServiceRequest(String username) {

		String checking = CommonUtilities.BASE_URL+"/serviceRequestJSON.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", username));
		
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}

	/**
	 * This method is used to update the mobile_sync_status field of CALLLOG.SERVICE_REQUEST table in server 
	 * @param requestId the sr_no of CALLLOG.SERVICE_REQUEST table to be affected in server
	 * @return result data in JSON form
	 */
	public JSONObject updateServiceRequestSyncStatus(String requestId) {

		String checking = CommonUtilities.BASE_URL+"/updateServiceRequestSyncStatus.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sr_no", requestId));
		
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}
	
	public JSONObject updateAdminServiceRequestSyncStatus(String requestId) {

		String checking = CommonUtilities.BASE_URL+"/updateAdminServiceRequestSyncStatus.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sr_no", requestId));
		
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}
	
/**
 * 	This method is used to update the status, acknowledgment, responded and requested date & time fields of CALLLOG.SERVICE_REQUEST table in server 
 * @param requestId the sr_no of CALLLOG.SERVICE_REQUEST table to be affected in server
 * @param status the statuof service request to be updated on CALLLOG.SERVICE_REQUEST table in server
 * @param ack_date_time the acknowledgment date and time of service request to be updated on CALLLOG.SERVICE_REQUEST table in server
 * @param resp_date_time the responded date and time of service request  to be updated on CALLLOG.SERVICE_REQUEST table in server
 * @param res_date_time the resolved date and time of service request to be updated on CALLLOG.SERVICE_REQUEST table in server
 * @return the results data in JSON form
 */
	public JSONObject updateServiceRequestTable(String requestId,String status,String expected_date_time,String ack_date_time,String resp_date_time,String res_date_time,String close_date_time,String rescheduledReason) {
	String checking = CommonUtilities.BASE_URL+"/updateSrRequestDataToServer.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request_id", requestId));
		params.add(new BasicNameValuePair("status",status ));
		params.add(new BasicNameValuePair("expected_date_time", expected_date_time));
		params.add(new BasicNameValuePair("ack_date_time", ack_date_time));
		params.add(new BasicNameValuePair("resp_date_time", resp_date_time));
		params.add(new BasicNameValuePair("res_date_time", res_date_time));
		params.add(new BasicNameValuePair("close_date_time", close_date_time));
		params.add(new BasicNameValuePair("rescheduledReason", rescheduledReason));
		
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);
		
		return json;
	}
	
/**
 *  This method used to insert service request activity details to server on CALLLOG.SERVICE_REQUEST_ACTIVITY table on server
 * @return the results data in JSON form
 */
	public JSONObject insertServiceRequestActivityTable(String requestId,String username,String ticket_no,String service_report_no,String status,String remarks,String arrival_time,String dept_time,String activity_date_time) {
		String checking = CommonUtilities.BASE_URL+"/insertSrReqActivityToServer.jsp";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request_id", requestId));
			params.add(new BasicNameValuePair("engineer_name", username));
			params.add(new BasicNameValuePair("activity_date_time", activity_date_time));
			params.add(new BasicNameValuePair("status",status ));
			params.add(new BasicNameValuePair("ticket_no", ticket_no));
			params.add(new BasicNameValuePair("service_report_no", service_report_no));
			params.add(new BasicNameValuePair("remarks", remarks));
			params.add(new BasicNameValuePair("arrival_time", arrival_time));
			params.add(new BasicNameValuePair("departure_time", dept_time));

			JSONObject json = JSONParser.getJSONFromUrl(checking, params);
			
			return json;
		}
	
	public JSONObject insertSalesColdCallActivityTable(List<NameValuePair> params) {
		String checking = CommonUtilities.BASE_URL+"/insertSalestColdCallToServer.jsp";
			JSONObject json = JSONParser.getJSONFromUrl(checking, params);
			System.out.println(params);
			
			return json;
		}
	
	public JSONObject insertSalesFollowUpActivityTable(List<NameValuePair> params) {
		String checking = CommonUtilities.BASE_URL+"/insertSalestFollowUpToServer.jsp";
			JSONObject json = JSONParser.getJSONFromUrl(checking, params);
			System.out.println(params);
			
			return json;
		}
	
	public JSONObject getActionFlag(String username) {

		String checking = CommonUtilities.BASE_URL+"/actionFlagJSON.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", username));
		
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}
		
	public JSONObject updateActionFLagSyncStatus(String salesPerson, String customerName,String contactPerson,String contactNo,String mobileSerialNo) {

		String checking = CommonUtilities.BASE_URL+"/updateActionFLagSyncStatus.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("salesPerson", salesPerson));
		params.add(new BasicNameValuePair("customerName", customerName));
		params.add(new BasicNameValuePair("contactPerson", contactPerson));
		params.add(new BasicNameValuePair("contactNo", contactNo));
		params.add(new BasicNameValuePair("mobileSerialNo", mobileSerialNo));
		
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}
	
	
	public JSONObject getUserDetails(String centercode,String username,String password) {

		String checking = CommonUtilities.BASE_URL+"/userLoginJSON.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("centercode", centercode));
		params.add(new BasicNameValuePair("Username", username));
		params.add(new BasicNameValuePair("Password", password));
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}
	public JSONObject getStudentEnqListDtl(String locationId) {

		String checking = CommonUtilities.BASE_URL+"/stduentEnqListJson.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("locationId", locationId));
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);

		return json;
	}

	public JSONObject getCourseDetails(String locationId) {

		String checking = CommonUtilities.BASE_URL+"/getCourseDetails.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		System.out.println("locationId---"+locationId);
		params.add(new BasicNameValuePair("locationId", locationId));
		JSONObject json = JSONParser.getJSONFromUrl(checking, params);
		return json;
	}

	public JSONObject getSudentNameDetails(int locationId) {

		//String checkingUrl = CommonUtilities.TEMP_URL+"/rest/StudentInfoJason/getActiveStudentList/" + locationId;
		String checkingUrl = CommonUtilities.BASE_URL+"/rest/StudentInfoJason/getActiveStudentList/" + locationId;
		//JSONObject json = JSONParser.getJSONFromUrlRest(checkingUrl.toString());
		JSONObject json = JSONParser.getJSONFromUrl(checkingUrl.toString());
		return json;
	}
}
