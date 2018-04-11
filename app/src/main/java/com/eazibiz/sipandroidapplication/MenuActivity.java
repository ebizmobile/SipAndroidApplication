package com.eazibiz.sipandroidapplication;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ebiz.constant.CommonUtilities;
import com.ebiz.constant.MasterConstants;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.json.UserFunctions;
import com.ebiz.receiver.InitialSyncReceiver;
import com.ebiz.to.StudentEnquiryTO;
import com.ebiz.to.StudentInfoTO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


public class MenuActivity  extends AppCompatActivity {

	DrawerLayout drawerLayout;
	ActionBarDrawerToggle drawerToggle;
	NavigationView navigation;
	AlertDialog loginWarrningMsg;
	private InputStream inputStream = null;
	private ProgressDialog progressBar;
	private ProgressDialog progressBarRecep;
	JSONObject stdInfo_json;
	SQLiteDatabase db;
	private DatabaseHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		progressBar = new ProgressDialog(MenuActivity.this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Loading...");
		progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		handler=new DatabaseHandler(getBaseContext());
		//System.out.print("Checking++++++" );
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerToggle = new ActionBarDrawerToggle(MenuActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
		drawerLayout.setDrawerListener(drawerToggle);

		navigation = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout.openDrawer(Gravity.START);
		//initInstances();


		navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.navigation_item_1:
						Intent i = new Intent(getApplicationContext(),StudentEnquiryActivity.class);
						startActivity(i);
						finish();
						break;
					case R.id.navigation_item_2:
						Intent l = new Intent(getApplicationContext(),EnqListActivity.class);
						startActivity(l);
						finish();
						break;
					case R.id.std_receipt:
						progressBarRecep = new ProgressDialog(MenuActivity.this);
						progressBarRecep.setCancelable(true);
						progressBarRecep.setMessage("Loading...");
						progressBarRecep.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
						progressBarRecep.setMax(100);
						progressBarRecep.setProgress(0);
						progressBarRecep.show();

						SharedPreferences.Editor setStdInfo = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE).edit();
						setStdInfo.putString("studName", "empty");
						setStdInfo.putString("studId", "empty");
						setStdInfo.putString("mobileNo", "empty");
						setStdInfo.putBoolean("isOpen", false);
						setStdInfo.commit();
						ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
								connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


						handler.clearTableStudentInfo();
							boolean isGotInfo = false;
							try {
								String [] a={ };
								 new MenuActivity.getStudentDetailAsAsync().execute(a);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}else{
							progressBarRecep.dismiss();
							AlertDialog internetWarrningMsg = new AlertDialog.Builder(MenuActivity.this).create();
							internetWarrningMsg.setTitle("Warning!");
							internetWarrningMsg.setMessage("Kindly check your internet connection");
							internetWarrningMsg.setButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

								}
							});
							// Showing Alert Message
							internetWarrningMsg.show();
						}
						break;
					case R.id.synchronization_data:
						progressBar = new ProgressDialog(MenuActivity.this);
						progressBar.setCancelable(true);
						progressBar.setMessage("Synchronizing...");
						progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
						progressBar.setProgress(0);
						progressBar.setMax(100);
						progressBar.show();

						/*loginWarrningMsg = new AlertDialog.Builder(MenuActivity.this).create();
						loginWarrningMsg.setTitle("Loading..");
						loginWarrningMsg.setMessage("Please wait !! Synchronizing ...");
						loginWarrningMsg.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								loginWarrningMsg.dismiss();
							}
						});
						loginWarrningMsg.show();*/
						SharedPreferences.Editor sysDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE).edit();
						sysDtl.putBoolean("syncStatus", true);
						sysDtl.commit();
						IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
						final InitialSyncReceiver rc = new InitialSyncReceiver();
						registerReceiver(rc,filter);

						Thread thread=new Thread()
						{
							public void run() {
								try
								{
									sleep(500000);
									unregisterReceiver(rc);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
						};
						thread.start();
						progressBar.dismiss();
						break;
					case R.id.logout:
						SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
						loginDtl.edit().remove("centerCode").commit();
						loginDtl.edit().remove("userName").commit();
						loginDtl.edit().remove("password").commit();
						loginDtl.edit().remove("location_id").commit();
						loginDtl.edit().remove("status").commit();
						Intent k = new Intent(getApplicationContext(),MainActivity.class);
						startActivity(k);
						finish();
						break;
				}
				return false;
			}
		});

		if(MainActivity.i == 1){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					initInstance();
				}
			},4000);

		}else{
			progressBar.dismiss();
		}
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	public void initInstance(){
		//do something
		//Validate User current version to be matched to released version
		ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE );
		NetworkInfo mobileDataNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiDataNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isConnected =((mobileDataNetInfo != null||wifiDataNetInfo != null) && (mobileDataNetInfo.isConnectedOrConnecting()||wifiDataNetInfo.isConnectedOrConnecting()));
		if (isConnected){


			String a[]={};
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			new validateVersionAsyncTask().execute(a);
		}else{
			progressBar.dismiss();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation_view_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item))

			return true;

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		//if (id == R.id.action_settings) {
		//	return true;
		//}
		//Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
		return super.onOptionsItemSelected(item);
	}

	class validateVersionAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... urls) {
			Boolean status=false;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
						AlertDialog versionUpdateMsg = new AlertDialog.Builder(MenuActivity.this).create();
						try {
							PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
							String version = pInfo.versionName;
							int verCode = pInfo.versionCode;
							String url = "";
							url = CommonUtilities.BASE_URL + "/" + "rest" + "/" + "MobileVersion" + "/" + "lastReleasedVersion" + "/" + "Android";
							url = url.replace(" ", "%20");
							//JSONObject jsonobject = new  JSONParser().getJSONFromUrl(url);
						//	if (urlStatus.equals("Success")) {

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
								//System.out.println(url);
								// / HttpGet Method /
								HttpGet httppost = new HttpGet(url);
								// / create Apache HttpClient /
								//HttpClient httpClient = new DefaultHttpClient();
								//  / Make http request call /
								HttpResponse response = httpClient.execute(httppost);
								// StatusLine stat = response.getStatusLine();
								int responseStatus = response.getStatusLine().getStatusCode();
								// 200 represents HTTP OK /
								//System.out.println(responseStatus);
								//System.out.println("********SUCCESS1111**********");
								if (responseStatus == 200) {
									//System.out.println("********SUCCESS**********");
									// receive response as inputStream
									inputStream = response.getEntity().getContent();
									if (inputStream != null) {
										String jsonStr = convertInputStreamToString(inputStream);
										//System.out.println("********jsonStr**********" + jsonStr);
										int lastReleaseVersion = Integer.valueOf(jsonStr);
										if (lastReleaseVersion != verCode) {
											versionUpdateMsg.setTitle("Alert");
											versionUpdateMsg.setMessage("New version available for your mobile.");
											versionUpdateMsg.setButton("Update Now", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													//Toast.makeText(getApplicationContext(), "clicked //////////", Toast.LENGTH_SHORT).show();
													SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
													loginDtl.edit().remove("centerCode").commit();
													loginDtl.edit().remove("userName").commit();
													loginDtl.edit().remove("password").commit();
													loginDtl.edit().remove("location_id").commit();
													loginDtl.edit().remove("status").commit();
													Toast.makeText(getApplicationContext(), "Updating..", Toast.LENGTH_SHORT).show();
													final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
													//Toast.makeText(getApplicationContext(), appPackageName, Toast.LENGTH_SHORT).show();
													try {
														startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
													} catch (android.content.ActivityNotFoundException anfe) {
														startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
													}
												}
											});
											// Showing Alert Message
											versionUpdateMsg.show();
										}
									}
								} else {
									versionUpdateMsg.setTitle("Failure !!");
									versionUpdateMsg.setMessage("Network Busy !! Kindly try some times !!");
									versionUpdateMsg.setButton("OK", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {

										}
									});
									// Showing Alert Message
									versionUpdateMsg.show();
								}
							//}
						} catch (Exception e) {
							versionUpdateMsg.setTitle("Failure !!");
							versionUpdateMsg.setMessage("Network Busy !! Kindly try some times !!");
							versionUpdateMsg.setButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {

								}
							});
							// Showing Alert Message
							versionUpdateMsg.show();
							e.printStackTrace();
						}
					}

			});
			return status;
		}

		protected void onPostExecute(Boolean result) {
			progressBar.dismiss();
			MainActivity.i =2;
		}
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public void initInstances(){

	}

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

	class getStudentDetailAsAsync extends AsyncTask<String, Void, Boolean> {
		@Override

		protected Boolean doInBackground(String... urls) {
			try {
				UserFunctions userInfo = new UserFunctions();
				SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
				int locId = loginDtl.getInt("location_id", 0);
				stdInfo_json = userInfo.getSudentNameDetails(locId);
				JSONArray list = stdInfo_json.getJSONArray("studentList");
				for (int j = 0; j < list.length(); j++) {
					JSONObject object = list.getJSONObject(j);

					StudentInfoTO studentInfoTO = new StudentInfoTO();
					studentInfoTO.setStudentName(object.getString("studentName"));
					studentInfoTO.setStudentId(object.getInt("studentId"));
					studentInfoTO.setLocId(object.getInt("locId"));
					studentInfoTO.setMobileNo(object.getString("mobileNo"));
					handler.insertStudentInfoDtl(studentInfoTO);
				}
				return true;
			}catch(JSONException e){
				return false;
			}
		}
		protected void onPostExecute(Boolean result) {
			progressBarRecep.dismiss();
			Intent K = new Intent(getApplicationContext(), ReceiptActivity.class);
			startActivity(K);
			finish();
		}


	}
}