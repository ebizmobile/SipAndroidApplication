package com.eazibiz.sipandroidapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ListAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.ebiz.constant.MasterConstants;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.db.StudentEnqInfoDBHelper;
import com.ebiz.to.EnqListTO;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajesh Kumar on 02-Feb-17.
 */

public class EnqListActivity extends AppCompatActivity {
    private ListView listView;

    DatabaseHandler handler;
    String[] enquiryList ;
    ArrayList<HashMap<String, String>> arryList;
    ListViewAdapter adapter;

    List<EnqListTO> data = new ArrayList<>();
    ListAdapter myadapt;
    String enqKeyId="";
    String isUpdate ="no";
    Cursor cursor;
    SimpleCursorAdapter cursorAdapter;
    private SwipeLayout swipeLayout;
    int locId;
    int orgId;
    Toolbar mToolbar;
    private ImageView iv;
    private ImageView callerImage;
    private ImageView enqListBackIV;
    private EditText text;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private float offset;
    private Interpolator interp;
    private int duration;
    private boolean expanded = false;
    private ProgressDialog progressBar;
    private List<EnqListTO> dataStor;
    private int pos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enq_listactivity);

        listView = (ListView)findViewById(R.id.enquiryList);

        arryList = new ArrayList<HashMap<String, String>>();

        getDataFromFile();
        setListViewHeader();

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        final View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);

        enqListBackIV = (ImageView)mCustomView. findViewById(R.id.enq_list_back);
        iv = (ImageView)mCustomView. findViewById(R.id.enq_list_search);
        text = (EditText)mCustomView. findViewById(R.id.enqListSearchText);
        searchToBar = (AnimatedVectorDrawable)mCustomView.getResources().getDrawable(R.drawable.anim_search_to_bar);
        barToSearch = (AnimatedVectorDrawable)mCustomView. getResources().getDrawable(R.drawable.anim_bar_to_search);
        interp = AnimationUtils.loadInterpolator(mCustomView.getContext(), android.R.interpolator.linear_out_slow_in);
        duration = getResources().getInteger(R.integer.duration_bar);
        offset = (int) getResources().getDisplayMetrics().scaledDensity;
        iv.setTranslationX(offset);

        //Toast.makeText(StudentEnquiryListActivity.this, "value222", Toast.LENGTH_SHORT).show();
        iv.setImageDrawable(barToSearch);
        // text.setVisibility(View.GONE);
        barToSearch.start();
        iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!expanded) {
                    //Toast.makeText(StudentEnquiryListActivity.this, "value111", Toast.LENGTH_SHORT).show();
                    iv.setImageDrawable(searchToBar);
                    //text.setVisibility(View.VISIBLE);
                    searchToBar.start();
                    iv.animate().translationX(0f).setDuration(duration).setInterpolator(interp);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text.setVisibility(View.VISIBLE);
                            iv.setVisibility(View.GONE);
                            text.setText("");
                            text.requestFocusFromTouch();
                            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
                        }
                    },800);


                    // text.animate().alpha(1f).setStartDelay(duration - 100).setDuration(100).setInterpolator(interp);
                } else {

                    // Toast.makeText(StudentEnquiryListActivity.this, "value222", Toast.LENGTH_SHORT).show();
                    iv.setImageDrawable(barToSearch);
                    //text.setVisibility(View.GONE);
                    barToSearch.start();
                    iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
                    //text.setAlpha(0f);
                }
                expanded = !expanded;

                // iv is sized to hold the search+bar so when only showing the search icon, translate the
                // whole view left by half the difference to keep it centered


            }
        });

        callerImage = (ImageView)mCustomView. findViewById(R.id.stdEnqCallBtId);

        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    iv.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                    iv.setImageDrawable(barToSearch);
                    barToSearch.start();
                    expanded = false;
                    iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
                    return true; // consume.

                }
                return false;
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        enqListBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Animate
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 2);
                myAnim.setInterpolator(interpolator);
                enqListBackIV.startAnimation(myAnim);
                //
                Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(i);
                finish();
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // ((SimpleAdapter)EnqListActivity.this.adapter).getFilter().filter(charSequence);
                System.out.print("-----------------------------");
                System.out.print(charSequence);
                final List<EnqListTO> filterlist = filter(data,charSequence.toString());
                myadapt.filter(filterlist);

                /*adapter.filter(charSequence.toString());
                Toast.makeText(EnqListActivity.this,charSequence, Toast.LENGTH_SHORT).show();*/
               // adapter.notifyDataSetInvalidated();
                //adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(),"entering",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private List<EnqListTO> filter(List<EnqListTO> mod, String qur){

        final List<EnqListTO> filterlist = new ArrayList<>();
        for (EnqListTO model : mod){
            final String text = (model.getStudName()).toLowerCase()+" "+model.getMobileNo();
            if (text.contains(qur.toLowerCase())){
                filterlist.add(model);
            }
        }
    return filterlist;
    }


    private void getDataFromFile() {
        try{
            SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
            int locId = loginDtl.getInt("location_id",0);// Default value is false
            handler= new DatabaseHandler(getApplicationContext());
            cursor = handler.getallStudentEnquiryList(locId);

            while(cursor.moveToNext()){
                HashMap<String, String> map = new HashMap<String, String>();
                //Toast.makeText(getApplicationContext(), "Location Id---"+cursor.getInt(cursor.getColumnIndex(StudentEnqInfoDBHelper.lOC_ID)), Toast.LENGTH_SHORT).show();
                map.put(StudentEnqInfoDBHelper.ENQ_LIST_ID, cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.ENQ_LIST_ID)));
                map.put(StudentEnqInfoDBHelper.STD_NAME, cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.STD_NAME)));
                map.put(StudentEnqInfoDBHelper.MOBILE_NO, cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.MOBILE_NO)));
                map.put(StudentEnqInfoDBHelper.COURSE, cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.COURSE)));
                map.put(StudentEnqInfoDBHelper.ENQUIRY_DT, cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.ENQUIRY_DT)));
                map.put(StudentEnqInfoDBHelper.KEY_ID, cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.KEY_ID)));

                data.add(new EnqListTO(cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.STD_NAME)),
                        cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.ENQUIRY_DT)),
                        cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.MOBILE_NO)),
                        cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.COURSE)),
                        cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.ENQ_LIST_ID)),
                        cursor.getString(cursor.getColumnIndex(StudentEnqInfoDBHelper.KEY_ID))));

                arryList.add(map);
            }
            String from[] = new String[] {
                    StudentEnqInfoDBHelper.STD_NAME,
                    StudentEnqInfoDBHelper.MOBILE_NO,
                    StudentEnqInfoDBHelper.COURSE,
                    StudentEnqInfoDBHelper.ENQUIRY_DT,
                    StudentEnqInfoDBHelper.ENQ_LIST_ID,
                    StudentEnqInfoDBHelper.KEY_ID
            };
            int to[]={R.id.std_enq_list_name,R.id.std_enq_list_mobile_no,R.id.std_enq_list_course,R.id.std_enq_list_enq_dt,R.id.enq_primary_id,R.id.local_primary_id};
            cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.activity_std_enquiry_list_item, cursor, from,to);

           /* adapter = new ListViewAdapter(EnqListActivity.this, arryList,
                    R.layout.activity_std_enquiry_list_item,from, to);

           // listView = (ListView) findViewById(R.id.enquiryList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //listView.setFastScrollEnabled(true);
            listView.setTextFilterEnabled(true);*/

            myadapt = new ListAdapter(EnqListActivity.this,arryList,R.layout.activity_std_enquiry_list_item,from,to,getApplicationContext(),data);
            listView.setAdapter(myadapt);

           // myadapt.notifyDataSetChanged();
            //listView.setTextFilterEnabled(true);


        } catch (Exception e) {
            //log the exception
            e.printStackTrace();
        } finally {
            System.out.print("finisssssssss");

        }
    }


    private void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.activity_enq_item_listview, null);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures();
        //listView.addHeaderView(header);
    }

    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

    }

public String updateDoNotCallFlag(String primaryId, List<EnqListTO> data, int posn){
    isUpdate="yes";
    try {
        dataStor= data;
        pos = posn;
        enqKeyId = primaryId;
        AlertDialog internetWarrningMsg = new AlertDialog.Builder(EnqListActivity.this).create();
        internetWarrningMsg.setTitle("Warning!");
        internetWarrningMsg.setMessage("Are you sure want to delete?");
        internetWarrningMsg.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
                locId = loginDtl.getInt("location_id", 0);// Default value is false
                orgId = loginDtl.getInt("org_id", 0);
                String mobileNo = "";
                Integer keyId = Integer.parseInt(enqKeyId);
                handler.updateDoNotCallFlag(locId, orgId, keyId);
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                isUpdate = "yes";
                myadapt.removePosition(pos,isUpdate);
            }
        });

        internetWarrningMsg.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                isUpdate="no";
            }
        });
        // Showing Alert Message
        internetWarrningMsg.show();
        Toast.makeText(getApplicationContext(), isUpdate, Toast.LENGTH_SHORT).show();
    }catch(Exception e){
        isUpdate="error";

    }

    return isUpdate;

}



    public  void makeCallFromPhone(String MobileNo){
        startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + MobileNo)));
    }
    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
       // totalClassmates.setText("(" + friendsList.size() + ")"); //update total friends in list
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enq_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //      return true;
        //  }

        return super.onOptionsItemSelected(item);
    }
}
