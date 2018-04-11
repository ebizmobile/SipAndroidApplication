package com.eazibiz.sipandroidapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ebiz.constant.MasterConstants;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.db.StudentEnqInfoDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KARTHI on 30-11-2016.
 */

public class StudentEnquiryListActivity extends ActionBarActivity {
    ListView list;

    DatabaseHandler handler;
    String[] enquiryList ;
    ArrayList<HashMap<String, String>> arry_list;

    Toolbar mToolbar;
    SimpleCursorAdapter cursorAdapter;
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
    BaseAdapter adapter;
    Cursor  cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_enquiry_list);
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        handler=new DatabaseHandler(getBaseContext());
        //Set search action
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        final View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        arry_list = new ArrayList<HashMap<String, String>>();
        // TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        // mTitleTextView.setText("My Own Title");

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
       /* ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });*/

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


        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        List<HashMap<String,String>> value = new ArrayList<HashMap<String,String>>();
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



            arry_list.add(map);
        }
        String from[] = new String[] {
                StudentEnqInfoDBHelper.ENQ_LIST_ID,
                StudentEnqInfoDBHelper.STD_NAME,
                StudentEnqInfoDBHelper.MOBILE_NO,
                StudentEnqInfoDBHelper.COURSE,
                StudentEnqInfoDBHelper.ENQUIRY_DT
        };
        int to[]={R.id.enq_primary_id,R.id.std_enq_list_name,R.id.std_enq_list_mobile_no,R.id.std_enq_list_course,R.id.std_enq_list_enq_dt};
        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.activity_std_enquiry_list_item, cursor, from,to);

        adapter = new SimpleAdapter(StudentEnquiryListActivity.this, arry_list,
                R.layout.activity_std_enquiry_list_item,from, to);

        list = (ListView) findViewById(R.id.enquiryList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setFastScrollEnabled(true);
        list.setTextFilterEnabled(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap<String,String> mobileno = (HashMap<String,String>) parent.getItemAtPosition(position);
                final  String value = mobileno.get(StudentEnqInfoDBHelper.MOBILE_NO);

                startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + value)));

              /* Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+value));

                if (ActivityCompat.checkSelfPermission(StudentEnquiryListActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);*/


               /* Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+txtPhn.getText().toString()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
*/
               // Toast.makeText(StudentEnquiryListActivity.this, "You Clicked at " + value, Toast.LENGTH_SHORT).show();

            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((SimpleAdapter)StudentEnquiryListActivity.this.adapter).getFilter().filter(charSequence);

                cursorAdapter.notifyDataSetInvalidated();
                cursorAdapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(),"entering",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



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
