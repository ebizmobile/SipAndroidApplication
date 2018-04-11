package com.eazibiz.sipandroidapplication;

import com.daimajia.swipe.SwipeLayout;
import com.ebiz.db.StudentEnqInfoDBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Rajesh Kumar on 02-Feb-17.
 */

public class ListViewAdapter extends SimpleAdapter{

    private EnqListActivity activity;
    private ArrayList<HashMap<String, String>> listData;
    private ArrayList<HashMap<String, String>> filterData;


    public ListViewAdapter(EnqListActivity context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.activity = context;
          this.listData = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.activity_enq_item_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
     //   ss=  (getItem(position));

        holder.stdName.setText(listData.get(position).get(StudentEnqInfoDBHelper.STD_NAME));
        holder.mobileNo.setText(listData.get(position).get(StudentEnqInfoDBHelper.MOBILE_NO));
        holder.enqDt.setText(listData.get(position).get(StudentEnqInfoDBHelper.ENQUIRY_DT));
        holder.courseName.setText(listData.get(position).get(StudentEnqInfoDBHelper.COURSE));
        holder.enqId.setText(listData.get(position).get(StudentEnqInfoDBHelper.ENQ_LIST_ID));
        holder.primaryId.setText(listData.get(position).get(StudentEnqInfoDBHelper.KEY_ID));



        //handling buttons event
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));
        holder.swipeLayout.getSurfaceView().setOnClickListener(onClickListener(position, holder));

        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {

                System.out.print("onclose");
               // activity.onClose();
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                System.out.print("swiping");
               // activity.onOpen();
                //Toast.makeText(activity, "You clicked on OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                // Log.i(TAG, "on start open")
                System.out.print("open");
               // activity.onStOpen();
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //Log.i(TAG, "the BottomView totally show");
                System.out.print("BottomView");


            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                // Log.i(TAG, "the BottomView totally close");

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        } );


        return convertView;
    }



    private View.OnClickListener onClickListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.out.print("clickDelete button");
               // holder.swipeLayout.close();
                //activity.alertMessage();
               // activity.updateAdapter();
            }
        };
    }
    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("clickDelete button");
                holder.swipeLayout.close();
                String priId=listData.get(position).get(StudentEnqInfoDBHelper.KEY_ID);
                String mo=listData.get(position).get(StudentEnqInfoDBHelper.MOBILE_NO);
                Toast.makeText(activity,priId, Toast.LENGTH_SHORT).show();
                Toast.makeText(activity,mo, Toast.LENGTH_SHORT).show();

                //activity.updateDoNotCallFlag();
                // activity.updateAdapter();
            }
        };
    }


    private class ViewHolder {
        private TextView stdName;
        private TextView mobileNo;
        private TextView enqDt;
        private TextView courseName;
        private TextView enqId;
        private TextView primaryId;

        private View btnDelete;
        private View btnEdit;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btnDelete = v.findViewById(R.id.delete);
            stdName = (TextView) v.findViewById(R.id.std_enq_list_name);
            mobileNo = (TextView) v.findViewById(R.id.std_enq_list_mobile_no);
            enqDt = (TextView) v.findViewById(R.id.std_enq_list_enq_dt);
            courseName = (TextView) v.findViewById(R.id.std_enq_list_course);
            enqId = (TextView) v.findViewById(R.id.enq_primary_id);
            primaryId = (TextView) v.findViewById(R.id.local_primary_id);


            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }

    public void filter(String charText) {
        final ArrayList<HashMap<String, String>> list = listData;

        int count = list.size();
        final ArrayList<String> nlist = new ArrayList<String>(count);

        String filterableString ;

        for (int i = 0; i < count; i++) {
            filterableString = list.get(i).get(StudentEnqInfoDBHelper.STD_NAME);

            if (filterableString.toLowerCase().contains(charText) ) {
                nlist.add(filterableString);
                filterData.add(list.get(i));
            }
        }
        listData = filterData;
        notifyDataSetChanged();
    }

    /*public class ItemFilter  {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            Filter.FilterResults results = new FilterResults();

            final ArrayList<HashMap<String, String>> list = listData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).get(StudentEnqInfoDBHelper.STD_NAME);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<String> filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }*/

}



