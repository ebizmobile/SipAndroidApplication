package com.eazibiz.sipandroidapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by KARTHI on 30-11-2016.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] list;
    public CustomList(Activity context,
                      String[] list) {
        super(context, R.layout.activity_std_enquiry_list_item, list);
        this.context = context;
        this.list = list;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_std_enquiry_list_item, null, true);
        TextView stdMobileNo = (TextView) rowView.findViewById(R.id.std_enq_list_mobile_no);

        //TextView stdName = (TextView) rowView.findViewById(R.id.stdEnqListStdName);
        stdMobileNo.setText(list[position]);

        return rowView;
    }
}