package app.wheather.com.newweather.util;

import android.util.Log;

/**
 * Created by Administrator on 2018/4/26.
 */

public class LogUtil {

    public static int LEVEL_VERBOSE = 0;
    public static int LEVEL_DEBUG = 1;
    public static int LEVEL_INFO = 2;
    public static int LEVEL_WARN = 3;
    public static int LEVEL_ERROR = 4;
    //将TAG改成-1就可以关闭日志
    public static int TAG = 5;

    public static void v(String tag,String msg){
        if (TAG > LEVEL_VERBOSE){
            Log.v(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if (TAG > LEVEL_DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if (TAG > LEVEL_INFO){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if (TAG > LEVEL_WARN){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if (TAG > LEVEL_ERROR){
            Log.e(tag,msg);
        }
    }
}
