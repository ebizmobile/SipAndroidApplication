package com.eazibiz.sipandroidapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.adapter.ListAdapter;
import com.adapter.StdReceiptAdapter;
import com.ebiz.constant.CommonUtilities;
import com.ebiz.constant.MasterConstants;
import com.ebiz.db.CourseInfoDBHelper;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.db.StudentEnqInfoDBHelper;
import com.ebiz.db.StudentInfoDBHelper;
import com.ebiz.json.JSONParser;
import com.ebiz.to.EnqListTO;
import com.ebiz.to.StudentReceiptTO;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.daimajia.swipe.SwipeLayout;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static com.eazibiz.sipandroidapplication.R.id.spinner;

/**
 * Created by Rajesh Kumar on 27-Dec-16.
 */

public class ReceiptActivity extends AppCompatActivity {

    private Spinner studentNameList;

    AutoCompleteTextView tv_customer_name;
    ArrayAdapter<String> customerAdapter;
    String stdName;
    int locId;
    String itemTp;
    EditText edt;
    ListView listV;
    String value[];
    DatabaseHandler handler;
    String[] enquiryList ;
    ArrayList<HashMap<String, String>> arry_list;

    private List<String> std_name_list;
    private List<String> std_mobile_list;

    ArrayAdapter<String> itemTypeAdapter;

    LinearLayout layOutOne;
    LinearLayout layOutTwo;
    private TextView stdRecp_clearText;
    EditText itemText;
    ListView itemList;
    String itemValue[];

    BaseAdapter adapter;
    Cursor cursor;
    ListView list;
    JSONObject invOfStud_json;

    EditText hiddenStudentId;
    boolean stdNameOnClickVal = false;
    boolean stdMobileNoOnClickVal =false;


    //shared name
    String storeStdName;
    String storeStdId;
    String storeStdMobile;
    Boolean storeBool;
    private ProgressDialog progressBar;
    FrameLayout frameLayoutList;
    FrameLayout frameLayoutListMobile;
    ImageView back_to_menu_btn;
    StdReceiptAdapter myRepAdapt;
    private SwipeLayout swipeLayout;
    List<StudentReceiptTO> repData = new ArrayList<>();
    StdReceiptAdapter repAdapter;

    private String jsonStr;
    private AlertDialog successMsg ;
    private Boolean status=false;
    private InputStream inputStream = null;



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


    String invHdrId = "";
    String  invNo  = "";
    String  invAmt  = "";
    String  invPaidAmt  = "";
    String organId  = "";
    String  locationId  = "";
    String  studentId  = "";
    String  invMonth = "";
    String  invDt  = "";
    String  userName  = "";
    String  invDueDt  = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_receipt);
        progressBar = new ProgressDialog(ReceiptActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
        locId = loginDtl.getInt("location_id", 0);
        storeStdName = loginDtl.getString("studName", "empty");
        storeStdId = loginDtl.getString("studId", "empty");
        storeStdMobile = loginDtl.getString("mobileNo", "empty");
        storeBool = loginDtl.getBoolean("isOpen", false);


        layOutOne =  (LinearLayout) findViewById(R.id.firstLayOutStudName);
        layOutTwo =  (LinearLayout) findViewById(R.id.secLayOutMobileNo);
        layOutOne.setZ(0.8f);
        layOutTwo.setZ(0.4f);

        handler=new DatabaseHandler(getBaseContext());


        hiddenStudentId =  (EditText) findViewById(R.id.edt_hidden_id);
    //this is for student combo list
    //first drop down field(studentName)
        edt = (EditText) findViewById(R.id.edt);


        frameLayoutList = (FrameLayout) findViewById(R.id.stdReceiptFrgId);
        frameLayoutListMobile = (FrameLayout) findViewById(R.id.frameLayoutMobile);
        listV = (ListView) findViewById(R.id.listview);
    //second drop down field(mobilen no)
        itemText = (EditText) findViewById(R.id.itemTypeEdit);
        itemList = (ListView) findViewById(R.id.itemTypeList);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        final View mCustomView = mInflater.inflate(R.layout.std_receipt_custom_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        back_to_menu_btn = (ImageView)mCustomView. findViewById(R.id.std_recpt_custom_bar_back_bt);

        stdRecp_clearText = (TextView)mCustomView. findViewById(R.id.std_recpt_bar_clear_bt);

        stdRecp_clearText.setOnClickListener(new View.OnClickListener() {
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
                        itemText.setText("");
                        edt.setText("");
                        hiddenStudentId.setText("");

                        listV.setVisibility(View.GONE);
                        itemList.setVisibility(View.GONE);
                        ListView listview2 = (ListView) findViewById(R.id.invoiceDtlList);
                        listview2.setVisibility(ListView.GONE);

                        progressBar.dismiss();
                    }
                }, 1000);
                // Animate

            }
        });



        if(!storeStdName.equals("empty")){
            edt.setText(storeStdName);
            itemText.setText(storeStdMobile);
            hiddenStudentId.setText(storeStdId);
            loadInvoiceListForStudent();
            setListViewHeader();
        }
        std_name_list = new ArrayList<String>();
        //Iterator<String> itr=value.iterator();
        //int i=0;

            Cursor cursor = handler.getStudentInfoDtl(locId);
            while (cursor.moveToNext()) {
                String stdName = cursor.getString(cursor.getColumnIndex(StudentInfoDBHelper.STUDENT_NAME));
                std_name_list.add(stdName);
            }

            // value =new String[]{"one","two","three","one","two","three","one","two","three","three","one","two","three","three","one","two","three"};
            customerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_student, R.id.itemname_std, std_name_list);
            customerAdapter.notifyDataSetChanged();
            listV.setAdapter(customerAdapter);
             progressBar.dismiss();
            edt.setFocusable(false);
            edt.setFocusableInTouchMode(false);
            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(R.id.itemname_std);
                    edt.setText(tv.getText().toString());
                    String srcSearch = "studentname";
                    Cursor cursorId = handler.getStudentSearchDtl(tv.getText().toString(), srcSearch);
                    while (cursorId.moveToNext()) {
                        String mbleNo = cursorId.getString(cursorId.getColumnIndex(StudentInfoDBHelper.STD_MOBILE_NO));
                        String stdId = cursorId.getString(cursorId.getColumnIndex(StudentInfoDBHelper.STUDENT_ID));
                        hiddenStudentId.setText(stdId);
                        itemText.setText(mbleNo);
                        hideSoftKeyboard(ReceiptActivity.this);
                        SharedPreferences.Editor setStdInfo = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE).edit();
                        setStdInfo.putString("studName", edt.getText().toString());
                        setStdInfo.putString("studId", stdId);
                        setStdInfo.putString("mobileNo", mbleNo);
                        setStdInfo.putBoolean("isOpen", true);
                        setStdInfo.commit();
                    }

                    itemList.setVisibility(View.GONE);
                    listV.setVisibility(View.GONE);
                    loadInvoiceListForStudent();
                }
            });

            edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edt.setFocusable(true);
                    edt.setFocusableInTouchMode(true);
                    ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
                     listV.setVisibility(View.VISIBLE);
                    frameLayoutList.setVisibility(View.VISIBLE);
                    stdMobileNoOnClickVal = false;
                    stdNameOnClickVal = true;

                }
            });
           edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    edt.setFocusableInTouchMode(true);
                    edt.setFocusable(true);
                      itemList.setVisibility(View.GONE);
                    listV.setVisibility(View.VISIBLE);
                    stdMobileNoOnClickVal = false;
                    stdNameOnClickVal=true;
                }
            });
            edt.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println(s);
                    if (stdNameOnClickVal) {
                        ((ArrayAdapter) ReceiptActivity.this.customerAdapter).getFilter().filter(s);

                        customerAdapter.notifyDataSetInvalidated();
                        customerAdapter.notifyDataSetChanged();
                        // TODO Auto-generated method stub


                        listV.setVisibility(View.VISIBLE);
                        itemList.setVisibility(View.GONE);
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub


                }
            });
//this is for item combo list
            std_mobile_list = new ArrayList<String>();
            //Iterator<String> itr=value.iterator();
            //int i=0;
            Cursor cursorMobile = handler.getStudentInfoDtl(locId);
            while (cursorMobile.moveToNext()) {
                String mblN = cursorMobile.getString(cursorMobile.getColumnIndex(StudentInfoDBHelper.STD_MOBILE_NO));
                std_mobile_list.add(mblN);
            }


            // itemValue = new String[]{"ALL","KIT ITEM","MONTHLY FEES"};
            itemTypeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_type, R.id.item_type_name, std_mobile_list);
            itemTypeAdapter.notifyDataSetChanged();
            itemList.setAdapter(itemTypeAdapter);
            itemText.setFocusable(false);
            itemText.setFocusableInTouchMode(false);
            itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(R.id.item_type_name);
                    itemText.setText(tv.getText().toString());

                    String srcSearch = "mobileno";
                    Cursor cursorNo = handler.getStudentSearchDtl(tv.getText().toString(), srcSearch);
                    while (cursorNo.moveToNext()) {
                        String stdNam = cursorNo.getString(cursorNo.getColumnIndex(StudentInfoDBHelper.STUDENT_NAME));
                        String stdId = cursorNo.getString(cursorNo.getColumnIndex(StudentInfoDBHelper.STUDENT_ID));
                        hiddenStudentId.setText(stdId);
                        edt.setText(stdNam);
                        hideSoftKeyboard(ReceiptActivity.this);
                        SharedPreferences.Editor setStdInfo = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE).edit();
                        setStdInfo.putString("studName", stdNam);
                        setStdInfo.putString("studId", stdId);
                        setStdInfo.putString("mobileNo", itemText.getText().toString());
                        setStdInfo.commit();
                    }

                    itemList.setVisibility(View.GONE);
                    listV.setVisibility(View.GONE);
                    loadInvoiceListForStudent();
                }
            });
            itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemText.setFocusableInTouchMode(true);
                    itemText.setFocusable(true);
                    ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(itemText, InputMethodManager.SHOW_IMPLICIT);
                    itemList.setVisibility(View.VISIBLE);
                    frameLayoutListMobile.setVisibility(View.VISIBLE);
                    //itemList.setVisibility(View.VISIBLE);
                    //listV.setVisibility(View.GONE);
                    stdMobileNoOnClickVal = true;
                    stdNameOnClickVal = false;


                }
            });
            itemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    itemText.setFocusableInTouchMode(true);
                    itemText.setFocusable(true);
                    stdMobileNoOnClickVal = true;
                    stdNameOnClickVal = false;
                    itemList.setVisibility(View.VISIBLE);
                    listV.setVisibility(View.GONE);
                }
            });

            itemText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println(s);
                    //If click xxxxx
                    if (stdMobileNoOnClickVal) {
                        ((ArrayAdapter) ReceiptActivity.this.itemTypeAdapter).getFilter().filter(s);

                        itemTypeAdapter.notifyDataSetInvalidated();
                        itemTypeAdapter.notifyDataSetChanged();
                        // TODO Auto-generated method stub
                        itemList.setVisibility(View.VISIBLE);
                        listV.setVisibility(View.GONE);
                    }


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });


        back_to_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                myAnim.setInterpolator(interpolator);
                back_to_menu_btn.startAnimation(myAnim);
                Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    public void loadInvoiceListForStudent(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            try {

            itemList.setVisibility(View.GONE);
            listV.setVisibility(View.GONE);
            List<HashMap<String, String>> value = new ArrayList<HashMap<String, String>>();
            arry_list = new ArrayList<HashMap<String, String>>();
            // Default value is false
            stdName = edt.getText().toString();
            itemTp = itemText.getText().toString();
            int stdId=Integer.parseInt(hiddenStudentId.getText().toString());
            JSONParser getJSONFromUrlRes = new JSONParser();
            String url = CommonUtilities.BASE_URL + "/rest/StudentInfoJason/getStudentInvoiceList/" + locId+"/"+stdId;
            invOfStud_json = getJSONFromUrlRes.getJSONFromUrl(url);
            JSONArray listJson = invOfStud_json.getJSONArray("studentList");
            if(listJson.length()>0) {
                for (int i = 0; i < listJson.length(); i++) {
                    JSONObject object = listJson.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(StudentInfoDBHelper.INVOICE_ID, object.getString("invHdrId"));
                    map.put(StudentInfoDBHelper.INVOICE_NO, object.getString("invNo"));
                    map.put(StudentInfoDBHelper.INVOICE_DATE, object.getString("invDt"));
                    map.put(StudentInfoDBHelper.MONTH, object.getString("invForMonth"));
                    map.put(StudentInfoDBHelper.YEAR, object.getString("invForYear"));
                    map.put(StudentInfoDBHelper.MONTHYEAR, object.getString("invForMonth").substring(0, 3)+"/"+object.getString("invForYear"));
                    map.put(StudentInfoDBHelper.INVOICE_AMT, object.getString("invAmt"));
                    map.put(StudentInfoDBHelper.PAID_AMT, object.getString("paidAmt"));
                    map.put(StudentInfoDBHelper.BAL_AMT, object.getString("balAmt"));
                    map.put(StudentInfoDBHelper.INVOICEDUEDT, object.getString("invDueDt"));

                    arry_list.add(map);
                }

                String from[] = new String[]{
                        StudentInfoDBHelper.INVOICE_ID,
                        StudentInfoDBHelper.INVOICE_NO,
                        StudentInfoDBHelper.INVOICE_DATE,
                        StudentInfoDBHelper.MONTHYEAR,
                        StudentInfoDBHelper.MONTH,
                        StudentInfoDBHelper.YEAR,
                        StudentInfoDBHelper.INVOICE_AMT,
                        StudentInfoDBHelper.PAID_AMT,
                        StudentInfoDBHelper.BAL_AMT,
                        StudentInfoDBHelper.INVOICEDUEDT
                };


                int to[] = {R.id.std_inv_id,R.id.std_inv_no, R.id.inv_dt_name, R.id.inv_monyear_name, R.id.std_inv_mon, R.id.std_inv_year, R.id.inv_amt_name, R.id.paid_amt_name, R.id.bal_amt_name,R.id.std_inv_due_dt};

                myRepAdapt = new StdReceiptAdapter(getApplicationContext(), arry_list,
                        R.layout.activity_std_receipt_list, from, to,ReceiptActivity.this);

                list = (ListView) findViewById(R.id.invoiceDtlList);
                list.setAdapter(myRepAdapt);
                list.setVisibility(ListView.VISIBLE);
               // adapter.notifyDataSetChanged();
                //list.setFastScrollEnabled(true);
                SharedPreferences.Editor setStdInfo1 = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE).edit();
                setStdInfo1.putBoolean("isOpen", true);
                setStdInfo1.commit();
                /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        HashMap<String, String> inv_list = (HashMap<String, String>) parent.getItemAtPosition(position);
                        String inv_amt = inv_list.get(StudentInfoDBHelper.INVOICE_AMT);
                        String bal_amt = inv_list.get(StudentInfoDBHelper.BAL_AMT);
                        String inv_dt = inv_list.get(StudentInfoDBHelper.INVOICE_DATE);
                        String inv_monYear = inv_list.get(StudentInfoDBHelper.MONTHYEAR);
                        String inv_month = inv_list.get(StudentInfoDBHelper.MONTH);
                        String inv_year = inv_list.get(StudentInfoDBHelper.YEAR);
                        String inv_no = inv_list.get(StudentInfoDBHelper.INVOICE_NO);
                        String inv_id = inv_list.get(StudentInfoDBHelper.INVOICE_ID);
                        String inv_duedt = inv_list.get(StudentInfoDBHelper.INVOICEDUEDT);
                        String paid_amt = inv_list.get(StudentInfoDBHelper.PAID_AMT);

                        Intent paidActivity = new Intent(getApplicationContext(), StudentReceiptPaidActivity.class);
                        paidActivity.putExtra("inv_amt", inv_amt);
                        paidActivity.putExtra("bal_amt", bal_amt);
                        paidActivity.putExtra("inv_dt", inv_dt);
                        paidActivity.putExtra("std_name", stdName);
                        paidActivity.putExtra("loc_id", locId);
                        paidActivity.putExtra("inv_monYear", inv_monYear);
                        paidActivity.putExtra("inv_month", inv_month);
                        paidActivity.putExtra("inv_year", inv_year);
                        paidActivity.putExtra("inv_no", inv_no);
                        paidActivity.putExtra("inv_id", inv_id);
                        paidActivity.putExtra("inv_due_dt", inv_duedt);
                        paidActivity.putExtra("paid_amt", paid_amt);
                        paidActivity.putExtra("stud_id", hiddenStudentId.getText().toString());
                        startActivity(paidActivity);
                        //finish();

                    }
                });*/
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        }else{
            AlertDialog internetWarrningMsg = new AlertDialog.Builder(ReceiptActivity.this).create();
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

    private void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.activity_std_receipt_listview, null);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout_2);
        setSwipeViewFeatures();
        //listView.addHeaderView(header);
    }

    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper_2));

    }


    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ReceiptActivity.this);
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


    public void updatePayment(String invId,String  invNo,String invAmt,String invBalAmt,String invMon,String invDt,String dueDt){
        progressBar = new ProgressDialog(ReceiptActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Validating...");
        progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
        progressBar.setMax(1000);
        progressBar.setProgress(0);
        progressBar.show();
        final Handler handler = new Handler();
        inv_Id = invId;

        inv_No = invNo;
        inv_amt = invAmt;
        pay_Amt =invBalAmt;
        inv_Month=  invMon;
        inv_dt = invDt;
        inv_due_dt = dueDt;


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                try {
                    SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
                    org_id = Integer.toString(loginDtl.getInt("org_id", 0));
                    loc_id = Integer.toString(loginDtl.getInt("location_id", 0));
                    user_name = loginDtl.getString("userName", "empty");
                    stud_id = hiddenStudentId.getText().toString();

                    String[] a = {inv_Id, inv_No, inv_amt, pay_Amt, org_id, loc_id, stud_id, inv_Month, inv_dt, user_name, inv_due_dt};


                    new ReceiptActivity.validateMobileAsyncTask().execute(a);
                }catch (Exception e){

                }
            }
        }, 100);
    }
    class validateMobileAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override

        protected Boolean doInBackground(String... urls) {

            invHdrId = urls[0];
            invNo  = urls[1];
            invAmt  = urls[2];
            invPaidAmt  = urls[3];
            organId  = urls[4];
            locationId  = urls[5];
            studentId  = urls[6];
            invMonth = urls[7];
            invDt  = urls[8];
            userName  = urls[9];
            invDueDt  = urls[10];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    successMsg = new AlertDialog.Builder(ReceiptActivity.this).create();
                    try {
                        String url = CommonUtilities.BASE_URL + "/" + "rest" + "/" + "StudentInfoJason" + "/" + "updateInvoiceByCashInMobile" + "/" + invHdrId + "/" + invNo + "/" + invAmt + "/" + invPaidAmt + "/" + organId + "/" + locationId + "/" + studentId + "/" + invMonth + "/" + invDt + "/" + userName + "/" + invDueDt;
                        url=url.replace(" ", "%20");
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

                        String values[]={url};
                        // / HttpGet Method /
                        HttpGet httppost = new HttpGet(url);
                        // / create Apache HttpClient /
                        //HttpClient httpClient = new DefaultHttpClient();
                        //  / Make http request call /
                        HttpResponse response = httpClient.execute(httppost);
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
                                                loadInvoiceListForStudent();
                                            }
                                        });
                                        // Showing Alert Message
                                        successMsg.show();
                                    }if(statusTemp.equals("ALREADYPAID")){

                                        // progressBar.dismiss();
                                        successMsg.setTitle("Alert !!");
                                        successMsg.setMessage("Payment has already updated WEB Application!!!");
                                        successMsg.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                loadInvoiceListForStudent();
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
                                    successMsg.setMessage("Network Busy !! Kindly try after some time!!");
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
                            successMsg.setMessage("Network Busy !! Kindly try after some time!!");
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
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
