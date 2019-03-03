package com.attendance.checkin.checkinforwork.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendance.checkin.checkinforwork.Adapter.ScheduleAdapter;
import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackScheduleListenner;
import com.attendance.checkin.checkinforwork.Models.ScheduleModel;
import com.attendance.checkin.checkinforwork.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;

public class FragmentSchedule extends Fragment {
    private String TAG = "<FragmentSchedule>";
    private ProgressDialog progressDialog;
    private Context context;

    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private TextView tv_not_data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        context = getContext();
        recyclerView  = v.findViewById(R.id.re_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        scheduleAdapter = new ScheduleAdapter(context);

        tv_not_data = v.findViewById(R.id.tv_not_data);
        tv_not_data.setVisibility(View.GONE);

        showProgress();
        new NetworkConnection().callSchedule(scheduleListenner);

    }

    private void showProgress(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("กำลังโหลดข้อมูล");
        progressDialog.show();
    }

    public void fragmentTran(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();

        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }


    OnCallbackScheduleListenner scheduleListenner = new OnCallbackScheduleListenner() {
        @Override
        public void onResponse(List<ScheduleModel> scheduleModelList) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e("Response ",""+new Gson().toJson(scheduleModelList));
            //update recycleview
            scheduleAdapter.UpdateData(scheduleModelList);

            recyclerView.setAdapter(scheduleAdapter);

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"data is null.");
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
        }
    };


}
