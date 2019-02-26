package com.attendance.checkin.checkinforwork.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attendance.checkin.checkinforwork.Adapter.Model.LeaveHistoryModel;
import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryLeaveAdapter extends RecyclerView.Adapter<HistoryLeaveAdapter.MyHolder> {

    private Context context;

    private String TAG = "<CardRecycleVIewAdapter>";
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private List<GetLeaveModel> val;

    public HistoryLeaveAdapter(Context context){

        this.context = context;

//        sh = ((AppCompatActivity)context).getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
//        editor = sh.edit();

    }

    public void UpdateData(List<GetLeaveModel> val){
        this.val = val;

    }

    @Override
    public HistoryLeaveAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_leave,parent,false);
        return new HistoryLeaveAdapter.MyHolder(v,context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(HistoryLeaveAdapter.MyHolder holder, final int position) {

        holder.tv_date.setText(val.get(position).getSaveAt());
        holder.tv_reason.setText(val.get(position).getLeaveType());
        holder.tv_range_time.setText(diff_date(val.get(position).getDateStart(),val.get(position).getDateEnd()) +" วัน");

        if(val.get(position).getApprove().equals("1")){
            holder.tv_approve.setText("อนุมัติ");
            holder.tv_approve.setBackgroundColor(context.getResources().getColor(R.color.bootstrap_brand_success));
            holder.tv_approve.setTextColor(context.getResources().getColor(R.color.color_text));
        }else {
            holder.tv_approve.setText("รอการอนุมัติ");
            holder.tv_approve.setBackgroundColor(context.getResources().getColor(R.color.bootstrap_brand_danger));
            holder.tv_approve.setTextColor(context.getResources().getColor(R.color.color_text));
        }
        holder.setOnClickRecycleView(new RecycleViewOnClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

            }

            @Override
            public void onLongClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String diff_date(String date_start, String date_end) {

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddd");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(date_start);
            d2 = format.parse(date_end);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();


            long diffDays = diff / (24 * 60 * 60 * 1000);

            return "" + (diffDays+1);

        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void clear() {
        final int size = val.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                val.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return val.size();
        }catch (Exception e){
//            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();

            return 0;

        }

    }

//    public void fragmentTran(Fragment fragment, Bundle bundle){
//
//        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
//        FragmentTransaction frgTran = fragmentManager.beginTransaction();
//        frgTran.replace(R.id., fragment).addToBackStack(null).commit();
//
//    }

    public class MyHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener{
        Context context;

        TextView tv_date,tv_reason,tv_range_time,tv_approve;
        RecycleViewOnClickListener listener;

        public MyHolder(View v,Context context) {

            super(v);

            this.context = context;
            tv_date  = v.findViewById(R.id.tv_date);
            tv_reason = v.findViewById(R.id.tv_reason);
            tv_range_time = v.findViewById(R.id.tv_range_time);
            tv_approve = v.findViewById(R.id.tv_approve);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        public void setOnClickRecycleView(RecycleViewOnClickListener listener){

            this.listener =  listener;

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), false, null);

        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(v, getAdapterPosition(), false, null);

            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onClick(v, getAdapterPosition(), false, event);

            return false;
        }
    }

}
