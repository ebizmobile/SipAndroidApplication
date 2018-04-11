package com.eazibiz.sipandroidapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ebiz.constant.CommonUtilities;
import com.ebiz.constant.DateTime;
import com.ebiz.constant.DateTimePicker;
import com.ebiz.constant.MasterConstants;
import com.ebiz.constant.SimpleDateTimePicker;
import com.ebiz.db.CourseInfoDBHelper;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.db.SourceOfEnquiryInfoDBHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by KARTHI on 28-11-2016.
 */

public class StudentEnquiryActivity  extends AppCompatActivity implements DateTimePicker.OnDateTimeSetListener {

    private List<String> actions_category;
    private TextView dateOfBirth;
    private EditText mobileNo ;
    private EditText stdName ;
    private EditText email ;
    private Spinner cousrse;
    private int locId;
    private int courseId;
    private int orgId;
    private EditText enqDate;
    private DatabaseHandler handler;
    private String enq_date_time ;
    private ImageView backBtImageView;
    private TextView saveText;
    private ProgressDialog progressBar;
    private AlertDialog successMsg ;
    private Boolean status=false;
    private InputStream inputStream = null;
    private String jsonStr;
    private String mobileNumberTemp;
    private String studentNameTemp;
    private String courseTemp;
    private String dateOfBirthTemp;
    private String enquiryDateTemp;
    private String emailIdTemp;
    private RadioGroup stdEnqRadioBt;
    private String genderTemp;
    private String sourceOfEnqTemp;
    private RadioButton gender;
    private Spinner sourceOfEnqiry;
    private List<String> source_of_enquiry;
    private int sourceOfEnqiryId;
    private String onClickValidation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_enquiry);
        handler=new DatabaseHandler(getBaseContext());

        //Set custom action bar
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        final View mCustomView = mInflater.inflate(R.layout.std_enquiry_custom_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        //Variable declare
        mobileNo = (EditText) findViewById(R.id.stdEnqMobileNo);
        stdName = (EditText) findViewById(R.id.stdEnqName);
        dateOfBirth = (TextView) findViewById(R.id.stdEnqDateOfBirth);
        email = (EditText) findViewById(R.id.stdEnqEmailAddress);
        cousrse = (Spinner) findViewById(R.id.stdEnqCourse);
        enqDate = (EditText) findViewById(R.id.stdEnqDate);
        stdEnqRadioBt=(RadioGroup) findViewById(R.id.studentEnqradioGroup);
        sourceOfEnqiry = (Spinner) findViewById(R.id.stdSourceOfEnq);


        backBtImageView = (ImageView)mCustomView. findViewById(R.id.custom_bar_back_bt);
        saveText = (TextView)mCustomView. findViewById(R.id.custom_bar_save_bt);

        setupUI(findViewById(R.id.parent));

        SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
        locId = loginDtl.getInt("location_id",0);// Default value is false
        orgId = loginDtl.getInt("org_id",0);// Default value is false
        //Set<String> value= loginDtl.getStringSet("actions_category",null);// Default value is false
        actions_category = new ArrayList<String>();
        actions_category.add("-Select Course-"); // Set first value is empty
        //Iterator<String> itr=value.iterator();
        //int i=0;
        Cursor cursor = handler.getCourseDtl(locId);
        while (cursor.moveToNext()) {
           String courseName= cursor.getString(cursor.getColumnIndex(CourseInfoDBHelper.COURSE));
            actions_category.add(courseName);
        }

        ArrayAdapter<String> aa_category=new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_main_activity, R.id.itemname1, actions_category);
        cousrse.setAdapter(aa_category);
        aa_category.setDropDownViewResource(R.layout.spinner_main_activity);
        /* Animation is defined in the res/anim directory*/
        //Animation  myAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
       /* The mySpinner running Animation */
        //cousrse.startAnimation(myAnimation);
        cousrse.setSelection(0,true);

        // Source Of Enuiry
        source_of_enquiry = new ArrayList<String>();
        source_of_enquiry.add("-Select Source Of Enquiry-"); // Set first value is empty
        //Iterator<String> itr=value.iterator();
        //int i=0;
        Cursor cursor1 = handler.getSourceOfEnquiry(orgId);
        while (cursor1.moveToNext()) {
            String mediaName= cursor1.getString(cursor1.getColumnIndex(SourceOfEnquiryInfoDBHelper.MEDIA_NAME));
            source_of_enquiry.add(mediaName);
        }

        ArrayAdapter<String> sourceOfEnq=new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_main_activity, R.id.itemname1, source_of_enquiry);
        sourceOfEnqiry.setAdapter(sourceOfEnq);
        sourceOfEnq.setDropDownViewResource(R.layout.spinner_main_activity);
        sourceOfEnqiry.setSelection(0,true);

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        enq_date_time = dateFormatter.format(today);
        enqDate.setText(enq_date_time);
       /* enqDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //do your stuff here..
                return false;
            }
        });*/
        //enqDate.setEnabled(false);
       // enqDate.setEd

        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animate
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 2);
                myAnim.setInterpolator(interpolator);
                saveText.startAnimation(myAnim);
                int selected=stdEnqRadioBt.getCheckedRadioButtonId();
                gender=(RadioButton) findViewById(selected);
               // Toast.makeText(getApplicationContext(), gender.getText(), Toast.LENGTH_SHORT).show();
                if(gender != null ) {
                    if (gender.getText().equals("Male")) {
                        genderTemp = "M";
                    } else if (gender.getText().equals("Female")) {
                        genderTemp = "F";
                    } else if (gender.getText().equals("Others")) {
                        genderTemp = "T";
                    } else {
                        genderTemp = "T";
                    }
                }
                //
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Validating...");
                progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                AlertDialog successMsg = new AlertDialog.Builder(StudentEnquiryActivity.this).create();
                Boolean validateEnq= false;
                if (mobileNo.getText().toString() != null && !mobileNo.getText().toString().equals("")) {
                    validateEnq = isValidMobile(mobileNo.getText().toString());
                }
                if(validateEnq){
                    if (email.getText().toString() != null && !email.getText().toString().equals("")) {
                        validateEnq = isValidMail(email.getText().toString());
                    }else{
                        validateEnq = true;
                    }
                        if(validateEnq){
                            if (sourceOfEnqiry.getSelectedItem().toString() != null && !sourceOfEnqiry.getSelectedItem().toString().equals("") && !sourceOfEnqiry.getSelectedItem().toString().equals("-Select Source Of Enquiry-")) {
                                    if (stdName.getText().toString() != null && !stdName.getText().toString().equals("")) {
                                        if (cousrse.getSelectedItem().toString() != null && !cousrse.getSelectedItem().toString().equals("") && !cousrse.getSelectedItem().toString().equals("-Select Course-")) {
                                            courseId= 0;
                                            Cursor cursor = handler.getCourseIdDtl(cousrse.getSelectedItem().toString());
                                            if (cursor.moveToFirst()) {
                                                courseId= cursor.getInt(cursor.getColumnIndex(CourseInfoDBHelper.COURSE_ID));
                                            }
                                            sourceOfEnqiryId= 0;
                                            Cursor cursor1 = handler.getSourceOfEnqIdDtl(sourceOfEnqiry.getSelectedItem().toString());
                                            if (cursor1.moveToFirst()) {
                                                sourceOfEnqiryId= cursor1.getInt(cursor1.getColumnIndex(SourceOfEnquiryInfoDBHelper.MEDIA_ID));
                                            }

                                            String a[] = { mobileNo.getText().toString(), stdName.getText().toString(), dateOfBirth.getText().toString(), email.getText().toString(), cousrse.getSelectedItem().toString(), enqDate.getText().toString(),sourceOfEnqiry.getSelectedItem().toString()};
                                            new StudentEnquiryActivity.validateMobileAsyncTask().execute(a);

                                        }else{
                                            progressBar.dismiss();
                                            successMsg.setTitle("Warning!");
                                            successMsg.setMessage("Kindly enter course name");
                                            successMsg.show();
                                        }
                                    }else{
                                        progressBar.dismiss();
                                        successMsg.setTitle("Warning!");
                                        successMsg.setMessage("Kindly enter student name");
                                        successMsg.show();
                                    }
                            }else{
                                progressBar.dismiss();
                                successMsg.setTitle("Warning!");
                                successMsg.setMessage("Kindly select source of enquiry");
                                successMsg.show();
                            }

                    }else{
                        progressBar.dismiss();
                        successMsg.setTitle("Warning!");
                        successMsg.setMessage("Kindly enter valid email id");
                        successMsg.show();
                    }
                }else{
                    progressBar.dismiss();
                    successMsg.setTitle("Warning!");
                    successMsg.setMessage("Kindly enter valid mobile number");
                    successMsg.show();
                }
            }
        });

        backBtImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                myAnim.setInterpolator(interpolator);
                backBtImageView.startAnimation(myAnim);
                Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(i);
                finish();

            }
        });
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickValidation ="DOB";
                showDateTimePicker();
            }
        });
        enqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickValidation ="ENQ";
                showDateTimePicker();
            }
        });
    }

    void showDateTimePicker(){
        // Create a SimpleDateTimePicker and Show it
        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                "",
                new Date(),
                this,
                getSupportFragmentManager()
        );
        // Show It baby!
        simpleDateTimePicker.show();
    }

    @Override
    public void DateTimeSet(Date date) {
        // TODO Auto-generated method stub
        // This is the DateTime class we created earlier to handle the conversion
        // of Date to String Format of Date String Format to Date object
        DateTime mDateTime = new DateTime(date);
        // Show in the LOGCAT the selected Date and Time
        if(onClickValidation.equals("DOB")){
            dateOfBirth.setText(mDateTime.getDateString());
        }else if(onClickValidation.equals("ENQ")){
            enqDate.setText(mDateTime.getDateString());
        }

    }

    private boolean isValidMobile(String phone){
        //return android.util.Patterns.PHONE.matcher(phone).matches();
        String Regex = "[^\\d]";
        String PhoneDigits = phone.replaceAll(Regex, "");
        if (PhoneDigits.length()!=10){
            return false;
        }else{
            return true;
        }
    }
    private boolean isValidMail(String email){
        //return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)){
            return true;
        }else{
            return false;
        }
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(StudentEnquiryActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    class validateMobileAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
             mobileNumberTemp =urls[0];
             studentNameTemp =urls[1];
             dateOfBirthTemp=urls[2];
             emailIdTemp=urls[3];
             courseTemp=urls[4];
             enquiryDateTemp=urls[5];
            sourceOfEnqTemp=urls[6];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    successMsg = new AlertDialog.Builder(StudentEnquiryActivity.this).create();
                    try {
                        String url = "";
                        url= CommonUtilities.BASE_URL+"/"+"rest"+"/"+"StudentEnquiry"+"/"+"validateMobileNo"+"/"+locId+"/"+studentNameTemp+"/"+mobileNumberTemp;
                        url=url.replace(" ", "%20");
                        String urlStatus=checkUrlStatus(CommonUtilities.BASE_URL);
                        //Toast.makeText(getApplicationContext(), urlStatus, Toast.LENGTH_SHORT).show();
                        //if(urlStatus.equals("Success")) {
                        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                        DefaultHttpClient client = new DefaultHttpClient();

                        SchemeRegistry registry = new SchemeRegistry();
                        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                        registry.register(new Scheme("https", socketFactory, 443));
                        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
                        DefaultHttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());

                        // Set verifier
                        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                        String values[]={url};
                        // / HttpGet Method /
                        HttpGet httppost = new HttpGet(url);
                        // / create Apache HttpClient /
                       // HttpClient httpclient = new DefaultHttpClient();
                        //  / Make http request call /
                        HttpResponse response = httpclient.execute(httppost);
                        // StatusLine stat = response.getStatusLine();
                        int responseStatus = response.getStatusLine().getStatusCode();
                        System.out.println(status);
                        // 200 represents HTTP OK /
                        if (responseStatus == 200) {
                            progressBar.dismiss();
                            System.out.println("********SUCCESS**********");
                            // receive response as inputStream
                            inputStream = response.getEntity().getContent();
                            if(inputStream != null){
                                jsonStr = convertInputStreamToString(inputStream);
                                System.out.println("********jsonStr**********" + jsonStr);
                                String statusTemp=jsonStr;
                                if(statusTemp!= null && !statusTemp.equals("")){
                                    if(statusTemp.equals("EXISTS")){
                                        successMsg.setTitle("Warning !!");
                                        successMsg.setMessage("Student Name & Mobile No Already Exist!!");
                                        successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                mobileNo.setFocusable(true);
                                            }
                                        });
                                        // Showing Alert Message
                                        successMsg.show();
                                    }else if(statusTemp.equals("NOT_EXISTS")){ // Check in server, mobile number already exist
                                        Cursor c=handler.validateMobileExist(mobileNumberTemp,studentNameTemp.toUpperCase(),locId);
                                        if(!c.moveToFirst()) {
                                            successMsg.setTitle("Success");
                                            successMsg.setMessage("Saved Successfully");
                                            //Toast.makeText(getApplicationContext(), "Course Id---"+courseId, Toast.LENGTH_SHORT).show();
                                            handler.insertStdEnqDtl(locId, orgId, mobileNumberTemp, studentNameTemp.toUpperCase(), 0, dateOfBirthTemp, emailIdTemp, courseTemp, courseId, 0, enquiryDateTemp,genderTemp,sourceOfEnqTemp,sourceOfEnqiryId,"N");
                                            successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mobileNo.setText("");
                                                    stdName.setText("");
                                                    dateOfBirth.setText("");
                                                    email.setText("");
                                                    enqDate.setText(enq_date_time);
                                                    enqDate.setEnabled(false);
                                                    mobileNo.setFocusable(true);
                                                    cousrse.setSelection(0);
                                                    sourceOfEnqiry.setSelection(0);
                                                }
                                            });
                                            // Showing Alert Message
                                            successMsg.show();
                                        }else{
                                            successMsg.setTitle("Warning !!");
                                            successMsg.setMessage("Student Name & Mobile No Already Exist!!");
                                            successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mobileNo.setFocusable(true);
                                                }
                                            });
                                            // Showing Alert Message
                                            successMsg.show();
                                        }
                                    }else{ // Failure status
                                        successMsg.setTitle("Failure !!");
                                        successMsg.setMessage("Network Busy !! Kindly try some times 4!!");
                                        successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        // Showing Alert Message
                                        successMsg.show();
                                    }
                                }else{
                                    successMsg.setTitle("Failure !!");
                                    successMsg.setMessage("Network Busy !! Kindly try some times3 !!");
                                    successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    // Showing Alert Message
                                    successMsg.show();
                                }
                            }
                        }else{
                            progressBar.dismiss();
                            successMsg.setTitle("Failure !!");
                            successMsg.setMessage("Network Busy !! Kindly try some times1 !!");
                            successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // Showing Alert Message
                            successMsg.show();
                        }
                        /*}else{
                            progressBar.dismiss();
                            successMsg.setTitle("Failure !!");
                            successMsg.setMessage("Network Busy !! Kindly try some times2 !!");
                            successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // Showing Alert Message
                            successMsg.show();
                        }*/


                    }catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            return status;
        }

        protected void onPostExecute(Boolean result) {

        }
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
}
