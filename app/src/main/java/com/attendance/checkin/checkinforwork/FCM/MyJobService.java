package com.attendance.checkin.checkinforwork.FCM;

import android.app.job.JobParameters;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.firebase.jobdispatcher.JobService;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {


    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }
}
