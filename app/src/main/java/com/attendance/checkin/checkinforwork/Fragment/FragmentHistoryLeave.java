package com.attendance.checkin.checkinforwork.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.Adapter.HistoryLeaveAdapter;
import com.attendance.checkin.checkinforwork.Adapter.Model.LeaveHistoryModel;
import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackGetLeaveListenner;
import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.gson.Gson;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;

import static android.support.constraint.Constraints.TAG;

public class FragmentHistoryLeave extends Fragment  implements View.OnClickListener {

    private RecyclerView recyclerView;
    private HistoryLeaveAdapter historyLeaveAdapter;
    private Context context;
    private List<LeaveHistoryModel> val;
    private FragmentManager fragmentManager;
    private String user_id;
    private EditText et_month;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private TextView tv_not_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_leave_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        recyclerView  = v.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        historyLeaveAdapter = new HistoryLeaveAdapter(context);
        et_month = v.findViewById(R.id.et_month);
        sharedPreferences = getActivity().getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);
        tv_not_data = v.findViewById(R.id.tv_not_data);
        tv_not_data.setVisibility(View.GONE);

        v.findViewById(R.id.btn_back).setOnClickListener(this);
        //update recycleview
        v.findViewById(R.id.btn_find_leave).setOnClickListener(this);


        recyclerView.setAdapter(historyLeaveAdapter);

        user_id = sharedPreferences.getString(MyFer.USER_ID,"");

        showProgress();
        new NetworkConnection().callGetLeave(listenner,user_id,"");   //connect network


        et_month.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // For a `android.support.v4.app.FragmentActivity`:
                        final Calendar today = Calendar.getInstance();
                        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(context, new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                Log.d(TAG, "selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);
                                String month = ""+(selectedMonth+1);
                                month = month.length() == 1 ? "0"+month : month ;
                                et_month.setText(""+selectedYear+"-"+month);
//                                Toast.makeText(context, "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                        builder//.setActivatedMonth(Calendar.JULY)
                                .setMinYear(2019)
                                .setActivatedYear(2019)
                                .setMaxYear(2030)
                                .setMinMonth(Calendar.JANUARY)
                                .setTitle("Select trading month")
                                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                                    @Override
                                    public void onMonthChanged(int selectedMonth) {
                                        Log.d(TAG, "Selected month : " + selectedMonth);
                                        // Toast.makeText(MainActivity.this, " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                                    @Override
                                    public void onYearChanged(int selectedYear) {
                                        Log.d(TAG, "Selected year : " + selectedYear);
                                        // Toast.makeText(MainActivity.this, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build()
                                .show();

                    }
                });
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


    private void find_leave() {
        String month = et_month.getText().toString();

        if(!month.isEmpty()) {

            historyLeaveAdapter.clear();
            recyclerView.setAdapter(historyLeaveAdapter);

            showProgress();
            new NetworkConnection().callGetLeave(listenner, user_id, et_month.getText().toString());

        }else {
            Toast.makeText(context, "กรุณาเลือกเดือนและปี", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_back:
                    fragmentManager.popBackStack();
                break;
            case R.id.btn_find_leave:
                find_leave();
                break;
        }
    }


    OnCallbackGetLeaveListenner listenner = new OnCallbackGetLeaveListenner() {
        @Override
        public void onResponse(List<GetLeaveModel> getLeaveModelList) {

            historyLeaveAdapter.UpdateData(getLeaveModelList);
            recyclerView.setAdapter(historyLeaveAdapter);


            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(new Gson().toJson(getLeaveModelList).equals("[]")){
                tv_not_data.setVisibility(View.VISIBLE);
            }else{
                tv_not_data.setVisibility(View.GONE);
            }
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
            tv_not_data.setVisibility(View.VISIBLE);
        }
    };
}
