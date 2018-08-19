package com.wy.schooltakenout.Banner;

import android.os.Handler;

import com.wy.schooltakenout.BottomNavition.AppreciateFragment;

import java.util.TimerTask;

public class BannerTimerTask extends TimerTask {
    /**
     * handler
     */
    Handler handler;

    public BannerTimerTask(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(AppreciateFragment.AUTOBANNER_CODE);
    }
}