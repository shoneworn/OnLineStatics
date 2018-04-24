package com.ailin.shoneworn.OnLineStatics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/3/2.
 * @author  chenxiangxiang
 * @email shoneworn@163.com
 * note: 根据activity的生命周期来判断用户的操作。在两种情况下结束app计时统计。
 * 一种是：当处于栈顶或顶层的activity被挂起，要强制杀掉进程的时候。
 * 二种是：当用户逐层退回到最底层的activity，并finish的时候。即：清空activity栈结束app
 * 三种是：用户退出到最底层activity，但是activity并没有销毁，直接存活于后台中，则开始调用。
 *
 */

 class OnLineStatisticsClass {
    private String TAG = "OnlineStatics";

    private boolean isAppAlive = true;  //判断app计时是否已经结束，如果结束，但是app并没有被杀掉，用此字段复活计时功能
    private boolean isSwitchActivity = false;  //用来标记是否为从顶层activity切换到其他层activity
    private boolean isAppExit = false;  //用来标记，程序退出后，application是否完全退出，如果只是activity清空，但是application为退出，则再次进入，触发active

    private String topActivity ;
    private Map<String ,String> map = new HashMap<>();
    private long timeStart =0;   //开始计时的时间戳

    private OnLineImpl impl; //设置监听回调

    public void init(final Context context){
        Application application  = (Application)context.getApplicationContext();
        timeStart = System.currentTimeMillis()/1000;

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                topActivity = activity.getClass().getSimpleName();
                map.put(topActivity,topActivity);
                isAppAlive = true;
                isSwitchActivity = false;
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                //为什么要写在这里，由于启动模式的问题。导致以singleTask方式启动的界面，回退时，onActivityStarted里回来的是顶层的activity
                if(!activity.getClass().getSimpleName().equals(topActivity)){
                    isSwitchActivity = true;
                }else{
                    isSwitchActivity = false;
                }
                topActivity=activity.getClass().getSimpleName();

                if(!isAppAlive||isAppExit){
                    isAppExit = false;
                    if(impl!=null){
                        impl.onReportAlive();
                    }
                    timeStart = System.currentTimeMillis()/1000;
                    isAppAlive = true;
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if(activity.getClass().getSimpleName().equals(topActivity) ){
                    if(!isSwitchActivity||map.size()==1){
                        long timeEnd = System.currentTimeMillis()/1000;
                        if(impl!=null){
                            long timegap = timeEnd-timeStart;
                            String onlineTime = String.valueOf(timegap);
                            impl.onReportDuration(onlineTime);
                            timeStart = System.currentTimeMillis()/1000;  //完成上报后重置时间
                        }
                        isAppAlive=false;
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                map.remove(activity.getClass().getSimpleName());
                if(map.size()==0&&isAppAlive){
                    long timeEnd = System.currentTimeMillis()/1000;
                    if(impl!=null){
                        long timegap = timeEnd-timeStart;
                        String onlineTime = String.valueOf(timegap);
                        impl.onReportDuration(onlineTime);
                        timeStart = System.currentTimeMillis()/1000;
                    }
                    isAppAlive = false;
                }
                if(map.size() ==0){
                    isAppExit = true;
                }
            }
        });
    }


    public void setOnLineImpl(OnLineImpl impl){
        this.impl = impl;
    }

}
