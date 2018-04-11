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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;

import com.ebiz.constant.CommonUtilities;
import com.ebiz.constant.MasterConstants;
import com.ebiz.json.JSONParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Rajesh Kumar on 30-Dec-16.
 */

public class StudentReceiptPaidActivity extends AppCompatActivity {

    EditText stdNameText;
    EditText invAmtText;
    EditText balAmtText;
    EditText payAmtText;
    EditText invNoText;
    private String jsonStr;
    private AlertDialog successMsg ;
    private Boolean status=false;
    private InputStream inputStream = null;
    private ImageView std_backBtImageView;
    private TextView stdRecp_saveText;
    private ProgressDialog progressBar;
    String invHdrId = "";
    String invNo  = "";
    String invAmt  = "";
    String invPaidAmt  = "";
    String orgId  = "";
    String locId  ="";
    String studentId  = "";
    String invMonth ="";
    String invDt  = "";
    String userName  = "";
    String invDueDt  = "";

    String inv_amt= "";
    String bal_amt ="";
    String inv_dt = "";
    String std_name ="";
    String loc_id = "";
    String inv_Month = "";
    String inv_Year = "";
    String inv_No = "";
    String inv_Id = "";
    String inv_due_dt = "";
    String paid_amt = "";
    String org_id;
    String stud_id = "";
    String user_name = "";
    String pay_Amt="";
    private AlertDialog indicateAlert ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_paid);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        final View mCustomView = mInflater.inflate(R.layout.std_receipt_paid_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        Intent i = getIntent();
        inv_amt = i.getStringExtra("inv_amt");
        bal_amt = i.getStringExtra("bal_amt");
        inv_dt = i.getStringExtra("inv_dt");
        std_name = i.getStringExtra("std_name");

        inv_Month = i.getStringExtra("inv_month");
        inv_Year = i.getStringExtra("inv_year");
        inv_No = i.getStringExtra("inv_no");
        inv_Id = i.getStringExtra("inv_id");
        inv_due_dt = i.getStringExtra("inv_due_dt");
        paid_amt = i.getStringExtra("paid_amt");
        stud_id = i.getStringExtra("stud_id");
        SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
        org_id = Integer.toString(loginDtl.getInt("org_id", 0));
        loc_id = Integer.toString(loginDtl.getInt("location_id", 0));
        user_name = loginDtl.getString("userName", "empty");
        pay_Amt  = i.getStringExtra("bal_amt");

        stdNameText =  (EditText) findViewById(R.id.paid_studName);
        invAmtText =  (EditText) findViewById(R.id.paid_invAmt);
        balAmtText =  (EditText) findViewById(R.id.paid_balAmt);
        invNoText =  (EditText) findViewById(R.id.paid_invNo);
        payAmtText =  (EditText) findViewById(R.id.paid_payAmt);


        stdNameText.setText(std_name);
        invAmtText.setText(inv_amt);
        balAmtText.setText(bal_amt);
        payAmtText.setText(bal_amt);
        payAmtText.setFocusable(true);
        invNoText.setText(inv_No);

        std_backBtImageView = (ImageView)mCustomView. findViewById(R.id.std_recpt_bar_back_bt);
        stdRecp_saveText = (TextView)mCustomView. findViewById(R.id.std_recpt_bar_save_bt);
        payAmtText.setFocusableInTouchMode(false);
        payAmtText.setFocusable(false);

        setupUI(findViewById(R.id.parent));


        stdRecp_saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* indicateAlert = new AlertDialog.Builder(StudentReceiptPaidActivity.this).create();
                indicateAlert.setTitle("PROCESSING !!");
                indicateAlert.setMessage("Updating... !!");
                indicateAlert.show();*/

                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Validating...");
                progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
                progressBar.setMax(100);
                progressBar.setProgress(0);
                progressBar.show();

               final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 2);
                        myAnim.setInterpolator(interpolator);
                        stdRecp_saveText.startAnimation(myAnim);

                        String [] a={inv_Id,inv_No,inv_amt,pay_Amt,org_id,loc_id,stud_id,inv_Month,inv_dt,user_name,inv_due_dt};

                        new StudentReceiptPaidActivity.validateMobileAsyncTask().execute(a);
                    }
                }, 100);
                // Animate

            }
        });
        std_backBtImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent K = new Intent(getApplicationContext(),ReceiptActivity.class);
                startActivity(K);
                finish();

            }
        });
        payAmtText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //do your stuff here..
                return false;
            }
        });
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(StudentReceiptPaidActivity.this);
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

             invHdrId = urls[0];
             invNo  = urls[1];
             invAmt  = urls[2];
             invPaidAmt  = urls[3];
             orgId  = urls[4];
             locId  = urls[5];
             studentId  = urls[6];
             invMonth = urls[7];
             invDt  = urls[8];
             userName  = urls[9];
             invDueDt  = urls[10];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    successMsg = new AlertDialog.Builder(StudentReceiptPaidActivity.this).create();
                    try {
                        String url = CommonUtilities.BASE_URL + "/" + "rest" + "/" + "StudentInfoJason" + "/" + "updateInvoiceByCashInMobile" + "/" + invHdrId + "/" + invNo + "/" + invAmt + "/" + invPaidAmt + "/" + orgId + "/" + locId + "/" + studentId + "/" + invMonth + "/" + invDt + "/" + userName + "/" + invDueDt;
                        url=url.replace(" ", "%20");
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
                        //HttpClient httpclient = new DefaultHttpClient();
                        //  / Make http request call /
                        HttpResponse response = httpclient.execute(httppost);
                        // StatusLine stat = response.getStatusLine();
                        int responseStatus = response.getStatusLine().getStatusCode();
                        System.out.println(status);
                        if (responseStatus == 200) {

                            System.out.println("********SUCCESS**********");
                            // receive response as inputStream
                            inputStream = response.getEntity().getContent();
                            if(inputStream != null){
                                jsonStr = convertInputStreamToString(inputStream);
                                System.out.println("********jsonStr**********" + jsonStr);
                                String statusTemp=jsonStr;
                                if(statusTemp!= null && !statusTemp.equals("")){
                                   System.out.println(statusTemp);
                                    if(statusTemp.equals("SUCCESS")){

                                       // progressBar.dismiss();
                                        successMsg.setTitle("Success !!");
                                        successMsg.setMessage("Receipt successfully updated!!");
                                        successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent K = new Intent(getApplicationContext(),ReceiptActivity.class);
                                                startActivity(K);
                                                finish();
                                            }
                                        });
                                        // Showing Alert Message
                                        successMsg.show();
                                    }else if(statusTemp.equals("ERROR")){
                                       // progressBar.dismiss();
                                        successMsg.setTitle("Warning !!");
                                        successMsg.setMessage("Error occured while receipt update!!");
                                        successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                    }

                                }else{
                                    //progressBar.dismiss();
                                    successMsg.setTitle("Failure !!");
                                    successMsg.setMessage("Network Busy !! Kindly try some times !!");
                                    successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    // Showing Alert Message
                                    successMsg.show();
                                }
                            }
                        }else{
                           // progressBar.dismiss();
                            successMsg.setTitle("Failure !!");
                            successMsg.setMessage("Network Busy !! Kindly try some times1 !!");
                            successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // Showing Alert Message
                            successMsg.show();
                        }

                    }catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });

            return status;
        }

        protected void onPostExecute(Boolean result) {
            progressBar.dismiss();

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
}
