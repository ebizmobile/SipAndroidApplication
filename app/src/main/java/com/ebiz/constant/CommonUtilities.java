package com.ebiz.constant;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * @author RAJESH KUMAR
 */
public class CommonUtilities {

	public static final String PREFERENCE_KEY = "eaziBiz" ;
	//public static final String BASE_URL ="http://192.168.1.16:2022/times"; // Local Machine Link
    //public static final String BASE_URL ="http://208.109.108.174:7070/timesdemo"; // Test Machine Link
	public static final String BASE_URL ="https://52.10.107.235:7443/times"; // Production Machine Link
	public static final String ALERT_MESSAGE = "Do you want to close Application?" ;
  //  public static final String TEMP_URL ="http://192.168.1.14:7070/times";  //43.169
 // public static final String TEMP_URL ="http://192.168.43.169:7070/times";  //

	public static void setTime(Context context,final TextView id)
	{
		 Calendar mcurrentTime = Calendar.getInstance();
         int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
         int minute = mcurrentTime.get(Calendar.MINUTE);
         TimePickerDialog mTimePicker;
         
         mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
             @Override
             public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
             	
             	id.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
             }
         }, hour, minute, true);//Yes 24 hour time
         mTimePicker.setTitle("Select Time");
         mTimePicker.show();
	}
	
	
}
