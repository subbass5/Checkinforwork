package com.attendance.checkin.checkinforwork.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackNotifyListener;
import com.attendance.checkin.checkinforwork.Models.NotifyModel;
import com.attendance.checkin.checkinforwork.Models.ScheduleModel;
import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyHolder> {

    private Context context;

    private String TAG = "<CardRecycleVIewAdapter>";
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private List<ScheduleModel> val;

    public ScheduleAdapter(Context context){

        this.context = context;

        sh = ((AppCompatActivity)context).getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

    }

    public void UpdateData(List<ScheduleModel> val){
        this.val = val;

    }

    @Override
    public ScheduleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule,parent,false);
        return new ScheduleAdapter.MyHolder(v,context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ScheduleAdapter.MyHolder holder, final int position) {

        holder.tv_fullname.setText(val.get(position).getFullname());
        holder.tv_schedule.setText(val.get(position).getWorkShift());

        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = sh.getString(MyFer.USER_ID,"");
                new NetworkConnection().callPushNotify(notifyListener,user_id,val.get(position).getUserId(),val.get(position).getLeaveId());
            }
        });


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

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }

    public class MyHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener{
        Context context;

        TextView tv_fullname,tv_schedule;
        Button btn_select;
        RecycleViewOnClickListener listener;

        public MyHolder(View v,Context context) {

            super(v);

            this.context = context;
            tv_fullname  = v.findViewById(R.id.tv_fullname);
            tv_schedule = v.findViewById(R.id.tv_schedule);
            btn_select = v.findViewById(R.id.btn_select);

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



    OnCallbackNotifyListener notifyListener = new OnCallbackNotifyListener() {
        @Override
        public void onResponse(NotifyModel notifyModel) {
//            Toast.makeText(context, ""+notifyModel.getState(), Toast.LENGTH_SHORT).show();
            if(notifyModel.getState().equals("success")){

                AlertDialog.Builder  builder = new AlertDialog.Builder(context);
                builder.setTitle("คำเตือน");
                builder.setMessage("ระบบได้คำร้องขอแลกเวรของคุณแล้ว");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();


                    }
                });
                builder.show();
            }else{
                Toast.makeText(context, "ไม่สามารถทำรายการได้ในขณะนี้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e(TAG,"Response is null.");
        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    };

}
