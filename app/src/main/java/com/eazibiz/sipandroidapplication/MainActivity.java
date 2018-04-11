package com.eazibiz.sipandroidapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ebiz.constant.CommonUtilities;
import com.ebiz.constant.MasterConstants;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.json.JSONParser;
import com.ebiz.json.UserFunctions;
import com.ebiz.receiver.InitialSyncReceiver;
import com.ebiz.to.StudentEnquiryTO;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    JSONObject json_validation;
    JSONObject loginDtl_json;
    JSONObject sourceOfEnq_json;
    EditText   centercode;
    EditText username;
    EditText   password;
    ProgressBar spinner;
    Button loginaction;
    private ProgressDialog progressBar;
    private String mainActivityStatus="";
    private DatabaseHandler handler;

    private PendingIntent pendingIntent;
    private AlarmManager manager;
    int interval = 1000*60*5;
    public static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
        Boolean alreadyLogin= loginDtl.getBoolean("status",false); // Default value is false
        i=1;
        if(alreadyLogin){
            Intent i = new Intent(getApplicationContext(),MenuActivity.class);
            startActivity(i);
            finish();

            Intent alarmIntent = new Intent(this, InitialSyncReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        }else{
            handler=new DatabaseHandler(getBaseContext());
            centercode=(EditText)findViewById(R.id.centerCode);
            username=(EditText)findViewById(R.id.loginName);
            password=(EditText)findViewById(R.id.password);
            loginaction=(Button)findViewById(R.id.LoginBt);
            loginaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Check internet connection enabled or not
                    final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                    myAnim.setInterpolator(interpolator);
                    loginaction.startAnimation(myAnim);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        progressBar = new ProgressDialog(v.getContext());
                        progressBar.setCancelable(true);
                        progressBar.setMessage("Validating...");
                        progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);

                        if (centercode.getText().toString() != null && !centercode.getText().toString().equals("") &&
                                username.getText().toString() != null && !username.getText().toString().equals("") &&
                                password.getText().toString() != null && !password.getText().toString().equals("")) {
                            progressBar.show();

                            // TODO Auto-generated method stub
                            String a[] = {centercode.getText().toString(), username.getText().toString(), password.getText().toString()};
                            new UserAsyncTask().execute(a);

                        } else {
                            String alertMsg = "";
                            if (centercode.getText().toString() == null || centercode.getText().toString().equals("")) {
                                alertMsg = "Center Code";
                            }
                            if (username.getText().toString() == null || username.getText().toString().equals("")) {
                                if (alertMsg != null && !alertMsg.equals("")) {
                                    alertMsg = alertMsg + "/" + " User Name";
                                } else {
                                    alertMsg = "User Name";
                                }
                            }
                            if (password.getText().toString() == null || password.getText().toString().equals("")) {
                                if (alertMsg != null && !alertMsg.equals("")) {
                                    alertMsg = alertMsg + " /" + " Password";
                                } else {
                                    alertMsg = "Password";
                                }
                            }
                            AlertDialog loginWarrningMsg = new AlertDialog.Builder(MainActivity.this).create();
                            loginWarrningMsg.setTitle("Warning!");
                            loginWarrningMsg.setMessage("Please enter the " + alertMsg);
                            loginWarrningMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // Showing Alert Message
                            loginWarrningMsg.show();

                        }
                    } else {
                        AlertDialog internetWarrningMsg = new AlertDialog.Builder(MainActivity.this).create();
                        internetWarrningMsg.setTitle("Warning!");
                        internetWarrningMsg.setMessage("Kindly check your internet connection");
                        internetWarrningMsg.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Showing Alert Message
                        internetWarrningMsg.show();
                    }
                }
            });
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //The UserAsyncTask class is used to load the user data such as username, password, user_id from server
    class UserAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            Boolean status=false;
            try {
                json_validation = new UserFunctions().getUserDetails(urls[0],urls[1],urls[2]);
                  if(json_validation != null && !json_validation.equals("") && !json_validation.equals("null")){
                      String result="";
                      int locId=0;
                      int orgId=0;
                      JSONArray jarray = json_validation.getJSONArray("userList");
                      for (int i = 0; i < jarray.length(); i++) {
                          //this jsonobject is used to retrieve value from json array
                          JSONObject object = jarray.getJSONObject(i);
                          locId=object.getInt("location_id");
                          orgId=object.getInt("org_id");
                      }
                      if(locId > 0){
                          //Get the location details and put into shared preference
                          SharedPreferences.Editor loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE).edit();
                          loginDtl.putString("centerCode", urls[0]);
                          loginDtl.putString("userName",urls[1]);
                          loginDtl.putString("password", urls[2]);
                          loginDtl.putInt("location_id", locId);
                          loginDtl.putInt("org_id", orgId);
                          loginDtl.putBoolean("status", true);
                          loginDtl.putBoolean("syncStatus", false);
                          loginDtl_json = new UserFunctions().getCourseDetails(String.valueOf(locId));
                          //Set<String> value = new HashSet<String>();
                          if (loginDtl_json != null && !loginDtl_json.equals("") && !loginDtl_json.equals("null")) {
                              JSONArray jarray1 = loginDtl_json.getJSONArray("courseList");
                              for (int i = 0; i < jarray1.length(); i++) {
                                  //this jsonobject is used to retrieve value from json array
                                  JSONObject object = jarray1.getJSONObject(i);
                                  StudentEnquiryTO studentEnquiryTO =new StudentEnquiryTO();
                                  studentEnquiryTO.setCourseName(object.getString("courseName"));
                                  studentEnquiryTO.setCourseId(object.getInt("courseId"));
                                  studentEnquiryTO.setLocId(locId);
                                  Cursor c=handler.validateCourseExist(object.getInt("courseId"));
                                  if(!c.moveToFirst()){
                                      handler.insertCourseDtl(studentEnquiryTO);
                                  }
                              }
                          }
                          //Set Stage Details Array into StageList
                          JSONParser getJSONFromUrl=new JSONParser();
                          String url= CommonUtilities.BASE_URL+"/"+"rest"+"/"+"StudentEnquiry"+"/"+"getSourceOfEnquiry"+"/"+orgId;
                          sourceOfEnq_json =getJSONFromUrl.getJSONFromUrl(url);

                          JSONArray list = sourceOfEnq_json.getJSONArray("sourceOfEnqDtl"); // get List array
                          for (int i=0;i<list.length();i++){
                              JSONObject object = list.getJSONObject(i);
                              String mediaName=object.getString("mediaName");
                              int mediaId=object.getInt("mediaId");
                              Cursor c=handler.validateSourceOfEnqCourseExist(object.getInt("mediaId"));
                              if(!c.moveToFirst()){
                                  handler.insertSourceOfEnqDtl(orgId,mediaId,mediaName);
                              }
                          }
                          loginDtl.commit();
                          Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                          startActivity(i);
                          finish();
                          status =true;
                          progressBar.dismiss();

                          Intent alarmIntent = new Intent(MainActivity.this, InitialSyncReceiver.class);
                          pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
                          manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                          manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                      }else{
                          progressBar.dismiss();
                          mainActivityStatus ="INVALID_EXCEPTION";
                      }
                  }else{
                      progressBar.dismiss();
                      mainActivityStatus ="JSON_EXCEPTION";
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(Boolean result) {
           if(!result) {
               if(mainActivityStatus.equals("INVALID_EXCEPTION")){
                   //Toast.makeText(getApplicationContext(), "Invalid user credential", Toast.LENGTH_LONG).show();
                   AlertDialog invalidLoginDialog = new AlertDialog.Builder(MainActivity.this).create();
                   invalidLoginDialog.setTitle("Warning!");
                   invalidLoginDialog.setMessage("Invalid user credential!!");
                   invalidLoginDialog.setButton("OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                       }
                   });
                   // Showing Alert Message
                   invalidLoginDialog.show();
                   centercode.setText("");
                   username.setText("");
                   password.setText("");
                   centercode.setFocusable(true);
               }else if(mainActivityStatus.equals("JSON_EXCEPTION")){

                   AlertDialog jsonExceptionDialog = new AlertDialog.Builder(MainActivity.this).create();
                   jsonExceptionDialog.setTitle("Warning!!");
                   jsonExceptionDialog.setMessage("Network Busy!!, Kindly login later");
                   //alertDialog.setIcon(R.drawable.tick);
                   jsonExceptionDialog.setButton("OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                       }
                   });
                   // Showing Alert Message
                   jsonExceptionDialog.show();
                   centercode.setText("");
                   username.setText("");
                   password.setText("");
                   centercode.setFocusable(true);
               }

           }
        }
    }

}
