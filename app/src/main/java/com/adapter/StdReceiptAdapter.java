package com.adapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.eazibiz.sipandroidapplication.EnqListActivity;
import com.eazibiz.sipandroidapplication.ListViewAdapter;
import com.eazibiz.sipandroidapplication.R;
import com.eazibiz.sipandroidapplication.ReceiptActivity;
import com.ebiz.db.StudentEnqInfoDBHelper;
import com.ebiz.db.StudentInfoDBHelper;
import com.ebiz.to.EnqListTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Rajesh Kumar on 05-Feb-17.
 */

public class StdReceiptAdapter  extends  SimpleAdapter{
    private ReceiptActivity activity;

    private ArrayList<HashMap<String, String>> repData;
    Context context;


    public StdReceiptAdapter(Context context, ArrayList<HashMap<String, String>>  data, int resource, String[] from, int[] to, ReceiptActivity context2) {
        super(context, data, resource, from, to);
        this.context = context;
        this.repData = data;
        this.activity = context2;
    }

    @Override
    public int getCount() {
        return repData.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return repData.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        StdReceiptAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (v == null) {
            // inflate UI from XML file
            v = inflater.inflate(R.layout.activity_std_receipt_listview,null);
            // get all UI view
            holder = new StdReceiptAdapter.ViewHolder();
            // set tag for holder
            v.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (StdReceiptAdapter.ViewHolder) v.getTag();
        }
        //   ss=  (getItem(position));


        holder.swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout_2);
        holder.btnPay = v.findViewById(R.id.payBtn);
        holder.Date = (TextView) v.findViewById(R.id.inv_dt_name);
        holder.invoiceId = (TextView) v.findViewById(R.id.std_inv_id);
        holder.invoiceNo = (TextView) v.findViewById(R.id.std_inv_no);
        holder.Month = (TextView) v.findViewById(R.id.std_inv_mon);
        holder.Year = (TextView) v.findViewById(R.id.std_inv_year);
        holder.DueDt = (TextView) v.findViewById(R.id.std_inv_due_dt);
        holder.MonthYear = (TextView) v.findViewById(R.id.inv_monyear_name);
        holder.Amount = (TextView) v.findViewById(R.id.inv_amt_name);
        holder.Paid = (TextView) v.findViewById(R.id.paid_amt_name);
        holder.balAmt = (TextView) v.findViewById(R.id.bal_amt_name);

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


        holder.Date.setText(repData.get(position).get(StudentInfoDBHelper.INVOICE_DATE));
        holder.invoiceId.setText(repData.get(position).get(StudentInfoDBHelper.INVOICE_ID));
        holder.invoiceNo.setText(repData.get(position).get(StudentInfoDBHelper.INVOICE_NO));
        holder.Month.setText(repData.get(position).get(StudentInfoDBHelper.MONTH));
        holder.Year.setText(repData.get(position).get(StudentInfoDBHelper.YEAR));
        holder.DueDt.setText(repData.get(position).get(StudentInfoDBHelper.INVOICEDUEDT));
        holder.MonthYear.setText(repData.get(position).get(StudentInfoDBHelper.MONTHYEAR));
        holder.Amount.setText(repData.get(position).get(StudentInfoDBHelper.INVOICE_AMT));
        holder.Paid.setText(repData.get(position).get(StudentInfoDBHelper.PAID_AMT));
        holder.balAmt.setText(repData.get(position).get(StudentInfoDBHelper.BAL_AMT));


        //handling buttons event
        holder.btnPay.setOnClickListener(onPaymentListener(position, holder));
       // holder.swipeLayout.getSurfaceView().setOnClickListener(onClickListener(position, holder));

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


        return v;
    }


    private View.OnClickListener onPaymentListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String invId = repData.get(position).get(StudentInfoDBHelper.INVOICE_ID);
                final String invNo = repData.get(position).get(StudentInfoDBHelper.INVOICE_NO);
                final  String invAmt= repData.get(position).get(StudentInfoDBHelper.INVOICE_AMT);
                final String invBalAmt = repData.get(position).get(StudentInfoDBHelper.BAL_AMT);
                final String invMon = repData.get(position).get(StudentInfoDBHelper.MONTH);
                final String invDt = repData.get(position).get(StudentInfoDBHelper.INVOICE_DATE);
                final String dueDt = repData.get(position).get(StudentInfoDBHelper.INVOICEDUEDT);
                AlertDialog internetWarrningMsg = new AlertDialog.Builder(activity).create();
                internetWarrningMsg.setTitle("Confirmation!");
                internetWarrningMsg.setMessage("Do you want to confirm payment!");
                internetWarrningMsg.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.updatePayment(invId, invNo,invAmt,invBalAmt,invMon,invDt,dueDt);
                    }
                });
                internetWarrningMsg.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // Showing Alert Message
                internetWarrningMsg.show();

            }
        };
    }

    private class ViewHolder {
        private TextView key_id;
        private TextView invoiceId;
        private TextView Date;
        private TextView Amount;
        private TextView balAmt;
        private TextView Paid;
        private TextView Month;
        private TextView invoiceNo;
        private TextView Year;
        private TextView MonthYear;
        private TextView DueDt;


        private SwipeLayout swipeLayout;

        private View btnPay;


    }
}
