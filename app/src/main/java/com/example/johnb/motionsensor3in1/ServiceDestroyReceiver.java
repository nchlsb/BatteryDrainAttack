package com.example.johnb.motionsensor3in1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceDestroyReceiver extends BroadcastReceiver{

     /*
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    + Runs attack in persistent notification. That notficaiton restars here if user tries to kill +
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        // start the service again here
        context.startService(new Intent(context, TheService.class));
    }
}
