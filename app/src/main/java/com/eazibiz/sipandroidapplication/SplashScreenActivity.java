package com.eazibiz.sipandroidapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ebiz.constant.MasterConstants;
import com.ebiz.db.DatabaseHandler;
import com.ebiz.receiver.InitialSyncReceiver;

import org.json.JSONObject;

/**
 * Created by KARTHI on 14-12-2016.
 */

public class SplashScreenActivity extends AppCompatActivity {

    JSONObject json_validation;
    JSONObject loginDtl_json;
    EditText centercode;
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
        setContentView(R.layout.activity_main1);

        ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        SharedPreferences loginDtl = getSharedPreferences(MasterConstants.USER_LOGIN_DTL, MODE_PRIVATE);
        Boolean alreadyLogin= loginDtl.getBoolean("status",false); // Default value is false
        i=1;
        if(alreadyLogin){

         /*   new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {*/
                    Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                    startActivity(i);

                    finish();
               /* }
            },4000);*/


        }else{
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            finish();
            startActivity(i);

        }

        Intent alarmIntent = new Intent(this, InitialSyncReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
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



}
