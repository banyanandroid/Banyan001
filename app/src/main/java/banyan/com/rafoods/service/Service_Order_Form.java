package banyan.com.rafoods.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import banyan.com.rafoods.activity.AppConfig;
import banyan.com.rafoods.database.DBManager;
import banyan.com.rafoods.database.DatabaseHelper;


/***
 * Vivekanandhan
 *
 * */

public class Service_Order_Form extends Service {

    private static final String TAG = "HelloService";

    private boolean isRunning = false;

    private RequestQueue queue;

    private DBManager dbManager;

    @Override
    public void onCreate() {
        Log.i(TAG, "### Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(TAG, "### Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {

                // get local db data
                dbManager = new DBManager(getApplicationContext());
                dbManager.open();

                Cursor cursor = dbManager.Fetch_Order_Form();

                System.out.println("ORDER FORM : " + cursor);

                if (cursor == null) {

                    cursor.close();
                } else {

                    try {

                        // get form order details from local database
                        if (cursor.moveToFirst()) {
                            do {

                                String order_form_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDER_FORM_ID));
                                String shop_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDER_FORM_SHOP_ID));
                                String final_order = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDER_FORM_FINAL_ORDER));
                                String user_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDER_FORM_USER_ID));
                                String user_type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDER_FORM_USER_TYPE));
                                String over_all_disc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDER_FORM_COMBO_OFFER));

                                Log.i(TAG, "### order_form_id : " + order_form_id);

                                queue = Volley.newRequestQueue(getApplicationContext());
                                // upload order data to server
                                Save_Order_Form(order_form_id, shop_id, final_order, user_id, user_type, over_all_disc);
                            } while (cursor.moveToNext());
                        }

                    } catch (Exception e) {

                        System.out.println("ERROR " + e);

                    }

                }

                //Stop service once it finishes its task
                cursor.close();

                stopSelf();

            }
        }).start();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "### Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "### Service onDestroy");
    }


    /* */

    /************************************

     //////// * SAVE ORDER FORM  * ////////

     ************************************/

    private void Save_Order_Form(final String order_form_id, final String shop_id, final String final_order, final String user_id, final String user_type, final String ov_disc) {

        System.out.println("### Save_Order_Form");
        System.out.println("###  App_Config.url_user_details " + AppConfig.url_dis_add_product);


        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_add_product, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Log.d("USER_LOGIN", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("### REG 00" + obj);

                    int success = obj.getInt("status");

                    System.out.println("### REG" + success);

                    if (success == 1) {

                        System.out.println("### Local Form Order Uploaded to Server Succesfully.");

                        // after data upload to server
                        //remove data in local db
                        int result = dbManager.delete_Order_Form(Long.parseLong(order_form_id));
                        if (result != 0)
                            System.out.println("### Local Form Order Details Deleted Succesfully.");
                        else
                            System.out.println("### Local Form Order Details Not Deleted.");

                    } else {

                        System.out.println("###  Local  Form Order data upload to server failed. ");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("shop_id", shop_id);
                params.put("product_details", final_order);
                params.put("user_id", user_id);
                params.put("user_type", user_type);
                params.put("overall_discount", ov_disc);

                System.out.println("### SHOP ID   :::::" + shop_id);
                System.out.println("### PRODUCT_DETAILS       :::::" + final_order);
                System.out.println("### USER_ID   :::::" + user_id);
                System.out.println("### OVER ALL DISC   :::::" + user_type);

                return checkParams(params);
                //return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        // Adding request to request queue
        queue.add(request);
    }
}