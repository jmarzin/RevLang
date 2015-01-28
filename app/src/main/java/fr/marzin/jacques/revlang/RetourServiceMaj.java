package fr.marzin.jacques.revlang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by jacques on 07/01/15.
 */
public class RetourServiceMaj extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras.getString(MiseAJour.EXTRA_MESSAGE);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();

    }
}
