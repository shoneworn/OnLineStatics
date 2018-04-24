package com.ailin.shoneworn.OnLineStatics;

import android.util.Log;

/**
 * Created by admin on 2018/3/2.
 * @author  chenxiangxiang
 * @email shoneworn@163.com
 */


public class LogUtils {

    private static boolean IS_PRINT_lOG = true;

    public static void e(String TAG,String msg){
        if(IS_PRINT_lOG){
            Log.e(TAG,msg);
        }
    }

    public static void d(String TAG,String msg){
        if(IS_PRINT_lOG){
            Log.d(TAG,msg);
        }
    }

    public static void i(String TAG,String msg){
        if(IS_PRINT_lOG){
            Log.i(TAG,msg);
        }
    }

}
