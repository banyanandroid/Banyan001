package banyan.com.rafoods.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.Bill_Adapter;
import banyan.com.rafoods.global.SessionManager;
import dmax.dialog.SpotsDialog;

public class Activity_Shop_Info extends AppCompatActivity {

    SpotsDialog dialog1;
    public static RequestQueue queue;
    String TAG = "Auto_Res";

    public static final String TAG_BILL_ID = "bill_id";
    public static final String TAG_INVOICE_ID = "invoice_id";
    public static final String TAG_INVOICE_DATE = "invoice_date";
    public static final String TAG_TOTAL_AMOUNT = "total_amount";
    public static final String TAG_PAID_AMOUNT = "paid_amount";
    public static final String TAG_BALANCE_AMOUNT = "balance_amount";

    static ArrayList<HashMap<String, String>> bill_list;
    HashMap<String, String> params = new HashMap<String, String>();

    public Bill_Adapter adapter;

    // Session Manager Class
    SessionManager session;

    private ListView listView;
    EditText alert_pending_amt, alert_collection_amt;

    String str_name, str_id, str_type;
    String str_Shop_id = "";
    String str_select_bill_id, str_select_balance_amt = "";
    String str_alert_pending, str_alert_collection = "";

    SharedPreferences sharedPreferences;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);


        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        str_name = user.get(SessionManager.KEY_USER);
        str_id = user.get(SessionManager.KEY_USER_ID);
        str_type = user.get(SessionManager.KEY_USER_TYPE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        str_Shop_id = sharedPreferences.getString("collection_shop_id", "");

        bill_list = new ArrayList<HashMap<String, String>>();

        listView = (ListView) findViewById(R.id.shop_collection_listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                str_select_bill_id = bill_list.get(position).get(TAG_BILL_ID);
                str_select_balance_amt = bill_list.get(position).get(TAG_BALANCE_AMOUNT);

                Alert_Add_Collection();

            }

        });


        if (str_Shop_id.equals("")) {
            TastyToast.makeText(getApplicationContext(), "Shop Info Not Found", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        } else {
            bill_list.clear();
            dialog1 = new SpotsDialog(Activity_Shop_Info.this);
            dialog1.show();
            queue = Volley.newRequestQueue(Activity_Shop_Info.this);
            Function_Get_Shop_Pending_Amt();
        }

    }

    /***************************
     * Alert Add Amount
     ***************************/

    public void Alert_Add_Collection() {

        LayoutInflater li = LayoutInflater.from(Activity_Shop_Info.this);
        View view = li.inflate(R.layout.alert_add_amount, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_Shop_Info.this);
        alertDialogBuilder.setTitle("Add Collection Amount")
                .setView(view)
                .setIcon(R.drawable.logo_anil);

        alert_pending_amt = (EditText) view.findViewById(R.id.alert_pending_amount);
        alert_collection_amt = (EditText) view.findViewById(R.id.alert_collection_amount);

        alert_pending_amt.setText("" + str_select_balance_amt);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        str_alert_pending = alert_pending_amt.getText().toString();
                        str_alert_collection = alert_collection_amt.getText().toString();

                        if (str_alert_collection.equals("") || str_alert_collection.isEmpty()) {
                            TastyToast.makeText(getApplicationContext(),"Please Enter Collection Amount", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                        }
                        else {
                            dialog1 = new SpotsDialog(Activity_Shop_Info.this);
                            dialog1.show();
                            queue = Volley.newRequestQueue(Activity_Shop_Info.this);
                            Function_Get_Shop_Collection_Amt(str_alert_collection);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    /***************************
     * GET Pending Amt
     ***************************/

    public void Function_Get_Shop_Pending_Amt() {

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME DA Enquiry" + AppConfig.url_dis_get_bill);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_get_bill, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String bill_id = obj1.getString(TAG_BILL_ID);
                            String invoice_id = obj1.getString(TAG_INVOICE_ID);
                            String invoice_date = obj1.getString(TAG_INVOICE_DATE);
                            String total_amount = obj1.getString(TAG_TOTAL_AMOUNT);
                            String paid_amt = obj1.getString(TAG_PAID_AMOUNT);
                            String balance_amt = obj1.getString(TAG_BALANCE_AMOUNT);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_BILL_ID, bill_id);
                            map.put(TAG_INVOICE_ID, invoice_id);
                            map.put(TAG_INVOICE_DATE, invoice_date);
                            map.put(TAG_TOTAL_AMOUNT, total_amount);
                            map.put(TAG_PAID_AMOUNT, paid_amt);
                            map.put(TAG_BALANCE_AMOUNT, balance_amt);

                            bill_list.add(map);

                            adapter = new Bill_Adapter(Activity_Shop_Info.this,
                                    bill_list);
                            listView.setAdapter(adapter);

                        }

                        dialog1.dismiss();

                    } else if (success == 0) {

                        dialog1.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No Bill Found For this Shop", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // adapter.notifyDataSetChanged();
                // stopping swipe refresh
                dialog1.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // stopping swipe refresh
                dialog1.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("shop_id", str_Shop_id);

                System.out.println("SHOP ID : " + str_Shop_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Collection Amt
     ***************************/

    public void Function_Get_Shop_Collection_Amt(String str_collection) {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_update_bill, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        TastyToast.makeText(getApplicationContext(), "Amount Updated Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        bill_list.clear();
                        queue = Volley.newRequestQueue(Activity_Shop_Info.this);
                        Function_Get_Shop_Pending_Amt();

                        dialog1.dismiss();
                    } else if (success == 0) {

                        dialog1.dismiss();

                    }

                    dialog1.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog1.dismiss();

                TastyToast.makeText(getApplicationContext(), "Update Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("bill_id", str_select_bill_id);
                params.put("amount", str_alert_collection);

                System.out.println("### bill id : " + str_select_bill_id);
                System.out.println("### amount : " + str_alert_collection);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

}
