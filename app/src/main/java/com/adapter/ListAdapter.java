package com.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.eazibiz.sipandroidapplication.EnqListActivity;
import com.eazibiz.sipandroidapplication.ListViewAdapter;
import com.eazibiz.sipandroidapplication.R;
import com.eazibiz.sipandroidapplication.StudentEnquiryActivity;
import com.ebiz.constant.MasterConstants;
import com.ebiz.db.StudentEnqInfoDBHelper;
import com.ebiz.to.EnqListTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Kumar on 04-Feb-17.
 */

public class ListAdapter extends SimpleAdapter {

    private EnqListActivity activity;
    private ArrayList<HashMap<String, String>> listData;
    private ArrayList<HashMap<String, String>> filterData;
    private List<EnqListTO> data;
    Context context;
    private AlertDialog successMsg ;

    public ListAdapter(EnqListActivity context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, Context context1, List<EnqListTO> data1) {
        super(context, data, resource, from, to);
        this.activity = context;
        this.context = context1;
        this.data = data1;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (v == null) {
            // inflate UI from XML file
            v = inflater.inflate(R.layout.activity_enq_item_listview,null);
            // get all UI view
            holder = new ViewHolder();
            // set tag for holder
            v.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) v.getTag();
        }
        //   ss=  (getItem(position));


        holder.swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
        holder.btnDelete = v.findViewById(R.id.delete);
        holder.stdName = (TextView) v.findViewById(R.id.std_enq_list_name);
        holder.mobileNo = (TextView) v.findViewById(R.id.std_enq_list_mobile_no);
        holder.enqDt = (TextView) v.findViewById(R.id.std_enq_list_enq_dt);
        holder.courseName = (TextView) v.findViewById(R.id.std_enq_list_course);
        holder.enqId = (TextView) v.findViewById(R.id.enq_primary_id);
        holder.primaryId = (TextView) v.findViewById(R.id.local_primary_id);
        holder.callImg = (ImageView) v.findViewById(R.id.stdEnqCallBtId);

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


        holder.stdName.setText(data.get(position).getStudName());
        holder.mobileNo.setText(data.get(position).getMobileNo());
        holder.enqDt.setText(data.get(position).getEnqDate());
        holder.courseName.setText(data.get(position).getCourseName());
        holder.enqId.setText(data.get(position).getStudId());
        holder.primaryId.setText(data.get(position).getKeyId());



        //handling buttons event
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));
        //holder.swipeLayout.getSurfaceView().setOnClickListener(onClickListener(position, holder));

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
        holder.callImg.setOnClickListener(onCallListener(position, holder));

        return v;
    }

    private View.OnClickListener onCallListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = data.get(position).getMobileNo();
                activity.makeCallFromPhone(mobile);
                // holder.swipeLayout.close();
                //activity.alertMessage();

            }
        };
    }

    /*private View.OnClickListener onClickListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = data.get(position).getMobileNo();
                activity.makeCallFromPhone(mobile);
                // holder.swipeLayout.close();
                //activity.alertMessage();
                notifyDataSetChanged();
            }
        };
    }*/
    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String keyId = data.get(position).getKeyId();
                    String getUpdate = activity.updateDoNotCallFlag(keyId ,data, position);
                    System.out.print("clickDelete button");
                    //
                               /* String priId=listData.get(position).get(StudentEnqInfoDBHelper.KEY_ID);
                                String mo=listData.get(position).get(StudentEnqInfoDBHelper.MOBILE_NO);*/

                    String priId = data.get(position).getStudName();
                    String mo = data.get(position).getMobileNo();

                    //Toast.makeText(context,priId, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,mo, Toast.LENGTH_SHORT).show();

                    //activity.updateDoNotCallFlag();
                    // activity.updateAdapter();
                }catch (Exception e){
                    AlertDialog internetWarrningMsg = new AlertDialog.Builder(activity).create();
                    internetWarrningMsg.setTitle("Warning!");
                    internetWarrningMsg.setMessage("Error while deleting Student Enquiry!");
                    internetWarrningMsg.setButton(AlertDialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

                        }
                    });
                    // Showing Alert Message
                    internetWarrningMsg.show();
                }
            }
        };
    }


    public void removePosition(int pos, String getUpdate){
        if(getUpdate=="yes") {
            data.remove(pos);
            notifyDataSetChanged();
        }else if(getUpdate =="error"){
            AlertDialog internetWarrningMsg = new AlertDialog.Builder(activity).create();
            internetWarrningMsg.setTitle("Warning!");
            internetWarrningMsg.setMessage("Error while deleting. Kindly try some other time!");
            internetWarrningMsg.setButton(AlertDialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

                }
            });
            // Showing Alert Message
            internetWarrningMsg.show();
        }
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
        private ImageView callImg;
        private EnqListActivity act;
    }

    public void filter(List<EnqListTO> dat) {

        data = new ArrayList<>();
        data.addAll(dat);
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
