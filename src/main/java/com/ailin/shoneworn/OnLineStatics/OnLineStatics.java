package com.ailin.shoneworn.OnLineStatics;

import android.content.Context;

/**
 * Created by admin on 2018/3/2.
 * @author  chenxiangxiang
 * @email shoneworn@163.com
 */

public class OnLineStatics {
    private String TAG = "OnLineStatics";
    private static OnLineStatics mInstance;
    private OnLineStatisticsClass onLineStatisticsClass;

    private OnLineStatics() {
    }

    public static OnLineStatics getInstance() {
        if (null == mInstance) {
            synchronized (OnLineStatics.class) {
                if (null == mInstance) {
                    mInstance = new OnLineStatics();
                }
            }
        }
        return mInstance;
    }

    //初始化
    public void init(Context context){
        if(context==null){
            LogUtils.e(TAG,"OnLineStatics初始化失败,context不能为空");
            return;
        }
        onLineStatisticsClass = new OnLineStatisticsClass();
        onLineStatisticsClass.init(context);
    }

    //设置监听
    public void setOnLineListener(OnLineImpl impl){
        if(onLineStatisticsClass!=null){
            onLineStatisticsClass.setOnLineImpl(impl);
        }else{
            LogUtils.d(TAG,"请先初始化OnLineStatics,初始化方法为在Appliation的onCreate方法中调用OnLineStatics.init(context);");
        }

    }

}
