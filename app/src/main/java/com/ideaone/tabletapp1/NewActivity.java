package com.ideaone.tabletapp1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Roman on 2017-08-28.
 */

public class NewActivity {

    public void startNewActivity(Context context, String packageName) {
        Log.e("test","teat");
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        Log.e("test","teat2");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.e("test","teat3");
    }
}
