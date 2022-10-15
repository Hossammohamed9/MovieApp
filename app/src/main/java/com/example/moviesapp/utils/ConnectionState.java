package com.example.moviesapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class ConnectionState {
    private static final String TAG = "ConnectionState";
    public static Thread t;
    public static String isOnline = "";
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static void realTimeConnectionState(Context context, Handler handler){
        if (t != null){
            t.interrupt();
            t = null;
        }
        t = new Thread(() -> {
            while (true){
                if (Thread.currentThread().isInterrupted()){
                    break;
                }
                if (!(isConnectedToInternet(context) + "").equals(isOnline)){
                    isOnline = isConnectedToInternet(context)+"";
                    Message m = new Message();
                    m.obj = isConnectedToInternet(context);
                    handler.sendMessage(m);
                }
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "realTimeConnectionState: "+ e.getMessage() );
                }
            }
        });t.start();

    }


    public static void dismiss(){
        try {
            t.interrupt();
            t = null;
        }catch (Exception e){
            Log.e(TAG, "dismiss: "+ e.getMessage() );
        }
    }


}
