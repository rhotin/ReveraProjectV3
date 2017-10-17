package com.ideaone.tabletapp1;

/**
 * Created by Roman on 2016-05-04.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class yourActivityRunOnStartup extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}