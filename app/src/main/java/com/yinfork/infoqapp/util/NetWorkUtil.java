
package com.yinfork.infoqapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class NetWorkUtil {
    private final static String TAG = "NetWorkUtil";
    private static Context mContext;

    public static enum NetType {
        NETWORK_TYPE_NONE, // 断网情况
        NETWORK_TYPE_WIFI, // WiFi模式
        NETWOKR_TYPE_MOBILE // gprs模式
    };

    public static void initContext(Context context) {
        mContext = context;
    }

    private NetWorkUtil(Context context) {
        mContext = context;
    }

    /**
     * 获取当前网络状态的类型
     * @return 返回网络类型
     */
    public NetType getCurrentNetType() {
        ConnectivityManager connManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
        NetworkInfo gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs

        if (wifi != null && wifi.getState() == State.CONNECTED) {
             Log.d(TAG, "getCurrentNetType | Current net type:  WIFI.");
            return NetType.NETWORK_TYPE_WIFI;
        } else if (gprs != null && gprs.getState() == State.CONNECTED) {
             Log.d(TAG, "getCurrentNetType | Current net type:  MOBILE.");
            return NetType.NETWOKR_TYPE_MOBILE;
        } else {
             Log.d(TAG, "getCurrentNetType | Current net type:  NONE.");
            return NetType.NETWORK_TYPE_NONE;
        }
    }


    /**
     * 判断网络是否连接
     *
     * @param
     * @return
     */
    public static boolean isConnected() {

        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }
}
