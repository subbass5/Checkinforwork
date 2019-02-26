package com.attendance.checkin.checkinforwork.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.Adapter.HistoryAdapter;
import com.attendance.checkin.checkinforwork.Adapter.HistoryLeaveAdapter;
import com.attendance.checkin.checkinforwork.Adapter.Model.HistoryModel;
import com.attendance.checkin.checkinforwork.Adapter.Model.LeaveHistoryModel;
import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackGetCheckinListener;
import com.attendance.checkin.checkinforwork.Models.GetCheckinModel;
import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.gson.Gson;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;

import static android.support.constraint.Constraints.TAG;

public class FragmentHistory extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private Context context;
    private List<HistoryModel> val;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;

    private TextView tv_notdata;
    private SharedPreferences sharedPreferences;


    private final String TAG = "<FragmentHistory>";
    private String user_id;
    private EditText et_month;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        context = getContext();
        recyclerView  = v.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(context);
        et_month = v.findViewById(R.id.et_month);

        tv_notdata = v.findViewById(R.id.tv_not_data);
        tv_notdata.setVisibility(View.GONE);

        fragmentManager  = getActivity().getSupportFragmentManager();

        v.findViewById(R.id.btn_cancel).setOnClickListener(this);
        v.findViewById(R.id.btn_find_history).setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(MyFer.USER_ID,"");

        showProgress();
        new NetworkConnection().callGetCheckin(listener,user_id,"");

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
                        .setTitle("เลือกเดือนเพื่อค้นหา")
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

    private void find_history() {
//        Toast.makeText(context, ""+et_month.getText(), Toast.LENGTH_SHORT).show();
        String month = et_month.getText().toString();
        if(!month.isEmpty()) {

            historyAdapter.clear();
            recyclerView.setAdapter(historyAdapter);

            showProgress();
            new NetworkConnection().callGetCheckin(listener, user_id, et_month.getText().toString());

        }else {
            Toast.makeText(context, "กรุณาเลือกเดือนและปี", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                fragmentManager.popBackStack();
                break;
            case R.id.btn_find_history:
                find_history();
                break;
        }
    }




    OnCallbackGetCheckinListener listener = new OnCallbackGetCheckinListener() {
        @Override
        public void onResponse(List<GetCheckinModel> getCheckinList) {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            //update recycleview
            historyAdapter.UpdateData(getCheckinList);

            recyclerView.setAdapter(historyAdapter);
            if(new Gson().toJson(getCheckinList).equals("[]")){
                tv_notdata.setVisibility(View.VISIBLE);

            }else {
                tv_notdata.setVisibility(View.GONE);
            }

            Log.e(TAG,new Gson().toJson(getCheckinList));

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"Data is null");
        }

        @Override
        public void onFailure(Throwable t) {

            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            tv_notdata.setVisibility(View.VISIBLE);
            t.printStackTrace();
        }
    };
}
