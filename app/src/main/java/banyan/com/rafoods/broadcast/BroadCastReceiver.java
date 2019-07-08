package banyan.com.rafoods.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 *
 * Vivekanandhan
 *
 * */

public class BroadCastReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;


        if (isNetworkConnected()) {

           /* System.out.println("### MyReceiver : onReceive");

            // run service to upload form details data
            Intent order_intent = new Intent(context, Service_Order_Form.class);
            context.startService(order_intent);

            System.out.println("### call Service_Order_Details");
            // run service to upload enquiry data
            Intent enquiry_intent = new Intent(context, Service_Add_Enquiry.class);
            context.startService(enquiry_intent);*/
        }

    }


    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}