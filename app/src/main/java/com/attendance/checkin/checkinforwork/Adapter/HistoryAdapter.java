package com.attendance.checkin.checkinforwork.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendance.checkin.checkinforwork.Adapter.Model.HistoryModel;
import com.attendance.checkin.checkinforwork.Adapter.Model.LeaveHistoryModel;
import com.attendance.checkin.checkinforwork.Models.GetCheckinModel;
import com.attendance.checkin.checkinforwork.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {

private Context context;

private String TAG = "<HistoryAdapter>";

private List<GetCheckinModel> val;

public HistoryAdapter(Context context){

        this.context = context;

//        sh = ((AppCompatActivity)context).getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
//        editor = sh.edit();

        }

        public void UpdateData(List<GetCheckinModel> val){
            this.val = val;

        }

        public String formatDatetime(String dateStr,String type){  //date , time
            if(type.equals("date")){
                try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                Date date = null;

                date = fmt.parse(dateStr);


                SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
                return fmtOut.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
            }else{
                try {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                    Date date = null;

                    date = fmt.parse(dateStr);


                    SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm:ss");
                    return fmtOut.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "";
                }

            }

        }

        @Override
        public HistoryAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history,parent,false);
        return new HistoryAdapter.MyHolder(v,context);
        }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyHolder holder, final int position) {

        holder.tv_date.setText(formatDatetime(val.get(position).getCheckinAt(),"date"));
        holder.tv_checkin.setText(formatDatetime(val.get(position).getCheckinAt(),"time"));
        holder.tv_checkout.setText(formatDatetime(val.get(position).getCheckoutAt(),"time"));

//        if(val.get(position).getState().equals("อนุมัติ")){
//        holder.tv_approve.setBackgroundColor(context.getResources().getColor(R.color.bootstrap_brand_success));
//        holder.tv_approve.setTextColor(context.getResources().getColor(R.color.color_text));
//        }else {
//        holder.tv_approve.setBackgroundColor(context.getResources().getColor(R.color.bootstrap_brand_danger));
//        holder.tv_approve.setTextColor(context.getResources().getColor(R.color.color_text));
//
//        }
        holder.setOnClickRecycleView(new RecycleViewOnClickListener() {
        @Override
        public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

                }

        @Override
        public void onLongClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

                }
                });

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

    TextView tv_date,tv_checkin,tv_checkout,tv_approve;
    RecycleViewOnClickListener listener;

    public MyHolder(View v,Context context) {

        super(v);

        this.context = context;

        tv_date  = v.findViewById(R.id.tv_date);
        tv_checkin = v.findViewById(R.id.tv_checkin);
        tv_checkout = v.findViewById(R.id.tv_checkout);


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